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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import search_algoritms_demonstrations.graphics.Misc;

public class BaseMazeCellDrawer extends MazeCellDrawer {
	/* Public: */
	public BaseMazeCellDrawer(MazeDrawingOptions options) {
		this.options = options;
	}

	@Override
	public void drawMazeCell(Graphics2D g2, MazeCell maze_cell, int cell_size) {
		Dimension d;
		String string;

		g2.setColor(this.options.background_color);
		g2.fillRect(0, 0, cell_size, cell_size);

		g2.setFont(new Font("", Font.PLAIN, 9));
		string = maze_cell.x + "," + maze_cell.y;
		g2.setColor(this.options.border_color);
		d = Misc.getStringPosition(g2, string, cell_size, cell_size);
		g2.drawString(string, d.width, d.height);
	}

	@Override
	public int getLegendItemsCount() {
		return 0;
	}

	@Override
	public void drawLegendItem(int item_number, Graphics2D g2, int cell_size) {
	}

	@Override
	public String getLegendItemsLabel(int item_number) {
		return null;
	}

	public void setMaze(Maze maze) {
		this.maze = maze;
	}

	/* Protected: */
	protected final MazeDrawingOptions options;
	protected Maze maze;

	protected void drawPathCell(Graphics2D g2, int cell_size) {
		g2.setColor(this.options.path_cell_color);
		Misc.drawBorderedRect(g2, 0, 0, cell_size, cell_size, this.options.internal_border);
	}

	protected void drawFrontierCell(Graphics2D g2, int cell_size) {
		g2.setColor(this.options.open_list_cell_color);
		Misc.drawBorderedRect(g2, 0, 0, cell_size, cell_size, this.options.internal_border);
	}

	protected void drawStart(Graphics2D g2, MazeDrawingOptions options, int cell_size) {
		g2.setColor(options.rhombus_color);
		g2.fill(this.getRhombus(cell_size));
		g2.setColor(options.border_color);
		g2.setFont(options.start_goal_cell_drawing_font);
		Dimension d = Misc.getStringPosition(g2, "S", cell_size, cell_size);
		g2.drawString("S", d.width, d.height);
	}

	protected void drawGoal(Graphics2D g2, MazeDrawingOptions options, int cell_size) {
		g2.setColor(options.rhombus_color);
		g2.fill(this.getRhombus(cell_size));
		g2.setColor(options.border_color);
		g2.setFont(options.start_goal_cell_drawing_font);
		Dimension d = Misc.getStringPosition(g2, "G", cell_size, cell_size);
		g2.drawString("G", d.width, d.height);
	}

	protected void drawBlockedCell(Graphics2D g2, MazeDrawingOptions options, int cell_size) {
		g2.setColor(options.blocked_cell_color);
		g2.fillRect(0, 0, cell_size, cell_size);
	}

	protected void drawFreeCell(Graphics2D g2, int maze_cell_cost, int cell_size) {
		switch (maze_cell_cost) {
			case 1:
				g2.setColor(this.options.free_cell_1_color);
			break;
			case 2:
				g2.setColor(this.options.free_cell_2_color);
			break;
			case 3:
				g2.setColor(this.options.free_cell_3_color);
			break;
		}
		g2.fillRect(0, 0, cell_size, cell_size);
	}

	protected void drawClosedCell(Graphics2D g2, int cell_size) {
		g2.setPaint(this.options.visited_cell_texture_paint);
		Misc.drawBorderedRect(g2, 0, 0, cell_size, cell_size, this.options.internal_border);
	}

	protected GeneralPath getRhombus(int cell_size) {
		GeneralPath rhombus = new GeneralPath();
		rhombus.moveTo(cell_size / 2, 0);
		rhombus.lineTo(cell_size, cell_size / 2);
		rhombus.lineTo(cell_size / 2, cell_size);
		rhombus.lineTo(0, cell_size / 2);
		rhombus.lineTo(cell_size / 2, 0);

		return rhombus;
	}
}
