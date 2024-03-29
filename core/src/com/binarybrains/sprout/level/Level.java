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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.binarybrains.sprout.entity.*;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.furniture.Chest;
import com.binarybrains.sprout.entity.npc.Arthur;
import com.binarybrains.sprout.entity.npc.Emma;
import com.binarybrains.sprout.entity.npc.NpcState;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.level.renderer.LevelMapRenderer;
import com.binarybrains.sprout.misc.AmbienceSound;
import com.binarybrains.sprout.misc.Camera;
import com.binarybrains.sprout.misc.EnviroManager;
import com.binarybrains.sprout.misc.GameTime;
import com.binarybrains.sprout.screen.GameScreen;

import java.util.List;


public class Level extends LevelEngine {

    private Camera camera;
    private ShapeRenderer debugRenderer;

    public Player player;
    public boolean debugMode = false;
    public GameTime gameTimer;
    public BitmapFont font;

    public Texture spritesheet; // 400x1264 pixels 25 tiles bred och 79 hög
    public GameScreen screen;

    // shader test
    public static final Color FLAME = new Color(0xe25822);
    private boolean lightOscillate = true;
    private Texture light;
    private FrameBuffer fbo;

    public float ambientIntensity = 1f;
    public static Vector3 ambientColor = new Vector3(1f, 1f, 1f); // .6 .6 .8

    Table ww; //  test world ui speech / action window

    //used to make the light flicker
    public float zAngle;
    public static final float zSpeed = 4.0f;
    public static final float PI2 = 3.1415926535897932384626433832795f * 2.0f;

    // sea waves
    private float					amplitudeWave = 1.15342f;
    private float					angleWave = 0.0f;
    private float					angleWaveSpeed = 4.0f;

    //read our shader files
    final String vertexShader = new FileHandle("shader/vertexShader.glsl").readString();
    final String defaultPixelShader = new FileHandle("shader/defaultPixelShader.glsl").readString();
    final String finalPixelShader =  new FileHandle("shader/pixelShader.glsl").readString();
    final String waterVertexShader =  new FileHandle("shader/waterVertexShader.glsl").readString();

    private ShaderProgram defaultShader;
    private ShaderProgram finalShader;
    private ShaderProgram waterShader;

    ParticleEffect pe;

