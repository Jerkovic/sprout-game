package com.binarybrains.sprout.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.binarybrains.sprout.entity.actions.Action;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

import java.util.ArrayList;
import java.util.List;

// Our abstract GameObject class

public abstract class Entity {

    private Level level;
    private Vector2 position = new Vector2();
    private int width, height;
    protected float stateTime = 0;
    protected Rectangle box = new Rectangle();
    protected Rectangle walkBox = new Rectangle(); // add more complex walkPolygon instead of box?
    public Boolean removed = false;
    private Color color = new Color(1, 1, 1, 1);
    private float rotation;
    private List<Entity> containsList = new ArrayList<Entity>();
    private final Array<Action> actions = new Array(0);
    private float temp;


    public Entity(Level level, Vector2 position, float width, float height) {
        init(level);
        this.position = position;
        this.width = (int)width;
        this.height = (int)height;
        updateBoundingBox();
    }

    /**
     * Add action to our entity
     * @param action
     */
    public void addAction (Action action) {
        action.setEntity(this);
        actions.add(action);
    }

    public Array<Action> getActions () {
        return actions;
    }

    /** Returns true if the actor has one or more actions. */
    public boolean hasActions () {
        return actions.size > 0;
    }

    public void removeAction (Action action) {
        if (actions.removeValue(action, true)) action.setEntity(null);
    }

    /** Removes all actions on this entity. */
    public void clearActions () {
        for (int i = actions.size - 1; i >= 0; i--)
            actions.get(i).setEntity(null);
        actions.clear();
    }

    public void tickActions(float delta) {
        Array<Action> actions = this.actions;
        if (actions.size > 0) {
            for (int i = 0; i < actions.size; i++) {
                Action action = actions.get(i);
                // System.out.println("tickAction" + action);
                if (action.act(delta) && i < actions.size) {
                    Action current = actions.get(i);
                    int actionIndex = current == action ? i : actions.indexOf(action, true);
                    if (actionIndex != -1) {
                        actions.removeIndex(actionIndex);
                        action.setEntity(null);
                        i--;
                    }
                }
            }
        }
    }

    public float getRotation () {
        return rotation;
    }

    public void setRotation (float degrees) {
        if (this.rotation != degrees) {
            this.rotation = degrees;
            //rotationChanged();
        }
    }

    /** Adds the specified rotation to the current rotation. */
    public void rotateBy (float amountInDegrees) {
        if (amountInDegrees != 0) {
            rotation += amountInDegrees;
            // rotationChanged();
        }
    }

    public float angleTo(Entity entity) {
        float angle = (float) Math.toDegrees(Math.atan2(entity.getX() - getX(), entity.getY() - getY()));
        return (float) (angle + Math.ceil( -angle / 360 ) * 360);
    }

    public float distanceTo(Entity entity) {
        float dx = entity.getX() - getX();
        float dy = entity.getY() - getY();
        return (float) (Math.sqrt((dx*dx)+(dy*dy)));
    }

    public float distanceToCenter(Entity entity) {
        float dx = entity.getCenterPos().x - getCenterPos().x;
        float dy = entity.getCenterPos().y - getCenterPos().y;
        return (float) (Math.sqrt((dx*dx)+(dy*dy)));
    }


    public Item getActiveItem() {
        return null;
    }

    public void renderDebug(ShapeRenderer renderer, Color walkBoxColor) {
        Color restoreColor = renderer.getColor();

        renderer.setColor(Color.CYAN); // bounding box
        renderer.rect(getBoundingBox().getX(),
                getBoundingBox().getY(),
                getBoundingBox().getWidth(),
                getBoundingBox().getHeight());

        renderer.setColor(walkBoxColor); // our walkbox
        renderer.rect(getWalkBox().getX(),
                getWalkBox().getY(),
                getWalkBox().getWidth(),
                getWalkBox().getHeight());

        renderer.setColor(Color.BLACK); // center cross hair
        renderer.line(getCenterPos().x - 1, getCenterPos().y, getCenterPos().x + 1, getCenterPos().y);
        renderer.line(getCenterPos().x, getCenterPos().y - 1, getCenterPos().x, getCenterPos().y + 1);
        renderer.setColor(restoreColor);
    }

    public Color getColor () {
        return color;
    }

    public void setColor (Color color) {
        this.color = color;
    }

    public void init(Level level)
    {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public boolean remove() {
        return removed = true;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void updateBoundingBox() {
        this.box.setWidth(width);
        this.box.setHeight(height);
        this.box.setPosition(position.x, position.y);
        this.walkBox.setWidth(width / 3);
        this.walkBox.setHeight(height / 4);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), position.y);
    }

    public float getWalkWest() {
        return walkBox.getX();
    }

    public float getWalkBoxCenterX() {
        return walkBox.getX() + (walkBox.getWidth() / 2);
    }

    public float getWalkBoxCenterY() {
        return walkBox.getY() + (walkBox.getHeight() / 2);
    }

    public float getWalkEast() {
        return walkBox.getX() + walkBox.getWidth();
    }

