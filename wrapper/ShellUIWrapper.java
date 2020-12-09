package sh4ll.wrapper;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import sh4ll.Shell;
import sh4ll.util.MinecraftFontRenderer;

import java.io.IOException;
import java.security.Key;

public class ShellUIWrapper extends GuiScreen {

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
        if (keyCode == Keyboard.KEY_ESCAPE) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void updateScreen() {

        if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && Shell._self.getWritingInput().length() > 0) {
            eraseTick++;
            if (eraseTick == 1 || eraseTick > 4) {
                Shell._self.getWritingInput().deleteCharAt(Shell._self.getWritingInput().length() - 1);
            }
        } else {
            eraseTick = 0;
        }
    }

    @Override
    public void initGui() {

    }

}
