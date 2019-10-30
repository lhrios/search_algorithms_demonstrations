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

import java.util.Random;

public class Maze implements Cloneable {
	/* Public: */
	public static final int N_DIRECTIONS_WITHOUT_DIAGONALS = 4;
	public static final int N_DIRECTIONS_WITH_DIAGONALS = 8;
	public static final int delta_x[] = {0, 1, -1, 0, 1, 1, -1, -1};
	public static final int delta_y[] = {1, 0, 0, -1, -1, 1, 1, -1};

	public Maze(long seed, int w, int h, float probability_to_block_a_cell, int max_cost, boolean fix_start_and_goal) {
		Random random = new Random(seed);

		if (w < 2 || h < 2 || probability_to_block_a_cell > 1 || probability_to_block_a_cell < 0 || max_cost > 0x7F || max_cost < 1)
			throw new IllegalArgumentException();

		this.w = w;
		this.h = h;
		this.cells = new MazeCell[h][w];

		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				boolean blocked = (random.nextFloat() < probability_to_block_a_cell);
				int cost = random.nextInt(max_cost) + 1;

				this.cells[y][x] = new MazeCell(x, y, cost);
				if (blocked)
					this.cells[y][x].block();
			}
		}

		/* Choose the start cell. */
		{
			int x, y;
			if (fix_start_and_goal) {
				x = this.w - 1;
				y = this.h - 1;
			} else {
				x = random.nextInt(w);
				y = random.nextInt(h);
			}
			this.setStart(x, y);
		}

		/* Choose the goal cell. */
		do {
			int x, y;
			if (fix_start_and_goal) {
				x = 0;
				y = 0;
			} else {
				x = random.nextInt(w);
				y = random.nextInt(h);
			}
			this.setGoal(x, y);
		} while (this.start == this.goal);
	}

	@Override
	public Maze clone() {
		Maze maze = null;
		try {
			maze = (Maze) super.clone();
		} catch (CloneNotSupportedException e) {
		}

		maze.w = this.w;
		maze.h = this.h;

		maze.cells = new MazeCell[this.h][this.w];
		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				maze.cells[y][x] = this.cells[y][x].clone();
			}
		}

		maze.start = maze.cells[this.start.y][this.start.x];
		maze.goal = maze.cells[this.goal.y][this.goal.x];

		return maze;
	}

	public void cleanPath() {
		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				this.cells[y][x].clearPathFlag();
			}
		}
	}

	public MazeCell getGoal() {
		return this.goal;
	}

	public MazeCell getStart() {
		return this.start;
	}

	public MazeCell getMazeCell(int x, int y) {
		return this.cells[y][x];
	}

	public int getH() {
		return this.h;
	}

	public int getW() {
		return this.w;
	}

	public void copy(Maze maze) {
		this.start = this.cells[maze.getStart().getY()][maze.getStart().getX()];
		this.goal = this.cells[maze.getGoal().getY()][maze.getGoal().getX()];

		if (maze.w != this.w || maze.h != this.h) {
			throw new IllegalArgumentException();
		}

		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				this.cells[y][x].copyConfiguration(maze.getMazeCell(x, y));
			}
		}
	}

	public void setGoal(MazeCell maze_cell) {
		this.goal = maze_cell;
		if (maze_cell.isBlocked())
			maze_cell.setCost(1);
	}

	public void setStart(MazeCell maze_cell) {
		this.start = maze_cell;
		if (maze_cell.isBlocked())
			maze_cell.setCost(1);
	}

	public void setGoal(int x, int y) {
		this.setGoal(this.cells[y][x]);
	}

	public void setStart(int x, int y) {
		this.setStart(this.cells[y][x]);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("W: " + this.w + " H: " + this.h + "\n");

		s.append("   ");
		for (int x = 0; x < this.w; x++) {
			s.append(String.format("%2d ", x + 1));
		}
		s.append('\n');

		for (int y = 0; y < this.h; y++) {
			s.append(" ").append((char) ('A' + y)).append(" ");
			for (int x = 0; x < this.w; x++) {
				if (this.cells[y][x] == this.goal) {
					s.append(String.format("G%d ", this.cells[y][x].getCost()));
				} else if (this.cells[y][x] == this.start) {
					s.append(String.format("S%d ", this.cells[y][x].getCost()));
				} else if (this.cells[y][x].isBlocked()) {
					s.append(" X ");
				} else if (this.cells[y][x].isPathFlagOn()) {
					s.append(" . ");
				} else {
					s.append(String.format(" %d ", this.cells[y][x].getCost()));
				}
			}
			s.append("\n");
		}
		return s.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Maze))
			return false;

		Maze maze = (Maze) obj;
		if (maze.h != this.h || maze.w != this.w || !maze.start.equalsCoordinatesAndCost(this.start) || !maze.goal.equalsCoordinatesAndCost(this.goal))
			return false;

		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				if (!maze.cells[y][x].equalsCoordinatesAndCost(this.cells[y][x])) {
					return false;
				}
			}
		}
		return true;
	}

	/* Private: */
	private int w, h;
	private MazeCell cells[][];
	private MazeCell start, goal;
}
