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
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import search_algoritms_demonstrations.graphics.Misc;
import search_algoritms_demonstrations.maze.MazeCell;
import search_algoritms_demonstrations.maze.MazeDrawingOptions;
import search_algoritms_demonstrations.search_algorithms.DistanceCalculator;

public class DStarLiteRealWorldMazeCellDrawer extends DStarLiteAbstractMazeCellDrawer {

	public DStarLiteRealWorldMazeCellDrawer(MazeDrawingOptions options) {
		super(options);
		this.initializeTextures();
	}

	public void setRobotPerceptionRange(int robot_perception_range) {
		this.robot_perception_range = robot_perception_range;
	}

	@Override
	public int getLegendItemsCount() {
		return DStarLiteLegendItem.ROBOT_PERCEPTION.ordinal() + 1;
	}

	@Override
	public String getLegendItemsLabel(int item_number) {
		return DStarLiteLegendItem.values()[item_number].getLabe();
	}

	@Override
	public void drawLegendItem(int item_number, Graphics2D g2, int cell_size) {
		DStarLiteLegendItem legend_item = DStarLiteLegendItem.values()[item_number];

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

			case ROBOT:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				this.drawRobot(g2, this.options, cell_size);
			break;

			case ROBOT_PERCEPTION:
				g2.setColor(this.options.background_color);
				g2.fillRect(0, 0, cell_size, cell_size);
				g2.setColor(Color.BLACK);
				g2.setPaint(this.perception_range_texture_paint);
				Misc.drawBorderedRect(g2, 0, 0, cell_size, cell_size, 6);

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
				this.drawRobot(g2, this.options, cell_size);

			} else if (this.maze.getStart() == maze_cell) {
				this.drawStart(g2, this.options, cell_size);

			} else if (this.maze.getGoal() == maze_cell) {
				this.drawGoal(g2, this.options, cell_size);
			}
		}

		int distance = DistanceCalculator.diagonalDistance(maze_cell.getX(), maze_cell.getY(), this.robot_cell.getX(), this.robot_cell.getY());
		if (this.robot_cell != maze_cell && distance <= this.robot_perception_range) {
			g2.setColor(Color.BLACK);
			g2.setPaint(this.perception_range_texture_paint);
			Misc.drawBorderedRect(g2, 0, 0, cell_size, cell_size, 6);
		}
	}

	/* Private: */
	private int robot_perception_range;
	private TexturePaint perception_range_texture_paint;

	private void drawRobot(Graphics2D g2, MazeDrawingOptions options, int cell_size) {
		g2.setColor(options.rhombus_color);
		g2.fill(this.getRhombus(cell_size));
		g2.setColor(options.border_color);
		g2.setFont(options.start_goal_cell_drawing_font);
		Dimension d = Misc.getStringPosition(g2, "R", cell_size, cell_size);
		g2.drawString("R", d.width, d.height);
	}

	private void initializeTextures() {
		BufferedImage buffered_image = new BufferedImage(4, 4, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D buffered_image_g2 = buffered_image.createGraphics();

		buffered_image_g2.setColor(Color.BLACK);

		buffered_image_g2.fillRect(0, 0, 2, 2);
		buffered_image_g2.fillRect(2, 2, 2, 2);

		this.perception_range_texture_paint = new TexturePaint(buffered_image, new Rectangle(0, 0, 4, 4));
	}
}
