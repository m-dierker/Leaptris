package com.dierkers.gui.transparent;

import java.awt.Color;

import javax.swing.JFrame;

public class JTransparentFrame extends JFrame {
	/**
	 * Generated
	 */
	private static final long serialVersionUID = 4447235365270699523L;

	public JTransparentFrame() {
		this("");
	}

	public JTransparentFrame(String title) {
		super(title);

		makeTransparent();

		setUndecorated(true);

	}

	public void makeTransparent() {
		// Assume only OS X for now
		setBackground(new Color(0, 0, 0, 0));
	}
}
