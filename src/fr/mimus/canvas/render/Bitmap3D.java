package fr.mimus.canvas.render;

import fr.mimus.canvas.utils.Vec3;

public class Bitmap3D extends Bitmap {
	
	private double fov, xCenter, yCenter;
	private Vec3 cam;
	private double rotateY, rySin, ryCos;
	private double fogIntensity;
	private double dephBuffer[];
	private double dephWallBuffer[];
	
	public Bitmap3D(int width, int height)
	{
		super(width, height);
		dephBuffer = new double[width * height];
		dephWallBuffer = new double[width * height];
		fov = height;
		cam = new Vec3();
		rotate(0);
		fogIntensity = 4;
	}
	
	public void translate(double x, double y, double z)
	{
		cam.x = x;
		cam.y = y;
		cam.z = z;
	}
	
	public void rotate(double rot)
	{
		rotateY = rot;
		rySin = Math.sin(rotateY);
		ryCos = Math.cos(rotateY);
	}
	
	public void render()
	{
		xCenter = width / 2.0;
		yCenter = height / 2.0;
		clear();
		for (int y = 0; y < height; y++)
		{
			double yd = (y - yCenter) / fov;
			double zd = (4 - cam.y) / yd;
			
			if (yd < 0)
				zd = (4 + cam.y) / -yd;
			
			for (int x = 0; x < width; x++)
			{
				double xd = (x - xCenter) / fov;
				xd *= zd;
				
				double xx = xd * ryCos - zd * rySin + (cam.x + 0.5) * 8;
				double yy = xd * rySin + zd * ryCos + (cam.z) * 8;

				int xPix = (int) xx * 2;
				int yPix = (int) yy * 2;
				if (xx < 0)
					xPix--;
				if (yy < 0)
					yPix--;
				
				dephBuffer[x + y * width] = 0xff;
				dephWallBuffer[x + y * width] = 0xff;
				if (zd != 0 && xx >= 0 && yy >= 0)
				{
					int xCase = (int) Math.floor(xx / 8.0);
					int zCase = (int) Math.floor(yy / 8.0);
					dephBuffer[x + y * width] = zd;
					pixels[x + y * width] = ((yPix & 0xf) * 16) << 16 | ((xPix & 0xf) * 16);
					if(xCase == 0 && zCase == 1)
						pixels[x + y * width] = Texture.floor.pixels[(xPix & 0xf) + (yPix & 0xf) * Texture.floor.width];
				}
			}
		}
//		renderWall(0, 0, 2, 1, 2, 0, Color.WHITE);
//		renderWall(1, 0, 2, 1, 1, 0, Color.LIGHT_GRAY);
//		renderWall(1, 0, 1, 2, 1, 0, Color.WHITE);
//		renderWall(double x0, double y0, double z0, double x1, double z1, int tex, int color)
		for (int i = 0; i < 16; i++)
		{
			renderWall(i + 1, 0, 0, i, 0, 0, Color.WHITE);
			renderWall(0, 0, i, 0, i + 1, 0, Color.LIGHT_GRAY);
		}
		renderEntity(1, 0.35, 1, 2, 1.5, 0, Color.WHITE);
	}
	
	public void renderEntity(double x, double y, double z, double scaleX, double scaleY, int tex, int color)
	{
		double xo = x * 2 - 1 - cam.x * 2;
		double yo = y + cam.y / 4;
		double zo = z * 2 - cam.z * 2;

		double xx = xo * ryCos + zo * rySin;
		double yy = yo;
		double zz = -xo * rySin + zo * ryCos;

		double xPixel0 = xx / zz * fov + xCenter - (fov / scaleX) / zz;
		double xPixel1 = xx / zz * fov + xCenter + (fov / scaleX) / zz;
		double yPixel0 = yy / zz * fov + yCenter - (fov / scaleY) / zz;
		double yPixel1 = yy / zz * fov + yCenter + (fov / scaleY) / zz;
		int px0 = (int) Math.round(xPixel0);
		int px1 = (int) Math.round(xPixel1);
		int py0 = (int) yPixel0;
		int py1 = (int) yPixel1;
		
		if (px0 < 0)
			px0 = 0;
		if (py0 < 0)
			py0 = 0;
		if (px1 > width)
			px1 = width;
		if (py1 > height)
			py1 = height;
		double dz = zz * 4;
		double xt0 = tex % 16;
		double xt1 = xt0 + 16;
		double yt0 = tex / 16;
		double yt1 = yt0 + 16;
		
		double r0 = ((color >> 16) & 0xff) / 255.0;
		double g0 = ((color >> 8) & 0xff) / 255.0;
		double b0 = ((color) & 0xff) / 255.0;
		
		for (int py = py0; py < py1; py++)
		{
			double dy = (py - yPixel0) / (yPixel1 - yPixel0);
			int yTex = (int) (yt0 + (yt1 - yt0) * dy);
			
			for (int px = px0; px < px1; px++)
			{
				double dx = (px - xPixel0) / (xPixel1 - xPixel0);
				int xTex = (int) (xt0 + (xt1 - xt0) * dx);
				
				if (dephBuffer[px + py * width] > dz && !Texture.sprites.hasAlpha(xTex & 0xf, yTex & 0xf))
				{
					dephBuffer[px + py * width] = dz;
//					pixels[px + py * width] = 0xffff00;
					int tColor = Texture.sprites.getRGB(xTex & 0xf, yTex & 0xf);//Texture.sprites.pixels[(xTex & 0xf) + (yTex & 0xf) * Texture.sprites.width];
					int r1 = (int) (((tColor >> 16) & 0xff) * r0);
					int g1 = (int) (((tColor >> 8) & 0xff) * g0);
					int b1 = (int) (((tColor) & 0xff) * b0);
					
					pixels[px + py * width] = r1 << 16 | g1 << 8 | b1;
				}
			}
		}
	}
	
