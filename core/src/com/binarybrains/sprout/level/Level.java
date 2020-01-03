package com.binarybrains.sprout.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.achievement.Achievement;
import com.binarybrains.sprout.entity.*;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.enemy.Slime;
import com.binarybrains.sprout.entity.furniture.Chest;
import com.binarybrains.sprout.entity.npc.Emma;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.artifact.Artifacts;
import com.binarybrains.sprout.misc.Camera;
import com.binarybrains.sprout.misc.GameTime;
import com.binarybrains.sprout.screen.GameScreen;

import java.io.IOException;
import java.util.List;


public class Level extends LevelEngine {

    private Camera camera;
    private ShapeRenderer debugRenderer;

    public Player player;
    public boolean debugMode = false;
    public GameTime gameTimer;
    public BitmapFont font;

    public Texture spritesheet; // 400x1264 pixels 25 tiles bred och 79 hög
    public Texture charsheet;

    public GameScreen screen;

    // shader test
    private boolean lightOscillate = true;
    private Texture light;
    private FrameBuffer fbo;

    public float ambientIntensity = 1f;
    public static final Vector3 ambientColor = new Vector3(.3f, .3f, 0f); // .6 .6 .8

    //used to make the light flicker
    public float zAngle;
    public static final float zSpeed = 4.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    //read our shader files
    final String vertexShader = new FileHandle("shader/vertexShader.glsl").readString();
    final String defaultPixelShader = new FileHandle("shader/defaultPixelShader.glsl").readString();
    final String finalPixelShader =  new FileHandle("shader/pixelShader.glsl").readString();

    private ShaderProgram defaultShader;
    private ShaderProgram finalShader;

    ParticleEffect pe;


    /**
     * Setup Ambient light and shaders.
     */
    public void setupAmbientLight() {
        ShaderProgram.pedantic = false;
        defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
        finalShader = new ShaderProgram(vertexShader, finalPixelShader);

        finalShader.begin();
        finalShader.setUniformi("u_lightmap", 1);
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        finalShader.end();

        light = new Texture("shader/camAlphaMat.jpg");
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight(), false);

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

        // BitmapFont to use for text Particles
        font = new BitmapFont(Gdx.files.internal("pixel.fnt"),
                Gdx.files.internal("pixel.png"), false);

        loadMap(this, level);

        camera = new Camera(this);
        camera.setToOrtho(false, screen.width / 4, screen.height / 4); // we scale 16x16 to 64x64

        player = new Player(this);
        player.setTilePos(3, 5);
        player.setHealth(1);

        camera.setPosition(new Vector3(player.getPosition().x, player.getPosition().y, 0));
        camera.update();

        add(this, player);
        add(this, new Chest(this, new Vector2(16 * 2, 16 * 4)));

        tileMapRenderer = new OrthogonalTiledMapRenderer(map);
        tileMapRenderer.setView(camera);
        debugRenderer = new ShapeRenderer();

        gameTimer = new GameTime(0, 0, 1, 0, 0);
        gameTimer.setDuration( ((60 * 60 * 26) * 27) + 34242 );
        gameTimer.start();

        // test some path finding stuff.. move this!!
        setupPathFinding(); // construct the A.star
        this.add(this, new Emma(this, new Vector2(5 * 16f, 6 * 16f), 16f, 16f));

        // Slime test
        this.add(new Slime(this, new Vector2(22 * 16f, 107 * 16f), 16f, 16f));

        // add(this, new SpeechBubble(this, "I am hungry!"));

