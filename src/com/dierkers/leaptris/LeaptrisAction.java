package com.dierkers.leaptris;

import java.awt.event.KeyEvent;

public enum LeaptrisAction {
	NONE(0, 0, 0, 0, 0), MOVE_LEFT(25, 150, 150, 150, KeyEvent.VK_LEFT), MOVE_RIGHT(275, 150, 150, 150,
			KeyEvent.VK_RIGHT), ROTATE(150, 0, 150, 150, KeyEvent.VK_UP), DROP(150, 250, 150, 150, KeyEvent.VK_SPACE), SWAP(
			0, 0, 0, 0, 0);

	private final int x, y, width, height, keyCode;

	private LeaptrisAction(int x, int y, int width, int height, int keyCode) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.keyCode = keyCode;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getKey() {
		return keyCode;
	}
}
