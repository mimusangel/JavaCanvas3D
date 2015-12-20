package fr.mimus.canvas.utils;

public class Vec3 {
	public double x, y, z;
	
	public Vec3()
	{
		this(0.0);
	}
	
	public Vec3(double v)
	{
		this(v, v, v);
	}

	public Vec3(Vec3 v)
	{
		this(v.x, v.y, v.z);
	}
	
	public Vec3(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double lenght() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vec3 normalize() {
		double l = lenght();
		this.x /= l;
		this.y /= l;
		this.z /= l;
		
		return this;
	}
	
	public Vec3 mul(double v)
	{
		this.x *= v;
		this.y *= v;
		this.z *= v;
		
		return this;
	}
	
	public Vec3 div(double v)
	{
		this.x /= v;
		this.y /= v;
		this.z /= v;
		
		return this;
	}
	
	public Vec3 add(double v)
	{
		this.x += v;
		this.y += v;
		this.z += v;
		
		return this;
	}
	
	public Vec3 sub(double v)
	{
		this.x -= v;
		this.y -= v;
		this.z -= v;
		
		return this;
	}
}
