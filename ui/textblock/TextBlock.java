package sh4ll.ui.textblock;


import sh4ll.etc.Tuple;

public abstract class TextBlock {

    private String text;

    public TextBlock(String _text) {
        text = _text;
    }

    public abstract int draw(int startX, int width);
    public abstract void update();

    // Returns block's line count and width
    public abstract Tuple<Integer, Integer> getTextLength();

    public String getText() {
        return text;
    }
}
