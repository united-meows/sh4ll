package sh4ll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import sh4ll.ui.UIShell;
import sh4ll.ui.textblock.TextBlock;
import sh4ll.ui.theme.ShellTheme;
import sh4ll.ui.theme.impl.DarkThemeSh4;
import sh4ll.value.XValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shell {

    private UIShell shellUI;
    private boolean opened;
    private HashMap<String, XValue> values;
    private HashMap<String, XValue> dynamic;
    public static Shell _self = new Shell();
    private static boolean DEBUG;
    private ArrayList<TextBlock> outputs;


    private StringBuilder writingInput;

    public void setup(final String clientName, final String userName)
    {
        writingInput = new StringBuilder();
        setup();
        dynamic().put("client_name", new XValue<String>().setValue(clientName));
        dynamic().put("client_username", new XValue<String>().setValue(userName));
    }
    //ipana coder best noob
    public void setup(final String clientName) {
        setup(clientName, "user");
    }

    public void setup() {
        values = new HashMap<>();
        dynamic = new HashMap<>();
        outputs = new ArrayList<>();
        addDefaultValues();
        shellUI = new UIShell();

        setTheme(new DarkThemeSh4());

    }

    public void setTheme(ShellTheme theme) {
        shellUI.setTheme(theme);
    }

    /**
     * Adds default values to prevent nullpointerexception
     */
    public void addDefaultValues() {
        values().put("shell_width", new XValue<Integer>().setValue(350));
        values().put("shell_height", new XValue<Integer>().setValue(
                (int) values().get("shell_width").getValue() / 12 * 7));

        values().put("shell_x", new XValue<Integer>().setValue(-777));
        values().put("shell_y", new XValue<Integer>().setValue(-777));


        dynamic().put("mouse_x", new XValue<Integer>().setValue(1));
        dynamic().put("mouse_y", new XValue<Integer>().setValue(1));
        dynamic().put("open_time", new XValue<Long>().setValue(System.currentTimeMillis()));


    }

    public void open() {
        opened = true;
        dynamic().get("open_time").setValue(System.currentTimeMillis());
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        if ((int) values().get("shell_x").getValue() == -777) {
            values().put("shell_x", new XValue<Integer>().setValue((int)(scaledresolution.getScaledWidth() / 2
            - (int)values().get("shell_width").getValue() / 2)));
        }

        if ((int) values().get("shell_y").getValue() == -777) {
            values().put("shell_y", new XValue<Integer>().setValue((int)(scaledresolution.getScaledHeight() / 2
                    - (int)values().get("shell_height").getValue() / 2)));
        }
        shellUI.onOpen();
    }


    public static void setDebug(boolean DEBUG) {
        Shell.DEBUG = DEBUG;
    }

    public static boolean isDebug() {
        return DEBUG;
    }

    public void close() {
        opened = false;
        shellUI.onClose();
    }

    public boolean isOpen() {
        return opened;
    }

    public HashMap<String, XValue> values() {
        return values;
    }

    public HashMap<String, XValue> dynamic() {
        return dynamic;
    }

    public int getX() {
        return (int) values().get("shell_x").getValue();
    }

    public int getY() {
        return (int) values().get("shell_y").getValue();
    }

    public int getWidth() {
        return (int) values().get("shell_width").getValue();
    }

    public int getHeight() {
        return (int) values().get("shell_height").getValue();
    }


    public ArrayList<TextBlock> outputs() {
        return outputs;
    }

    public UIShell getShellUI() {
        return shellUI;
    }

    public StringBuilder getWritingInput() {
        return writingInput;
    }
}
