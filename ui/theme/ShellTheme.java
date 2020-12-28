package sh4ll.ui.theme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sh4ll.Shell;
import sh4ll.etc.IUpdatable;
import sh4ll.etc.Tuple;
import sh4ll.ui.DragMethod;
import sh4ll.ui.textblock.TextBlock;
import sh4ll.util.CFont;
import sh4ll.util.MinecraftFontRenderer;
import sh4ll.value.XValue;

public abstract class ShellTheme {

	private final String name;
	protected boolean drag;
	protected int lastX;
	protected int lastY;
	private DragMethod dragMethod;
	private int dragBarHeight;
	private ArrayList<Tuple<Integer, IUpdatable>> updatableHolder;
	private ArrayList<Thread> threads;

	/** last key press time **/
	private long lastKeyPress;

	/**
	 * if you have custom dragbar's set dragmethod to custom and topBarHeight to 0
	 * and define your drag area with 'isOverDragArea(mouseX, mouseY)' method
	 * 
	 * @param _name
	 * @param _dragMethod
	 * @param _dragBarHeight
	 */
	public ShellTheme(final String _name, DragMethod _dragMethod, int _dragBarHeight) {
		name = _name;
		dragMethod = _dragMethod;
		dragBarHeight = _dragBarHeight;
		updatableHolder = new ArrayList<Tuple<Integer, IUpdatable>>();
		threads = new ArrayList<>();
	}

	/**
	 * Preset values like Prefix: 'theme_' is suggested but not required
	 * theme_back_color theme_shell_opacity theme_pointer_color
	 */
	private HashMap<String, XValue> _presets;

	public HashMap<String, XValue> presets() {
		return _presets;
	}

	/**
	 * Thread list of updatables
	 */

	/**
	 * You can use this fontRenderer private MinecraftFontRenderer fontRenderer;
	 */

	/**
	 * Registers updatable's with updaterate updaterate is in miliseconds ex: 1000 =
	 * 1 second 500 = 500 ms 60 * 1000 = 1 minute
	 * 
	 * @param updatable
	 * @param updateRate
	 */
	public void registerUpdatable(IUpdatable updatable, int updateRate) {
		updatableHolder.add(new Tuple<>(updateRate, updatable));
	}

	public boolean isOverDragBar(final int mouseX, final int mouseY) {
		// TODO
		switch (dragMethod) {
		case ALL: {
			return mouseX >= Shell._self.getX() && mouseX <= Shell._self.getX() + Shell._self.getWidth()
					&& mouseY >= Shell._self.getY() && mouseY <= Shell._self.getY() + Shell._self.getHeight();
		}
		case TOP: {
			return mouseX >= Shell._self.getX() && mouseX <= Shell._self.getX() + Shell._self.getWidth()
					&& mouseY >= Shell._self.getY()
					&& mouseY <= Shell._self.getY() + dragBarHeight;
		}
		case BOTTOM: {
			return mouseX >= Shell._self.getX() && mouseX <= Shell._self.getX() + Shell._self.getWidth()
					&& mouseY >= Shell._self.getY() + Shell._self.getHeight() - dragBarHeight
					&& mouseY <= Shell._self.getX() + Shell._self.getHeight();
		}
		case CUSTOM:
			break;
		case NO_DRAG:
			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * on disable
	 **/
	public void onDisable() {
		for (Thread thread : threads) {
			try {
				thread.stop();
			} catch (Exception ex) {
			}
		}
		threads.clear();
	}

	/**
	 * on enable
	 **/
	public void onEnable() {
		for (Tuple<Integer, IUpdatable> updatableTuple : updatableHolder) {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (Shell._self.isOpen()) {
						updatableTuple.getSecond().update();
						try {
							Thread.sleep(updatableTuple.getFirst());
						} catch (InterruptedException ex) {

						}
					}
				}
			});
			thread.start();
			threads.add(thread);
		}
	}

	/**
	 * gets called right before gui opened
	 **/
	public abstract void setup();


	/**
	 * @returns selected text
	 */
	public abstract String selection();

	/**
	 * you can also get mouse values with Shell instance dynamic mouse_x mouse_y
	 **/
	public abstract void draw(int mouseX, int mouseY, float partialTicks);

	/**
	 * Gets called when mouse is clicked
	 * 
	 * @param mouseX      mouse location x
	 * @param mouseY      mouse location y
	 * @param mouseButton mouse button
	 */
	public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

	/**
	 * Gets called when mouse is released
	 * 
	 * @param mouseX mouse location x
	 * @param mouseY mouse location y
	 * @param state  mouse button
	 */
	public abstract void mouseReleased(int mouseX, int mouseY, int state);

	/**
	 * Gets called when mouse click-drag
	 * 
	 * @param mouseX             mouse location x
	 * @param mouseY             mouse location y
	 * @param clickedMouseButton mouse button
	 * @param timeSinceLastClick -
	 */
	public abstract void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick);

	/**
	 * method from Minecraft's GuiScreen class
	 */
	public abstract void update();