	public void renderWall(double x0, double y0, double z0, double x1, double z1, int tex, int color)
	{
		double xo0 = x0 - 0.5 - cam.x;
		double u0 = -y0 - 0.5 + cam.y / 4;
		double d0 = -y0 + 0.5 + cam.y / 4;
		double zo0 = z0 - cam.z;
		
		double xx0 = xo0 * ryCos + zo0 * rySin;
		double zz0 = -xo0 * rySin + zo0 * ryCos;
		
		double xo1 = x1- 0.5 - cam.x;
		double u1 = -y0 - 0.5 + cam.y / 4;
		double d1 = -y0 + 0.5 + cam.y / 4;
		double zo1 = z1 - cam.z;
		
		double xx1 = xo1 * ryCos + zo1 * rySin;
		double zz1 = -xo1 * rySin + zo1 * ryCos;
		
		double xPixel0 = xx0 / zz0 * fov + xCenter;
		double xPixel1 = xx1 / zz1 * fov + xCenter;

		if (xPixel0 > xPixel1)
			return;
		int px0 = (int) xPixel0;
		int px1 = (int) xPixel1;
		if (px0 < 0)
			px0 = 0;
		if (px1 > width)
			px1 = width;

		double yPixel00 = (u0 / zz0 * fov + yCenter);
		double yPixel10 = (u1 / zz1 * fov + yCenter);
		double yPixel01 = (d0 / zz0 * fov + yCenter);
		double yPixel11 = (d1 / zz1 * fov + yCenter);
		double xt0 = tex % 16;
		double xt1 = xt0 + 16;
		double yt0 = tex / 16;
		double yt1 = yt0 + 16;
		
		double r0 = ((color >> 16) & 0xff) / 255.0;
		double g0 = ((color >> 8) & 0xff) / 255.0;
		double b0 = ((color) & 0xff) / 255.0;
		
		for (int x = px0; x < px1; x++)
		{
			double dx = (x - xPixel0) / (xPixel1 - xPixel0);
			double yPixel0 = yPixel00 + 0 + (yPixel10 - yPixel00) * dx;
			double yPixel1 = yPixel01 + 1 + (yPixel11 - yPixel01) * dx;
			
			double dz = zz0 + (zz1 - zz0) * dx;
			int xTex = (int) (xt0 + (xt1 - xt0) * dx);
			
			if (yPixel0 > yPixel1)
				return;
			int py0 = (int)Math.round(yPixel0);
			int py1 = (int)Math.round(yPixel1);
			if (py0 < 0)
				py0 = 0;
			if (py1 > height)
				py1 = height;
			for (int y = py0; y < py1; y++)
			{
				double dy = (y - yPixel0) / (yPixel1 - yPixel0);
				int yTex = (int) (yt0 + (yt1 - yt0) * dy);
				
				if (dephWallBuffer[x + y * width] > dz * 8 && !Texture.wall.hasAlpha(xTex & 0xf, yTex & 0xf))
				{
					dephBuffer[x + y * width] = dz * 8;
					dephWallBuffer[x + y * width] = dz * 8;
					int tColor = Texture.wall.pixels[(xTex & 0xf) + (yTex & 0xf) * Texture.wall.width];
					int r1 = (int) (((tColor >> 16) & 0xff) * r0);
					int g1 = (int) (((tColor >> 8) & 0xff) * g0);
					int b1 = (int) (((tColor) & 0xff) * b0);
					
					pixels[x + y * width] = r1 << 16 | g1 << 8 | b1;
				}
			}
		}
	}
	
	public void renderFog()
	{
		for(int i = 0; i < dephBuffer.length; i++)
		{
			int color = pixels[i];
			if (color == 0)
				continue;
			int r = (color >> 16) & 0xff;
			int g = (color >> 8) & 0xff;
			int b = (color) & 0xff;
			double brightness = 1.0 - (dephBuffer[i] * fogIntensity) / 255.0;
			if (brightness < 0) brightness = 0;
			if (brightness > 1.0) brightness = 1.0;
			r = (int)(r * brightness);
			g = (int)(g * brightness);
			b = (int)(b * brightness);
			
			pixels[i] = r << 16 | g << 8 | b;
		}
	}

	public double getFogIntensity() {
		return fogIntensity;
	}

	public void setFogIntensity(double fogIntensity) {
		this.fogIntensity = fogIntensity;
	}
}
