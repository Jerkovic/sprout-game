package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.actions.SequenceAction;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the human class ..how about a dog whats is that animal class
 */
public class Npc extends Mob {

    private Texture framesTexture;
    private ActionState actionState = ActionState.EMPTY_NORMAL; // Idle normal state

    public List<Vector2> debugPathList;
    public List<PointDirection> findPath;
    public StateMachine<Npc, NpcState> stateMachine;

    public static enum ActionState {
        EMPTY_NORMAL, CARRYING, THROWING, CHOPPING, ATTACKING
    }

    public Animation animationMatrix[][] = new Animation[2][4]; // actions, directions
    private TextureRegion currentFrame;

    Sprite shadow;
    public float lockShadowY = 0;

    public Npc(Level level, Vector2 position, float width, float height, Texture framesTexture) {
        super(level, position, width, height);
        this.stateMachine = new DefaultStateMachine<>(this, NpcState.IDLE);
        this.debugPathList = new ArrayList<Vector2>();
        this.shadow = new Sprite((Texture) SproutGame.assets.get("sprites/shadow.png"));
        this.framesTexture = framesTexture;
        setupAnimations();
    }


    /**
     * Sets the sprite sheet frame texture
     * @param texture
     */
    public void setFramesTexture(Texture texture) {
        this.framesTexture = texture;
    }

    /**
     * Performs a happy jump
     */
    public void jump() {
        float ground_y = getY();
        float jump_to_y = getY() + 16;

        lockShadowY = ground_y;
        SproutGame.playSound("jump", 0.8f);
        addAction(Actions.sequence(
                Actions.moveTo(getX(), jump_to_y, .3f, Interpolation.pow2),
                Actions.moveTo(getX(), ground_y, .15f, Interpolation.exp5),
                Actions.run(() -> { lockShadowY = 0; })
        ));
    }

    @Override
    public float getSortOrder() {
        return lockShadowY > 0 ? lockShadowY : getY(); // prevents jump in y-axis fuck up sortOrder
    }

    // temp method for an npc to leave players house
    public void leaveHouse() {
        setTilePos(18,91); // outside the house test
        SproutGame.playSound("door_close1", 0.4f);
        stateMachine.changeState(NpcState.IDLE);
    }

    /**
     *
     * @param x
     * @param y
     * @param state
     */
    public void updateWalkDirections(int x, int y, NpcState state) {
        this.clearActions();
        clearFindPath();
        IntArray rawPath = generatePath(x, y);
        findPath = generatePathFindingDirections2(rawPath);

        SequenceAction seq = new SequenceAction();

        for (int i = 0; i < findPath.size(); i++) {
            PointDirection pd = findPath.get(i);
            Rectangle temp = getLevel().getTileBounds(pd.x, pd.y);
            seq.addAction(Actions.moveTo(temp.x, temp.y, .5f, Interpolation.linear));
            seq.addAction(Actions.run((() -> {
                setDirection(pd.direction);
                setState(State.WALKING);
            })
            ));
        }
        seq.addAction(Actions.run((() -> {
            setState(State.STANDING);
            setDirection(Direction.SOUTH); // should this be here even?
            stateMachine.changeState(state);
            MessageManager.getInstance().dispatchMessage(
                    this,
                    TelegramType.NPC_MESSAGE,
                    new PointDirection(x, y, getDirection()));
        })
        ));

        this.addAction(seq);
    }

    /**
     * Use this to clear this Mob's path finding route
     */
    public void clearFindPath() {
        findPath = null;
    }


    @Override
    public void updateBoundingBox() {
        this.box.setWidth(16);
        this.box.setHeight(32);
        this.box.setPosition(getPosition());
        // this.walkBox
        this.walkBox.setWidth(16);
        this.walkBox.setHeight(16);
        this.walkBox.setPosition(getPosition().x, getPosition().y);
    }

    /**
     * Draw shadow
     * @param batch
     * @param delta
     */
    public void drawShadow(Batch batch, float delta) {
        shadow.setX(getX());
        if (lockShadowY > 0) {
            shadow.setY(lockShadowY - 3);
        }
        else {
            shadow.setY(getY() - 3);
        }

        shadow.draw(batch, 0.55f);
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

    public List<PointDirection> generatePathFindingDirections2(IntArray path) {
        this.debugPathList.clear();
        List<PointDirection> travelDirections = new ArrayList<PointDirection>();

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
            travelDirections.add(new PointDirection(px, py, dir));

            if (i == n - 4) {
                break;
            }
        }

        return travelDirections;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stateMachine.update();
    }

    /**
     *
     * @return
     */
    public String getFeetSurface() {
        return getLevel().getTile(getTileX(), getTileY()).getClass().getSimpleName();
    }

    /**
     *
     * @param batch
     * @param parentAlpha
     */
    public void draw(Batch batch, float parentAlpha) {

        drawShadow(batch, Gdx.app.getGraphics().getDeltaTime());

        Direction animDirection = Direction.getAnimationDirection(getDirection());
        if (getState() == State.STANDING) { // IDLE - NOT Walking
            currentFrame = (TextureRegion)animationMatrix[getActionState().ordinal()][animDirection.ordinal()].getKeyFrames()[0];
        }
        else if (getState() == State.WALKING) {
            animationMatrix[getActionState().ordinal()][animDirection.ordinal()].setPlayMode(Animation.PlayMode.LOOP);
            currentFrame = (TextureRegion) animationMatrix[getActionState().ordinal()][animDirection.ordinal()].getKeyFrame(stateTime, true);
        }
        if (currentFrame == null) {
            Gdx.app.log(this.getClass().getName(), "Frame not found state: " + getState() + " actionState: " + getActionState() + " Dir:" + animDirection.name());
        }

        batch.draw(currentFrame, getX(), getY());
    }

    public ActionState getActionState() {
        return actionState;
    }

    public void setActionState(ActionState state) {
        this.actionState = state;
    }

    /**
     * Setup simple animations (4 directional for idle, walk)
     */
    public void setupAnimations() {
        TextureRegion[][] framesRegions = TextureRegion.split(this.framesTexture, getWidth(), getHeight());

        int row = 0;
        for (int d = Direction.SOUTH.ordinal(); d <= Direction.WEST.ordinal(); d++) { // directions
            Object[] currentAnimFrames = new TextureRegion[4]; // hardcoded really?
            for (int col = 0; col < 4; col++) {
                currentAnimFrames[col] = framesRegions[row][col];
            }
            row++;
            float animSpeed = .14f; // maybe we need getSpeed() for animations?
            animationMatrix[ActionState.EMPTY_NORMAL.ordinal()][d] = new Animation(animSpeed, currentAnimFrames);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " ActionState: " + getActionState();
    }

    public void dispose() {
        // todo disposal
    }

}
