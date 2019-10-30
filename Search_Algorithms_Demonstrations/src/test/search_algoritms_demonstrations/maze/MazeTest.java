/*
 * Copyright 2010, 2013, 2014 Luis Henrique Oliveira Rios
 *
 * This file is part of Search Algorithms Demonstrations.
 *
 * Search Algorithms Demonstrations is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Search Algorithms Demonstrations is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Search Algorithms Demonstrations.  If not, see <http://www.gnu.org/licenses/>.
 */

package search_algoritms_demonstrations.maze;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MazeTest implements MouseListener, WindowListener {
	/* Public: */
	public Maze maze;
	public MazeDrawer mazeDrawer;
	public Frame frame;
	public MazeDrawingOptions options;

	public MazeTest() {
		this.maze = new Maze(55, 10, 3, 0.25f, 1, false);
		this.options = new MazeDrawingOptions();
		BaseMazeCellDrawer cellDrawer = new BaseMazeCellDrawer(this.options);
		cellDrawer.setMaze(this.maze);
		this.mazeDrawer = new MazeDrawer(this.maze, this.options, cellDrawer);
		this.mazeDrawer.addMouseListener(this);

		this.frame = new Frame("Maze Test");

		this.frame.addWindowListener(this);
		this.frame.add(this.mazeDrawer);
		this.frame.setMinimumSize(new Dimension(1000, 300));
		this.frame.pack();
		this.frame.setVisible(true);

		System.out.println(this.maze);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		MazeCell cell = this.mazeDrawer.getMazeCellFromDrawing(e.getX(), e.getY());
		if (cell != null) {
			System.out.println(cell.getX() + " " + cell.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.frame.dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
