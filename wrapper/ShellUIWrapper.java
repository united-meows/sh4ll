package sh4ll.wrapper;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import sh4ll.Shell;
import sh4ll.etc.RepeatableKey;

public class ShellUIWrapper extends GuiScreen {

    private ArrayList<RepeatableKey> repeatableKeys;

    public ShellUIWrapper() {
        repeatableKeys = new ArrayList<>();
        repeatableKeys.add(new RepeatableKey(Keyboard.KEY_BACK) {
            @Override
            public void action() {
                if (Shell._self.getWritingInput().length() > 0) {
                    Shell._self.getWritingInput().deleteCharAt(Math.max(0, Shell._self.getShellUI().getCursorPos() - 1));
                    Shell._self.getShellUI().setCursorPos(Shell._self.getShellUI().getCursorPos() - 1);
                }
            }
        });
        repeatableKeys.add(new RepeatableKey(Keyboard.KEY_RIGHT) {
            @Override
            public void action() {
                if (Shell._self.getShellUI().getCursorPos() < Shell._self.getWritingInput().length())
                    Shell._self.getShellUI().incrementCursorPos();
            }
        });
        repeatableKeys.add(new RepeatableKey(Keyboard.KEY_LEFT) {
            @Override
            public void action() {
                if (Shell._self.getShellUI().getCursorPos() > 0)
                    Shell._self.getShellUI().decreaseCursorPos();
            }
        });
    }

    @Override
    public void onGuiClosed() {
        Shell._self.close();
    }

    private int eraseTick;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Shell._self.dynamic().get("mouse_x").setValue(mouseX);
        Shell._self.dynamic().get("mouse_y").setValue(mouseY);
        if (Shell._self.getShellUI().getTheme().isDragging()) {
            Shell._self.getShellUI().getTheme()._clickDrag(mouseX, mouseY);
        }

        Shell._self.getShellUI().getTheme().draw(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        Shell._self.dynamic().get("mouse_x").setValue(mouseX);
        Shell._self.dynamic().get("mouse_y").setValue(mouseY);
        Shell._self.getShellUI().getTheme()._mouseClick(mouseX, mouseY, mouseButton);
        Shell._self.getShellUI().getTheme().mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        Shell._self.dynamic().get("mouse_x").setValue(mouseX);
        Shell._self.dynamic().get("mouse_y").setValue(mouseY);
        Shell._self.getShellUI().getTheme()._mouseRelease(mouseX, mouseY, state);
        Shell._self.getShellUI().getTheme().mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        Shell._self.dynamic().get("mouse_x").setValue(mouseX);
        Shell._self.dynamic().get("mouse_y").setValue(mouseY);
        Shell._self.getShellUI().getTheme().mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        Shell._self.getShellUI().keyTyped(typedChar, keyCode);


        //TODO: Check later cursor pos
        Shell._self.getShellUI().setCursorPos(Math.min(Shell._self.getShellUI().getCursorPos(),Shell._self.getWritingInput().toString().length()));
        Shell._self.getShellUI().setCursorPos(Math.max(Shell._self.getShellUI().getCursorPos(),0));

        if (keyCode == Keyboard.KEY_ESCAPE) {
            super.keyTyped(typedChar, keyCode);
        }


    }

    @Override
    public void updateScreen() {

        for (RepeatableKey repeatableKey : repeatableKeys) {
            repeatableKey.setPressed(Keyboard.isKeyDown(repeatableKey.getKeyCode()));
            if (repeatableKey.isPressed()) {
                repeatableKey.update();
            }
        }
        // Update theme's 'update()' function
        Shell._self.getShellUI().getTheme().update();
    }

    @Override
    public void initGui() {

    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
