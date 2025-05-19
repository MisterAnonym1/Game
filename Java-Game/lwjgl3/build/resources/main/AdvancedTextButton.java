public class AdvancedTextButton extends TextButton {
    private Runnable onClick;  // Wird einmal beim Klick ausgeführt
    private Runnable onHold;   // Wird durchgehend ausgeführt, solange gedrückt
    private Runnable onUp;     // Wird ausgeführt, wenn losgelassen
    private Consumer<Boolean> onCheck; // Checkbox-Funktion
    private boolean isChecked = false;
    private boolean isPressed = false;

    public AdvancedTextButton(String text, Skin skin, float x, float y, float scale) {
        super(text, skin);
        setPosition(x, y);
        setSize(getWidth() * scale, getHeight() * scale);
        toFront();

        addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isDisabled()) return false;
                onMouseDown();
                if (onClick != null) onClick.run();
                return true; // WICHTIG: Muss `true` zurückgeben, damit `touchUp()` ausgelöst wird
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isChecked = !isChecked;
                if (onUp != null) onUp.run();
                if (onCheck != null) onCheck.accept(isChecked);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                onMouseEnter();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                onMouseLeave();
            }
        });
    }

    public void onMouseEnter() {
        setColor(0.7f, 0.7f, 0.7f, 1);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
    }

    public void onMouseLeave() {
        setColor(1f, 1f, 1f, 1);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        if (isPressed) onMouseEnter();
        isPressed = false;
    }

    public void onMouseDown() {
        isPressed = true;
        setColor(0.5f, 0.5f, 0.5f, 1);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isDisabled()) return;
        if (isPressed) {
            onMouseDown();
            if (onHold != null) onHold.run();
        }
    }

    public void setOnClick(Runnable action) { this.onClick = action; }
    public void setOnHold(Runnable action) { this.onHold = action; }
    public void setOnCheck(Consumer<Boolean> action) { this.onCheck = action; }
    public void setOnUp(Runnable action) { this.onUp = action; }
}
