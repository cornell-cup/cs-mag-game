import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;

public class BoardSimulation {

	public static void main(String[] args) {
		JFrame window = new JFrame();
		Simulator s = new Simulator();
		Canvas c = new DrawCanvas(s);

		c.setSize(800, 600);
		window.add(c);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (s.inBounds()) {
						s.run();
						c.repaint();
					}
					Thread.sleep(33);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		t.start();
	}

}

class Simulator implements MouseListener {

	// 1 pixel ~ 1 millimeter
	static final int WIDTH = 800;
	static final int HEIGHT = 600;
	static final int GAP = 10; // Distance between underlying magnet and board
	static final double STRENGTH = .1;
	static final double GRAV = 10000;

	// The piece
	double x, y;
	double vx, vy;
	static final double MASS = .1;
	static final double uk = .000001;
	static final double us = .000001;

	// Magnets
	ArrayList<Magnet> magnets;

	public Simulator() {
		x = 100;
		y = 180;
		vx = 0.;
		vy = 0.;

		magnets = new ArrayList<Magnet>();

		for (int i = 0; i < WIDTH; i += 40)
			for (int j = 0; j < HEIGHT; j += 40)
				magnets.add(new Magnet(i, j, 50));

		for (int i = 20; i < WIDTH; i += 40)
			for (int j = 20; j < HEIGHT; j += 40)
				magnets.add(new Magnet(i, j, 50));
	}

	public void run() {
		x += vx / 30.;
		y += vy / 30.;
		incrVel();
//		System.out.println(vx);
//		System.out.println(vy);
	}

	public void incrVel() {
		for (Magnet m : magnets) {
			if (m.isOn()) {
				double surfaceDist = Math.hypot(m.x - x, m.y - y);
				double d = Math.hypot(GAP, surfaceDist);
				double a = m.charge() * STRENGTH / (d * d); // a = 1 / r^2
				double az = (GAP * a / d) + GRAV;
				double ax = (m.x - x) * a / d;
				double ay = (m.y - y) * a / d;
				double N = MASS * az;

				double v = Math.hypot(vx, vy);
				if (v < .1) { // Static
					double f = N * us;
					if (a > f) {
						a -= f;
						ax = (m.x - x) * a / d; 
						ay = (m.y - y) * a / d;
					} else {
						ax = 0;
						ay = 0;
					}
					// a should never be negative due to static friction
				} else { // Kinetic
					double f = N * uk;
					ax = ax - (f * (vx / v));
					ay = ay - (f * (vy / v));
				}

				vx += ax / 30.;
				vy += ay / 30.;
			}
		}
	}

	public boolean inBounds() {
		return x >= 0 && y >= 0 && x <= WIDTH && y <= HEIGHT;
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int type = e.getButton();
		switch (type) {
		case 1: // Left click turns magnet on or off
			for (Magnet m : magnets) {
				if (m.clicked(x, y))
					m.onOff();
			}
			break;
		case 2: // Step sequence
			
			break;
		case 3: // Right click flips charge
			for (Magnet m : magnets) {
				if (m.clicked(x, y))
					m.flip();
			}
			break;
		default:
			System.out.println("yo");
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}

class Magnet {
	int x, y;
	double charge;
	boolean on;

	public Magnet(int x, int y, double charge) {
		this.x = x;
		this.y = y;
		this.charge = charge;
		on = false;
	}

	public void onOff() {
		if (on)
			on = false;
		else
			on = true;
	}

	public void flip() {
		charge = -charge;
	}

	public double charge() {
		if (on)
			return charge;
		return 0;
	}

	public boolean isOn() {
		return on;
	}

	public boolean clicked(int x, int y) {
		int xUpper = this.x + 15;
		int yUpper = this.y + 15;
		return x > this.x && x < xUpper && y > this.y && y < yUpper;
	}

	// public void handle(new MouseEvent)

}

class DrawCanvas extends Canvas {

	Simulator s;
	BufferedImage img;

	static final int WIDTH = 800;
	static final int HEIGHT = 600;

	public DrawCanvas(Simulator s) {
		this.s = s;
		img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.addMouseListener(s);
	}

	@Override
	public void update(Graphics g2) {
		Graphics g = img.getGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.RED);
		g.fillRoundRect((int) s.x, (int) s.y, 20, 20, 20, 20);

		for (Magnet m : s.magnets) {
			if (m.charge == 0 || !m.isOn())
				g.setColor(Color.GRAY);
			else if (m.charge > 0)
				g.setColor(Color.RED);
			else if (m.charge < 0)
				g.setColor(Color.BLUE);
			g.fillRoundRect(m.x, m.y, 15, 15, 15, 15);
		}
		g2.drawImage(img, 0, 0, null);
	}
}