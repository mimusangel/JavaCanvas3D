package fr.mimus.canvas.render;

public class Color {
	public static final int BLACK =			0;
	public static final int DARK_GRAY =		getRGB(64, 64, 64);
	public static final int GRAY =			getRGB(128, 128, 128);
	public static final int LIGHT_GRAY = 	getRGB(192, 192, 192);
	public static final int WHITE =			0xffffff;
	public static final int RED =			getRGB(255, 0, 0);
	public static final int GREEN =			getRGB(0, 255, 0);
	public static final int BLUE =			getRGB(0, 0, 255);
	public static final int YELLOW =		getRGB(255, 255, 0);
	public static final int PINK =			getRGB(255, 0, 255);
	public static final int CYAN =			getRGB(0, 255, 255);
	
	public static int getRGB(int r, int g, int b)
	{
		r = r & 0xff;
		g = g & 0xff;
		b = b & 0xff;
		return r << 16 | g << 8 | b;
	}
}
