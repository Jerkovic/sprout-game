package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.binarybrains.sprout.entity.Mob.Direction.*;

/**
 * This is the human class
 */
public class Npc extends Mob implements Telegraph {

    public int spriteRow = 0; // the row on the spriteSheet where the NPC starts on
    private ActionState actionState = ActionState.EMPTY_NORMAL; // Idle normal state

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    public static enum ActionState {
        EMPTY_NORMAL, CARRYING, THROWING, CHOPPING, ATTACKING
    }

    // this needs to be dynamic X,Y ??
    public Animation animationMatrix[][] = new Animation[2][4]; // action, direction
    TextureRegion currentFrame;


    public Npc(Level level, Vector2 position, float width, float height, int spriteRow) {
        super(level, position, width, height);
        setSpriteRow(spriteRow);
        setupAnimations();
    }

    @Override
    public long getPosHash() {
        int tile_x=0, tile_y=0;

        if (getDirection() == Direction.EAST) {
            tile_x = (int)getWalkBox().getX() >> 4;
            tile_y = getTileY();
        }
        else if(getDirection() == Direction.WEST) {
            tile_x = (int)(getWalkBox().getX()+16) >> 4;
            tile_y = getTileY();
        }
        else if(getDirection() == Direction.SOUTH) {
            tile_x = getTileX();
            tile_y = (int)getWalkBox().getY()+8 >> 4;
        }
        else if(getDirection() == Direction.NORTH) {
            tile_x = getTileX();
            tile_y = (int)getWalkBox().getY() >> 4;
        }
        return (long)tile_x + (tile_y * 256); // grid[x + y * width]
    }


    /**
     * Generates the path finding array
     * @param targetX
     * @param targetY
     * @return IntArray
     */
    public IntArray generatePath(int targetX, int targetY) {
        int startX = getTileX();
        int startY = getTileY();
        IntArray path = getLevel().getPath(startX, startY, targetX, targetY);
        path.add(startX);
        path.add(startY);
        path.reverse();
        return path;
    }

    /**
     * Returns a map containing the travel direction guide
     */
    public Map<Long, Direction> generatePathFindingDirections(IntArray path) {

        Map<Long, Direction> travelDirections = new HashMap<Long, Direction>();

        Mob.Direction dir = Mob.Direction.WEST;

        for (int i = 0, n = path.size; i < n; i += 2) {
            int py = path.get(i);
            int px = path.get(i + 1);
            int next_py = path.get(i + 2);
            int next_px = path.get(i + 3);

            if (next_py > py)
            {
                dir = Mob.Direction.NORTH;
            }
            if (next_py < py)
            {
                dir = Mob.Direction.SOUTH;
            }
            if (next_px > px)
            {
                dir = Mob.Direction.EAST;
            }
            if (next_px < px)
            {
                dir = Mob.Direction.WEST;
            }

            travelDirections.put((long)px + (py * 256), dir); // grid[x + y * width]

            if (i == n - 4) {
                break;
            }
        }

        return travelDirections;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        int newX, newY;
        // update motion - move this?
        if (getDirection() == WEST && getState() == State.WALKING) {
            newX = (int) (getWalkWest() - (getSpeed() * delta)) / 16;
            newY = (int) (getWalkBoxCenterY() / 16);

            float new2X = getWalkBox().x - (getSpeed() * delta);
            float new2Y = getWalkBox().y;
            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().x -= getSpeed() * delta;
            } else {
                setState(State.STANDING);
            }
        }
        else if (getDirection() == EAST && getState() == State.WALKING) {
            newX = (int) (getWalkEast() + (getSpeed() * delta)) / 16;
            newY = (int) (getWalkBoxCenterY() / 16);

            float new2X = getWalkBox().x + (getSpeed() * delta);
            float new2Y = getWalkBox().y;
            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().x += getSpeed() * delta;
            } else {
                setState(State.STANDING);
            }
        }
        else if (getDirection() == NORTH && getState() == State.WALKING) {
            newX = (int) (getWalkBoxCenterX() / 16);
            newY = (int) (getWalkNorth() + (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x;
            float new2Y = getWalkBox().y + (getSpeed() * delta);

            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().y += getSpeed() * delta;
            } else {
                setState(State.STANDING);
            }
        }
        else if (getDirection() == SOUTH && getState() == State.WALKING) {
            newX = (int) (getWalkBoxCenterX() / 16);
            newY = (int) (getWalkSouth() - (getSpeed() * delta)) / 16;

            float new2X = getWalkBox().x;
            float new2Y = getWalkBox().y - (getSpeed() * delta);

            if (canMoveToTile(newX, newY) && canMoveToPos(new2X, new2Y)) {
                getPosition().y -= getSpeed() * delta;
            } else {
                setState(State.STANDING);
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
    }

    public int getSpriteRow() {
        return spriteRow;
    }

    public void setSpriteRow(int spriteRow) {
        this.spriteRow = spriteRow;
    }

    public boolean canMoveToTile(int tx, int ty) {
        if (getLevel().isTileBlocked(tx, ty, this)) {
            return false;
        }
        return true;
    }

    private boolean canMoveToPos(float new2X, float new2Y) {
        List<Entity> entities = getLevel().getEntities();
        Rectangle newPos = new Rectangle(new2X, new2Y, getWalkBox().getWidth(), getWalkBox().getHeight());
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getBoundingBox().overlaps(newPos)) {
                entities.get(i).touchedBy(this);
            }

            if (entities.get(i).blocks(this) && entities.get(i).getWalkBox().overlaps(newPos)) {
                return false;
            }
        }
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {

        //drawShadow(batch, Gdx.app.getGraphics().getDeltaTime()); // move to ShadowSystem
        Direction animDirection = Direction.getAnimationDirection(getDirection());

        if (getState() == State.STANDING) { // IDLE - NOT Walking
            currentFrame = animationMatrix[getActionState().ordinal()][animDirection.ordinal()].getKeyFrames()[0];
        }
        else if (getState() == State.WALKING) {
            // override animations
            currentFrame = animationMatrix[getActionState().ordinal()][animDirection.ordinal()].getKeyFrame(stateTime, true);
        }

        batch.draw(currentFrame, getX(), getY());
    }

    public ActionState getActionState() {
        return actionState;
    }

    public void setActionState(ActionState state) {
        this.actionState = state;
    }

    public void setupAnimations() {

        TextureRegion[][] frames = TextureRegion.split(getLevel().charsheet, 16, 16);

        for (int a = ActionState.EMPTY_NORMAL.ordinal(); a <= ActionState.CARRYING.ordinal(); a++) {
            int col = 0; // column counter
            for (int d = Direction.SOUTH.ordinal(); d <= Direction.WEST.ordinal(); d++) { // directions
                TextureRegion[] currentAnimFrames = new TextureRegion[4];
                for (int f = 0; f < 4; f++) {
                    currentAnimFrames[f] = frames[getSpriteRow() + a][col];
                    col++;
                }
                float animSpeed = .14f;

                animationMatrix[a][d] = new Animation(animSpeed, currentAnimFrames);
                // maybe we need a different getSpeed() for animations
            }
        }

    }

    public void dispose() {

    }

}
