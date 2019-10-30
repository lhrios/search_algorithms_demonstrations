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

package search_algoritms_demonstrations.graphics;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Misc {
	/* Public: */
	public static final String INFINITY_SYMBOL = Character.toString((char) 0x221E);

	public static Dimension getStringPosition(Graphics2D g2, String string, int w, int h) {
		Dimension position = new Dimension();
		Rectangle2D r;

		r = g2.getFontMetrics().getStringBounds(string, g2);
		position.width = (w - (int) r.getWidth()) / 2;
		position.height = (h + (int) r.getHeight()) / 2;

		return position;
	}

	public static void drawBorderedRect(Graphics2D g2, int x, int y, int width, int height, int line_width) {
		if (width < 2 * line_width || height < 2 * line_width)
			throw new IllegalArgumentException();
		g2.fillRect(0, 0, width, line_width);
		g2.fillRect(0, 0, line_width, height);
		g2.fillRect(0, height - line_width, width, line_width);
		g2.fillRect(width - line_width, 0, line_width, height);
	}
}