    /**
     * Setup Ambient light and shaders.
     */
    public void setupAmbientLight() {
        ShaderProgram.pedantic = false;
        defaultShader = new ShaderProgram(vertexShader, defaultPixelShader);
        finalShader = new ShaderProgram(vertexShader, finalPixelShader);
        waterShader = new ShaderProgram(waterVertexShader, finalPixelShader);

        finalShader.begin();
        finalShader.setUniformf("resolution", Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        finalShader.setUniformi("u_lightmap", 1);
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        finalShader.end();

        waterShader.begin();
            waterShader.setUniformf("resolution", Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
            waterShader.setUniformi("u_lightmap", 1);
            waterShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        waterShader.end();

        // dont load like this
        light = new Texture("shader/camAlphaMat.jpg");

        // the fbo of the lights
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight(), false);
    }

    public Level(GameScreen screen, int level) {
        setupAmbientLight();
        this.screen = screen;
        // dont load sprites like this.
        // contains trees, chest, test bitwise auto tiles, house broken/fixed
        spritesheet = new Texture(Gdx.files.internal("levels/stardew_valley_01.png"));

        // BitmapFont to use for text Particles
        font = new BitmapFont(Gdx.files.internal("pixel.fnt"),
                Gdx.files.internal("pixel.png"), false);

        loadMap(this, level);

        camera = new Camera(this);
        camera.setToOrtho(false, screen.width / 4, screen.height / 4); // we scale 16x16 to 64x64

        player = new Player(this);
        player.setTilePos(305, 203);
        player.setHealth(100);

        camera.setPosition(new Vector3(player.getPosition().x-16, player.getPosition().y-16, 0));
        camera.update();

        // test world ui speech / action window
        ww = new Table(screen.skin);
        ww.add("lets get som text going");
        ww.pad(10).background(screen.skin.get("test-draw", NinePatchDrawable.class));
        ww.setSize(60f, 30f);
        addActor(ww);

        add(this, player);
        add(this, new Chest(this, new Vector2(300 * 16, 16 * 200)));

        tileMapRenderer = new LevelMapRenderer(map);
        tileMapRenderer.setView(camera);
        debugRenderer = new ShapeRenderer();

        gameTimer = new GameTime(0, 0, 1, 0, 0);
        gameTimer.setDuration( ((60 * 60 * 26) * 27) + 11 );
        gameTimer.start();

        // test some path finding stuff.. move this!!
        Emma emma = new Emma(this, new Vector2(300 * 16f,  172f * 16f), 16f, 32f);
        this.add(this, emma);
        emma.stateMachine.changeState(NpcState.IDLE);

        Arthur arthur = new Arthur(this, new Vector2(301 * 16f,  179f * 16f), 16f, 32f);
        this.add(this, arthur);

        // particle effects test ...need to make pools?
        pe = new ParticleEffect();
        pe.load(Gdx.files.internal("pfx/mysmoke1.p"),Gdx.files.internal("pfx/images")); // effect dir and images dir
        pe.getEmitters().first().setPosition(309 * 16f,  217f * 16f);
        pe.start();

        // showIntroDialog();
    }

    public void add(Entity entity) {
        entity.removed = false;
        entities.add(entity);
        entity.init(this);
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * Set Ambience Color
     * @param color
     */
    public void setAmbientColor(Color color) {
        Level.ambientColor.x = color.r;
        Level.ambientColor.y = color.g;
        Level.ambientColor.z = color.b;
        ambientIntensity = color.a;
    }

    public void update(float delta) {
        gameTimer.update();

        // particles update!!
        pe.update(delta);
        if (pe.isComplete()) pe.reset();

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

        // Test render a window in world space
        Vector3 testPos = getCamera().project(new Vector3(player.getX()-16f, player.getY() + 46f, 0));
        ww.setPosition(testPos.x, testPos.y);

        // Dispatch any delayed messages
        MessageManager.getInstance().update();
    }

    public void showIntroDialog() {
        player.freezePlayerControl();
        player.addAction(Actions.sequence(
                Actions.delay(1f),
                Actions.run(() -> {
                    player.setActionState(Npc.ActionState.EMPTY_NORMAL);

                    player.unFreezePlayerControl();
                    screen.hud.speakDialog(
                            "Intro",
                            "The game starts here. \n All you got is this old teddy bear you found in the playing ground. A note on it says 'Emma'. \n Return to it to the poor girl..."
                    );
                })
        ));
    }

    public void draw() {
        // Input ctrl should not be here in draw!!
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            debugMode = !debugMode;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            setAmbientColor(EnviroManager.DAWN__COLOR);
            AmbienceSound.setSoundAndStart("forest_morning_ambience");
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            setAmbientColor(EnviroManager.DAY_COLOR);
            AmbienceSound.setSoundAndStart("forest_morning_ambience");
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.C)) {
            setAmbientColor(EnviroManager.DUSK_COLOR);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            setAmbientColor(EnviroManager.NIGHT_COLOR);
            AmbienceSound.setSoundAndStart("forest_night_ambience");
        }


        if(Gdx.input.isKeyJustPressed(Input.Keys.ALT_RIGHT)) {
            player.increaseXP(100); // test
        }

        final float dt = Gdx.graphics.getRawDeltaTime();

        // sea
        angleWave += dt * angleWaveSpeed;
        while(angleWave > PI2)
            angleWave -= PI2;

        // fires
        zAngle += dt * zSpeed;
        while(zAngle > PI2)
            zAngle -= PI2;


        fbo.begin();

        finalShader.begin();
        finalShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        finalShader.end();

        waterShader.begin();
        waterShader.setUniformf("ambientColor", ambientColor.x, ambientColor.y,
                ambientColor.z, ambientIntensity);
        waterShader.end();

        float lightSize = lightOscillate ? (105.0f + 3.25f * (float)Math.sin(zAngle) + .677f * MathUtils.random()):105.0f;

        tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);
        tileMapRenderer.setView(camera);

