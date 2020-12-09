package sh4ll.ui.theme;

import sh4ll.Shell;
import sh4ll.etc.IUpdatable;
import sh4ll.etc.Tuple;
import sh4ll.ui.DragMethod;
import sh4ll.util.MinecraftFontRenderer;
import sh4ll.value.XValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ShellTheme {

    private final String name;
    protected boolean drag;
    protected int lastX;
    protected int lastY;
    private DragMethod dragMethod;
    private int dragBarHeight;
    private ArrayList<Tuple<Integer, IUpdatable>> updatableHolder;
    private ArrayList<Thread> threads;
    /**
     * if you have custom dragbar's set dragmethod to custom and topBarHeight to 0
     * and define your drag area with 'isOverDragArea(mouseX, mouseY)' method
     * @param _name
     * @param _dragMethod
     * @param _dragBarHeight
     */
    public ShellTheme(final String _name, DragMethod _dragMethod, int _dragBarHeight) {
        name = _name;
        dragMethod = _dragMethod;
        dragBarHeight = _dragBarHeight;
        updatableHolder = new ArrayList<Tuple<Integer, IUpdatable>>();
        threads = new ArrayList<>();
    }

    /**
     * Preset values like
     * Prefix: 'theme_' is suggested but not required
     * theme_back_color
     * theme_shell_opacity
     * theme_pointer_color
     */
    private HashMap<String, XValue> _presets;
    public HashMap<String, XValue> presets() { return _presets; }

    /**
     * Thread list of updatables
     */






    /**
     * You can use this fontRenderer
     * private MinecraftFontRenderer fontRenderer;
     */

    /**
     * Registers updatable's with updaterate
     * updaterate is in miliseconds
     * ex:
     * 1000 = 1 second
     * 500 = 500 ms
     * 60 * 1000 = 1 minute
     * @param updatable
     * @param updateRate
     */
    public void registerUpdatable(IUpdatable updatable, int updateRate) {
       updatableHolder.add(new Tuple<>(updateRate, updatable));
    }

    public boolean isOverDragBar(final int mouseX, final int mouseY) {
        switch (dragMethod) {
            case ALL: {
                return mouseX >= Shell._self.getX() && mouseX <= Shell._self.getX() + Shell._self.getWidth()
                        && mouseY >= Shell._self.getY() && mouseY <=  Shell._self.getY() + Shell._self.getHeight();
            }
            case TOP: {
                return mouseX >= Shell._self.getX() && mouseX <= Shell._self.getX() + Shell._self.getWidth()
                        && mouseY >= Shell._self.getY() && mouseY <= Shell._self.getY() + Shell._self.getY() + dragBarHeight;
            }
            case BOTTOM: {
                return mouseX >= Shell._self.getX() && mouseX <= Shell._self.getX() + Shell._self.getWidth()
                        && mouseY >= Shell._self.getY() + Shell._self.getHeight() - dragBarHeight && mouseY <= Shell._self.getX() + Shell._self.getHeight();
            }
        }
        return false;
    }

    /**
        on disable
     **/
    public void onDisable() {
        for (Thread thread : threads) {
            try {
                thread.stop();
            } catch (Exception ex) {}
        }
        threads.clear();
    }

    /**
        on enable
     **/
    public void onEnable() {
       for (Tuple<Integer, IUpdatable> updatableTuple : updatableHolder) {
           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   while (Shell._self.isOpen()) {
                       updatableTuple.getSecond().update();
                       try {
                           Thread.sleep(updatableTuple.getFirst());
                       } catch (InterruptedException ex) {

                       }
                   }
               }
           });
           thread.start();
           threads.add(thread);
       }
    }



    /**
        gets called right before gui opened
     **/
    public abstract void setup();

    /**
        you can also get mouse values with
        Shell instance
        dynamic mouse_x mouse_y
     **/
    public abstract void draw(int mouseX, int mouseY, float partialTicks);

    /**
     * Gets called when mouse is clicked
     * @param mouseX mouse location x
     * @param mouseY mouse location y
     * @param mouseButton mouse button
     */
    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    /**
     * Gets called when mouse is released
     * @param mouseX mouse location x
     * @param mouseY mouse location y
     * @param state mouse button
     */
    public abstract void mouseReleased(int mouseX, int mouseY, int state);

    /**
     * Gets called when mouse click-drag
     * @param mouseX mouse location x
     * @param mouseY mouse location y
     * @param clickedMouseButton mouse button
     * @param timeSinceLastClick -
     */
    public abstract void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);

    /**
     * method from Minecraft's GuiScreen class
     */
    public abstract void update();

    public void _clickDrag(int mouseX, int mouseY) {
        Shell._self.values().get("shell_x").setValue(Shell._self.getX() + (mouseX - lastX));
        Shell._self.values().get("shell_y").setValue(Shell._self.getY() + (mouseY - lastY));
        lastX = mouseX;
        lastY = mouseY;
    }

    public void _mouseClick(int mouseX, int mouseY, int mouseButton) {

        if (isOverDragBar(mouseX, mouseY)) {
            lastX = mouseX;
            lastY = mouseY;
            drag = true;
        }
    }

    public boolean isDragging() {
        return drag;
    }

    public void _mouseRelease(int mouseX, int mouseY, int mouseButton) {
        drag = false;
    }
}
