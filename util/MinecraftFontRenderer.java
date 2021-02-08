package sh4ll.util;

import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.*;
import java.util.*;
import java.util.List;

public class MinecraftFontRenderer extends CFont
{
    protected CharData[] boldChars;
    protected CharData[] italicChars;
    protected CharData[] boldItalicChars;
    public final Random fontRandom;
    private final int[] colorCode;
    private final String colorcodeIdentifiers = "0123456789abcdefklmnor";
    protected DynamicTexture texBold;
    protected DynamicTexture texItalic;
    protected DynamicTexture texItalicBold;

    public MinecraftFontRenderer(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        super(font, antiAlias, fractionalMetrics);
        this.boldChars = new CharData[256];
        this.italicChars = new CharData[256];
        this.boldItalicChars = new CharData[256];
        this.fontRandom = new Random();
        this.colorCode = new int[32];
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }


    public float drawStringWithShadow(final String text, final double x, final double y, final int color) {
        final float shadowWidth = this.drawString(text, x + 1.0, y + 1.0, color, true);
        return Math.max(shadowWidth, this.drawString(text, x, y, color, false));
    }

    public float drawString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, x, y, color, false);
    }
    public void drawStringCustomColor(final String text, final float x, final float y, final int color) {
        char c = '(';
        this.drawString(text.split(String.valueOf(c))[0], x, y, color, false);
        int size = text.length()-text.replace("(","").length();
        for (int i = 0; i < size; i++) {
            String sub = text.substring(text.indexOf("("),text.indexOf(")")+1);
            String[] colorArray = sub.split(",");
            Color newColor = new Color(Integer.parseInt(colorArray[0]),Integer.parseInt(colorArray[1]),Integer.parseInt(colorArray[2]));
            this.drawString(text.split(String.valueOf(c))[0], x, y, color, false);
        }
    }
    public float drawCenteredString(final String text, final float x, final float y, final int color) {
        return this.drawString(text, (float) (x - this.getStringWidth(text) / 2), y, color);
    }

    public float drawCenteredStringWithShadow(final String text, final float x, final float y, final int color) {
        return this.drawString(text, (float) (x - this.getStringWidth(text) / 2), y, color);
    }

    public void drawBorderedString(final String text, final float x, final float y, final int color) {
        this.drawString(text, x - 0.5f, y, 0);
        this.drawString(text, x + 0.5f, y, 0);
        this.drawString(text, x, y - 0.5f, 0);
        this.drawString(text, x, y + 0.5f, 0);
        this.drawString(text, x, y, color);
    }

    public float drawString(final String actualString, double x, double y, int color, final boolean shadow) {
        --x;
        if (actualString == null) {
            return 0.0f;
        }
        final String renamedString = actualString.replace('\u0131', 'i').replace('\u0130', 'I').replace('\u011f', 'g').replace('\u011e', 'G').replace('\u00e7', 'c')
				.replace('\u00c7', 'C').replace('\u00fc', 'u').replace('\u00dc', 'U').replace('\u015f', 's').replace('\u015e', 'S')
				.replace('\u00f6', 'o').replace('\u00d6', 'O');
        if (color == 553648127) {
            color = 16777215;
        }
        if ((color & 0xFC000000) == 0x0) {
            color |= 0xFF000000;
        }
        if (shadow) {
            color = ((color & 0xFCFCFC) >> 2 | (color & 0xFF000000));
        }
        CharData[] currentData = this.charData;
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        boolean randomCase = false;
        boolean bold = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean underline = false;
        final boolean render = true;
        x *= 2.0;
        y = (y - 3.0) * 2.0;
        if (render) {
            GL11.glPushMatrix();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
            final int size = renamedString.length();
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(this.tex.getGlTextureId());
            GL11.glBindTexture(3553, this.tex.getGlTextureId());
            for (int i = 0; i < size; ++i) {
                char character = renamedString.charAt(i);
                if (character == '\247' && i < size) {
                    int colorIndex = 21;
                    try {
                        colorIndex = "0123456789abcdefklmnor".indexOf(renamedString.charAt(i + 1));
                    }
                    catch (Exception ex) {}
                    if (colorIndex < 16) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                        if (colorIndex < 0 || colorIndex > 15) {
                            colorIndex = 15;
                        }
                        if (shadow) {
                            colorIndex += 16;
                        }
                        final int colorcode = this.colorCode[colorIndex];
                        GlStateManager.color((colorcode >> 16 & 0xFF) / 255.0f, (colorcode >> 8 & 0xFF) / 255.0f, (colorcode & 0xFF) / 255.0f, alpha);
                    }
                    else if (colorIndex == 16) {
                        randomCase = true;
                    }
                    else if (colorIndex == 17) {
                        bold = true;
                        if (italic) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        }
                        else {
                            GlStateManager.bindTexture(this.texBold.getGlTextureId());
                            currentData = this.boldChars;
                        }
                    }
                    else if (colorIndex == 18) {
                        strikethrough = true;
                    }
                    else if (colorIndex == 19) {
                        underline = true;
                    }
                    else if (colorIndex == 20) {
                        italic = true;
                        if (bold) {
                            GlStateManager.bindTexture(this.texItalicBold.getGlTextureId());
                            currentData = this.boldItalicChars;
                        }
                        else {
                            GlStateManager.bindTexture(this.texItalic.getGlTextureId());
                            currentData = this.italicChars;
                        }
                    }
                    else if (colorIndex == 21) {
                        bold = false;
                        italic = false;
                        randomCase = false;
                        underline = false;
                        strikethrough = false;
                        GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, alpha);
                        GlStateManager.bindTexture(this.tex.getGlTextureId());
                        currentData = this.charData;
                    }
                    ++i;
                }
                else if (character < currentData.length && character >= '\0') {
                    final boolean developement = true;
                    final String character2 = this.toRandom(String.valueOf(character));
                    GL11.glBegin(4);
                    final int m = 5;
                    if (randomCase) {
                        char newChar;
                        for (newChar = '\0'; this.charData[newChar].width != this.charData[character].width; newChar = (char)(Math.random() * 128.0)) {}
                        if (character != ' ') {
                            character = newChar;
                        }
                    }
                    this.drawChar(currentData, character, (float)x, (float)y);
                    GL11.glEnd();
                    if (strikethrough) {
                        this.drawLine(x, y + currentData[character].height / 2, x + currentData[character].width - 8.0, y + currentData[character].height / 2, 1.0f);
                    }
                    if (underline) {
                        this.drawLine(x, y + currentData[character].height - 2.0, x + currentData[character].width - 8.0, y + currentData[character].height - 2.0, 1.0f);
                    }
                    x += currentData[character].width - 8 + this.charOffset;
                }
            }
            GL11.glHint(3155, 4352);
            GL11.glPopMatrix();
        }
        return (float)x / 2.0f;
    }

    public double getStringWidthWithControlCodes(String text) {
        double width = 0;
        byte b;
        int i;
        char[] arrayOfChar;
        for (i = (arrayOfChar = text.toCharArray()).length, b = 0; b < i; ) {
            char c = arrayOfChar[b];
            if (c < this.charData.length)
                width += (this.charData[c]).width - 8 + this.charOffset;
            b++;
        }
        return width / 2;
    }

    @Override
    public double getStringWidth(String text) {
        if (text == null) {
            return 0;
        }
        text = text.replace('\u0131', 'i').replace('\u0130', 'I').replace('\u011f', 'g').replace('\u011e', 'G').replace('\u00e7', 'c')
				.replace('\u00c7', 'C').replace('\u00fc', 'u').replace('\u00dc', 'U').replace('\u015f', 's').replace('\u015e', 'S')
				.replace('\u00f6', 'o').replace('\u00d6', 'O');
        double width = 0;
        CharData[] currentData = this.charData;
        boolean bold = false;
        boolean italic = false;
        for (int size = text.length(), i = 0; i < size; ++i) {
            final char character = text.charAt(i);
            if (character == '\247') {
                final int colorIndex = "0123456789abcdefklmnor".indexOf(character);
                if (colorIndex < 16) {
                    bold = false;
                    italic = false;
                }
                else if (colorIndex == 17) {
                    bold = true;
                    if (italic) {
                        currentData = this.boldItalicChars;
                    }
                    else {
                        currentData = this.boldChars;
                    }
                }
                else if (colorIndex == 20) {
                    italic = true;
                    if (bold) {
                        currentData = this.boldItalicChars;
                    }
                    else {
                        currentData = this.italicChars;
                    }
                }
                else if (colorIndex == 21) {
                    bold = false;
                    italic = false;
                    currentData = this.charData;
                }
                ++i;
            }
            else if (character < currentData.length && character >= '\0') {
                width += currentData[character].width - 8 + this.charOffset;
            }
        }
        return width / 2;
    }

    @Override
    public void setFont(final Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(final boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(final boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();  
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(1), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(2), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(3), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    

    private void drawLine(final double x, final double y, final double x1, final double y1, final float width) {
        GL11.glDisable(3553);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glEnable(3553);
    }

    public List<String> wrapWords(String text, final double width) {
        final List finalWords = new ArrayList();
        text = text.replace('\u0131', 'i').replace('\u0130', 'I').replace('\u011f', 'g').replace('\u011e', 'G').replace('\u00e7', 'c')
				.replace('\u00c7', 'C').replace('\u00fc', 'u').replace('\u00dc', 'U').replace('\u015f', 's').replace('\u015e', 'S')
				.replace('\u00f6', 'o').replace('\u00d6', 'O');
        if (this.getStringWidth(text) > width) {
            final String[] words = text.split(" ");
            String currentWord = "";
            char lastColorCode = '\uffff';
            String[] array;
            for (int length = (array = words).length, j = 0; j < length; ++j) {
                final String word = array[j];
                for (int i = 0; i < word.toCharArray().length; ++i) {
                    final char c = word.toCharArray()[i];
                    if (c == '\247' && i < word.toCharArray().length - 1) {
                        lastColorCode = word.toCharArray()[i + 1];
                    }
                }
                if (this.getStringWidth(String.valueOf(currentWord) + word + " ") < width) {
                    currentWord = String.valueOf(currentWord) + word + " ";
                }
                else {
                    finalWords.add(currentWord);
                    currentWord = "\247" + lastColorCode + word + " ";
                }
            }
            if (currentWord.length() > 0) {
                if (this.getStringWidth(currentWord) < width) {
                    finalWords.add("\247" + lastColorCode + currentWord + " ");
                    currentWord = "";
                }
                else {
                    for (final String s : this.formatString(currentWord, width)) {
                        finalWords.add(s);
                    }
                }
            }
        }
        else {
            finalWords.add(text);
        }
        return (List<String>)finalWords;
    }

    public List<String> formatString(String string, final double width) {
        final List finalWords = new ArrayList();
        String currentWord = "";
        char lastColorCode = '\uffff';
        string = string.replace('\u0131', 'i').replace('\u0130', 'I').replace('\u011f', 'g').replace('\u011e', 'G').replace('\u00e7', 'c')
				.replace('\u00c7', 'C').replace('\u00fc', 'u').replace('\u00dc', 'U').replace('\u015f', 's').replace('\u015e', 'S')
				.replace('\u00f6', 'o').replace('\u00d6', 'O');
        final char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; ++i) {
            final char c = chars[i];
            if (c == '\247' && i < chars.length - 1) {
                lastColorCode = chars[i + 1];
            }
            if (this.getStringWidth(String.valueOf(currentWord) + c) < width) {
                currentWord = String.valueOf(currentWord) + c;
            }
            else {
                finalWords.add(currentWord);
                currentWord = "\247" + lastColorCode + String.valueOf(c);
            }
        }
        if (currentWord.length() > 0) {
            finalWords.add(currentWord);
        }
        return (List<String>)finalWords;
    }

    private void setupMinecraftColorcodes() {
        for (int index = 0; index < 32; ++index) {
            final int noClue = (index >> 3 & 0x1) * 85;
            int red = (index >> 2 & 0x1) * 170 + noClue;
            int green = (index >> 1 & 0x1) * 170 + noClue;
            int blue = (index >> 0 & 0x1) * 170 + noClue;
            if (index == 6) {
                red += 85;
            }
            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            this.colorCode[index] = ((red & 0xFF) << 16 | (green & 0xFF) << 8 | (blue & 0xFF));
        }
    }

}
