package com.binarybrains.sprout.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.experience.LevelRank;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.artifact.Artifacts;
import com.binarybrains.sprout.item.resource.FoodResource;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.item.tool.Tools;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.mail.Mailbox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.binarybrains.sprout.entity.Mob.Direction.*;

public class Player extends Npc implements InputProcessor {

    private Sprite shadow;
    public  Inventory inventory;
    public Item activeItem;
    public Vector3 clickedPos = new Vector3();
    public int inventoryCapacity = 24;
    public Portable carriedItem;
    public boolean CanMove = true;
    private Stats stats = new Stats();
    private Mailbox mailBox = new Mailbox();

    public boolean passedOut = false;
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

        inventory = new Inventory(inventoryCapacity);
        getInventory().add(new ToolItem(Tools.hoe, 0));
        getInventory().add(new ToolItem(Tools.wateringcan, 0));
        getInventory().add(new ToolItem(Tools.axe, 0));
        getInventory().add(new ToolItem(Tools.pickaxe, 0));
        getInventory().add(new ToolItem(Tools.mace, 0));

        getInventory().add(new ArtifactItem(Artifacts.teddy));
        getInventory().add(new ToolItem(Tools.hammer, 0));

        getInventory().add(new ResourceItem(Resources.bomb,392));
        getInventory().add(new ResourceItem(Resources.ironOre, 40));
        getInventory().add(new ResourceItem(Resources.copperOre, 48));
        getInventory().add(new ResourceItem(Resources.seeds, 8));
        getInventory().add(new ResourceItem(Resources.wood, 501));
        getInventory().add(new ResourceItem(Resources.string, 3));
        getInventory().add(new ResourceItem(Resources.potato, 8));
        getInventory().add(new ResourceItem(Resources.banana, 1));
        getInventory().add(new ResourceItem(Resources.coconut, 1));

        getInventory().add(new ResourceItem(Resources.wool, 3));

        //getInventory().add(new ArtifactItem(Artifacts.backpack));

        getInventory().add(new ResourceItem(Resources.coal, 21));
        getInventory().add(new ResourceItem(Resources.stone, 12));
        getInventory().add(new ResourceItem(Resources.goldNugget, 13));
        getInventory().add(new ResourceItem(Resources.apple, 12));
        getInventory().add(new ResourceItem(Resources.ladder, 1));

        // todo setActiveItemByName?
        setActiveItem(getInventory().getItems().get(3));

        // Setup Player as a Listener


        // move this to a shadow system ?
        shadow = new Sprite(new Texture(Gdx.files.internal("sprites/shadow.png")));
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        System.out.println("Player got a msg " + msg);
        return true;
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

    public void heal(int hp) {
        super.setHealth(getHealth() + hp);
        if (getHealth() + hp > 100) setHealth(100);
        // SEND SOME EVENT
    }

    public void setHealth(int hp) {
        super.setHealth(hp);
        // SEND EVENT
    }

    @Override
    public void die() { // player cant die but pass out
        if (passedOut) {
            passedOut = false;
            MessageManager.getInstance().dispatchMessage(this, TelegramType.PLAYER_PASSED_OUT);
        }
    }

    public void payStamina(int hp) {
        setHealth(getHealth() - hp);
        if (getHealth() < 0) setHealth(0);
        if (getHealth() < 1) {
            passedOut = true;
            die();
        }
    }

    /**
     *
     * @param increment
     */
    public void increaseXP(int increment) {
        increaseStats("xp", increment);

        MessageManager.getInstance().dispatchMessage(this, TelegramType.PLAYER_STATS_XP_INCREASED);

        if (getStats("rank") != LevelRank.getLevelRankByXP(getStats("xp"))) {
            getStats().set("rank", LevelRank.getLevelRankByXP(getStats("xp")));
            MessageManager.getInstance().dispatchMessage(this, TelegramType.PLAYER_STATS_RANK_INCREASED, getStats("rank"));
        }
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
            x = getX() - (getWidth() / 2);
            y = getY() - getHeight();
            height = getHeight();
            width = getWidth() * 2;
        }
        if (getDirection() == Direction.WEST) {
            x = getX() - getWidth();
            y = getY() - ((getHeight() / 2));
            height = getHeight() * 2;
            width = getWidth();

        }
        if (getDirection() == Direction.EAST) {
            x = getX() + getWidth();
            y = getY() - ((getHeight() / 2));
            height = getHeight() * 2;
            width = getWidth();
        }
        if (getDirection()== Direction.NORTH) {
            x = getX() - (getWidth() / 2);
            y = getY() + getWalkBox().getHeight();
            height = getHeight() + getWalkBox().getHeight();
            width = getWidth() * 2;
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
            entity.setPosition(getInteractBox().getX()+16, getInteractBox().getY());
            getLevel().add(getLevel(), entity);
            carriedItem = null;
            setActionState(ActionState.EMPTY_NORMAL);
            return true;
        }

