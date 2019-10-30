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

package search_algoritms_demonstrations.a_star_demonstration;

import java.awt.Dimension;
import java.awt.Graphics2D;

import search_algoritms_demonstrations.graphics.Misc;
import search_algoritms_demonstrations.maze.BaseMazeCellDrawer;
import search_algoritms_demonstrations.maze.MazeCell;
import search_algoritms_demonstrations.maze.MazeDrawingOptions;
import search_algoritms_demonstrations.search_algorithms.AStar;

public class AStarMazeCellDrawer extends BaseMazeCellDrawer {
	/* Public: */
	public AStarMazeCellDrawer(MazeDrawingOptions options) {
		super(options);
	}

	@Override
	public void drawMazeCell(Graphics2D g2, MazeCell maze_cell, int cell_size) {
		Dimension d;

		if (maze_cell.isBlocked()) {
			this.drawBlockedCell(g2, this.options, cell_size);

		} else {
			this.drawFreeCell(g2, maze_cell.getCost(), cell_size);

			if (this.maze.getStart() == maze_cell) {
				this.drawStart(g2, this.options, cell_size);

			} else if (this.maze.getGoal() == maze_cell) {
				this.drawGoal(g2, this.options, cell_size);
			}

			if (!maze_cell.isPathFlagOn()) {
				if (this.a_star.isInOpenList(maze_cell)) {
					this.drawFrontierCell(g2, cell_size);

				} else if (this.a_star.isInClosedList(maze_cell)) {
					this.drawClosedCell(g2, cell_size);
				}
			}

			if (maze_cell != this.maze.getGoal() && maze_cell != this.maze.getStart()) {
				if (maze_cell.isPathFlagOn()) {
					this.drawPathCell(g2, cell_size);
				}
				{
					String text = new String();

					g2.setFont(this.options.cell_drawing_font);
					if (this.a_star.isInOpenList(maze_cell) || this.a_star.isInClosedList(maze_cell)) {
						text = Integer.toString(this.a_star.getMazeCellG(maze_cell));
					} else {
						text += Misc.INFINITY_SYMBOL;
					}
					text += "/" + this.a_star.getMazeCellH(maze_cell);
					g2.setColor(this.options.border_color);
					d = Misc.getStringPosition(g2, text, cell_size, cell_size);
					g2.drawString(text, d.width, d.height);
				}
			}
		}
	}

	@Override
	public int getLegendItemsCount() {
		return AStarLegendItem.values().length;
	}

	@Override
	public String getLegendItemsLabel(int item_number) {
		return AStarLegendItem.values()[item_number].getLabe();
	}

	@Override
	public void drawLegendItem(int item_number, Graphics2D g2, int cell_size) {
		AStarLegendItem legend_item = AStarLegendItem.values()[item_number];

		switch (legend_item) {
			case GOAL:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				this.drawGoal(g2, this.options, cell_size);
			break;

			case START:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				this.drawStart(g2, this.options, cell_size);
			break;

			case BLOCKED:
				this.drawBlockedCell(g2, this.options, cell_size);
			break;

			case FREE_CELL_1:
				this.drawFreeCell(g2, 1, cell_size);
			break;

			case FREE_CELL_2:
				this.drawFreeCell(g2, 2, cell_size);
			break;

			case FREE_CELL_3:
				this.drawFreeCell(g2, 3, cell_size);
			break;

			case CLOSED_CELL:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				this.drawClosedCell(g2, cell_size);
			break;

			case FRONTIER_CELL:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				this.drawFrontierCell(g2, cell_size);
			break;

			case PATH_CELL:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				this.drawPathCell(g2, cell_size);
			break;

		}
		g2.setColor(this.options.border_color);
		Misc.drawBorderedRect(g2, 0, 0, cell_size, cell_size, this.options.cell_border);
	}

	public void setAStar(AStar a_star) {
		this.a_star = a_star;
	}

	/* Private: */
	private AStar a_star;

}
