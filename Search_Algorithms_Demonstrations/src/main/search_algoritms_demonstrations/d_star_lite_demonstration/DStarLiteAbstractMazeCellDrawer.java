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

package search_algoritms_demonstrations.d_star_lite_demonstration;

import java.awt.Graphics2D;

import search_algoritms_demonstrations.maze.BaseMazeCellDrawer;
import search_algoritms_demonstrations.maze.MazeCell;
import search_algoritms_demonstrations.maze.MazeDrawingOptions;
import search_algoritms_demonstrations.search_algorithms.DStarLite;

public abstract class DStarLiteAbstractMazeCellDrawer extends BaseMazeCellDrawer {
	/* Public: */
	public void setRobotCell(MazeCell robot_cell) {
		this.robot_cell = robot_cell;
	}

	public DStarLiteAbstractMazeCellDrawer(MazeDrawingOptions options) {
		super(options);
	}

	@Override
	public int getLegendItemsCount() {
		return 0;
	}

	@Override
	public String getLegendItemsLabel(int item_number) {
		return null;
	}

	@Override
	public void drawLegendItem(int item_number, Graphics2D g2, int cell_size) {
	}

	public void setDStarLite(DStarLite d_star_lite) {
		this.d_star_lite = d_star_lite;
	}

	/* Protected: */
	protected DStarLite d_star_lite;
	protected MazeCell robot_cell;
}
