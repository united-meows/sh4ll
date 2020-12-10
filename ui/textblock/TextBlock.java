package sh4ll.ui.textblock;


import sh4ll.etc.Tuple;
import sh4ll.util.MinecraftFontRenderer;

public abstract class TextBlock {

    private String text;
        private MinecraftFontRenderer fontRenderer;

    public TextBlock(String _text, MinecraftFontRenderer _fontRenderer) {
        text = _text;
        fontRenderer = _fontRenderer;
    }

    public abstract void draw(int startX, int width, int logStartX);
    public abstract void update();
    public abstract void onClick(int mouseX, int mouseY);

    // Returns block's line count width, height
    public abstract Tuple<Integer, Tuple<Integer, Integer>> getTextLength(MinecraftFontRenderer fontRenderer);

    public String getText() {
        return text;
    }

    protected MinecraftFontRenderer getFontRenderer() {
        return fontRenderer;
    }
}
