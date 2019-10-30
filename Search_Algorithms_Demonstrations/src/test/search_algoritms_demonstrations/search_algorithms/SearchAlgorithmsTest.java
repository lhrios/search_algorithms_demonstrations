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

package search_algoritms_demonstrations.search_algorithms;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import search_algoritms_demonstrations.maze.Maze;
import search_algoritms_demonstrations.maze.MazeCell;

public class SearchAlgorithmsTest {
	/* Public: */
	public SearchAlgorithmsTest() {
		Set<MazeCell> blocked_cells = new HashSet<MazeCell>(), unblocked_cells = new HashSet<MazeCell>();
		long seed = System.nanoTime();
		MazeCell maze_cell;
		Random random = new Random(seed);
		System.out.println("Seed: " + seed);

		int neighborhood = DIAGONALS ? Maze.N_DIRECTIONS_WITH_DIAGONALS : Maze.N_DIRECTIONS_WITHOUT_DIAGONALS;
		Heuristic heuristic = DIAGONALS ? DiagonalDistanceHeuristic.getInstance() : ManhattanDistanceHeuristic.getInstance();

		Maze maze = new Maze(seed, MAZE_W, MAZE_H, PROBABILITY_TO_BLOCK_A_CELL, MAZE_CELL_MAX_COST, FIX_START_AND_GOAL);
		DStarLite d_star_lite = new DStarLite(maze, true, false, heuristic, neighborhood);

		for (maze_cell = maze.getStart(); maze_cell != maze.getGoal();) {
			AStar a_star = new AStar(maze, true, false, tie_breaking_strategy, heuristic, neighborhood);
			a_star.solve();
			if (DEBUG)
				System.out.println("A*:\n" + maze);
			maze.cleanPath();
			d_star_lite.solve();
			if (DEBUG)
				System.out.println("D* Lite:\n" + maze);
			maze.cleanPath();
			if (DEBUG)
				System.out.println("Map:\n" + maze);

			if (!a_star.hasSolution() || !d_star_lite.hasSolution()) {
				System.out.println("No solution.");
				if (a_star.hasSolution() || d_star_lite.hasSolution()) {
					System.err.println("Fail: Some algorithms found the solution.");
					System.err.println("A*: " + a_star.hasSolution());
					System.err.println("D* Lite: " + d_star_lite.hasSolution());
					System.exit(1);
				}
				System.exit(0);
			} else {
				if (a_star.getPathCost() != d_star_lite.getPathCost()) {
					System.err.println("Fail: The solution isn't the same.");
					System.err.println("A*: " + a_star.getPathCost());
					System.err.println("D* Lite: " + d_star_lite.getPathCost());
					System.exit(1);
				} else {
					System.out.println("The solution has the following cost: " + a_star.getPathCost());
				}
			}

			for (int distance = 0; maze_cell != maze.getGoal(); distance++, maze_cell = maze_cell.getNextMazeCell()) {
				if (distance >= DISTANCE_BEFORE_CHANGE || !a_star.hasSolution()) {

					maze.cleanPath();
					maze.setStart(maze_cell);
					d_star_lite.informNewStart(maze_cell);
					blocked_cells.clear();
					unblocked_cells.clear();

					/* Block some cells. */
					for (int i = 0; i < N_CHANGED_CELLS; i++) {
						MazeCell blocked_maze_cell;
						int x, y;
						x = random.nextInt(maze.getW());
						y = random.nextInt(maze.getH());

						blocked_maze_cell = maze.getMazeCell(x, y);
						if (blocked_maze_cell != maze.getStart() && blocked_maze_cell != maze.getGoal() && !blocked_maze_cell.isBlocked()
								&& !blocked_cells.contains(blocked_maze_cell)) {
							blocked_maze_cell.block();
							blocked_cells.add(blocked_maze_cell);
						}
					}
					if (DEBUG) {
						System.out.println("Blocked cells:");
						for (MazeCell mazeCell : blocked_cells) {
							System.out.println(mazeCell);
						}
					}

					/* Unblock or change the cost of some cells. */
					for (int i = 0; i < N_CHANGED_CELLS; i++) {
						MazeCell unblocked_maze_cell;
						int x, y;
						x = random.nextInt(maze.getW());
						y = random.nextInt(maze.getH());
						unblocked_maze_cell = maze.getMazeCell(x, y);

						if (!blocked_cells.contains(unblocked_maze_cell) && !unblocked_cells.contains(unblocked_maze_cell) && unblocked_maze_cell != maze.getGoal()
								&& unblocked_maze_cell != maze_cell) {
							int new_cost = random.nextInt(MAZE_CELL_MAX_COST) + 1;

							if (unblocked_maze_cell.isBlocked() || unblocked_maze_cell.getCost() > new_cost) {
								unblocked_cells.add(unblocked_maze_cell);
								unblocked_maze_cell.setCost(new_cost);
							}
						}
					}
					if (DEBUG) {
						System.out.println("Unblocked cells:");
						for (MazeCell mazeCell : unblocked_cells) {
							System.out.println(mazeCell);
						}
					}

					if (unblocked_cells.size() > 0 || blocked_cells.size() > 0) {
						d_star_lite.informBlockedCells(blocked_cells);
						d_star_lite.informUnblockedCells(unblocked_cells);
					}
					break;
				}
			}
		}
	}

	/* Private: */
	private final static boolean FIX_START_AND_GOAL = false;
	private final static boolean DIAGONALS = true;
	private final static boolean DEBUG = false;
	private final static int DISTANCE_BEFORE_CHANGE = 4;
	private final static int MAZE_W = 20;
	private final static int MAZE_H = 20;
	private final static int N_CHANGED_CELLS = (int) Math.round((MAZE_W * MAZE_H) * 0.5);
	private final static int MAZE_CELL_MAX_COST = 10;
	private final static float PROBABILITY_TO_BLOCK_A_CELL = 0.33f;
	private final static TieBreakingStrategy tie_breaking_strategy = TieBreakingStrategy.NONE;
}
