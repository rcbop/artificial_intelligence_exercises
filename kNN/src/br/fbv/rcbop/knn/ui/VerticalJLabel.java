package br.fbv.rcbop.knn.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class VerticalJLabel extends JLabel {
	private static final long	serialVersionUID	= 8148886004594746860L;

	public VerticalJLabel(String s) {
		super(s);
		setPreferredSize(new Dimension(15, 130));
		setMinimumSize(new Dimension(15, 130));
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(13.0, 120.0);
		g2d.rotate(300);
		g2d.drawString(this.getText(), 0, 0);
	}
}
