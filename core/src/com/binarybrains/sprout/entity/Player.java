package com.binarybrains.sprout.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.artifact.Artifacts;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.item.tool.Tools;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.mail.Mailbox;
import com.binarybrains.sprout.mail.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.binarybrains.sprout.entity.Mob.Direction.*;

public class Player extends Npc implements InputProcessor {

    private Sprite shadow;
    public  Inventory inventory;
    public Item activeItem;
    public Vector3 clickedPos = new Vector3();
    public int newX = 0, newY = 0;
    public int inventoryCapacity = 24;
    public Portable carriedItem;
    public boolean CanMove = true;
    private Stats stats = new Stats();
    private Mailbox mailBox = new Mailbox();

    private long lastUseTime = 0;
    private long coolDownUseTime = 900; // this is how long before in millis the player can perform use/view again


    enum Keys {
        W, A, S, D
    }

    static Map<Keys, Boolean> keys = new HashMap<Keys, Boolean>();
    static {
        keys.put(Keys.W, false);
        keys.put(Keys.A, false);
        keys.put(Keys.S, false);
        keys.put(Keys.D, false);
    }

    public Player(Level level) {

        super(level, new Vector2(0, 0), 16f, 16f, 0);
        setSpeed(64);

        inventory = new Inventory(level, inventoryCapacity);
        getInventory().add(new ToolItem(Tools.hoe, 0));
        getInventory().add(new ToolItem(Tools.wateringcan, 0));
        getInventory().add(new ToolItem(Tools.axe, 0));
        getInventory().add(new ToolItem(Tools.pickaxe, 0));
        getInventory().add(new ToolItem(Tools.mace, 0));

        getInventory().add(new ResourceItem(Resources.bomb,392));
        getInventory().add(new ResourceItem(Resources.ironOre, 40));
        getInventory().add(new ResourceItem(Resources.seeds, 8));
        getInventory().add(new ResourceItem(Resources.wood, 501));
        getInventory().add(new ResourceItem(Resources.string, 3));
        getInventory().add(new ResourceItem(Resources.potato, 8));
        getInventory().add(new ResourceItem(Resources.banana, 1));
        getInventory().add(new ResourceItem(Resources.coconut, 1));

        getInventory().add(new ResourceItem(Resources.wool, 3));

        getInventory().add(new ArtifactItem(Artifacts.teddy));
        //getInventory().add(new ArtifactItem(Artifacts.backpack));

        getInventory().add(new ResourceItem(Resources.coal, 21));
        getInventory().add(new ResourceItem(Resources.stone, 12));
        getInventory().add(new ResourceItem(Resources.goldNugget, 13));
        getInventory().add(new ResourceItem(Resources.apple, 12));
        getInventory().add(new ResourceItem(Resources.ladder, 1));

        // todo setActiveItemByName?
        setActiveItem(getInventory().getItems().get(3));

        // Mailbox
        mailBox.add(new Message("Hello World", "This is the first mail sent to you"));
        mailBox.add(new Message("Hello Again", "This is the second mail sent to you!"));


        // move this to a shadow system ?
        shadow = new Sprite(new Texture(Gdx.files.internal("sprites/shadow.png")));

    }

    public Stats getStats() {
        return stats;
    }

    public int getStats(String statKey) {
        return stats.get(statKey);
    }

    public void increaseStats(String statKey, int increment) {
        stats.increase(statKey, increment);
    }

    public void increaseFunds(int increment) {
        increaseStats("money", increment);
        getLevel().screen.hud.updateFunds(this);
    }

    public void setActiveItem(Item item) {
        if (item == null) return;
        activeItem = item;
        if (getLevel().screen.hud != null && !(activeItem instanceof ToolItem))
            getLevel().screen.hud.setMouseItem(activeItem.getRegionId());
        else if(getLevel().screen.hud != null && activeItem != null) {
            getLevel().screen.hud.removeMouseItem();
        }
    }

    @Override
    public Item getActiveItem() {
        return activeItem;
    }

    public void setCarriedItem(Portable carriedItem) {
        this.carriedItem = carriedItem;
        if (carriedItem != null) {
            carriedItem.setCarried();
            setActionState(Npc.ActionState.CARRYING);
        } else {
            setActionState(ActionState.EMPTY_NORMAL);
        }
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
            y = getY() + getWalkBox().getHeight();
            height = getHeight() + getWalkBox().getHeight();
            width = getWidth();
        }

