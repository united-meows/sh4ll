package sh4ll.ui.textblock.def;

import sh4ll.etc.Tuple;
import sh4ll.ui.textblock.TextBlock;
import sh4ll.util.MinecraftFontRenderer;

public class NormalTextBlock extends TextBlock {

    public NormalTextBlock(String _text) {
        super(_text, null);
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
}
