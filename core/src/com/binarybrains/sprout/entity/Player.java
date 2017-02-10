package com.binarybrains.sprout.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.binarybrains.sprout.entity.Mob.Direction.*;

public class Player extends Npc implements InputProcessor {

    // much of this can be moved to the Npc OR WHAT EVER WE GONNA CALL THE CLASS.

    private Sprite shadow;
    public  Inventory inventory;
    public Item activeItem;
    public Vector3 clickedPos = new Vector3();
    public int newX = 0, newY = 0; // temp remove later
    public int inventoryCapacity = 12;
    public Portable carriedItem;

    enum Keys {
        LEFT, RIGHT, UP, DOWN, CTRL, SPACE, W, A, S, D
    }

    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    static {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.CTRL, false);
        keys.put(Keys.W, false);
        keys.put(Keys.A, false);
        keys.put(Keys.S, false);
        keys.put(Keys.D, false);
        keys.put(Keys.SPACE, false);
    }

    public Player(Level level, float x, float y) {

        super(level, new Vector2(x, y), 16f, 16f, 0);
        setSpeed(64);
        // Add some stuff to the player from start
        inventory = new Inventory(level, inventoryCapacity);
        getInventory().add(new ToolItem(Tool.hoe, 0));
        getInventory().add(new ToolItem(Tool.scythe, 0));
        getInventory().add(new ToolItem(Tool.wateringcan, 0));
        getInventory().add(new ToolItem(Tool.axe, 0));
        getInventory().add(new ToolItem(Tool.pickaxe, 0));
        getInventory().add(new ToolItem(Tool.fishingpole, 0));

        setActiveItem(getInventory().getItems().get(3));

        getInventory().add(new ResourceItem(Resource.wood, 2));
        getInventory().add(new ResourceItem(Resource.coal, 21));
        getInventory().add(new ResourceItem(Resource.stone, 12));
        getInventory().add(new ResourceItem(Resource.acorn, 3));

        // move this to a shadow system ?
        shadow = new Sprite(new Texture(Gdx.files.internal("sprites/shadow.png")));
    }


    public void setActiveItem(Item item) {
        activeItem = item;
    }

    @Override
    public Item getActiveItem() {
        return activeItem;
    }

    public void setCarriedItem(Portable carriedItem) {
        this.carriedItem = carriedItem;
        setActionState(Npc.ActionState.CARRYING);
    }

    @Override
    public boolean blocks(Entity e) {
        if (this.equals(e)) return false;
        if (e instanceof Npc) {
            return true;
        }
        return false;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void touchedBy(Entity entity) {
        if (!(entity instanceof Player)) {
            entity.touchedBy(this);
        }
    }

    // move to NPC ?
    // this should be smaller and close to the player
    public Rectangle getInteractBox() {
        float x=0, y=0, width =0, height=0;

        if (getDirection() == Direction.SOUTH) {
            x = getX();
            y = getY() - getHeight();
            height = getHeight();
            width = getWidth();
        }
        if (getDirection() == Direction.WEST) {
            x = getX() - getWidth();
            y = getY();
            height = getHeight();
            width = getWidth();

        }
        if (getDirection() == Direction.EAST) {
            x = getX() + getWidth();
            y = getY();
            height = getHeight();
            width = getWidth();

        }
        if (getDirection()== Direction.NORTH) {
            x = getX();
            y = getY() + getHeight();
            height = getHeight();
            width = getWidth();
        }

        return new Rectangle(x, y, width, height);

    }

    //  called on left click
    public void interactWithActiveItem() {
        // can we really interact while carrying stuff?
        if (getActionState() != ActionState.EMPTY_NORMAL) {
            System.out.println("Cannot interact. Current actionState: " + getActionState());
            return;
        }

        boolean done = false;
        List<Entity> entities = getLevel().getEntities(getInteractBox());
        // should we really interact with all items here?
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this)
				if (e.interact(this, activeItem, getDirection())) {
                    done = true; // we have encountered an entity in the interact box
                }
		}
        if (done) return; // move up in for loop?

        int tile_x = getTileX();
        int tile_y = getTileY();

        if (getDirection() == WEST) {
            tile_x -=1;
        }
        if (getDirection() == EAST) {
            tile_x +=1;
        }
        if (getDirection() == NORTH) {
            tile_y +=1;
        }
        if (getDirection() == SOUTH) {
            tile_y -=1;
        }

        getLevel().interact(tile_x, tile_y, this);


    }

    //  use/check/investigate called on right click
    public void use() {
        // can we really interact while carrying stuff?
        if (getActionState() != ActionState.EMPTY_NORMAL) {
            System.out.println("Cannot use/check. Current actionState: " + getActionState());
            return;
        }

        List<Entity> entities = getLevel().getEntities(getInteractBox());
        // should we really interact with all items here?
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e != this)
                e.use(this, getDirection());
        }

        // can we right click a tile? if so implement that here
    }

    public void update(float delta) {
        super.update(delta);
        updateMovement();
    }

    private void updateMovement() {

        if (keys.get(Keys.LEFT) || keys.get(Keys.A)) {
            setDirection(Mob.Direction.WEST);
            setState(Mob.State.WALKING);
        }
        if (keys.get(Keys.RIGHT) || keys.get(Keys.D)) {
            setDirection(Mob.Direction.EAST);
            setState(Mob.State.WALKING);
        }
        if (keys.get(Keys.UP) || keys.get(Keys.W)) {
            setDirection(Mob.Direction.NORTH);
            setState(Mob.State.WALKING);
        }

        if (keys.get(Keys.DOWN) || keys.get(Keys.S)) {
            setDirection(Mob.Direction.SOUTH);
            setState(Mob.State.WALKING);
        }

        if (keys.get(Keys.D) && keys.get(Keys.S)) {
            setDirection(Direction.SOUTH_EAST);
            setState(Mob.State.WALKING);
        }

        if (keys.get(Keys.D) && keys.get(Keys.W)) {
            setDirection(Direction.NORTH_EAST);
            setState(Mob.State.WALKING);
        }

        if (keys.get(Keys.A) && keys.get(Keys.W)) {
            setDirection(Direction.NORTH_WEST);
            setState(Mob.State.WALKING);
        }

        if (keys.get(Keys.A) && keys.get(Keys.S)) {
            setDirection(Direction.SOUTH_WEST);
            setState(Mob.State.WALKING);
        }

        if ( !keys.get(Keys.UP) && !keys.get(Keys.DOWN) && !keys.get(Keys.LEFT) && !keys.get(Keys.RIGHT) &&
                !keys.get(Keys.W) && !keys.get(Keys.A) && !keys.get(Keys.S) && !keys.get(Keys.D) ) {
            setState(State.STANDING);
        }
    }


    @Override
    public void hurt(Mob mob, int dmg, Direction attackDir) {
        super.hurt(mob, dmg, attackDir);
    }

    public void drawShadow(Batch batch, float delta) {
        shadow.setX(getX() + 1);
        shadow.setY(getY() - 6);
        shadow.draw(batch, 0.55f);
    }

    public void draw(Batch batch, float parentAlpha) {

        drawShadow(batch, Gdx.app.getGraphics().getDeltaTime());
        super.draw(batch, 1f);
    }

    public void plantTest() {
        plantTestFinished();
    }

    public void plantTestFinished() {

        int tile_x = getTileX();
        int tile_y = getTileY();

        if (getDirection() == WEST) {
            tile_x -=1;
        }
        if (getDirection() == EAST) {
            tile_x +=1;
        }
        if (getDirection() == NORTH) {
            tile_y +=1;
        }
        if (getDirection() == SOUTH) {
            tile_y -=1;
        }

        getLevel().interact(tile_x, tile_y, this);

        // Below can be removed!!
        // MUST CHECK ! also that we really can plant on this tile. isPlantableGround or something
        TiledMapTileLayer ground = (TiledMapTileLayer)getLevel().map.getLayers().get("ground");

        TiledMapTileLayer layer = (TiledMapTileLayer)getLevel().map.getLayers().get("ground_top");
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        //int ground_tile_id = ground.getCell(tile_x, tile_y).getTile().getId();

        cell.setTile(getLevel().map.getTileSets().getTile(39)); /* or some other id, identifying the tile */
        // layer.setCell(tile_x, tile_y, cell);
    }

    /**
     * Returns the current selected mouse tile pos for player actions
     * @return
     */
    public Vector2 getMouseSelectedTile() {
        return new Vector2(clickedPos.x / 16, clickedPos.y / 16);
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode)
        {
            case Input.Keys.LEFT:
                keys.get(keys.put(Keys.LEFT, true));
                break;
            case Input.Keys.RIGHT:
                keys.get(keys.put(Keys.RIGHT, true));
                break;
            case Input.Keys.UP:
                keys.get(keys.put(Keys.UP, true));
                break;
            case Input.Keys.DOWN:
                keys.get(keys.put(Keys.DOWN, true));
                break;
            case Input.Keys.A:
                keys.get(keys.put(Keys.A, true));
                break;
            case Input.Keys.D:
                keys.get(keys.put(Keys.D, true));
                break;
            case Input.Keys.W:
                keys.get(keys.put(Keys.W, true));
                break;
            case Input.Keys.S:
                keys.get(keys.put(Keys.S, true));
                break;
            case Input.Keys.SPACE:
                plantTest();
                break;
            case Input.Keys.CONTROL_LEFT: // interact
                // interactWithActiveItem(); // moved to left button
                break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode)
        {
            case Input.Keys.LEFT:
                keys.get(keys.put(Keys.LEFT,false));
                break;
            case Input.Keys.RIGHT:
                keys.get(keys.put(Keys.RIGHT, false));
                break;
            case Input.Keys.UP:
                keys.get(keys.put(Keys.UP, false));
                break;
            case Input.Keys.DOWN:
                keys.get(keys.put(Keys.DOWN, false));
                break;
            case Input.Keys.A:
                keys.get(keys.put(Keys.A, false));
                break;
            case Input.Keys.D:
                keys.get(keys.put(Keys.D, false));
                break;
            case Input.Keys.W:
                keys.get(keys.put(Keys.W, false));
                break;
            case Input.Keys.S:
                keys.get(keys.put(Keys.S, false));
                break;
            case Input.Keys.SPACE:
                // keys.get(keys.put(Keys.SPACE, false));
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        clickedPos.set(screenX, screenY, 0);
        float deltaX = (float)Gdx.input.getDeltaX();
        float deltaY = (float)Gdx.input.getDeltaY();
        getLevel().getCamera().translate(-deltaX, deltaY, 0);
        clickedPos = getLevel().getCamera().unproject(clickedPos);

        int mouseWorldPosX = (int)clickedPos.x / 16;
        int mouseWorldPosY = (int)clickedPos.y / 16;

        System.out.println("Mouse world pos: " + mouseWorldPosX + " x " + mouseWorldPosY);
        System.out.println("Player: " + getTileX() + " x " + getTileY());

        if (mouseWorldPosX <= getTileX() + 1 && mouseWorldPosX >= getTileX() -1
                && mouseWorldPosY <= getTileY() + 1 && mouseWorldPosY >= getTileY() -1) {
        }

        switch (button)
        {
            case Input.Buttons.LEFT:
                interactWithActiveItem(); // Left Click  -> Use item/tool or pickup
                break;
            case Input.Buttons.RIGHT:
                use(); // Right Click -> Check/Use without any item
                break;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public String toString()
    {
        return super.toString() + " ActionState: " + getActionState();
    }

    public void dispose() {

    }
}
