package fr.mimus.canvas.render;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture extends Bitmap {
	public static Texture floor =	loadTexture("floor.png");
	public static Texture wall =	loadTexture("wall.png");
	public static Texture sprites =	loadTexture("sprites.png");
	
	int alpha[];
	public Texture(int width, int height) {
		super(width, height);
		alpha = new int[width * height];
		for (int i = 0; i < alpha.length; i++) alpha[i] = 0xff;
	}
	
	public int getRGB(int x, int y)
	{
		return pixels[x + y * width];
	}
	
	public int getAlpha(int x, int y)
	{
		return alpha[x + y * width];
	}
	
	public boolean hasAlpha(int x, int y)
	{
		return getAlpha(x, y) != 0xff;
	}
	
	public int getRGBWithAlpha(int x, int y)
	{
		double a = getAlpha(x, y) / 255.0;
		int color = getRGB(x, y);
		int r1 = (int) (((color >> 16) & 0xff) * a);
		int g1 = (int) (((color >> 8) & 0xff) * a);
		int b1 = (int) (((color) & 0xff) * a);
		return r1 << 16 | g1 << 8 | b1;
	}
	
	public static Texture loadTexture(String path)
	{
		Texture texture = null;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			texture = new Texture(image.getWidth(), image.getHeight());
			
			image.getRGB(0, 0, texture.width, texture.height, texture.pixels, 0, texture.width);
			for(int i = 0; i < texture.pixels.length; i++) // While for delete alpha
			{
				texture.alpha[i] = (texture.pixels[i] >> 24) & 0xff;
				texture.pixels[i] = texture.pixels[i] & 0xffffff;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}
}
