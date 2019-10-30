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

package search_algoritms_demonstrations.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class ImageExhibitor extends Canvas {
	/* Public: */
	public ImageExhibitor(Image image) {
		this.image = image;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.image.getWidth(null), this.image.getHeight(null));
	}

	@Override
	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		int o_x = ((int) this.getBounds().getWidth() - this.image.getWidth(null)) / 2;
		int o_y = ((int) this.getBounds().getHeight() - this.image.getHeight(null)) / 2;

		g.drawImage(this.image, o_x, o_y, null);
	}

	@Override
	public void update(Graphics g) {
		this.paint(g);
	}

	/* Private: */
	private static final long serialVersionUID = 6442726041214308936L;

	private Image image;
}
