package sh4ll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import sh4ll.etc.StateResult;
import sh4ll.etc.Tuple;
import sh4ll.exec.Exec;
import sh4ll.exec.impl.ClearExec;
import sh4ll.exec.impl.HelpExec;
import sh4ll.ui.UIShell;
import sh4ll.ui.textblock.TextBlock;
import sh4ll.ui.textblock.def.NormalTextBlock;
import sh4ll.ui.theme.ShellTheme;
import sh4ll.ui.theme.impl.DarkThemeSh4;
import sh4ll.value.XValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Shell {

    private static int MAX_TEXTBLOCKS = 1500;
    private static int EXECUTE_HISTORY_LIMIT = 70;
    private UIShell shellUI;
    private boolean opened;
    private HashMap<String, XValue> values;
    private HashMap<String, XValue> dynamic;
    public static Shell _self = new Shell();
    private static boolean DEBUG;
    private ArrayList<Tuple<TextBlock, Boolean>> outputs;
    private ArrayList<Exec> registeredExecs;
    private ArrayList<String> executeHistory;
    private StringBuilder writingInput;
    private boolean nextInput;
    private String lastInput;

    public void setup(final String clientName, final String userName)
    {
        setup();
        dynamic().put("client_name", new XValue<String>().setValue(clientName));
        dynamic().put("client_username", new XValue<String>().setValue(userName));
    }

    public void setup(final String clientName) {
        setup(clientName, "user");
    }

    public void setup() {
        writingInput = new StringBuilder();
        registeredExecs = new ArrayList<>();
        values = new HashMap<>();
        dynamic = new HashMap<>();
        outputs = new ArrayList<>();
        executeHistory = new ArrayList<>();
        addDefaultValues();
        shellUI = new UIShell();

        /* default clear exec */ registerExec(new ClearExec());
        /* default help exec */ registerExec(new HelpExec());
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
        values().put("shell_width_min", new XValue<Integer>().setValue(137));
        values().put("shell_height", new XValue<Integer>().setValue((int) values().get("shell_width").getValue() / 12 * 7));
        values().put("shell_height_min", new XValue<Integer>().setValue(20));

        values().put("shell_x", new XValue<Integer>().setValue(-777));
        values().put("shell_y", new XValue<Integer>().setValue(-777));
        values().put("shell_scroll_location", new XValue<Integer>().setValue(0));
        values().put("shell_scrollbar_x", new XValue<Integer>().setValue(0));
        values().put("shell_scrollbar_y", new XValue<Integer>().setValue(0));



        dynamic().put("mouse_x", new XValue<Integer>().setValue(1));
        dynamic().put("mouse_y", new XValue<Integer>().setValue(1));
        dynamic().put("open_time", new XValue<Long>().setValue(System.currentTimeMillis()));
    }

    /** gets next line **/
    public String readLine() {
        /* if you are accessing this method within a exec you have to enable useThread */
        nextInput = true;
        while (nextInput) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return lastInput;
    }

    public char readChar() {
        return getShellUI().readChar();
    }

    public byte execute() {
        final String input = getWritingInput().toString();
        final String baseCommand = getBaseCommand(input);
        if (nextInput) {
            lastInput = input;
            nextInput = false;
            return StateResult.UNKNOWN_EXIT;
        }
        Shell._self.writeLine(new NormalTextBlock("\247d" + Shell._self.getShellUI().getCustomUserAlias() + "\2475 ~ \247f" + Shell._self.getWritingInput().toString()));

        /* adding to execute history */
        executeHistory.add(input);

        /* check if history has more than EXECUTE_HISTORY_LIMIT size */
        if (getExecuteHistory().size() >= EXECUTE_HISTORY_LIMIT) {
            getExecuteHistory().remove(0);          /* remove first element */
        }


        /** EXEC CHECK */

        final Exec exec = getExec(baseCommand);
        if (exec != null) {
            exec.setup();

            if (input.contains(" ")) {
                int i = 0;
                final String[] splitted = input.substring(baseCommand.length() + 1).split(" ");
                for (String str : splitted) {
                    if (str.startsWith("--"))
                        exec.getSettings().add(str.replace("--", ""));
                }
                String[] arrayOfString1;
                int j;
                byte b;
                for (arrayOfString1 = exec.getExpectedInputs(), j = arrayOfString1.length, b = 0; b < j; ) {
                    String exceptedInput = arrayOfString1[b];
                    if (splitted.length > i && !splitted[i].startsWith("--")) {
                        exec.getInputs().put(exceptedInput, splitted[i]);
                        i++;
                        b++;
                    }
                }
                if (exec.isUseThread()) {
                    (new Thread(new Runnable() {
                        public void run() {
                            exec.runExec(input, splitted);
                        }
                    })).start();
                } else {
                    exec.runExec(input, splitted);
                }
            } else if (exec.isUseThread()) {
                (new Thread(new Runnable() {
                    public void run() {
                        exec.runExec(input, new String[0]);
                    }
                })).start();
            } else {
                return exec.runExec(input, new String[0]);
            }

        }
        /** ========== **/

        return StateResult.UNKNOWN_EXIT;
    }

    public void write(final TextBlock block) {
        outputs().add(new Tuple<TextBlock, Boolean>(block, false));
    }
    public void writeLine(final TextBlock block) {
        outputs().add(new Tuple<TextBlock, Boolean>(block, true));
    }

    public String[] split(String input) {
        return input.contains(" ") ? input.split(" ") : new String[]{input};
    }

    public String getBaseCommand(String input) {
        if (input.contains(" ")) {
            return input.split(" ")[0];
        }
        return input;
    }

    public Exec getExec(Class clazz) {
        for (Exec exec : Shell._self.getRegisteredExecs()) {
            if (exec.getClass() == clazz) {
                return exec;
            }
        }
        return /* Maybe NotFoundExec? */ null;
    }

    public Exec getExec(String alias) {
        for (Exec exec : Shell._self.getRegisteredExecs()) {
            for (String execAlias : exec.getTriggers()) {
                if (execAlias.equalsIgnoreCase(alias)) {
                    return exec;
                }
            }
        }
        return /* Maybe NotFoundExec */ null;
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

    public boolean capturingNextInput() {
        return nextInput;
    }

    public void stopCapturing() {
        nextInput = false;
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

    public void unregisterAllExecs() { registeredExecs.clear(); }

    public ArrayList<Tuple<TextBlock, Boolean>> outputs() {
        return outputs;
    }

    public UIShell getShellUI() {
        return shellUI;
    }

    public ArrayList<String> getExecuteHistory() {
        return executeHistory;
    }

    public StringBuilder getWritingInput() {
        return writingInput;
    }

    public void setWritingInput(StringBuilder writingInput) {
        this.writingInput = writingInput;
    }

    public void registerExec(Exec ex) {
        registeredExecs.add(ex);
    }

    public ArrayList<Exec> getRegisteredExecs() {
        return registeredExecs;
    }
}
