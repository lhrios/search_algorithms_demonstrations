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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import search_algoritms_demonstrations.graphics.Misc;

public class MazeDrawer extends Panel {
	/* Public: */
	public MazeDrawer(Maze maze, MazeDrawingOptions options, MazeCellDrawer drawer) {
		this.maze = maze;
		this.options = options;
		this.drawer = drawer;
	}

	public MazeCell getMazeCellFromDrawing(int x, int y) {
		if (this.maze != null) {
			x -= this.o_x;
			y -= this.o_y;

			x = x / this.size_with_border;
			y = y / this.size_with_border;
			if (this.options.draw_coordinates) {
				x--;
				y--;
			}

			if (x < 0 || x >= this.maze.getW() || y < 0 || y >= this.maze.getH())
				return null;
			return this.maze.getMazeCell(x, y);

		} else {
			return null;
		}
	}

	@Override
	public Dimension getMinimumSize() {
		return this.getDrawnMazeSize(null);
	}

	@Override
	public Dimension getPreferredSize() {
		return this.getDrawnMazeSize(null);
	}

	@Override
	public void update(Graphics g) {
		/* As we do not want do clear the background twice. */
		this.paint(g);
	}

	@Override
	public void paint(Graphics g) {
		if (this.maze == null) {
			super.paint(g);

		} else {
			this.draw((Graphics2D) g);
		}
	}

	public void setMaze(Maze maze) {
		this.maze = maze;
		this.repaint();
		this.invalidate();
		this.validate();
	}

	/* Private: */
	private static final long serialVersionUID = -8503925252923929092L;

	private void ComputeCellSize(Dimension gridDimension, Dimension availableArea) {
		this.cell_size = 0;

		if (availableArea != null) {
			this.cell_size =
					Math.min((availableArea.height - (this.options.cell_border * 2 * gridDimension.height)) / gridDimension.height,
							(availableArea.width - (this.options.cell_border * 2 * gridDimension.width)) / gridDimension.width);
		}
		this.cell_size = Math.max(this.cell_size, this.options.min_cell_size);
		this.size_with_border = this.cell_size + this.options.cell_border * 2;

		assert (this.options.min_cell_size == this.cell_size || availableArea != null);
	}

	private Dimension getDrawnMazeSize(Dimension availableArea) {
		Dimension gridDimension = new Dimension(0, 0);

		if (this.maze != null) {
			gridDimension.width = this.maze.getW() + (this.options.draw_coordinates ? 1 : 0);
			gridDimension.height = this.maze.getH() + (this.options.draw_coordinates ? 1 : 0);

			this.ComputeCellSize(gridDimension, availableArea);

			gridDimension.width *= this.size_with_border;
			gridDimension.height *= this.size_with_border;
		}

		return gridDimension;
	}

	private Maze maze; // TODO: Use MVC.
	private MazeDrawingOptions options;
	private MazeCellDrawer drawer;
	private int cell_size, size_with_border, o_x, o_y;

	private void draw(Graphics2D g2) {
		Dimension drawn_maze_size, available_area;

		available_area = new Dimension((int) this.getBounds().getWidth(), (int) this.getBounds().getHeight());

		drawn_maze_size = this.getDrawnMazeSize(available_area);

		this.o_x = (available_area.width - drawn_maze_size.width) / 2;
		this.o_y = (available_area.height - drawn_maze_size.height) / 2;

		BufferedImage buffer = new BufferedImage(available_area.width, available_area.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D bg2 = (Graphics2D) buffer.getGraphics();
		bg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		bg2.setColor(super.getBackground());
		bg2.fillRect(0, 0, available_area.width, available_area.height);

		bg2.translate(this.o_x, this.o_y);

		if (this.options.draw_coordinates) {
			String string;
			int x, y;

			bg2.setColor(this.options.border_color);
			bg2.fillRect(0, 0, this.size_with_border, this.size_with_border);

			for (int i = 1; i <= this.maze.getW(); i++) {
				string = Integer.toString(i);

				bg2.setColor(this.options.border_color);
				bg2.fillRect(i * this.size_with_border, 0, this.size_with_border, this.size_with_border);
				bg2.setColor(this.options.background_color);
				bg2.fillRect(i * this.size_with_border + this.options.cell_border, this.options.cell_border, this.cell_size, this.cell_size);

				bg2.setColor(this.options.border_color);
				drawn_maze_size = Misc.getStringPosition(bg2, string, this.cell_size, this.cell_size);
				x = i * this.size_with_border + this.options.cell_border + drawn_maze_size.width;
				y = this.options.cell_border + drawn_maze_size.height;
				bg2.drawString(string, x, y);
			}

			for (int i = 1; i <= this.maze.getH(); i++) {
				string = Character.toString((char) (i - 1 + 'A'));

				bg2.setColor(this.options.border_color);
				bg2.fillRect(0, i * this.size_with_border, this.size_with_border, this.size_with_border);
				bg2.setColor(this.options.background_color);
				bg2.fillRect(this.options.cell_border, i * this.size_with_border + this.options.cell_border, this.cell_size, this.cell_size);

				bg2.setColor(this.options.border_color);
				drawn_maze_size = Misc.getStringPosition(bg2, string, this.cell_size, this.cell_size);
				x = this.options.cell_border + drawn_maze_size.width;
				y = i * this.size_with_border + this.options.cell_border + drawn_maze_size.height;
				bg2.drawString(string, x, y);
			}
		}

		{

			if (this.options.draw_coordinates) {
				bg2.translate(this.options.cell_border * 2 + this.cell_size, this.options.cell_border * 2 + this.cell_size);
			}

			BufferedImage cell_buffer = new BufferedImage(this.cell_size, this.cell_size, BufferedImage.TYPE_INT_RGB);
			Graphics2D cg2 = (Graphics2D) cell_buffer.getGraphics();
			cg2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (int y = 0; y < this.maze.getH(); y++) {
				for (int x = 0; x < this.maze.getW(); x++) {

					bg2.setColor(this.options.border_color);
					bg2.fillRect(x * this.size_with_border, y * this.size_with_border, this.size_with_border, this.size_with_border);
					bg2.setColor(this.options.background_color);
					bg2.fillRect(x * this.size_with_border + this.options.cell_border, y * this.size_with_border + this.options.cell_border, this.cell_size, this.cell_size);

					this.drawer.drawMazeCell(cg2, this.maze.getMazeCell(x, y), this.cell_size);
					bg2.drawImage(cell_buffer, x * this.size_with_border + this.options.cell_border, y * this.size_with_border + this.options.cell_border, null);
				}
			}
		}

		bg2.dispose();
		g2.drawImage(buffer, 0, 0, null);
	}
}