//TODO: NOT ANYMORE
//	public List<TextBlock> getRenderBlocks(int height, MinecraftFontRenderer fontRenderer) {
//		List<TextBlock> blocks = new ArrayList<>();
//		int currentHeight = 0;
//		int totalHeight = 0;
//		int scrollLoc = (int) Shell._self.values().get("shell_scroll_location").getValue();
//		for (TextBlock textBlock : Shell._self.outputs()) {
//			if (totalHeight >= height) break;
//
//			if (currentHeight >= scrollLoc) {
//				blocks.add(textBlock);
//			}
//
//			/* adds textblocks height */
//			currentHeight += textBlock.getTextLength(fontRenderer).getSecond().getSecond();
//		}
//
//		return blocks;
//	}

	public int getTotalHeightBlocks(MinecraftFontRenderer font, int offsetY, int startX, int shellWidth) {
		int y = 0;
		for (Tuple<TextBlock, Boolean> block : Shell._self.outputs()) {
			List<String> lines = getLines(font, block.getFirst().getText(), startX, shellWidth);
			int i = 0;
			for (String line : lines) {
				if (i + 1 == line.length()) {
					if (block.getSecond()) {
						y += offsetY;
					}
				} else {
					y += offsetY;
				}
				i++;
			}
		}
		return y;
	}

	public List<String> getLines(MinecraftFontRenderer fontRenderer, String input, int startX, int maxWidth) {
		List<String> lines = new ArrayList<>();
		StringBuilder currentLine = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			if (fontRenderer.getStringWidth(currentLine.toString()) + startX > maxWidth) {
				lines.add(currentLine.toString());
				currentLine = new StringBuilder();
			}
			currentLine.append(input.charAt(i));
		}
		if (currentLine.length() > 0) {
			lines.add(currentLine.toString());
		}
		return lines;
	}
    public List<String> getLinesWithControlCodes(MinecraftFontRenderer fontRenderer, String input, int startX, int maxWidth) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (fontRenderer.getStringWidthWithControlCodes(currentLine.toString()) + startX > maxWidth) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            currentLine.append(input.charAt(i));
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }
        return lines;
    }
	public void _clickDrag(int mouseX, int mouseY) {
		Shell._self.values().get("shell_x").setValue(Shell._self.getX() + (mouseX - lastX));
		Shell._self.values().get("shell_y").setValue(Shell._self.getY() + (mouseY - lastY));
		lastX = mouseX;
		lastY = mouseY;
	}

	public void _mouseClick(int mouseX, int mouseY, int mouseButton) {

		if (isOverDragBar(mouseX, mouseY)) {
			lastX = mouseX;
			lastY = mouseY;
			drag = true;
		}
	}

	public boolean isDragging() {
		return drag;
	}

	/**
	 * @return if user is typing on console
	 */
	public boolean isTyping() {
		return System.currentTimeMillis() - lastKeyPress < /* 0.5 seconds */ 300;
	}

	public void updateKeyPress() {
		lastKeyPress = System.currentTimeMillis();
	}



	public void _mouseRelease(int mouseX, int mouseY, int mouseButton) {
		drag = false;
	}
}
