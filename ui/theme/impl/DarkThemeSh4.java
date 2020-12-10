package sh4ll.ui.theme.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import sh4ll.Shell;
import sh4ll.etc.Anchor;
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
	private boolean cursorState;
	private boolean scaling;
	private Anchor side;
	private int clickedX, clickedY;

	private static Color TRANSPARANT_COLOR = new Color(0, 0, 0, 0);

	public DarkThemeSh4() {
		super("DarkTheme", DragMethod.TOP, DRAGBAR_HEIGHT);
		colors = new HashMap<>();
		colors.put("background", new Color(0, 0, 0, 230));
		colors.put("writing", new Color(255, 254, 254));
		colors.put("dragbar_start", new Color(255, 255, 255));
		colors.put("dragbar_end", new Color(220, 218, 218));
		colors.put("owner", new Color(56, 56, 56));


		colors.put("cursor_outline", new Color(87, 85, 85, 255));
		colors.put("cursor_inside", new Color(255, 255, 255, 255));
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
							if (entry.getKey().equalsIgnoreCase("background") && opacity > 230) {
								continue;
							}
							colors.put(entry.getKey(), new Color(entry.getValue().getRed(), entry.getValue().getGreen(),
									entry.getValue().getBlue(), opacity));
						}
					}
				} else {
					for (Map.Entry<String, Color> entry : colors.entrySet()) {
						if (opacity < 255) {
							opacity++;
							if (entry.getKey().equalsIgnoreCase("background") && opacity > 230) {
								continue;
							}
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
		RenderMethods.drawGradientRect(shellX, shellY, shellX + shellWidth, shellY + DRAGBAR_HEIGHT, colors.get("dragbar_start").getRGB(), colors.get("dragbar_end").getRGB());
		GL11.glPopMatrix();
		titleFont.drawString(Shell._self.getWritingInput().toString(), shellX + 5, shellY + shellHeight + 8, colors.get("writing").getRGB());
		titleFont.drawString(Shell._self.dynamic().get("client_name").getValue().toString().toLowerCase() + "@" + Shell._self.dynamic().get("client_username").getValue().toString().toLowerCase(), shellX + shellWidth / 2f - titleFont.getStringWidth(Shell._self.dynamic().get("client_name").getValue().toString().toLowerCase() + "@" + Shell._self.dynamic().get("client_username").getValue().toString().toLowerCase()) / 2f, shellY + 8, colors.get("owner").getRGB());


		final float writingWidth = titleFont.getStringWidth(Shell._self.getWritingInput().toString());

		// TEXT BLOCKS CODE
		// ==============================

		// ==============================


		// CURSOR (POINTER) CODE
		// ===============================
		Color insideColor = cursorState && Shell._self.getShellUI().isCursorAtLastChar() ? colors.get("cursor_inside") : TRANSPARANT_COLOR;
		int cursorPos = Shell._self.getShellUI().getCursorPos();
		int cursorX = cursorPos == 0 ? 0 : titleFont.getStringWidth(Shell._self.getWritingInput().substring(0, cursorPos));
		RenderMethods.drawBorderedRect(5 + shellX + cursorX, shellY + shellHeight +
				5.5f, 10 + shellX + cursorX, shellY + shellHeight + 14, 0.5F, insideColor.getRGB(), colors.get("cursor_outline").getRGB());
		// ===============================

		// SCALING CODE
		// ===============================
		if (scaling) {
			if (side == Anchor.TOP || side == Anchor.TOP_RIGHT || side == Anchor.TOP_LEFT) {
				Shell._self.values().get("shell_y").setValue(mouseY);
				Shell._self.values().get("shell_height").setValue(shellY + shellHeight - mouseY);
			}

			if (side == Anchor.BOTTOM || side == Anchor.BOTTOM_LEFT || side == Anchor.BOTTOM_RIGHT) {
				Shell._self.values().get("shell_height").setValue(mouseY - shellY - 20);
			}

			if (side == Anchor.LEFT || side == Anchor.BOTTOM_LEFT || side == Anchor.TOP_LEFT) {
				Shell._self.values().get("shell_x").setValue(mouseX);
				Shell._self.values().get("shell_width").setValue(shellX + shellWidth - mouseX);
			}

			if (side == Anchor.RIGHT || side == Anchor.BOTTOM_RIGHT || side == Anchor.TOP_RIGHT) {
				Shell._self.values().get("shell_width").setValue(mouseX - shellX);
			}
		}
		// ===============================
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		colors.put("writing", new Color(201, 201, 201));
		if (isSidesHovered(mouseX, mouseY)) {
			clickedX = mouseX;
			clickedY = mouseY;
			scaling = true;
		}
	}

	private boolean isSidesHovered(int mouseX, int mouseY) {
		int FAIL_RATE = 20;
		int shellX = (int) Shell._self.values().get("shell_x").getValue();
		int shellY = (int) Shell._self.values().get("shell_y").getValue();
		int shellWidth = (int) Shell._self.values().get("shell_width").getValue();
		int shellHeight = (int) Shell._self.values().get("shell_height").getValue();
		boolean right = mouseX >= shellX + shellWidth - 3 && mouseX <= shellX + shellWidth + 1 && mouseY + FAIL_RATE >= shellY && mouseY <= shellY + shellHeight + FAIL_RATE;
		boolean left = mouseX >= shellX - 1 && mouseX <= shellX + 1 && mouseY + FAIL_RATE >= shellY && mouseY <= shellY + shellHeight + FAIL_RATE;
		boolean top = mouseX + FAIL_RATE >= shellX && mouseX <= shellX + shellWidth + FAIL_RATE && mouseY >= shellY - 3 && mouseY <= shellY + 3;
		boolean bottom = mouseX >= shellX && mouseX <= shellX + shellWidth && mouseY + FAIL_RATE >= shellY + shellHeight + 19 && mouseY <= shellY + shellHeight + 24 + FAIL_RATE;

		if (right) {
			if (top) {
				side = Anchor.TOP_RIGHT;
			} else if (bottom) {
				side = Anchor.BOTTOM_RIGHT;
			} else {
				side = Anchor.RIGHT;
			}
		} else if (left) {
			if (top) {
				side = Anchor.TOP_LEFT;
			} else if (bottom) {
				side = Anchor.BOTTOM_LEFT;
			} else {
				side = Anchor.LEFT;
			}
		} else if (top) {
			side = Anchor.TOP;
		} else if (bottom) {
			side = Anchor.BOTTOM;
		}

		return right || left || top || bottom;
	}


	@Override
	public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {

	}

	@Override
	public void update() {
		// increment cursor tick
		if (++cursorTicks >= 201) {
			cursorTicks = 0;
		}

		// cursor state change
		if (isTyping()) {
			if (cursorTicks % 4 == 0) {
				cursorState = !cursorState;
			}
		} else {
			if (cursorTicks % 10 == 0) {
				cursorState = !cursorState;
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		scaling = false;
	}


}