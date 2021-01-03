package sh4ll.exec;

import sh4ll.Shell;
import sh4ll.etc.StateResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Exec {
    private String[] expectedInputs;
    private String[] triggers;
    private String description;
    private HashMap<String, String> inputs;
    private List<String> settings;
    private boolean useThread;

    public Exec(String[] _triggers, String[] _expectedInputs, String _description) {
        triggers = _triggers;
        expectedInputs = _expectedInputs;
        description = _description;

        inputs = new HashMap<>();
        settings = new ArrayList<>();
    }

    public Exec(String[] _triggers, String[] _expectedInputs, String _description, boolean _useThread) {
        this(_triggers, _expectedInputs, _description);
        useThread = _useThread;
    }

    public void setup() {
        inputs.clear();
        settings.clear();
    }

    public abstract byte /* StateResult */ runExec(String fullText, String[] splitted);
    public abstract String usage();

    public String getInput(final String name) {
        if (hasInput(name)) {
            return inputs.get(name);
        }
        return null;
    }


    public boolean hasInput(final String name) {
        return inputs.containsKey(name);
    }

    public void clear() {
    }


    public HashMap<String, String> getInputs() {
        return inputs;
    }

    public List<String> getSettings() {
        return settings;
    }

    public boolean checkSetting(final String name) {
        return settings.contains(name);
    }

    public String getDescription() {
        return description;
    }

    public String[] getExpectedInputs() {
        return expectedInputs;
    }

    public boolean isUseThread() {
        return useThread;
    }

    public String[] getTriggers() {
        return triggers;
    }

    protected Exec getExec(Class clazz) {
        for (Exec exec : Shell._self.getRegisteredExecs()) {
            if (exec.getClass() == clazz) {
                return exec;
            }
        }
        return null;
    }

    protected Exec getExec(String trigger) {
        for (Exec exec : Shell._self.getRegisteredExecs()) {
            for (String execTrigger : exec.getTriggers()) {
                if (execTrigger.equalsIgnoreCase(trigger)) {
                    return exec;
                }
            }
        }
        return null;
    }

}
