package sh4ll.etc;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderMethods {

	public static void drawGradientRect(double x, double y, double x2, double y2, int col1, int col2) {
		float f = (float) (col1 >> 24 & 0xFF) / 255F;
		float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
		float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
		float f3 = (float) (col1 & 0xFF) / 255F;

		float f4 = (float) (col2 >> 24 & 0xFF) / 255F;
		float f5 = (float) (col2 >> 16 & 0xFF) / 255F;
		float f6 = (float) (col2 >> 8 & 0xFF) / 255F;
		float f7 = (float) (col2 & 0xFF) / 255F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);

		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glVertex2d(x2, y);
		GL11.glVertex2d(x, y);

		GL11.glColor4f(f5, f6, f7, f4);
		GL11.glVertex2d(x, y2);
		GL11.glVertex2d(x2, y2);
		GL11.glEnd();
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glShadeModel(GL11.GL_FLAT);
	}

	public static void enableGL2D() {
		GL11.glDisable(2929);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glDepthMask(true);
		GL11.glEnable(2848);
		GL11.glHint(3154, 4354);
		GL11.glHint(3155, 4354);
	}

	public static void glColor(int hex) {
		float alpha = (hex >> 24 & 0xFF) / 255.0F;
		float red = (hex >> 16 & 0xFF) / 255.0F;
		float green = (hex >> 8 & 0xFF) / 255.0F;
		float blue = (hex & 0xFF) / 255.0F;
		GL11.glColor4f(red, green, blue, alpha);
	}

	public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int internalColor, int borderColor) {
		enableGL2D();
		glColor(internalColor);
		drawRect(x + width, y + width, x1 - width, y1 - width);
		glColor(borderColor);
		drawRect(x + width, y, x1 - width, y + width);
		drawRect(x, y, x + width, y1);
		drawRect(x1 - width, y, x1, y1);
		drawRect(x + width, y1 - width, x1 - width, y1);
		disableGL2D();
	}

	public static void drawRect(float x, float y, float x1, float y1, int color) {
		enableGL2D();
		glColor(color);
		drawRect(x, y, x1, y1);
		disableGL2D();
	}

	public static void drawRect(double left, double top, double right, double bottom, int color)
	{
		if (left < right)
		{
			double i = left;
			left = right;
			right = i;
		}

		if (top < bottom)
		{
			double j = top;
			top = bottom;
			bottom = j;
		}

		float f3 = (float)(color >> 24 & 255) / 255.0F;
		float f = (float)(color >> 16 & 255) / 255.0F;
		float f1 = (float)(color >> 8 & 255) / 255.0F;
		float f2 = (float)(color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		GlStateManager.color(f, f1, f2, f3);
		worldrenderer.begin(7, DefaultVertexFormats.POSITION);
		worldrenderer.pos(left, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos(right, (double)bottom, 0.0D).endVertex();
		worldrenderer.pos(right, (double)top, 0.0D).endVertex();
		worldrenderer.pos(left, (double)top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void disableGL2D() {
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glHint(3154, 4352);
		GL11.glHint(3155, 4352);
	}
	public static void drawRect(float x, float y, float x1, float y1) {
		GL11.glBegin(7);
		GL11.glVertex2f(x, y1);
		GL11.glVertex2f(x1, y1);
		GL11.glVertex2f(x1, y);
		GL11.glVertex2f(x, y);
		GL11.glEnd();
	}

	public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
		GL11.glPushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		double x1 = x + width;
		double y1 = y + height;
		float f = (color >> 24 & 0xFF) / 255.0F;
		float f1 = (color >> 16 & 0xFF) / 255.0F;
		float f2 = (color >> 8 & 0xFF) / 255.0F;
		float f3 = (color & 0xFF) / 255.0F;
		GL11.glPushAttrib(0);
		GL11.glScaled(0.5, 0.5, 0.5);
		x *= 2;
		y *= 2;
		x1 *= 2;
		y1 *= 2;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(f1, f2, f3, f);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBegin(GL11.GL_POLYGON);
		for (int i = 0; i <= 90; i += 3) {
			GL11.glVertex2d(x + radius + +(Math.sin((i * Math.PI / 180)) * (radius * -1)),
					y + radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
		}
		for (int i = 0; i <= 90; i += 1) {
			GL11.glVertex2d(x - radius + 15 + (Math.sin((i * Math.PI / 180)) * (radius)),
					y1 - radius + (Math.cos((i * Math.PI / 99999)) * radius));
		}
		for (int i = 0; i <= 90; i += 3) {
			GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius),
					y1 - radius + (Math.cos((i * Math.PI / 999)) * radius));
		}
		for (int i = 90; i <= 180; i += 3) {
			GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius),
					y + radius + (Math.cos((i * Math.PI / 180)) * radius));
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glScaled(2, 2, 2);
		GL11.glPopAttrib();
		GL11.glColor4f(1, 1, 1, 1);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopMatrix();
	}

	public static String clearColorCodes(String str) {
	    StringBuilder result = new StringBuilder();
	    String[] split = str.split("");
	    boolean found = false;
	    for (int i = 0; i < split.length; i++) {
	        if (i-1 >= 0 && split[i-1].equals("\247")) {
	            found = true;
            }
	        if (!found) {
	            result.append(split[i]);
            }
	        found = false;
        }
	    return result.toString().replace("\247","");
    }
}