        // particle effects test
        pe = new ParticleEffect();
        //pe.load(Gdx.files.internal("pfx/mysmoke1.p"),Gdx.files.internal("")); // effect dir and images dir
        //pe.getEmitters().first().setPosition(10, 40);
        //pe.start();
    }



    public void add(Entity entity) {
        entity.removed = false;
        entities.add(entity);
        entity.init(this);
    }

    public Camera getCamera() {
        return camera;
    }

    public void update(float delta) {
        gameTimer.update();

        // also event based..check for achievement ...really here? timed event check?
        Achievement.checkAwards(player.getStats(), this);

        // particles update
        //pe.update(delta);
        //if (pe.isComplete()) pe.reset();

        // AI time piece
        GdxAI.getTimepiece().update(delta);

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

            if (getCamera().isCameraBottomWorld()) {
                screen.hud.inventoryTop();
            } else {
                screen.hud.inventoryBottom();
            }
        }

        // Dispatch any delayed messages
        MessageManager.getInstance().update();
    }

    public void draw() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            ambientIntensity -= .1f;
            if (ambientIntensity < 0) ambientIntensity = 0;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            ambientIntensity += .1f;
            if (ambientIntensity > 1f) ambientIntensity = 1f;
        }

        // Input ctrl should not be here in draw!!


        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            debugMode = !debugMode;
            // save game test
            try {
                player.getStats().save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            SproutGame.playSound("pickup_fanfar", .45f);
            player.setDirection(Mob.Direction.SOUTH);
            player.setCarriedItem(new TemporaryCarriedItem(player.getLevel(), new ArtifactItem(Artifacts.backpack)));
            player.freezePlayerControl();
            player.addAction(Actions.sequence(
                    Actions.delay(1.3f),
                    Actions.run(new Runnable() { public void run(){
                        player.setActionState(Npc.ActionState.EMPTY_NORMAL);
                        player.setCarriedItem(null);
                        player.unFreezePlayerControl();
                        player.inventory.upgrade(); // test upgrade backpack
                        screen.hud.refreshInventory();
                        screen.hud.addToasterMessage("Inventory Upgrade", "You were awarded a backpack.");
                        // screen.hud.moveCamera(600, 1000);

                    }})
            ));
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ALT_RIGHT)) {
            player.increaseXP(100); // test
        }

        zAngle += Gdx.app.getGraphics().getRawDeltaTime() * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;

        //draw the light to the FBO
        // we have to get entities that emmits light here
        if (ambientIntensity < .5) {
            finalShader.begin();
            finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                    ambientColor.z, ambientIntensity);
            finalShader.end();

            fbo.begin();
            Gdx.gl.glClearColor(0f,0f,0f,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            float lightSize = lightOscillate ? (75.0f + 3.25f * (float)Math.sin(zAngle) + .5f * MathUtils.random()):75.0f;

            tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);
            tileMapRenderer.getBatch().enableBlending();
            tileMapRenderer.getBatch().setShader(defaultShader);

            int src = tileMapRenderer.getBatch().getBlendSrcFunc();
            int dest = tileMapRenderer.getBatch().getBlendDstFunc();

            tileMapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
            tileMapRenderer.getBatch().begin();
            if (ambientIntensity > 0f) { // how dark should it get before lights come on?
                tileMapRenderer.getBatch().draw(light, (17 * 16) - lightSize / 2, (85 * 16) - lightSize / 2, lightSize , lightSize);
                tileMapRenderer.getBatch().draw(light,90, 1289, lightSize , lightSize);
                tileMapRenderer.getBatch().draw(light, player.getWalkBoxCenterX() - lightSize / 2, player.getWalkBoxCenterY() - lightSize / 2, lightSize , lightSize);
            }

            tileMapRenderer.getBatch().end();
            tileMapRenderer.getBatch().setBlendFunction(src, dest);
            tileMapRenderer.getBatch().setShader(finalShader);
            fbo.end();
        } else {
            fbo.begin();
            Gdx.gl.glClearColor(0f,0f,0f,0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            tileMapRenderer.getBatch().setShader(defaultShader);
            fbo.end();
        }
        // end draw lights to fbo

        // draw the screen
        Gdx.gl.glClearColor(0f,0f,0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tileMapRenderer.setView(camera);
        tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);
        // tileMapRenderer.getBatch().setShader(finalShader);

        int[] bg_layers = {0,1,2}; // water, ground and ground_top
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

        // render our particles test
        tileMapRenderer.getBatch().begin();
            // pe.draw(tileMapRenderer.getBatch());
        tileMapRenderer.getBatch().end();

        renderHighlightCell(); // mouse selection test
    }

    private void renderDebug (List<Entity> entities) {
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("ground");
        debugRenderer.setColor(Color.DARK_GRAY);

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                Rectangle tile = new Rectangle(row*16, col*16, 16, 16);
                debugRenderer.rect(tile.getX(), tile.getY(), tile.width, tile.height);
                if (getTile(row, col) != null && !getTile(row, col).mayPass) {
                    debugRenderer.line(tile.getX(), tile.getY(), tile.getX()+16, tile.getY()+16);
                    debugRenderer.line(tile.getX()+16, tile.getY(), tile.getX(), tile.getY()+16);
                }
            }
        }
        debugRenderer.end();

        debugRenderer.setAutoShapeType(true);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity entity : entities) {
            // entity.renderDebug(debugRenderer, Color.BLUE);

            Color c = new Color(50,50, 50, .1f);
            debugRenderer.setColor(c);

            if ((entity instanceof Npc) && ((Npc) entity).debugPathList != null) {
                List<Vector2> positions = ((Npc) entity).debugPathList;
                for(int ind = 0; ind < positions.size(); ind++) {
                    float x = positions.get(ind).x;
                    float y = positions.get(ind).y;

                    Rectangle pathtile = new Rectangle(x*16, y*16, 16, 16);
                    debugRenderer.rect(pathtile.getX(), pathtile.getY(), pathtile.width, pathtile.height);
                }
            }
        }
        debugRenderer.end();

        // Player interact box
        debugRenderer.setAutoShapeType(false);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.LIGHT_GRAY);
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
        defaultShader.dispose();
        light.dispose();
        spritesheet.dispose();
        charsheet.dispose();
        fbo.dispose();
        pe.dispose();
    }

    public void cameraFix() {
        getCamera().reset();
    }
}
