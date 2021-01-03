package sh4ll.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import sh4ll.Shell;
import sh4ll.ui.textblock.def.NormalTextBlock;
import sh4ll.ui.theme.ShellTheme;
import sh4ll.wrapper.ShellUIWrapper;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;


public class UIShell {

    private static final String ALLOWED_CHARS = " ;.,abcdefgğhıijklmnoöprsştuüvyzwxqABCDEFGHIİJKLMNOÖPRSŞTUÜVYZXQW0123456789?_-~^$#-+%&@!'\"€₺æ/`\\()[]{}";

    private ShellTheme theme;
    private ShellUIWrapper wrapper;
    private int cursorPos;
    private int scroll;
    private int historyIndex;
    private boolean canPosScroll,canNegScroll;
    private char lastChar;
    private boolean nextChar;

    private static final String USER_ALIAS = "$user@$client";

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

    // ipana abi nolur yorum satiri koy
    public void keyTyped(char typedChar, int keyCode) {

        // Update last key press time
        getTheme().updateKeyPress();
//        if (keyCode == Keyboard.KEY_LEFT) {
//            if (cursorPos > 0)
//                cursorPos--;
//        } else if (keyCode == Keyboard.KEY_RIGHT) {
////            if (cursorPos < Shell._self.getWritingInput().toString().length())
////                cursorPos++;
//        }
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
            if (nextChar) {
                lastChar = typedChar;
                nextChar = false;
            }
            cursorPos++;
            return;
        }




        if (Shell._self.getWritingInput().length() > 0) {
            if (keyCode == Keyboard.KEY_RETURN) {
                //TODO: Change this to custom textBlock for command input
                 Shell._self.execute();
                cursorPos=0;
                scroll = 0; /* reset scroll location */
                historyIndex = Shell._self.getExecuteHistory().size() -1;
                Shell._self.getWritingInput().delete(0, Shell._self.getWritingInput().length());
            }
        }
        // Copy from shell
        if (keyCode == Keyboard.KEY_C && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
            String copyText = getTheme().selection();
            if (copyText.length() > 0) {

            }
        }

        // Paste
        if (keyCode == Keyboard.KEY_V && (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))) {
            String copyText = getTheme().selection();
            if (copyText.length() > 0) {
                Shell._self.getWritingInput().append(copyText);
                cursorPos += StringUtils.stripControlCodes(copyText).length();
            }
        }


    }

    public void historyUp() {
        if (--historyIndex < 0) {
            historyIndex = Shell._self.getExecuteHistory().size() -1;
        }
        if (!Shell._self.getExecuteHistory().isEmpty()) {
            Shell._self.setWritingInput(new StringBuilder(Shell._self.getExecuteHistory().get(historyIndex)));
            cursorPos = Shell._self.getWritingInput().length();
        }
    }

    public void historyDown() {
        if (++historyIndex >= Shell._self.getExecuteHistory().size()) {
            historyIndex = 0;
        }
        if (!Shell._self.getExecuteHistory().isEmpty()) {
            Shell._self.setWritingInput(new StringBuilder(Shell._self.getExecuteHistory().get(historyIndex)));
            cursorPos = Shell._self.getWritingInput().length();
        }
    }

    public void copySelected(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }





    public void onPostRender() {
        int dWheel = Mouse.getDWheel();
        int mouse = dWheel > 0 ? 10 : dWheel < 0 ? -10 : 0;
        if ((canPosScroll() && mouse < 0) || (canNegScroll() && mouse > 0)) {
            scroll += mouse;
        }
    }

    /** gets current theme */
    public ShellTheme getTheme() {
        return theme;
    }

    public int getCursorPos() {
        return cursorPos;
    }

    public void incrementCursorPos() {
        cursorPos++;
    }

    public char readChar() {
        nextChar = true;
        while (nextChar) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return lastChar;
    }

    public void decreaseCursorPos(){
        cursorPos--;
    }

    public void setCursorPos(int cursorPos) {
        this.cursorPos = cursorPos;
    }

    public boolean isCursorAtLastChar() {
        return cursorPos == Shell._self.getWritingInput().length();
    }

    public String getCustomUserAlias() {
        return USER_ALIAS.replace("$user", (String) Shell._self.dynamic().get("client_username").getValue()).replace("$client", (String) Shell._self.dynamic().get("client_name").getValue());
    }

    public void setTheme(ShellTheme newTheme) {
        if (theme != newTheme) {
            newTheme.setup();
            this.theme = newTheme;
        }
    }

    public boolean canPosScroll() {
        return canPosScroll;
    }

    public void setCanPosScroll(boolean canPosScroll) {
        this.canPosScroll = canPosScroll;
    }

    public boolean canNegScroll() {
        return canNegScroll;
    }

    public void setCanNegScroll(boolean canNegScroll) {
        this.canNegScroll = canNegScroll;
    }

    public int getScroll() {
        return scroll;
    }

    public void setScroll(int scroll) {
        this.scroll = scroll;
    }
}
