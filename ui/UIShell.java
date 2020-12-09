package sh4ll.ui;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import sh4ll.Shell;
import sh4ll.ui.theme.ShellTheme;
import sh4ll.wrapper.ShellUIWrapper;


public class UIShell {

    private static String ALLOWED_CHARS = " abcdefgğhıijklmnoöprsştuüvyzwxABCDEFGHIİJKLMNOÖPRSŞTUÜVYZXW0123456789_-~^$#-+%&@!'\"€₺æ/`\\()[]{}";

    private ShellTheme theme;
    private ShellUIWrapper wrapper;

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
        if (ALLOWED_CHARS.contains(String.valueOf(typedChar))) {
            Shell._self.getWritingInput().append(typedChar);
            return;
        }

        if (Shell._self.getWritingInput().length() > 0) {
            if (keyCode == Keyboard.KEY_RETURN) {         	
                Shell._self.getWritingInput().delete(0, Shell._self.getWritingInput().length());
            }
        }
    }

    public ShellTheme getTheme() {
        return theme;
    }

    public void setTheme(ShellTheme newTheme) {
        if (theme != newTheme) {
            newTheme.setup();
            this.theme = newTheme;
        }
    }
}
