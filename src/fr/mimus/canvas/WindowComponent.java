package fr.mimus.canvas;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import fr.mimus.canvas.render.Game;


public class WindowComponent extends Canvas implements Runnable
{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
	private static final int HEIGHT = WIDTH * 3 / 4;
	private static final int SCALE = 2;
	
	private boolean running;
	private Thread thread;
	private BufferedImage image;
	private int[] pixels;
	private Game screen;
	private InputHandler input;
	
	public WindowComponent()
	{
		Dimension d = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);

		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		input = new InputHandler();
		
		this.addFocusListener(input);
		this.addMouseListener(input);
		this.addMouseMotionListener(input);
		this.addKeyListener(input);
		
		this.setFocusable(true);
	}
	
	public synchronized void start()
	{
		if (running) return;
		running = true;
		thread = new Thread(this);
		init();
		thread.start();
	}

	public synchronized void stop()
	{
		if (!running) return;
		running = false;
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private void init()
	{
		screen = new Game(WIDTH, HEIGHT);
	}
	
	public void run()
	{
		int frames = 0;
		int ticks = 0;
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		
		requestFocus();
		
		while (running)
		{
			long now = System.nanoTime();
			
			if (now - lastTime > nsPerTick)
			{
				lastTime += nsPerTick;
				update();
				ticks++;
			}
			else
			{
				render();
				frames++;
			}
			if (System.currentTimeMillis() - timer > 1000)
			{
				System.out.println("Frames: " + frames + ", Ticks: " + ticks);
				timer += 1000;
				frames = 0;
				ticks = 0;
			}
		}
		dispose();
	}

	private void render()
	{
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null)
		{
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();

		screen.render();
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = screen.pixels[i];

		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
		bs.show();
	}

	private void update()
	{
		screen.update(input);
	}
	
	private void dispose()
	{
		System.exit(0);
	}
	
	public static void main(String[] args)
	{
		WindowComponent game = new WindowComponent();
		Frame frame = new Frame();
		
		frame.setTitle("Java Canvas 3D");
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent arg0)
			{
				game.stop();
			}
		});
		frame.add(game);
		frame.pack();
		frame.setVisible(true);
		game.start();
	}
}
