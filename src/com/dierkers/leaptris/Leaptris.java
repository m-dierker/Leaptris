package com.dierkers.leaptris;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.dierkers.gui.transparent.JTransparentFrame;
import com.leapmotion.leap.*;

@SuppressWarnings("unused")
public class Leaptris extends Listener implements KeyListener {
	private JTransparentFrame frame;
	private boolean exiting = false;
	private Robot robot;
	private Controller controller;
	private LeaptrisAction action = LeaptrisAction.NONE;

	@SuppressWarnings("serial")
	public Leaptris(final Controller controller) {
		this.controller = controller;
		frame = new JTransparentFrame("Leaptris") {
			public void paint(Graphics gOrig) {
				gOrig.clearRect(0, 0, frame.getWidth(), frame.getHeight());

				BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_ARGB);
				Graphics g = image.getGraphics();

				g.setColor(Color.blue);
				for (LeaptrisAction action : LeaptrisAction.values()) {
					if (action.getWidth() != 0 && action.getHeight() != 0) {
						g.fillRect(action.getX(), action.getY(), action.getWidth(), action.getHeight());
					}
				}

				Frame frame = controller.frame();

				if (!frame.hands().empty()) {
					Hand hand = frame.hands().get(0);

					FingerList fingers = hand.fingers();

					if (!fingers.empty()) {
						Finger finger = null;

						for (Finger f : fingers) {
							if (f.isValid()) {
								finger = f;
								break;
							}
						}

						if (finger == null) {
							return;
						}

						Vector pos = finger.tipPosition();

						// Approx range is -[200, 200]
						int x = (int) pos.getX();

						// Seems like it's [0, 500], although 0 is unlikely
						int y = (int) pos.getY();

						x += 200;
						x = (int) (5.0 * x / 4);
						y = 500 - y;

						// range of (x,y) is (0,0) to (500, 500)

						// adjust y for comfort
						y -= 100;

//						System.out.println(x + ", " + y);

						g.setColor(Color.red);
						g.fillOval(x, y, 20, 20);

						processAction(x, y);
					}
				}

				// About 60 FPS
				sleep(16);

				gOrig.drawImage(image, 0, 0, null);
				repaint();
			}
		};
		frame.setSize(500, 500);
		frame.addKeyListener(this);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void processAction(int x, int y) {
		boolean found = false;
		for (LeaptrisAction action : LeaptrisAction.values()) {
			if (action.getWidth() == 0) {
				continue;
			}

			if (x >= action.getX() && y >= action.getY() && x <= action.getX() + action.getWidth()
					&& y <= action.getY() + action.getHeight()) {
				found = true;
				if (this.action != action) {
					this.action = action;
//					System.out.println("Pressing Key");
					System.out.println(action.name());
					pressKey(action.getKey());
				}
				break;
			}
		}
		if (!found && this.action != LeaptrisAction.NONE) {
			action = LeaptrisAction.NONE;
		}
	}

	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (Exception e) {

		}
	}

	public void pressKey(int keyCode) {
		robot.keyPress(keyCode);
		sleep(50);
		robot.keyRelease(keyCode);
	}

	public static void main(String... args) {
		Controller controller = new Controller();
		Leaptris leaptris = new Leaptris(controller);

		controller.addListener(leaptris);

		leaptris.getFrame().repaint();

		while (!leaptris.isExiting()) {
			Thread.yield();
			sleep(500);
		}

		controller.removeListener(leaptris);
		System.exit(0);
	}

	private boolean isExiting() {
		return exiting;
	}

	private JFrame getFrame() {
		return frame;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			exit();
		}
	}

	public void exit() {
		frame.setVisible(false);
		frame.dispose();
		exiting = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

}
