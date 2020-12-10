package sh4ll.etc;

public abstract class RepeatableKey {

    private final int keyCode;
    private int pressedTime;
    private boolean pressed;

    public RepeatableKey(final int _keyCode) {
        keyCode = _keyCode;
    }

    public abstract void action();


    public void update() {
        pressedTime++;
        if (pressedTime == 1 || pressedTime > 4) {
            action();
        }
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
        if (!this.pressed) {
            pressedTime = 0;
        }
    }

    public int getKeyCode() {
        return keyCode;
    }

    public boolean isPressed() {
        return pressed;
    }
}
