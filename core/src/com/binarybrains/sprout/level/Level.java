package com.binarybrains.sprout.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.furniture.Chest;
import com.binarybrains.sprout.entity.npc.Emma;
import com.binarybrains.sprout.level.tile.Tile;
import com.binarybrains.sprout.misc.Camera;
import com.binarybrains.sprout.misc.GameTime;
import com.binarybrains.sprout.screen.GameScreen;

import java.util.List;


public class Level extends LevelEngine {

    private Camera camera;
    private ShapeRenderer debugRenderer;

    public Player player;
    public boolean debugMode = false;
    public GameTime gameTimer;

    public Texture spritesheet; // 400x1264 pixels 25 tiles bred och 79 hög
    public Texture charsheet;

    public GameScreen screen;

    // shader test
    private boolean lightOscillate = true;
    private Texture light;
    private FrameBuffer fbo;

    public float ambientIntensity = .58f;
    public static final Vector3 ambientColor = new Vector3(.3f, .3f, .8f); // .6 .6 .8

    //used to make the light flicker
    public float zAngle;
    public static final float zSpeed = 4.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    //read our shader files
    final String vertexShader = new FileHandle("shader/vertexShader.glsl").readString();
    final String defaultPixelShader = new FileHandle("shader/defaultPixelShader.glsl").readString();
    final String ambientPixelShader = new FileHandle("shader/ambientPixelShader.glsl").readString();
    final String lightPixelShader =  new FileHandle("shader/lightPixelShader.glsl").readString();
    final String finalPixelShader =  new FileHandle("shader/pixelShader.glsl").readString();

    private ShaderProgram defaultShader;
    private ShaderProgram ambientShader;
    private ShaderProgram lightShader;
    private ShaderProgram finalShader;

    /**
     * Setup Ambient light and shaders.
     */
    public void setupAmbientLight() {
        ShaderProgram.pedantic = false;
        defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
        ambientShader = new ShaderProgram(vertexShader, ambientPixelShader);
        lightShader = new ShaderProgram(vertexShader, lightPixelShader);
        finalShader = new ShaderProgram(vertexShader, finalPixelShader);

        ambientShader.begin();
        ambientShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        ambientShader.end();

        lightShader.begin();
        lightShader.setUniformi("u_lightmap", 1);
        lightShader.end();

        finalShader.begin();
        finalShader.setUniformi("u_lightmap", 1);
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        finalShader.end();

        light = new Texture("shader/light.png");


        fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight(), false);

        lightShader.begin();
        lightShader.setUniformf("resolution", Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        lightShader.end();

        finalShader.begin();
        finalShader.setUniformf("resolution", Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        finalShader.end();
    }


    public Level(GameScreen screen, int level) {

        setupAmbientLight();
        this.screen = screen;
        spritesheet = new Texture(Gdx.files.internal("levels/stardew_valley_01.png"));
        charsheet = new Texture(Gdx.files.internal("spritesheet.png"));
        // temp code

        loadMap(this, level);

        player = new Player(this);
        player.setTilePos(13, 100);
        this.add(this, player);

        add(this, new Chest(this, new Vector2(16 * 22, 16 * 110)));

        camera = new Camera(this);
        camera.setToOrtho(false, screen.width / 4, screen.height / 4); // we scale 16x16 to 64x64
        camera.setPosition(new Vector3(player.getPosition().x, player.getPosition().y, 0));
        camera.update();

        tileMapRenderer = new OrthogonalTiledMapRenderer(map);
        tileMapRenderer.setView(camera);

        debugRenderer = new ShapeRenderer();

        gameTimer = new GameTime(1, 1, 1, 0, 0);
        gameTimer.setDuration(0);
        gameTimer.start();

        // test some path finding stuff.. move this!!
        setupPathFinding(); // construct the A.star
        this.add(this, new Emma(this, new Vector2(3 * 16f, 1 * 16f), 16f, 16f));
    }

    public Camera getCamera() {
        return camera;
    }

    public void update(float delta) {

        gameTimer.update();

        // Update all our entities
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            e.update(delta);

            if (e.removed) {
                entities.remove(i--);
            }
        }

        if (camera != null) {
            camera.followPosition(player.getPosition(), delta);
            camera.update();
        }
    }