        return new Rectangle(x, y, width, height);
    }

    //  called on left click, boolean return type?
    private boolean interactWithActiveItem() {

        if (getActionState() == ActionState.CARRYING) {

            Entity entity = (Entity)carriedItem;

            List<Entity> entities = getLevel().getEntities(getInteractBox());
            if (entities.size() > 0) {
                return false;
            }

            int x = (int)getInteractBox().getX() / 16;
            int y = (int)getInteractBox().getY() / 16;
            if (getLevel().isTileBlocked(x, y, entity)) {
                return false;
            }

            if (carriedItem != null) carriedItem.deleteCarried();
            entity.setPosition(getInteractBox().getX(), getInteractBox().getY());
            getLevel().add(getLevel(), entity);
            carriedItem = null;
            setActionState(ActionState.EMPTY_NORMAL);
            return true;
        }

        boolean done = false;
        // List<Entity> entities = getLevel().getEntities(getInteractBox());
        List<Entity> entities = getLevel().getNearestEntity(this, getInteractBox());
        // should we really interact with all items here?
        // maybe the items closest to the player?
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this)
				if (e.interact(this, activeItem, getDirection())) {
                    done = true; // we have encountered an entity in the interact box
                }
            if (done) return done;
		}

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
        return true;
    }

    public boolean canUse() {
        long useTime = TimeUtils.millis();
        if (TimeUtils.timeSinceMillis(lastUseTime) > coolDownUseTime)
        {
            lastUseTime = useTime;
            return true;
        } else {
            return false;
        }
    }

    //  use/check/investigate called on right click.. boolean return type?
    public boolean use() {
        // can we really interact while carrying stuff?
        // we need a use coolDown timer
        if (!canUse()) {
            // System.out.println("cooldown use");
            return false;
        }



            
        if (getActionState() != ActionState.EMPTY_NORMAL) {
            return false;
        }

        List<Entity> entities = getLevel().getEntities(getInteractBox());
        // should we really interact with all items here?
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e != this)
                return e.use(this, getDirection());
        }

        // can we right click a tile? if so implement that here

        return false;
    }

    @Override
    public void updateBoundingBox() {
        this.box.setWidth(getWidth());
        this.box.setHeight(getHeight());
        this.box.setPosition(getPosition());
        this.walkBox.setWidth(12);
        this.walkBox.setHeight(4);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    public void update(float delta) {
        super.update(delta);
        updateMovement();
        surfaceSoundEffect();
    }

    private long walkSoundId = -1; // temp

    private void surfaceSoundEffect() {
        // plays different walking sounds here.
        // https://www.youtube.com/watch?v=wYREdw4nz4E
        //
        if (getState() == State.WALKING) {
            // System.out.println("Sound effect Surface : " + getFeetSurface());
            if (walkSoundId < 0) {
                walkSoundId = ((Sound) SproutGame.assets.get("sfx/grass_walk.wav")).loop(.15f);
            } else {
                ((Sound) SproutGame.assets.get("sfx/grass_walk.wav")).resume(walkSoundId);
            }
        } else {
            if (walkSoundId > 0L) {
                ((Sound) SproutGame.assets.get("sfx/grass_walk.wav")).pause(walkSoundId);
            }
        }

    }

    public void pausSurfaceSound() {
        if (walkSoundId > 0L) {
            ((Sound) SproutGame.assets.get("sfx/grass_walk.wav")).pause(walkSoundId);
        }
    }

    public void freezePlayerControl() {
        CanMove = false;
        // releaseKeys();
        setState(State.STANDING);
    }

    public void unFreezePlayerControl() {
        CanMove = true;
    }


    private void updateMovement() {

        if (!CanMove) {
            return;
        }

        if (keys.get(Keys.A)) {
            setDirection(Mob.Direction.WEST);
            setState(Mob.State.WALKING);
        }
        if (keys.get(Keys.D)) {
            setDirection(Mob.Direction.EAST);
            setState(Mob.State.WALKING);
        }
        if (keys.get(Keys.W)) {
            setDirection(Mob.Direction.NORTH);
            setState(Mob.State.WALKING);
        }

        if (keys.get(Keys.S)) {
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

        if (!keys.get(Keys.W) && !keys.get(Keys.A) && !keys.get(Keys.S) && !keys.get(Keys.D) ) {
            setState(State.STANDING);
        }
    }

    @Override
    public void hurt(Mob mob, int dmg, Direction attackDir) {
        super.hurt(mob, dmg, attackDir);
    }

    public void drawShadow(Batch batch, float delta) {
        shadow.setX(getX());
        shadow.setY(getY()-5); // if we want the player to jump ... we should decrease the y value.
        shadow.draw(batch, 0.55f);
    }

    public void draw(Batch batch, float parentAlpha) {

        drawShadow(batch, Gdx.app.getGraphics().getDeltaTime());
        super.draw(batch, 1f);
        // draw carried item
        if (carriedItem != null) {
            Entity carried = (Entity)carriedItem;
            if (!(carriedItem instanceof TemporaryCarriedItem)) {
                carried.setPosition(getPosition().x, getPosition().y + TemporaryCarriedItem.OFFSET_PLAYER_Y);
            }
            carried.draw(batch, 1f);
        }
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

        if (!CanMove) {
            return false;
        }

        switch (keycode)
        {
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
        }
        return true;
    }

    /**
     * Release WASD keys
     */
    public void releaseKeys() {
        pausSurfaceSound();
        keys.put(Keys.A, false);
        keys.put(Keys.D, false);
        keys.put(Keys.W, false);
        keys.put(Keys.S, false);
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode)
        {
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
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (!CanMove) {
            return false;
        }

        clickedPos.set(screenX, screenY, 0);
        float deltaX = (float)Gdx.input.getDeltaX();
        float deltaY = (float)Gdx.input.getDeltaY();
        getLevel().getCamera().translate(-deltaX, deltaY, 0);
        clickedPos = getLevel().getCamera().unproject(clickedPos);

        float mouseWorldPosX = clickedPos.x;
        float mouseWorldPosY = clickedPos.y;

        if (mouseWorldPosX <= getInteractBox().getX() + getInteractBox().getWidth() && mouseWorldPosX >= getInteractBox().getX()
                && mouseWorldPosY <= getInteractBox().getY()+ getInteractBox().getHeight() && mouseWorldPosY >= getInteractBox().getY()) {
            if (button == Input.Buttons.LEFT) {
                return interactWithActiveItem();
            }
            else if (button == Input.Buttons.RIGHT) {
                return use();
            }
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