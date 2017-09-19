package com.binarybrains.sprout.hud;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ObjectMap;
import com.binarybrains.sprout.SproutGame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;


public class TypeWriterDialog extends Window {
    Table contentTable, buttonTable;
    private Skin skin;
    ObjectMap<Actor, Object> values = new ObjectMap();
    boolean cancelHide;
    Actor previousKeyboardFocus, previousScrollFocus;
    FocusListener focusListener;
    private float stringCompleteness = 0;
    Label textLabel;

    String dialogText = "I have nothing to say.";
    Boolean skipTypeWriter = false;
    int charCountLastFrame = 0;

    protected InputListener ignoreTouchDown = new InputListener() {
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            event.cancel();
            return false;
        }
    };

    public TypeWriterDialog (String title, Skin skin) {
        super(title, skin.get(WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        initialize();
    }

    public TypeWriterDialog (String title, Skin skin, String windowStyleName) {
        super(title, skin.get(windowStyleName, WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        initialize();
    }

    public TypeWriterDialog (String title, WindowStyle windowStyle) {
        super(title, windowStyle);
        initialize();
    }

    public void act (float delta) {
        stringCompleteness += 40f * delta;
        int charCountThisFrame = (int)stringCompleteness;
        if (skipTypeWriter) {
            charCountThisFrame = dialogText.length();
        }
        if (charCountThisFrame > charCountLastFrame && charCountThisFrame < dialogText.length() && charCountThisFrame % 2 == 0) {
            SproutGame.playSound("speaker_blip", .34f, MathUtils.random(0.5f, 1.2f), 1f);
            //System.out.println("play" + charCountThisFrame);
        }

        if (charCountThisFrame > dialogText.length())
        {
            charCountThisFrame = dialogText.length();
        }
        if (textLabel != null) {
            textLabel.setText(dialogText.substring(0, charCountThisFrame));
        }

        charCountLastFrame = charCountThisFrame;
        super.act(delta);
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    private void initialize () {
        setModal(true);
        setMovable(false);

        defaults().space(32);
        // this should also have a profile pic of the NPC
        contentTable = new Table(skin);
        //add(new Label("icon", skin)).padLeft(30).width(100);
        add(contentTable).expand().fill();
        row();
        add(buttonTable = new Table(skin));


        buttonTable.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                if (!values.containsKey(actor)) return;
                while (actor.getParent() != buttonTable)
                    actor = actor.getParent();
                result(values.get(actor));
                if (!cancelHide) hide();
                cancelHide = false;
            }
        });

        focusListener = new FocusListener() {
            public void keyboardFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                if (!focused) focusChanged(event);
            }

            public void scrollFocusChanged (FocusEvent event, Actor actor, boolean focused) {
                if (!focused) focusChanged(event);
            }

            private void focusChanged (FocusEvent event) {
                Stage stage = getStage();
                if (stage != null && stage.getRoot().getChildren().size > 0
                        && stage.getRoot().getChildren().peek() == TypeWriterDialog.this) { // Dialog is top most actor.
                    Actor newFocusedActor = event.getRelatedActor();
                    if (newFocusedActor != null && !newFocusedActor.isDescendantOf(TypeWriterDialog.this) &&
                            !(newFocusedActor.equals(previousKeyboardFocus) || newFocusedActor.equals(previousScrollFocus)) )
                        event.cancel();
                }
            }
        };
    }

    protected void setStage (Stage stage) {
        if (stage == null)
            addListener(focusListener);
        else
            removeListener(focusListener);
        super.setStage(stage);
    }

    public Table getContentTable () {
        return contentTable;
    }

    public Table getButtonTable () {
        return buttonTable;
    }

    /** Adds a label to the content table. The dialog must have been constructed with a skin to use this method. */
    public TypeWriterDialog text (String text) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the tp-dialog was constructed with a Skin.");
        return text(text, skin.get(Label.LabelStyle.class));
    }

    /** Adds a label to the content table. */
    public TypeWriterDialog text (String text, Label.LabelStyle labelStyle) {
        return text(new Label(text, labelStyle));
    }

    /** Adds the given Label to the content table */
    public TypeWriterDialog text (Label label) {
        textLabel = label;
        textLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skipTypeWriter = true;
            };
        });
        dialogText = "" + label.getText();
        contentTable.align(Align.topLeft);
        contentTable.add(textLabel);
        return this;
    }

    /** Adds a text button to the button table. Null will be passed to {@link #result(Object)} if this button is clicked. The dialog
     * must have been constructed with a skin to use this method. */
    public TypeWriterDialog button (String text) {
        return button(text, null);
    }

    /** Adds a text button to the button table. The dialog must have been constructed with a skin to use this method.
     * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
    public TypeWriterDialog button (String text, Object object) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the dialog was constructed with a Skin.");
        return button(text, object, skin.get(TextButton.TextButtonStyle.class));
    }

    /** Adds a text button to the button table.
     * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
    public TypeWriterDialog button (String text, Object object, TextButton.TextButtonStyle buttonStyle) {
        return button(new TextButton(text, buttonStyle), object);
    }

    /** Adds the given button to the button table. */
    public TypeWriterDialog button (Button button) {
        return button(button, null);
    }

    /** Adds the given button to the button table.
     * @param object The object that will be passed to {@link #result(Object)} if this button is clicked. May be null. */
    public TypeWriterDialog button (Button button, Object object) {
        buttonTable.add(button);
        setObject(button, object);
        return this;
    }

    /** {@link #pack() Packs} the dialog and adds it to the stage with custom action which can be null for instant show */
    public TypeWriterDialog show (Stage stage, Action action) {
        clearActions();
        removeCaptureListener(ignoreTouchDown);

        previousKeyboardFocus = null;
        Actor actor = stage.getKeyboardFocus();
        if (actor != null && !actor.isDescendantOf(this)) previousKeyboardFocus = actor;

        previousScrollFocus = null;
        actor = stage.getScrollFocus();
        if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;

        pack();
        stage.addActor(this);
        stage.setKeyboardFocus(this);
        stage.setScrollFocus(this);
        if (action != null) addAction(action);

        return this;
    }

    /** {@link #pack() Packs} the dialog and adds it to the stage, centered with default fadeIn action */
    public TypeWriterDialog show (Stage stage) {
        show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
        setSize(stage.getWidth() / 2, 200);
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
        stringCompleteness = 0;
        return this;
    }

    /** Hides the dialog with the given action and then removes it from the stage. */
    public void hide (Action action) {
        Stage stage = getStage();
        if (stage != null) {
            removeListener(focusListener);
            if (previousKeyboardFocus != null && previousKeyboardFocus.getStage() == null) previousKeyboardFocus = null;
            Actor actor = stage.getKeyboardFocus();
            if (actor == null || actor.isDescendantOf(this)) stage.setKeyboardFocus(previousKeyboardFocus);

            if (previousScrollFocus != null && previousScrollFocus.getStage() == null) previousScrollFocus = null;
            actor = stage.getScrollFocus();
            if (actor == null || actor.isDescendantOf(this)) stage.setScrollFocus(previousScrollFocus);
        }
        if (action != null) {
            addCaptureListener(ignoreTouchDown);
            addAction(sequence(action, Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
        } else
            remove();
    }

    /** Hides the dialog. Called automatically when a button is clicked. The default implementation fades out the dialog over 400
     * milliseconds and then removes it from the stage. */
    public void hide () {
        hide(sequence(fadeOut(0.4f, Interpolation.fade), Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
    }

    public void setObject (Actor actor, Object object) {
        values.put(actor, object);
    }

    /** If this key is pressed, {@link #result(Object)} is called with the specified object.
     * @see Input.Keys */
    public TypeWriterDialog key (final int keycode, final Object object) {
        addListener(new InputListener() {
            public boolean keyDown (InputEvent event, int keycode2) {
                if (keycode == keycode2) {
                    result(object);
                    if (!cancelHide) hide();
                    cancelHide = false;
                }
                return false;
            }
        });
        return this;
    }

    /** Called when a button is clicked. The dialog will be hidden after this method returns unless {@link #cancel()} is called.
     * @param object The object specified when the button was added. */
    protected void result (Object object) {
    }

    public void cancel () {
        cancelHide = true;
    }
}