    public float getWalkNorth() {
        return walkBox.getY() + walkBox.getHeight();
    }

    public float getWalkSouth() {
        return walkBox.getY();
    }

    public Vector2 getCenterPos() {
        return new Vector2(position.x + (width / 2), position.y + (height / 2));
    }

    public Vector2 getTopCenterPos() {
        return new Vector2(position.x + (width / 2), position.y + (height ));
    }

    public Vector2 getBottomCenterPos() {
        return new Vector2(position.x + (width / 2), position.y);
    }


    public Rectangle getBoundingBox() {
        return box;
    }

    public Rectangle getWalkBox() {
        return walkBox;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public int getTileX() {
        return (int)getCenterPos().x >> 4;
    }

    public int getWBTileY() {
        return (int)getWalkBox().y >> 4;
    }

    public int getWBTileX() {
        return (int)getWalkBox().x >> 4;
    }

    public int getTileY() {
        return (int)getCenterPos().y >> 4;
    }

    public void setTileX(float x) {
        position.x = x * 16f;
    }

    public void setTileY(float y) {
        position.y = y * 16f;
    }

    public void setCenterPos(float x, float y) {
        setPosition(x- (getWidth() /2 ), y -(getHeight() /2));
    }

    /**
     * Return as hash based on pos to be able to get moving directions from Travel HashMap
     * @return long
     */
    public long getPosHash() {
        return (long)getTileX() + (getTileY() * 256); // grid[x + y * width]
    }

    public void setTilePos(int x, int y) {
        if (!getLevel().isBlockingEntitiesAtTile(this, x, y)) {
            setTileX(x);
            setTileY(y);
        } else {
            System.out.println("setTilePos failed for" + this + " : " + getLevel().getEntitiesAtTile(x, y));
        }
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        updateBoundingBox();
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBoundingBox();
    }

    public boolean canMoveToTile(int tx, int ty) {
        if (getLevel().isTileBlocked(tx, ty, this)) {
            return false;
        }
        return true;
    }

    public boolean canMoveToTile2(float new2X, float new2Y) {
        Rectangle newWalkPos = new Rectangle(new2X, new2Y, getWalkBox().getWidth(), getWalkBox().getHeight());
        // surrounding tiles
        // East
        if (newWalkPos.overlaps(getLevel().getTileBounds(getWBTileX()+1, getWBTileY()))) {
            //System.out.println("canMoveToTile2 false" + newWalkPos + " " + getLevel().getTileBounds(getTileX(), getTileY()));
            return false;
        }
        return true;
    }

    public boolean canMoveToPos(float new2X, float new2Y) {
        List<Entity> entities = getLevel().getEntities();
        Rectangle newPos = new Rectangle(new2X, new2Y, getWalkBox().getWidth(), getWalkBox().getHeight());
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getBoundingBox().overlaps(newPos)) {
                entities.get(i).touchedBy(this);
            }
            if (!entities.get(i).equals(this) && entities.get(i).getBoundingBox().contains(newPos)) {
                entities.get(i).contains(this);
            } else {
                entities.get(i).clearContains(this);
            }
            if (entities.get(i).blocks(this) && entities.get(i).getWalkBox().overlaps(newPos)) {
                return false;
            }
        }
        return true;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        tickActions(deltaTime); // we need our actions to update
        updateBoundingBox();
    }

    public void hurt(Entity ent, int damage) {
        System.out.println(this + " is hurt by" + ent + "with " + damage);
    }

    final public void contains(Entity entity) {
        if (!containsList.contains(entity)) {
            containsList.add(entity);
            this.containTrigger(entity);
        }
    }

    /**
     * This has left/stepped out  container - Trigger
     * @param entity
     */
    public void leftContainTrigger(Entity entity) {
        System.out.println(entity.getClass() + " LEFT CONTAINS " + this.getClass());
    }

    /**
     * This has entered container - Trigger
     * @param entity
     */
    public void containTrigger(Entity entity) {
        System.out.println(this.getClass() + " CONTAINS " + entity.getClass());
    }

    final public void clearContains(Entity entity) {
        if (containsList.contains(entity)) {
            containsList.remove(entity);
            leftContainTrigger(entity);
        }
    }

    public void touchedBy(Entity entity) {
        // System.out.println(this + " touchedBy " + entity);
    }

    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        System.out.println(player + " interacts with " + this + " using  " + item);
        return false;
    }

    public boolean use(Player player, Mob.Direction attackDir) {
        System.out.println(player + " using/viewing " + this);
        return false;
    }

    public boolean blocks(Entity e) {
        return false;
    }


    public void draw(Batch batch, float parentAlpha) { }

    @Override
    public String toString()
    {
        return "[" + this.getClass().getSimpleName() +  this.hashCode() + "@Pos:" + position + "]";
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getSortOrder() {
        return getY();
    }

    public float getTempFloat() {
        return temp;
    }

    public void setTempFloat(float temp) {
        this.temp = temp;
    }

    public void dispose() {

    }



}