        Gdx.gl.glClearColor(0f,0f,0f,0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tileMapRenderer.getBatch().enableBlending();
        tileMapRenderer.getBatch().setShader(defaultShader);

        int src = tileMapRenderer.getBatch().getBlendSrcFunc();
        int dest = tileMapRenderer.getBatch().getBlendDstFunc();

        tileMapRenderer.getBatch().setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        tileMapRenderer.getBatch().begin();

        // TODO Draw light if they are on screen and we might need Light to be own object in tiled and entity
        if (ambientColor.x <= 100f  / 255f) { // how dark should it get before lights come on automatic?
            Color color = tileMapRenderer.getBatch().getColor();

            // light size is a animated value
            tileMapRenderer.getBatch().setColor(Color.FIREBRICK); // color of the light
            tileMapRenderer.getBatch().draw(light, player.getWalkBoxCenterX() - lightSize / 2, player.getWalkBoxCenterY() - lightSize / 2, lightSize , lightSize);
            tileMapRenderer.getBatch().setColor(color);
        }

        tileMapRenderer.getBatch().end();
        tileMapRenderer.getBatch().setBlendFunction(src, dest);

        fbo.end();
        // end draw lights to fbo

        // draw the screen
        Gdx.gl.glClearColor(0f,0f,0f,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tileMapRenderer.setView(camera);
        tileMapRenderer.getBatch().setProjectionMatrix(camera.combined);

        //feed the shader with the new data
        waterShader.begin();
            waterShader.setUniformf("waveData", angleWave, amplitudeWave);
        waterShader.end();

        //render the first layer (the water) using our special vertex shader
        tileMapRenderer.getBatch().setShader(waterShader);
        int[] water_layers = {0}; // water - not a super solutions to specify layers by index

        tileMapRenderer.render(water_layers);

        tileMapRenderer.getBatch().setShader(finalShader);

        //render the other layers using the default shader
        int[] bg_layers = {1,2}; // ground and ground_top (flat)

        tileMapRenderer.render(bg_layers);

        tileMapRenderer.getBatch().begin();

        //bind the FBO to the 2nd texture unit
        //we force the binding of a texture on first texture unit to avoid artefacts
        //this is because our default and ambiant shader dont use multi texturing...
        //youc can basically bind anything, it doesnt matter
        fbo.getColorBufferTexture().bind(1);
        light.bind(0);

        // reset
        for (Entity ent : entities) {
            ent.setOnScreen(false);
        }

        tileMapRenderer.renderSortedRowTileLayer(entities, (TiledMapTileLayer) map.getLayers().get(3));

        // particles
        pe.draw(tileMapRenderer.getBatch());

        tileMapRenderer.getBatch().end();

        int[] fg_layers = {5}; // layerIndex 3 should be broken up
        tileMapRenderer.render(fg_layers);

        // debug mode
        if (debugMode) renderDebug(entities);

        // renderHighlightCell(); // mouse selection test

        super.draw();
    }

    private void renderDebug (List<Entity> entities) {
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("ground");
        debugRenderer.setColor(Color.DARK_GRAY);

        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {
                Rectangle tile = new Rectangle(row*16, col*16, 16, 16);

                if (tileMapRenderer.getViewBounds().overlaps(tile)) {
                    debugRenderer.rect(tile.getX(), tile.getY(), tile.width, tile.height);
                    if (getTile(row, col) != null && !getTile(row, col).mayPass) {
                        debugRenderer.line(tile.getX(), tile.getY(), tile.getX()+16, tile.getY()+16);
                        debugRenderer.line(tile.getX()+16, tile.getY(), tile.getX(), tile.getY()+16);
                    }
                }
            }
        }
        debugRenderer.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        debugRenderer.setAutoShapeType(true);
        debugRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity entity : entities) {
            Color c = new Color(0,0, 0, .6f);
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

        Gdx.gl.glDisable(GL20.GL_BLEND);
        debugRenderer.end();

        debugRenderer.setAutoShapeType(false);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (Entity entity : entities) {
            entity.renderDebug(debugRenderer, new Color(250,100, 10, 1f));
        }
        // Player interact box debug
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
        fbo.dispose();
        pe.dispose();
    }

    public void cameraFix() {
        getCamera().reset();
    }
}
