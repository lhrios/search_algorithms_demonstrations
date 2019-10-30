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
package search_algoritms_demonstrations.maze;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import search_algoritms_demonstrations.graphics.ImageExhibitor;
import search_algoritms_demonstrations.gui.GUIConstants;

public class MazeDrawerLegend extends Container {
	/* Public: */
	public MazeDrawerLegend(MazeDrawingOptions options, int items_per_line, MazeCellDrawer... drawers) {
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = GUIConstants.DEFAULT_INSETS;

		for (MazeCellDrawer drawer : drawers) {
			for (int i = 0; i < drawer.getLegendItemsCount(); i++) {
				BufferedImage image = new BufferedImage(options.min_cell_size, options.min_cell_size, BufferedImage.TYPE_INT_RGB);
				Graphics2D g2 = (Graphics2D) image.getGraphics();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				drawer.drawLegendItem(i, g2, options.min_cell_size);
				g2.dispose();

				Panel panel = new Panel();
				panel.setLayout(new GridBagLayout());

				GridBagConstraints panel_c = new GridBagConstraints();
				panel.add(new Label(drawer.getLegendItemsLabel(i) + ":", Label.RIGHT), panel_c);
				panel.add(new ImageExhibitor(image), panel_c);

				this.add(panel, c);
				if (c.gridx + 1 < items_per_line) {
					c.gridx++;

				} else {
					c.gridx = 0;
					c.gridy++;
				}
			}
		}

	}

	/* Private: */
	private static final long serialVersionUID = 5455533150324780778L;
}
