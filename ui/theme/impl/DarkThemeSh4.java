package sh4ll.ui.theme.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import sh4ll.Shell;
import sh4ll.etc.Anchor;
import sh4ll.etc.IUpdatable;
import sh4ll.etc.RenderMethods;
import sh4ll.etc.Tuple;
import sh4ll.ui.DragMethod;
import sh4ll.ui.textblock.TextBlock;
import sh4ll.ui.textblock.def.SelectableTextBlock;
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
	private int[] indexes;
	private TextBlock clickedTextBlock;
	private SelectableTextBlock selectedTextBlock = new SelectableTextBlock(false);

    private static Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);

	public DarkThemeSh4() {
		super("DarkTheme", DragMethod.TOP, DRAGBAR_HEIGHT);
		colors = new HashMap<>();
		colors.put("background", new Color(0, 0, 0, 230));
		colors.put("writing", new Color(255, 254, 254));
		colors.put("dragbar_start", new Color(255, 255, 255));
		colors.put("dragbar_end", new Color(220, 218, 218));
		colors.put("owner", new Color(56, 56, 56));
		colors.put("output_text", new Color(255, 255, 255));


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
        Gui.drawRect(0,0,0,0, -1);
        titleFont.drawString(Shell._self.dynamic().get("client_name").getValue().toString().toLowerCase() + "@" + Shell._self.dynamic().get("client_username").getValue().toString().toLowerCase(), (float) (shellX + shellWidth / 2f - titleFont.getStringWidth(Shell._self.dynamic().get("client_name").getValue().toString().toLowerCase() + "@" + Shell._self.dynamic().get("client_username").getValue().toString().toLowerCase()) / 2f), shellY + 8, colors.get("owner").getRGB());


        // TEXT BLOCKS CODE
        // ==============================


        int scrollY = Shell._self.getShellUI().getScroll();
        int totalHeight = getTotalHeightBlocks(titleFont, 10, 8, shellWidth);
        int substractY = 0;
        if (totalHeight > shellHeight - DRAGBAR_HEIGHT - 3) {
            substractY = totalHeight - (shellHeight - DRAGBAR_HEIGHT - 3);
        }


        int y = shellY + DRAGBAR_HEIGHT + 3 + scrollY - substractY;
        ;
        // RenderMethods.drawRect(shellX+shellWidth-2,y+(Shell._self.outputs().size()*10)/(Shell._self.outputs().size()*10-scroll),shellX+shellWidth,y+(Shell._self.outputs().size()*10)/(Shell._self.outputs().size()*10-scroll)+10, Color.white.getRGB());
        if (clickedTextBlock == null && y + selectedTextBlock.getY() >= shellY + DRAGBAR_HEIGHT && y + selectedTextBlock.getY() + selectedTextBlock.getHeight() <= shellY + shellHeight) {
        	Gui.drawRect(0,0,0,0, -1);
        	titleFont.drawString(selectedTextBlock.getString(), 2, 2, colors.get("owner").getRGB());
            //titleFont.drawString(RenderMethods.clearColorCodes(selectedTextBlock.getText()),2,12, colors.get("owner").getRGB());
            RenderMethods.drawRect(shellX + 5 + selectedTextBlock.getX(), y + selectedTextBlock.getY(), shellX + 5 + selectedTextBlock.getWidth(), y + selectedTextBlock.getY() + selectedTextBlock.getHeight(), new Color(255, 2, 2, 100).getRGB());
        }

        Shell._self.getShellUI().setCanNegScroll(false);
        Shell._self.getShellUI().setCanPosScroll(false);
        for (Tuple<TextBlock, Boolean> textBlockVal : Shell._self.outputs()) {
            final TextBlock textBlock = textBlockVal.getFirst();
            if (y < shellY + DRAGBAR_HEIGHT + 3) {
                Shell._self.getShellUI().setCanNegScroll(true);
            }
            if (y > shellY + shellHeight - 10) {
                Shell._self.getShellUI().setCanPosScroll(true);
            }
            List<String> lines = getLines(titleFont, textBlock.getText(), 8, shellWidth);
            int i = 0;
            for (String line : lines) {
                if (clickedTextBlock != null) {
                    double wordX = shellX + 5;
                    String[] split = RenderMethods.clearColorCodes(line).split(""); /* wtf ipana */
                    boolean firstWord = false;
                    for (int i1 = 0; i1 < split.length; i1++) {
                        String words = split[i1];
                        if (y >= shellY + DRAGBAR_HEIGHT + 3 && y <= shellY + shellHeight - 10 && mouseY >= y - 2 && mouseY <= y + 6) {
                            if (((clickedX >= wordX && mouseX <= wordX + titleFont.getStringWidth(words)) || (clickedX <= wordX + titleFont.getStringWidth(words) && mouseX >= wordX))) {
                               	if (indexes.length > i1) {
									indexes[i1] = i1;
								}
                                if (!firstWord) {
                                    selectedTextBlock.setX(wordX - (shellX + 5));
                                    firstWord = true;
                                }
                                selectedTextBlock.setY((y - 2) - (shellY + DRAGBAR_HEIGHT + 3 + scrollY - substractY));
                                selectedTextBlock.setWidth(wordX + titleFont.getStringWidth(words) - (shellX + 5));
                                selectedTextBlock.setHeight((y + 6) - (y - 2));
                                RenderMethods.drawRect(wordX, y - 2, wordX + titleFont.getStringWidth(words), y + 6, Color.red.getRGB());
                            } else {
                            	if (indexes.length <= i1) {
                            		return;
								}
                                indexes[i1] = 0;
                            }
                            wordX += titleFont.getStringWidth(words);
                        }
                    }
                }
                if (y >= shellY + DRAGBAR_HEIGHT + 3 && y <= shellY + shellHeight - 10) {
                	Gui.drawRect(0,0,0,0, -1);
                	titleFont.drawString(line, shellX + 5, y, colors.get("output_text").getRGB());
                    
                }
                if (i + 1 == line.length()) {
                    if (textBlockVal.getSecond()) {
                        y += 10;
                    }
                } else {
                    y += 10;
                }
                i++;
            }
        }
        int writeY = substractY > 0 ? shellY + shellHeight + 8 : y;
        String writing = reverseCutLine(titleFont, Shell._self.getWritingInput().toString(), 8 + (int)titleFont.getStringWidth("\247d" + (Shell._self.capturingNextInput() ? "" : (Shell._self.capturingNextInput() ? "" : Shell._self.getShellUI().getCustomUserAlias())) + "\2475 ~ \247f"), shellWidth);
       	boolean isCutted = writing.length() !=  Shell._self.getWritingInput().length();
        // the text might look like shit on 1.8.0 because of rendering issues so to fix this we make an invisible rect look like shit
	Gui.drawRect(0,0,0,0, -1);
	titleFont.drawString("\247d" + (Shell._self.capturingNextInput() ? "" : Shell._self.getShellUI().getCustomUserAlias()) + "\2475 ~ \247f" + writing, shellX + 5, writeY, colors.get("writing").getRGB());
 
	// ==============================
        // CURSOR (POINTER) CODE<
        // ===============================
        Color insideColor = cursorState && Shell._self.getShellUI().isCursorAtLastChar() ? colors.get("cursor_inside") : TRANSPARENT_COLOR;
        int cursorPos = Math.max(0, Shell._self.getShellUI().getCursorPos());
        int cursorX;
        if (isCutted) {
        	cursorX = -777 /* being lazy is cool */;
		} else {
        	cursorX = (int) titleFont.getStringWidth((Shell._self.capturingNextInput() ? "" : Shell._self.getShellUI().getCustomUserAlias())) + 10 + (cursorPos == 0 ? 0 : (int) titleFont.getStringWidth(Shell._self.getWritingInput().substring(0, cursorPos))) - 1;
		}
         RenderMethods.drawBorderedRect(5 + shellX + cursorX, writeY - 2, 10 + shellX + cursorX, writeY + 6, 0.5F, insideColor.getRGB(), colors.get("cursor_outline").getRGB());
        // ===============================

        // SCALING CODE
        // ===============================
        if (scaling) {
            if (side == Anchor.TOP || side == Anchor.TOP_RIGHT || side == Anchor.TOP_LEFT) {
            	if ((int) Shell._self.values().get("shell_height_min").getValue() < shellY + shellHeight - mouseY) {
					Shell._self.values().get("shell_y").setValue(mouseY);
					Shell._self.values().get("shell_height").setValue(shellY + shellHeight - mouseY);
				}
            }

            if (side == Anchor.BOTTOM || side == Anchor.BOTTOM_LEFT || side == Anchor.BOTTOM_RIGHT) {
				if ((int) Shell._self.values().get("shell_height_min").getValue() < mouseY - shellY - 20) {
					Shell._self.values().get("shell_height").setValue(mouseY - shellY - 20);
				}
            }

            if (side == Anchor.LEFT || side == Anchor.BOTTOM_LEFT || side == Anchor.TOP_LEFT) {
            	if ((int) Shell._self.values().get("shell_width_min").getValue() <shellX + shellWidth - mouseX) {
					Shell._self.values().get("shell_x").setValue(mouseX);
					Shell._self.values().get("shell_width").setValue(shellX + shellWidth - mouseX);
				}
            }

            if (side == Anchor.RIGHT || side == Anchor.BOTTOM_RIGHT || side == Anchor.TOP_RIGHT) {
				if ((int) Shell._self.values().get("shell_width_min").getValue() < mouseX - shellX) {
					Shell._self.values().get("shell_width").setValue(mouseX - shellX);
				}
            }
        }
        // ===============================
    }


	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		colors.put("writing", new Color(201, 201, 201));
        int shellX = (int) Shell._self.values().get("shell_x").getValue();
        int shellY = (int) Shell._self.values().get("shell_y").getValue();
        int shellWidth = (int) Shell._self.values().get("shell_width").getValue();
        int shellHeight = (int) Shell._self.values().get("shell_height").getValue();
        if (Shell._self.outputs().size() > 0) {
            int scrollY = Shell._self.getShellUI().getScroll();
            int totalHeight = getTotalHeightBlocks(titleFont, 10, 8, shellWidth);
            int substractY = 0;
            if (totalHeight > shellHeight - DRAGBAR_HEIGHT - 3) {
                substractY = totalHeight - (shellHeight - DRAGBAR_HEIGHT - 3);
            }
            int y = shellY + DRAGBAR_HEIGHT + 3 + scrollY - substractY;
            for (Tuple<TextBlock, Boolean> textBlockVal : Shell._self.outputs()) {
            	final TextBlock textBlock = textBlockVal.getFirst();
                if (y >= shellY + DRAGBAR_HEIGHT + 3 && y <= shellY + shellHeight - 10) {
                    //System.out.println(mouseX+" : "+(shellX+5+titleFont.getStringWidth(textBlock.getText())));
                    if (mouseX >= shellX+5 && mouseX <= shellX+5+titleFont.getStringWidth(RenderMethods.clearColorCodes(textBlock.getText())) && mouseY >= y-2 && mouseY <= y+6) {
                        clickedX = mouseX;
                        clickedY = mouseY;
                        clickedTextBlock = textBlock;
                        selectedTextBlock.setString("");
                        String[] split = RenderMethods.clearColorCodes(textBlock.getText()).split("");
                        indexes = new int[split.length];
                    }
                }
                if (textBlockVal.getSecond())
              	  y += 10;
            }
        }
		if (isSidesHovered(mouseX, mouseY)) {
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
	public String selection() {
		return selectedTextBlock.getString();
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		scaling = false;
		if (clickedTextBlock != null) {
            String[] split = RenderMethods.clearColorCodes(clickedTextBlock.getText()).split("");
            for (int setupString : indexes) {
                if (setupString != 0) {
                    selectedTextBlock.setString(selectedTextBlock.getString() + split[setupString]);
                }
            }
            clickedTextBlock = null;
        }
	}


}
