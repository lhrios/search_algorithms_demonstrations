/*
 * Copyright 2013 Luis Henrique Oliveira Rios
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

package search_algoritms_demonstrations.d_star_lite_demonstration;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

import search_algoritms_demonstrations.graphics.Misc;
import search_algoritms_demonstrations.maze.MazeCell;
import search_algoritms_demonstrations.maze.MazeDrawingOptions;

public class DStarLiteRobotVisionMazeCellDrawer extends DStarLiteAbstractMazeCellDrawer {

	public DStarLiteRobotVisionMazeCellDrawer(MazeDrawingOptions options) {
		super(options);
	}

	@Override
	public int getLegendItemsCount() {
		return DStarLiteLegendItem.values().length - DStarLiteLegendItem.PATH_CELL.ordinal();
	}

	@Override
	public String getLegendItemsLabel(int item_number) {
		return DStarLiteLegendItem.values()[item_number + DStarLiteLegendItem.PATH_CELL.ordinal()].getLabe();
	}

	@Override
	public void drawLegendItem(int item_number, Graphics2D g2, int cell_size) {
		DStarLiteLegendItem legend_item = DStarLiteLegendItem.values()[item_number + DStarLiteLegendItem.PATH_CELL.ordinal()];

		switch (legend_item) {
			case ITERATION:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				this.drawChangedCell(g2, cell_size, 1);
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
			default:
				assert false;
		}
		g2.setColor(this.options.border_color);
		Misc.drawBorderedRect(g2, 0, 0, cell_size, cell_size, this.options.cell_border);
	}

	@Override
	public void drawMazeCell(Graphics2D g2, MazeCell maze_cell, int cell_size) {
		if (maze_cell.isBlocked()) {
			this.drawBlockedCell(g2, this.options, cell_size);

		} else {
			this.drawFreeCell(g2, maze_cell.getCost(), cell_size);

			if (this.robot_cell == maze_cell) {
				this.drawStart(g2, this.options, cell_size);

			} else if (this.maze.getGoal() == maze_cell) {
				this.drawGoal(g2, this.options, cell_size);
			}

			if (!maze_cell.isPathFlagOn()) {
				if (this.d_star_lite.isInOpenList(maze_cell)) {
					this.drawFrontierCell(g2, cell_size);
				}
			}

			if (maze_cell != this.maze.getGoal() && maze_cell != this.robot_cell) {
				if (maze_cell.isPathFlagOn()) {
					this.drawPathCell(g2, cell_size);
				}
				{
					String text = new String();
					Dimension d;

					g2.setFont(this.options.cell_drawing_font);
					int g, rhs, h;
					g = this.d_star_lite.getMazeCellG(maze_cell);
					rhs = this.d_star_lite.getMazeCellRHS(maze_cell);
					h = this.d_star_lite.getMazeCellH(maze_cell);
					text += g == Integer.MAX_VALUE ? Misc.INFINITY_SYMBOL : Integer.toString(g);
					text += "/";
					text += rhs == Integer.MAX_VALUE ? Misc.INFINITY_SYMBOL : Integer.toString(rhs);
					text += "/";
					text += h == Integer.MAX_VALUE ? Misc.INFINITY_SYMBOL : Integer.toString(h);

					g2.setColor(this.options.border_color);
					d = Misc.getStringPosition(g2, text, cell_size, cell_size);
					g2.drawString(text, d.width, d.height);
				}
			}

			if (this.d_star_lite.getMazeCellLastChangeIteration(maze_cell) > 0) {
				this.drawChangedCell(g2, cell_size, this.d_star_lite.getMazeCellLastChangeIteration(maze_cell));
			}
		}
	}

	/* Private: */
	private void drawChangedCell(Graphics2D g2, int cell_size, int iteration) {
		Paint paint = g2.getPaint();

		Rectangle2D r;
		String text = Integer.toString(iteration);
		g2.setFont(this.options.cell_drawing_font);
		r = g2.getFontMetrics().getStringBounds(text, g2);

		g2.setPaint(paint);
		int l = (int) Math.max(r.getWidth(), r.getHeight()) * 2;
		Path2D.Float path = new Path2D.Float();
		path.moveTo(0, 0);
		path.lineTo(0, l);
		path.lineTo(l, 0);
		path.lineTo(0, 0);
		g2.setColor(Color.BLACK);
		g2.fill(path);

		g2.setColor(Color.WHITE);
		g2.drawString(text, 0, (int) r.getHeight());
	}
}
