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


public class MazeCell {
	/* Public: */

	public MazeCell(int x, int y, int cost) {
		if (x < 0 || y < 0 || cost < 1 || cost >= BLOCKED)
			throw new IllegalArgumentException();
		this.x = x;
		this.y = y;
		this.cost = (byte) cost;
		this.next_maze_cell = null;
	}

	public void block() {
		this.setCost(BLOCKED);
	}

	public void clearPathFlag() {
		this.cost &= 0x7F;
	}

	@Override
	public MazeCell clone() {
		MazeCell maze_cell = new MazeCell();

		maze_cell.x = this.x;
		maze_cell.y = this.y;
		maze_cell.cost = this.cost;
		maze_cell.next_maze_cell = this.next_maze_cell;

		return maze_cell;
	}

	public int getCost() {
		return (this.cost & 0x7F);
	}

	public MazeCell getNextMazeCell() {
		return this.next_maze_cell;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean isBlocked() {
		return this.getCost() == BLOCKED;
	}

	public boolean isPathFlagOn() {
		return (this.cost & 0x80) != 0;
	}

	public void setCost(int cost) {
		if (cost > 0x7F)
			throw new IllegalArgumentException();
		this.cost = (byte) cost;
	}

	public void setNextMazeCell(MazeCell maze_cell) {
		this.next_maze_cell = maze_cell;
	}

	public void unsetPathFlag() {
		this.cost &= ~0x80;
	}

	public void setPathFlag() {
		this.cost |= 0x80;
	}

	public void copyConfiguration(MazeCell mazeCell) {
		this.cost = mazeCell.cost;
	}

	@Override
	public String toString() {
		int aux = this.x + 1;
		return new String((aux < 10 ? " " : "") + Integer.toString(aux) + Character.toString((char) (this.y + 'A')));
	}

	public boolean equalsCoordinatesAndCost(MazeCell maze_cell) {
		return maze_cell.x == this.x && maze_cell.y == this.y && maze_cell.getCost() == this.getCost();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MazeCell))
			return false;

		MazeCell maze_cell = (MazeCell) obj;
		return maze_cell.x == this.x && maze_cell.y == this.y;
	}

	/* Protected: */
	protected int x, y;

	/* Private: */
	private static final byte BLOCKED = 0x7F;

	private byte cost;
	private MazeCell next_maze_cell;

	private MazeCell() {
	}
}
