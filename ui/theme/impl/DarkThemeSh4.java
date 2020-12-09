package sh4ll.ui.theme.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import sh4ll.Shell;
import sh4ll.etc.IUpdatable;
import sh4ll.etc.RenderMethods;
import sh4ll.ui.DragMethod;
import sh4ll.ui.theme.ShellTheme;
import sh4ll.util.MinecraftFontRenderer;
import sh4ll.value.XValue;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DarkThemeSh4 extends ShellTheme {


    private int opacity;

    private MinecraftFontRenderer fontRenderer;
    private MinecraftFontRenderer biggerFont;

    private HashMap<String, Color> colors;
    private static int DRAGBAR_HEIGHT = 30;

    public DarkThemeSh4() {
        super("DarkTheme", DragMethod.TOP, DRAGBAR_HEIGHT);
        colors = new HashMap<>();
        colors.put("background", new Color(0, 0, 0, 255));
        colors.put("writing", new Color(255, 255, 255));
        colors.put("dragbar", new Color(57, 56, 56));
        opacity = 255;
    }

    

    @Override
    public void setup() {
        fontRenderer = new MinecraftFontRenderer(new Font("Consolas", Font.PLAIN, 16), false, false);
        biggerFont = new MinecraftFontRenderer(new Font("Consolas", Font.PLAIN, 18), false, false);
        Shell._self.values().put("shell_anim_x", new XValue().setValue((int) Shell._self.values().get("shell_x").getValue()));
        Shell._self.values().put("shell_anim_y", new XValue().setValue((int) Shell._self.values().get("shell_y").getValue()));
        registerUpdatable(new IUpdatable() {
            @Override
            public void update() {
                if (isDragging()) {
                    for (Map.Entry<String, Color> entry : colors.entrySet()) {
                        if (opacity > 200) {
                            opacity--;
                            colors.put(entry.getKey(), new Color(entry.getValue().getRed(), entry.getValue().getGreen(), entry.getValue().getBlue(), opacity));
                        }
                    }
                } else {
                    for (Map.Entry<String, Color> entry : colors.entrySet()) {
                        if (opacity < 255) {
                            opacity++;
                            colors.put(entry.getKey(), new Color(entry.getValue().getRed(), entry.getValue().getGreen(), entry.getValue().getBlue(), opacity));
                        }
                    }
                }

            }
        }, 10);
    }



    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        int shellX = (int) Shell._self.values().get("shell_x").getValue();
        int shellY = (int) Shell._self.values().get("shell_y").getValue();

        int shellWidth = (int) Shell._self.values().get("shell_width").getValue();
        int shellHeight = (int) Shell._self.values().get("shell_height").getValue();

        Gui.drawRect(shellX, shellY + DRAGBAR_HEIGHT, shellX + shellWidth, shellY + DRAGBAR_HEIGHT + shellHeight, colors.get("background").getRGB());
        RenderMethods.drawRoundedRect(shellX, shellY, shellWidth, DRAGBAR_HEIGHT, 15, colors.get("dragbar").getRGB());
          fontRenderer.drawString(Shell._self.getWritingInput().toString(), shellX + 5, shellY + shellHeight - 10, colors.get("writing").getRGB());

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        colors.put("writing", new Color(201, 201, 201));
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        // defaultDrag();
    }

    @Override
    public void update() {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}
