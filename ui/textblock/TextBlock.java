package sh4ll.ui.textblock;


import sh4ll.etc.Tuple;

public abstract class TextBlock {

    private String text;

    public TextBlock(String _text) {
        text = _text;
    }

    public abstract void draw(int startX, int width, int logStartX);
    public abstract void update();
    public abstract void onClick(int mouseX, int mouseY);

    // Returns block's line count width, height
    public abstract Tuple<Integer, Tuple<Integer, Integer>> getTextLength();

    public String getText() {
        return text;
    }
}
