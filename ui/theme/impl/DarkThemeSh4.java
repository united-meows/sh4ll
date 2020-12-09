package sh4ll.ui.theme.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import sh4ll.Shell;
import sh4ll.etc.IUpdatable;
import sh4ll.etc.RenderMethods;
import sh4ll.ui.DragMethod;
import sh4ll.ui.theme.ShellTheme;
import sh4ll.util.MinecraftFontRenderer;
import sh4ll.value.XValue;

public class DarkThemeSh4 extends ShellTheme {

	private int opacity;

	private MinecraftFontRenderer fontRenderer;
	private MinecraftFontRenderer biggerFont;
	private MinecraftFontRenderer titleFont;

	private HashMap<String, Color> colors;
	private static int DRAGBAR_HEIGHT = 20;
	private int cursorTicks;

	public DarkThemeSh4() {
		super("DarkTheme", DragMethod.TOP, DRAGBAR_HEIGHT);
		colors = new HashMap<>();
		colors.put("background", new Color(0, 0, 0, 255));
		colors.put("writing", new Color(255, 254, 254));
		colors.put("dragbar", new Color(255, 255, 255));
		colors.put("owner", new Color(56, 56, 56));
		opacity = 255;
	}

	@Override
	public void setup() {
		fontRenderer = new MinecraftFontRenderer(new Font("Courier", Font.PLAIN, 16), true, false);
		biggerFont = new MinecraftFontRenderer(new Font("Courier", Font.PLAIN, 16), true, false);
		titleFont = new MinecraftFontRenderer(new Font("Courier", Font.BOLD, 14), true, false);
		Shell._self.values().put("shell_anim_x",
				new XValue().setValue((int) Shell._self.values().get("shell_x").getValue()));
		Shell._self.values().put("shell_anim_y",
				new XValue().setValue((int) Shell._self.values().get("shell_y").getValue()));
		registerUpdatable(new IUpdatable() {
			@Override
			public void update() {
				if (isDragging()) {
					for (Map.Entry<String, Color> entry : colors.entrySet()) {
						if (opacity > 150) {
							opacity--;
							colors.put(entry.getKey(), new Color(entry.getValue().getRed(), entry.getValue().getGreen(),
									entry.getValue().getBlue(), opacity));
						}
					}
				} else {
					for (Map.Entry<String, Color> entry : colors.entrySet()) {
						if (opacity < 255) {
							opacity++;
							colors.put(entry.getKey(), new Color(entry.getValue().getRed(), entry.getValue().getGreen(),
									entry.getValue().getBlue(), opacity));
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
		GL11.glPushMatrix();
		RenderMethods.drawRoundedRect(shellX, shellY, shellWidth, DRAGBAR_HEIGHT, 15, colors.get("dragbar").getRGB());
		GL11.glPopMatrix();
		biggerFont.drawString(Shell._self.getWritingInput().toString(), shellX + 5, shellY + shellHeight + 8, colors.get("writing").getRGB());
		titleFont.drawString(Shell._self.dynamic().get("client_name").getValue().toString().toLowerCase() + "@" + Shell._self.dynamic().get("client_username").getValue().toString().toLowerCase(), shellX + shellWidth / 2f - titleFont.getStringWidth(Shell._self.dynamic().get("client_name").getValue().toString().toLowerCase() + "@" + Shell._self.dynamic().get("client_username").getValue().toString().toLowerCase())/2f, shellY + 7, colors.get("owner").getRGB());
        if (cursorTicks>=75)
            Gui.drawRect(shellX + 5 + biggerFont.getStringWidth(Shell._self.getWritingInput().toString())+1, shellY+shellHeight+6, shellX + 5 + biggerFont.getStringWidth(Shell._self.getWritingInput().toString())+6, shellY+shellHeight+14, colors.get("writing").getRGB());
        if (cursorTicks--<=0)
            cursorTicks=150;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		colors.put("writing", new Color(201, 201, 201));
		colors.put("background", new Color(0, 0, 0, 200));
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