        boolean done = false;
        List<Entity> entities = getLevel().getEntities(getInteractBox());
        // List<Entity> entities = getLevel().getNearestEntity(this, getInteractBox());
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this)
				if (e.interact(this, activeItem, getDirection())) {
                    done = true; // we have encountered an entity in the interact box
                }
            if (done) return done;
		}

        int x = (int)this.getMouseSelectedTile().x;
        int y = (int)this.getMouseSelectedTile().y;
        System.out.println("Clicked Tiled: " + x + "x" + y);

        getLevel().interact(x, y, this);
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
        if (!canUse()) {
            return false;
        }

        if (getActionState() != ActionState.EMPTY_NORMAL) {
            return false;
        }

        List<Entity> entities = getLevel().getEntities(getInteractBox());
        for (Entity e : entities) {
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

        int newX, newY;
        // update motion - move this?
        if (getDirection() == WEST && getState() == State.WALKING) {
            newX = (int) (getWalkWest() - (getSpeed() * delta)) / 16;
            newY = (int) (getWalkBoxCenterY() / 16);

            float new2X = getWalkBox().x - (getSpeed() * delta);
            float new2Y = getWalkBox().y;

            int new2YBottom = (int) ((getWalkBox().getY()+ getWalkBox().getHeight()) / 16);
            int new2YTop = (int) ((getWalkBox().getY()) / 16);

            if (new2YBottom != new2YTop) {
                if (canMoveToTile(newX, new2YTop) && canMoveToTile(newX, new2YBottom) && canMoveToPos(new2X, new2Y)) {
                    getPosition().x -= getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }

            } else { // single tile movement
                if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                    getPosition().x -= getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }
            }

        }
        else if (getDirection() == EAST && getState() == State.WALKING) {
            newX = (int) (getWalkEast() + (getSpeed() * delta)) / 16;
            newY = (int) (getWalkBoxCenterY() / 16);

            float new2X = getWalkBox().x + (getSpeed() * delta);
            float new2Y = getWalkBox().y;

            int new2YBottom = (int) ((getWalkBox().getY()+ getWalkBox().getHeight()) / 16);
            int new2YTop = (int) ((getWalkBox().getY()) / 16);

            if (new2YBottom != new2YTop) {
                if (canMoveToTile(newX, new2YTop) && canMoveToTile(newX, new2YBottom) && canMoveToPos(new2X, new2Y)) {
                    getPosition().x += getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }

            } else { // single tile movement
                if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                    getPosition().x += getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }
            }

        }
        else if (getDirection() == NORTH && getState() == State.WALKING) {
            newX = (int) (getWalkBoxCenterX() / 16);
            newY = (int) (getWalkNorth() + (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x;
            float new2Y = getWalkBox().y + (getSpeed() * delta);

            int newXLeft = (int) (getWalkBox().getX() / 16);
            int newXRight = (int) ((getWalkBox().getX()+ getWalkBox().getWidth()) / 16);

            if (newXLeft != newXRight) {
                if (canMoveToTile(newXLeft, newY) && canMoveToTile(newXRight, newY) && canMoveToPos(new2X, new2Y)) {
                    getPosition().y += getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }

            } else { // single tile movement
                if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                    getPosition().y += getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }
            }

        }
        else if (getDirection() == SOUTH && getState() == State.WALKING) {
            newX = (int) (getWalkBoxCenterX() / 16);
            newY = (int) (getWalkSouth() - (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x;
            float new2Y = getWalkBox().y - (getSpeed() * delta);

            // test of the new multi tile collision
            int newXLeft = (int) (getWalkBox().getX() / 16);
            int newXRight = (int) ((getWalkBox().getX()+ getWalkBox().getWidth()) / 16);

            if (newXLeft != newXRight) {
                if (canMoveToTile(newXLeft, newY) && canMoveToTile(newXRight, newY) && canMoveToPos(new2X, new2Y)) {
                    getPosition().y -= getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }

            } else { // single tile movement
                if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                    getPosition().y -= getSpeed() * delta;
                } else {
                    setState(State.STANDING);
                }
            }
        }

        // Diagonal Movement - Read more: http://himeworks.com/2014/11/diagonal-movement-and-movement-speed/
        if (getDirection() == SOUTH_EAST && getState() == State.WALKING) {
            newX = (int) (getWalkEast() + (getSpeed() * delta)) / 16;
            newY = (int) (getWalkSouth() - (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x + (getSpeed() * delta);;
            float new2Y = getWalkBox().y - (getSpeed() * delta);

            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().y -= getSpeed() * delta;
                getPosition().x += getSpeed() * delta;
            } else {
                setState(State.STANDING);
            }
        }
        if (getDirection() == NORTH_EAST && getState() == State.WALKING) {
            newX = (int) (getWalkEast() + (getSpeed() * delta)) / 16;
            newY = (int) (getWalkSouth() + (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x + (getSpeed() * delta);;
            float new2Y = getWalkBox().y + (getSpeed() * delta);

            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().y += getSpeed() * delta;
                getPosition().x += getSpeed() * delta;
            } else {
                setState(State.STANDING);
            }
        }

        if (getDirection() == NORTH_WEST && getState() == State.WALKING) {
            newX = (int) (getWalkWest() - (getSpeed() * delta)) / 16;
            newY = (int) (getWalkNorth() + (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x - (getSpeed() * delta);;
            float new2Y = getWalkBox().y + (getSpeed() * delta);

            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().y += getSpeed() * delta;
                getPosition().x -= getSpeed() * delta;
            } else {
                setState(State.STANDING);
            }
        }

        if (getDirection() == SOUTH_WEST && getState() == State.WALKING) {
            newX = (int) (getWalkWest() - (getSpeed() * delta)) / 16;
            newY = (int) (getWalkSouth() - (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x - (getSpeed() * delta);;
            float new2Y = getWalkBox().y - (getSpeed() * delta);

            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().y -= getSpeed() * delta;
                getPosition().x -= getSpeed() * delta;
            } else {
                setState(State.STANDING);
            }
        }
        // super.update(delta);
        updateMovement();



        surfaceSoundEffect();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            // todo show some sort of tabbed menu window
        }
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
        switch (keycode) {
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

        int x = (int)this.getMouseSelectedTile().x;
        int y = (int)this.getMouseSelectedTile().y;
        System.out.println("Tile coordinates: " + x + "x" + y);

        // player clicked himself
        if (mouseWorldPosX <= getBoundingBox().getX() + getBoundingBox().getWidth() && mouseWorldPosX >= getBoundingBox().getX()
                && mouseWorldPosY <= getBoundingBox().getY()+ getBoundingBox().getHeight() && mouseWorldPosY >= getBoundingBox().getY()) {
            if (button == Input.Buttons.RIGHT && activeItem != null) {

                if (activeItem.isFood() && canUse()) {
                    ResourceItem healItem = (ResourceItem) activeItem;
                    heal(((FoodResource) healItem.resource).heal());
                    SproutGame.playSound("eating", 1f);
                    getInventory().removeResource(((ResourceItem) activeItem).resource, 1);
                    getLevel().screen.hud.refreshInventory();
                }
                return true;
            }
        }

        if (mouseWorldPosX <= getInteractBox().getX() + getInteractBox().getWidth() &&
            mouseWorldPosX >= getInteractBox().getX() &&
            mouseWorldPosY <= getInteractBox().getY() + getInteractBox().getHeight() &&
            mouseWorldPosY >= getInteractBox().getY()
        ) {

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