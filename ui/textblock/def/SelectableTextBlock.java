package sh4ll.ui.textblock.def;

import sh4ll.etc.Tuple;
import sh4ll.ui.textblock.TextBlock;
import sh4ll.util.MinecraftFontRenderer;

public class SelectableTextBlock  extends TextBlock {
    private double x,y,width,height;
    /**
     * Selected first and Last word of the parent
     */
    private String first,last;
    /**
     * Selected first and Last word index of the parent
     */
    private int firstIndex,lastIndex;

    public SelectableTextBlock(String _text) {
        super(_text, null);
    }
    public SelectableTextBlock() {
        super("", null);
    }
    @Override
    public void draw(int startX, int width, int logStartX) {

    }

    @Override
    public void update() {

    }

    @Override
    public void onClick(int mouseX, int mouseY) {

    }

    @Override
    public Tuple<Integer, Tuple<Integer, Integer>> getTextLength(MinecraftFontRenderer fontRenderer) {
        return null;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }
}
