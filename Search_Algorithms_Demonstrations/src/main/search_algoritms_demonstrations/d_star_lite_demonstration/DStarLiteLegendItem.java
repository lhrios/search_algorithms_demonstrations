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

public enum DStarLiteLegendItem {
	/* Public: */
	//@formatter:off
	START("Start"), 
	GOAL("Goal"),
	ROBOT("Robot"), 
	BLOCKED("Blocked cell"), 
	FREE_CELL_1("Free cell (cost 1)"), 
	FREE_CELL_2("Free cell (cost 2)"), 
	FREE_CELL_3("Free cell (cost 3)"),
	ROBOT_PERCEPTION("Robot perception"), 
	PATH_CELL("Path cell"), 
	FRONTIER_CELL("Frontier cell"),
	ITERATION("Iteration number");
	//@formatter:on

	public String getLabe() {
		return this.label;
	}

	/* Private: */
	private String label;

	private DStarLiteLegendItem(String label) {
		this.label = label;
	}
}
