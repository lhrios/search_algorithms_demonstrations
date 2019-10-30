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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

public class MazeDrawingOptions {
	/* Public: */
	public boolean draw_coordinates;
	public int min_cell_size, cell_border, internal_border;
	public Color background_color, border_color, blocked_cell_color, open_list_cell_color, path_cell_color, free_cell_1_color, free_cell_2_color, free_cell_3_color,
			rhombus_color;
	public Font cell_drawing_font, start_goal_cell_drawing_font;
	public TexturePaint visited_cell_texture_paint;

	public MazeDrawingOptions() {
		this.initialize();
	}

	/* Private: */
	private void initialize() {
		this.draw_coordinates = true;
		this.min_cell_size = 32;
		this.cell_border = 1;
		this.internal_border = 4;

		this.background_color = Color.WHITE;
		this.border_color = Color.BLACK;
		this.blocked_cell_color = new Color(0xCC, 0, 0);
		this.open_list_cell_color = new Color(0, 0x66, 0);
		this.path_cell_color = new Color(0, 0, 0xCC);
		this.free_cell_1_color = new Color(0xFF, 0xFF, 0xFF);
		this.free_cell_2_color = new Color(0xCC, 0xCC, 0xCC);
		this.free_cell_3_color = new Color(0x99, 0x99, 0x99);
		this.rhombus_color = new Color(0xFF, 0xCC, 0);
		this.cell_drawing_font = new Font("", Font.PLAIN, 8);
		this.start_goal_cell_drawing_font = new Font("", Font.BOLD, 10);

		this.initializeTextures();
	}

	private void initializeTextures() {
		BufferedImage buffered_image = new BufferedImage(2, 2, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D buffered_image_g2 = buffered_image.createGraphics();

		buffered_image_g2.setColor(Color.BLACK);
		buffered_image_g2.fillRect(0, 0, 1, 1);
		buffered_image_g2.fillRect(1, 1, 2, 2);
		this.visited_cell_texture_paint = new TexturePaint(buffered_image, new Rectangle(0, 0, 2, 2));
	}
}
