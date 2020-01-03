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
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.binarybrains.sprout.entity.Mob.Direction.*;

/**
 * This is the human class ..how about a dog whats is that animal class
 */
public class Npc extends Mob {

    public List<Vector2> debugPathList;

    public int spriteRow = 0; // the row on the spriteSheet where the NPC starts on
    private ActionState actionState = ActionState.EMPTY_NORMAL; // Idle normal state

    public static enum ActionState {
        EMPTY_NORMAL, CARRYING, THROWING, CHOPPING, ATTACKING
    }

    // this needs to be dynamic X,Y ??
    public Animation animationMatrix[][] = new Animation[2][4]; // action, direction
    TextureRegion currentFrame;


    public Npc(Level level, Vector2 position, float width, float height, int spriteRow) {
        super(level, position, width, height);
        this.debugPathList = new ArrayList<Vector2>();
        setSpriteRow(spriteRow);
        setupAnimations();
    }

    public boolean hasArrivedToTile(int tile_x,  int tile_y) {
        if (getLevel().getTileBounds(tile_x, tile_y).contains(getAiBox())) {
            System.out.println("Has arrived to " + tile_x + "x" + tile_y);
            return true;
        }
        return false;
    }

    public long getPosHash() {
        int tile_x=0, tile_y=0;

        if (getDirection() == Direction.EAST) {
            tile_x = (int)(box.getX()) >> 4;
            tile_y = getTileY();
        }
        else if(getDirection() == Direction.WEST) {
            tile_x = (int)(box.getX() + box.getWidth()) >> 4;
            tile_y = getTileY();
        }
        else if(getDirection() == Direction.SOUTH) {
            tile_x = getTileX();
            tile_y = (int)(box.getY() + box.getHeight()) >> 4;
        }
        else if(getDirection() == Direction.NORTH) {
            tile_x = getTileX();
            tile_y = (int)(box.getY()) >> 4;
        }
        return (long)(tile_x + (tile_y * 256)); // grid[x + y * width]wd
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

        if (path.size < 1) {
            path.add(targetX);
            path.add(targetY);
        }
        path.add(startX);
        path.add(startY);
        path.reverse();
        return path;
    }

    /**
     * Returns a map containing the travel direction guide
     */
    public Map<Long, Direction> generatePathFindingDirections(IntArray path) {
        this.debugPathList.clear();
        Map<Long, Direction> travelDirections = new HashMap<Long, Direction>();

        Mob.Direction dir = Mob.Direction.WEST;

        if (path.size < 4) { // there must be at least two pos(x,y) to be able to generate travel directions
            throw new RuntimeException("There must be at least two pos(x,y) to be able to generate travel directions");
        }
        int py, px, next_py, next_px;

        for (int i = 0, n = path.size; i < n; i += 2) {
            py = path.get(i);
            px = path.get(i + 1);
            next_py = path.get(i + 2);
            next_px = path.get(i + 3);

            debugPathList.add(new Vector2(px, py));

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
            travelDirections.put((long)(px + (py * 256)), dir); // grid[x + y * width]

            System.out.println(px + "x" + py + " turn " + dir);
            if (i == n - 4) {
                break;
            }
        }

        return travelDirections;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (this instanceof Player) return;

        if (getDirection() == WEST && getState() == State.WALKING) {
            getPosition().x -= getSpeed() * delta;
        }

        if (getDirection() == EAST && getState() == State.WALKING) {
            getPosition().x += getSpeed() * delta;
        }

        if (getDirection() == NORTH && getState() == State.WALKING) {
            getPosition().y += getSpeed() * delta;

        }
        if (getDirection() == SOUTH && getState() == State.WALKING) {
            getPosition().y -= getSpeed() * delta;
        }
    }

    public int getSpriteRow() {
        return spriteRow;
    }

    public void setSpriteRow(int spriteRow) {
        this.spriteRow = spriteRow;
    }


    public String getFeetSurface() {
        return "surfaceTile: " + getLevel().getTile(getTileX(), getTileY());
    }


    public void draw(Batch batch, float parentAlpha) {

        Direction animDirection = Direction.getAnimationDirection(getDirection());

        if (getState() == State.STANDING) { // IDLE - NOT Walking
            currentFrame = (TextureRegion)animationMatrix[getActionState().ordinal()][animDirection.ordinal()].getKeyFrames()[0];
        }
        else if (getState() == State.WALKING) {
            animationMatrix[getActionState().ordinal()][animDirection.ordinal()].setPlayMode(Animation.PlayMode.LOOP);
            currentFrame = (TextureRegion) animationMatrix[getActionState().ordinal()][animDirection.ordinal()].getKeyFrame(stateTime, true);
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
                Object[] currentAnimFrames = new TextureRegion[4];
                for (int f = 0; f < 4; f++) {
                    currentAnimFrames[f] = frames[getSpriteRow() + a][col];
                    col++;
                }
                float animSpeed = .14f; // maybe we need getSpeed() for animations?
                animationMatrix[a][d] = new Animation(animSpeed, currentAnimFrames);
            }
        }
    }

    @Override
    public String toString()
    {
        return super.toString() + " ActionState: " + getActionState();
    }


    public void dispose() {
        // todo disposal
    }

}
