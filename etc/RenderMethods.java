package sh4ll.etc;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderMethods {

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
}
