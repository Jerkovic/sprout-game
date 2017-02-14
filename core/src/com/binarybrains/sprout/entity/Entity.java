package com.binarybrains.sprout.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

import java.util.List;

// Our abstract GameObject class

public abstract class Entity {

    private Level level;
    private Vector2 position = new Vector2();
    private int width, height;
    public float zIndex = 0;
    public float stateTime = 0;
    public Rectangle box = new Rectangle();
    public Rectangle walkBox = new Rectangle(); // add more complex walkPolygon instead of box?
    public Boolean removed = false;

    public int previousTileX = -1;
    public int previousTileY = -1;


    List<Entity> triggeredTouchList; // should we use for overlap check?

    public Entity(Level level, Vector2 position, float width, float height) {
        init(level);
        this.position = position;
        this.zIndex = position.y;
        this.width = (int)width;
        this.height = (int)height;
        updateBoundingBox();
    }

    public float angleTo(Entity entity) {
        float angle = (float) Math.toDegrees(Math.atan2(entity.getX() - getX(), entity.getY() - getY()));
        return (float) (angle + Math.ceil( -angle / 360 ) * 360);
    }

    public Item getActiveItem() {
        return null;
    }

    public void renderDebug(ShapeRenderer renderer, Color walkBoxColor) {
        Color restoreColor = renderer.getColor();

        renderer.setColor(Color.BLUE); // bounding box
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
        // Sets the x and y-coordinates of the bottom left corner
        this.box.setPosition(position.x, position.y);

        // update the walkBox hit detector for tiles hit detection
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

    public int getTileY() {
        return (int)getCenterPos().y >> 4;
    }

    public void setTileX(float x) {
        position.x = x * 16f;
    }

    public void setTileY(float y) {
        position.y = y * 16f;
    }

    public void setTilePos(float x, float y) {
        setTileX(x);
        setTileY(y);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
        updateBoundingBox();
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
        updateBoundingBox();

    }

    public void intersects(Entity entity) {

    }

    public void hurt(Mob mob, int damage, int attackDir) {
    }

    public void hurt(int tile, int x, int y, int dmg) {
    }

    protected void overlaps(Entity entity) {
        // debug this method should be empty
        System.out.println(entity + " overlaps " + this);

    }


    public  void touchedBy(Entity entity) {
        // System.out.println(this + " touchedBy " + entity);
    }

    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        System.out.println(player + " interacts  with " + this + " using Item: " + item);
        return false;
    }

    public boolean use(Player player, Mob.Direction attackDir) {
        System.out.println(player + " using " + this);
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
}
