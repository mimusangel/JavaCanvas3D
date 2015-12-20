package fr.mimus.canvas.utils;

public class Vec2 {
	public double x, y;
	
	public Vec2()
	{
		this(0);
	}
	
	public Vec2(double v)
	{
		this(v, v);
	}

	public Vec2(Vec2 v)
	{
		this(v.x, v.y);
	}
	
	public Vec2(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
