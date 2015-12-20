package fr.mimus.canvas.render;

public class Bitmap {
	public final int width;
	public final int height;
	public final int[] pixels;
	public Bitmap(int width, int height)
	{
		this.width = width;
		this.height = height;
		pixels = new int[width * height];
		for(int i = 0; i < pixels.length; i++) pixels[i] = 0;
	}
	
	public void clear()
	{
		for(int i = 0; i < pixels.length; i++) pixels[i] = 0;
	}
	
	public void draw(Bitmap b, int x, int y)
	{
		for (int j = 0; j < b.height; j++)
		{
			int yy = j + y;
			if (yy < 0 || yy >= height)
				continue;
			for (int i = 0; i < b.width; i++)
			{
				int xx = i + x;
				if (xx < 0 || xx >= width)
					continue;

				pixels[xx + yy * width] = b.pixels[i + j * b.width];
			}
		}
	}
	
	public void fill(int x, int y, int w, int h, int color)
	{
		for (int i = 0; i < h; i++)
		{
			for (int j = 0; j < w; j++)
			{
				pixels[(x + j) + (y + i) * width] = color;
			}
		}
	}
	
	public void circle(int x, int y, int s, int color)
	{
		int ss = s / 2;
		int cx = x + ss;
		int cy = y + ss;
		
		for (int i = 0; i < s; i++)
		{
			for (int j = 0; j < s; j++)
			{
				int xx = (x + j) - cx;
				int yy = (y + i) - cy;
				if (xx * xx + yy * yy <= ss * ss)
					pixels[(x + j) + (y + i) * width] = color;
			}
		}
	}
}
