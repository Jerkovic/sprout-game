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
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.bomb.Bomb;
import com.binarybrains.sprout.entity.crop.Crop;
import com.binarybrains.sprout.entity.furniture.Chest;
import com.binarybrains.sprout.entity.npc.Emma;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.Tool;
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

    public float nightTimeAlpha = 0f; // test darkness

    public Texture spritesheet;
    public Texture charsheet;

    public GameScreen screen;

    // shader test
    private boolean lightOscillate = true;
    private Texture light;
    private FrameBuffer fbo;

    public static float ambientIntensity = .12f;
    public static final Vector3 ambientColor = new Vector3(.2f, .2f, .9f); // .6 .6 .8

    //used to make the light flicker
    public float zAngle;
    public static final float zSpeed = 5.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 1.0f;

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

    public void setupAmbientLight() {
        // shader
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


        // this was laying around in the resize
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight(), false);

        lightShader.begin();
        lightShader.setUniformf("resolution", Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        lightShader.end();

        finalShader.begin();
        finalShader.setUniformf("resolution", Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        finalShader.end();
        // end shader

    }


    public Level(GameScreen screen, int level) {

        setupAmbientLight();
        this.screen = screen;
        spritesheet = new Texture(Gdx.files.internal("levels/rpg_tileset.png"));
        charsheet = new Texture(Gdx.files.internal("spritesheet.png"));
        // temp code

        loadMap(this, level);

        player = new Player(this, 0, 0);
        player.setTilePos(29, 103);
        this.add(this, player);

        // starting test of our crops
        add(this, new Crop(this, 30, 104));

        // bomb test
        add(this, new Bomb(this, 31, 103));

        // test some scattered Pickup items
        add(this, new PickupItem(this, new ResourceItem(Resource.coal, 2), new Vector2(16f * 28, 16f * 118)));
        add(this, new PickupItem(this, new ResourceItem(Resource.goldIngot, 1), new Vector2(16f * 26, 16f * 119)));
        add(this, new PickupItem(this, new ToolItem(Tool.fishingpole, 0), new Vector2(16 * 27, 16 * 120)));
        add(this, new Chest(this, new Vector2(16 * 22, 16 * 110)));

        camera = new Camera(this);
        camera.setToOrtho(false, screen.width / 4, screen.height / 4); // we scale 16x16 to 64x64
        camera.update();

        tileMapRenderer = new OrthogonalTiledMapRenderer(map);
        tileMapRenderer.setView(camera);

        debugRenderer = new ShapeRenderer();

        gameTimer = new GameTime(1, 1, 1, 0, 0);
        gameTimer.setDuration(0);
        gameTimer.start();

        // test some path finding stuff.. move this!!
        setupPathFinding();

        this.add(this, new Emma(this, new Vector2(12 * 16f, 119 * 16f), 16f, 16f));

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
    }

    public void draw() {

        // Input ctrl should not be here
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            nightTimeAlpha += 0.01f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            nightTimeAlpha -= 0.01f;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            // show menu window
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            debugMode = !debugMode;

        }

        if (camera != null) {
            camera.followPosition(player.getPosition(), Gdx.graphics.getDeltaTime());
            camera.update();
        }

        zAngle += Gdx.app.getGraphics().getRawDeltaTime() * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;

        //draw the light to the FBO
        if (false) {
            fbo.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);

            tileMapRenderer.getBatch().setShader(defaultShader);

            float lightSize = lightOscillate ? (85.0f + 3.25f * (float)Math.sin(zAngle) + .5f * MathUtils.random()):85.0f;

            tileMapRenderer.getBatch().begin();
                tileMapRenderer.getBatch().draw(light, 48 - lightSize * 0.5f + 0.5f, 48 + 0.5f - lightSize * 0.5f, lightSize, lightSize);
                tileMapRenderer.getBatch().draw(light, player.getX(),player.getY(), lightSize, lightSize);
            tileMapRenderer.getBatch().end();
            fbo.end();
        }
        // end draw lights

        // draw the screen
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tileMapRenderer.setView(camera);
        tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);
        /*if (gameTimer.getGameTime().minute > 30) {
            tileMapRenderer.getBatch().setShader(finalShader);

        }*/

        fbo.getColorBufferTexture().bind(1); //this is important! bind the FBO to the 2nd texture unit
        light.bind(0); //we force the binding of a texture on first texture unit to avoid artefacts
        //this is because our default and ambiant shader dont use multi texturing...
        //youc can basically bind anything, it doesnt matter
        int[] bg_layers = {0,1};
        tileMapRenderer.render(bg_layers);

        // todo draw crops layer. the layer where the player can make changes to the ground

        tileMapRenderer.getBatch().begin();
            // here we should render only entities on screen right?
            sortAndRender(entities, tileMapRenderer.getBatch());
        tileMapRenderer.getBatch().end();

        //int[] fg_layers = {3};
        //tileMapRenderer.render(fg_layers);

        // Testing the night overlay
        if (false) {

            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
            debugRenderer.setColor(new Color(0f, 0f, 0.031f, nightTimeAlpha));
            debugRenderer.rect(0, 0, 18000, 20000);
            debugRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
        // end test night

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

        debugRenderer.setColor(Color.FIREBRICK);

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                Rectangle tile = new Rectangle(row*16, col*16, 16, 16);
                debugRenderer.rect(tile.getX(), tile.getY(), tile.width, tile.height);
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

    public boolean setTile(int tile_x, int tile_y, boolean blocked) {
        // TiledMapTileLayer ground = (TiledMapTileLayer)map.getLayers().get("ground");

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("ground_top");
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        //int ground_tile_id = ground.getCell(tile_x, tile_y).getTile().getId();

        cell.setTile(map.getTileSets().getTile(202)); /* or some other id, identifying the tile */
        layer.setCell(tile_x, tile_y, cell);
        Tile currentTile = tile[tile_x][tile_y];
        currentTile.setMayPass(!blocked);

        return true;
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
