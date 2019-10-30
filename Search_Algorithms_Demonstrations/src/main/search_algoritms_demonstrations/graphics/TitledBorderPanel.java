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

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Panel;

/*
 * Base on the following solution: http://www.coderanch.com/t/336341/GUI/java/Panel-border
 */
public class TitledBorderPanel extends Panel {
	/* Public: */
	public TitledBorderPanel(String title) {
		this.title = title;
	}

	@Override
	public Insets getInsets() {
		return this.insets;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(this.getForeground());
		g.drawRect(5, 5, this.getWidth() - 10, this.getHeight() - 10);
		int width = g.getFontMetrics().stringWidth(this.title);
		g.setColor(this.getBackground());
		g.fillRect(10, 0, width, 10);
		g.setColor(this.getForeground());
		g.drawString(this.title, 10, 10);
	}

	/* Private: */
	private static final long serialVersionUID = 3337530458652389801L;

	private String title;
	private Insets insets = new Insets(10, 10, 10, 10);
}