    public void draw() {

        // Input ctrl should not be here
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            // todo show some sort of tabbed menu window
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            debugMode = !debugMode;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            player.getInventory().renderDebug();
        }

        zAngle += Gdx.app.getGraphics().getRawDeltaTime() * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;

        //draw the light to the FBO
        if (true) {
            fbo.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            float lightSize = lightOscillate ? (75.0f + 3.25f * (float)Math.sin(zAngle) + .5f * MathUtils.random()):75.0f;

            tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);
            tileMapRenderer.getBatch().enableBlending();
            tileMapRenderer.getBatch().setShader(defaultShader);
            int src = tileMapRenderer.getBatch().getBlendSrcFunc();
            int dest = tileMapRenderer.getBatch().getBlendDstFunc();

            tileMapRenderer.getBatch().setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
            tileMapRenderer.getBatch().begin();
                tileMapRenderer.getBatch().draw(light,(16*16) - lightSize / 2, (85*16) - lightSize / 2, lightSize, lightSize);
                //tileMapRenderer.getBatch().draw(light,90, 1289, lightSize, lightSize);
                tileMapRenderer.getBatch().draw(light, player.getWalkBoxCenterX() - lightSize / 2,player.getWalkBoxCenterY() - lightSize / 2, lightSize, lightSize);
            tileMapRenderer.getBatch().end();
            tileMapRenderer.getBatch().setBlendFunction(src, dest);
            fbo.end();

        }
        // end draw lights to fbo

        // draw the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tileMapRenderer.setView(camera);
        tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);
        if (gameTimer.getGameTime().minute > 35) {
            tileMapRenderer.getBatch().setShader(finalShader);
        }

        int[] bg_layers = {0,1,2}; // ground and ground_top
        tileMapRenderer.render(bg_layers);

        fbo.getColorBufferTexture().bind(1);
        light.bind(0);

        tileMapRenderer.getBatch().begin();
            sortAndRender(entities, tileMapRenderer.getBatch()); // todo render only entities on screen right
        tileMapRenderer.getBatch().end();

        int[] fg_layers = {3,5};
        tileMapRenderer.render(fg_layers);

        // debug mode
        if (debugMode) renderDebug(entities);

        renderHighlightCell(); // mouse selection test
    }

    private void renderDebug (List<Entity> entities) {
        debugRenderer.setProjectionMatrix(camera.combined);

        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        debugRenderer.setColor(Color.RED);

        Rectangle collisonRect = getTileBounds(player.newX, player.newY);
        debugRenderer.rect(collisonRect.getX(), collisonRect.getY(), collisonRect.width, collisonRect.height);

        debugRenderer.end();

        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("ground");

        debugRenderer.setColor(Color.DARK_GRAY);

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                Rectangle tile = new Rectangle(row*16, col*16, 16, 16);
                debugRenderer.rect(tile.getX(), tile.getY(), tile.width, tile.height);
                if (getTile(row, col) instanceof Tile && !getTile(row, col).mayPass) {
                    debugRenderer.line(tile.getX(), tile.getY(), tile.getX()+16, tile.getY()+16);
                    debugRenderer.line(tile.getX()+16, tile.getY(), tile.getX(), tile.getY()+16);
                }
            }
        }

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).renderDebug(debugRenderer, Color.BLACK);
        }

        // Player interactWithActiveItem /interact box
        debugRenderer.setColor(Color.WHITE);
        debugRenderer.rect(player.getInteractBox().getX(), player.getInteractBox().getY(), player.getInteractBox().width, player.getInteractBox().height);

        debugRenderer.end();
    }

    private void renderHighlightCell() {

        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.FIREBRICK);
        Vector2 clickedPos = player.getMouseSelectedTile();

        Rectangle highRect = new Rectangle( (int)clickedPos.x,  (int)clickedPos.y, 16, 16);
        debugRenderer.rect((highRect.getX()) * 16, (highRect.getY()) * 16, highRect.width, highRect.height);
        debugRenderer.end();
    }

    public void dispose() {
        tileMapRenderer.dispose();
        debugRenderer.dispose();
        map.dispose();
        finalShader.dispose();
        lightShader.dispose();
        ambientShader.dispose();
        defaultShader.dispose();
        light.dispose();
        fbo.dispose();
    }


}
