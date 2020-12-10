package sh4ll.ui;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import sh4ll.Shell;
import sh4ll.ui.theme.ShellTheme;
import sh4ll.wrapper.ShellUIWrapper;


public class UIShell {

    private static String ALLOWED_CHARS = " ;.,abcdefgğhıijklmnoöprsştuüvyzwxqABCDEFGHIİJKLMNOÖPRSŞTUÜVYZXQW0123456789?_-~^$#-+%&@!'\"€₺æ/`\\()[]{}";

    private ShellTheme theme;
    private ShellUIWrapper wrapper;
    private int cursorPos;

    public void onOpen() {
        if (wrapper == null) {
            wrapper = new ShellUIWrapper();
        }
        Minecraft.getMinecraft().displayGuiScreen(wrapper);
        theme.onEnable();
    }

    public void onClose() {
        theme.onDisable();
    }


    public void keyTyped(char typedChar, int keyCode) {

        // Update last key press time
        getTheme().updateKeyPress();
        if (keyCode == Keyboard.KEY_LEFT) {
            if (cursorPos > 0)
                cursorPos--;
        } else if (keyCode == Keyboard.KEY_RIGHT) {
            if (cursorPos < Shell._self.getWritingInput().toString().length())
                cursorPos++;
        }
        if (ALLOWED_CHARS.contains(String.valueOf(typedChar))) {
            if (cursorPos == Shell._self.getWritingInput().toString().length()) {
                Shell._self.getWritingInput().append(typedChar);

            } else {
                String str = Shell._self.getWritingInput().toString();
                Shell._self.getWritingInput().delete(0, Shell._self.getWritingInput().length());
                Shell._self.getWritingInput().append(str, 0, cursorPos);
                Shell._self.getWritingInput().append(typedChar);
                Shell._self.getWritingInput().append(str, cursorPos, str.length());
            }
            cursorPos++;
            return;
        }

        if (Shell._self.getWritingInput().length() > 0) {
            if (keyCode == Keyboard.KEY_RETURN) {
                cursorPos=0;
                Shell._self.getWritingInput().delete(0, Shell._self.getWritingInput().length());
            }
        }
    }

    /** gets current theme */
    public ShellTheme getTheme() {
        return theme;
    }

    public int getCursorPos() {
        return cursorPos;
    }

    public void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }

    public void setTheme(ShellTheme newTheme) {
        if (theme != newTheme) {
            newTheme.setup();
            this.theme = newTheme;
        }
    }
}
