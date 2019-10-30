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

import java.util.Iterator;
import java.util.Set;

import search_algoritms_demonstrations.binary_heap.BinaryHeap;
import search_algoritms_demonstrations.binary_heap.BinaryHeapElement;
import search_algoritms_demonstrations.maze.Maze;
import search_algoritms_demonstrations.maze.MazeCell;

public class DStarLite {
	/* Public: */
	public DStarLite(Maze maze, boolean mark_path, boolean step_by_step, Heuristic heuristic, int neighborhood) {
		this.heuristic = heuristic;

		this.w = maze.getW();
		this.h = maze.getH();

		/* Initialize the graph. */
		this.graph = new DStarLiteNode[this.h][this.w];
		for (int y = 0; y < this.h; y++) {
			for (int x = 0; x < this.w; x++) {
				this.graph[y][x] = new DStarLiteNode(maze.getMazeCell(x, y));
			}
		}
		this.open_list = new BinaryHeap(this.w * this.h);

		this.has_solution = false;
		this.step_by_step = step_by_step;
		this.neighborhood = neighborhood;
		this.mark_path = mark_path;

		this.last_agent_position = this.agent_position = this.graph[maze.getStart().getY()][maze.getStart().getX()];
		this.goal = this.graph[maze.getGoal().getY()][maze.getGoal().getX()];
		this.goal.rhs = 0;
		this.calculateKey(this.goal);
		this.open_list.insert(this.goal);
	}

	public int getPathCost() {
		return this.path_cost;
	}

	public void setStepByStep(boolean step_by_step) {
		this.step_by_step = step_by_step;
	}

	public boolean hasSolution() {
		return this.has_solution;
	}

	public void informNewStart(MazeCell new_start) {
		DStarLiteNode new_agent_position = this.graph[new_start.getY()][new_start.getX()];

		this.last_agent_position = this.agent_position;
		this.agent_position = new_agent_position;
	}

	public void informUnblockedCells(Set<MazeCell> unblocked_cells) {
		this.unblocked_cells = unblocked_cells;
	}

	public void informBlockedCells(Set<MazeCell> blocked_cells) {
		this.blocked_cells = blocked_cells;
	}

	public void solve() {
		if (this.iteration == 0 || !this.unfinished_iteration) {
			this.unfinished_iteration = true;
			this.execution_finished = false;
			this.has_solution = false;

			this.km += this.heuristic.distanceToGoal(this.agent_position.getMazeCell(), this.last_agent_position.getMazeCell());
			this.iteration++;
		}

		/* If some cell cost changed. */
		if (this.blocked_cells != null) {
			for (Iterator<MazeCell> i = this.blocked_cells.iterator(); i.hasNext();) {
				MazeCell mazeCell = i.next();
				DStarLiteNode node = this.graph[mazeCell.getY()][mazeCell.getX()];

				node.g = node.rhs = Integer.MAX_VALUE;
				node.last_change_iteration = this.iteration;

				if (this.open_list.has(node)) {
					this.open_list.delete(node);
				}

				for (int j = 0; j < this.neighborhood; j++) {
					int x, y;
					x = node.getMazeCell().getX() + Maze.delta_x[j];
					y = node.getMazeCell().getY() + Maze.delta_y[j];
					if (0 <= x && x < this.w && 0 <= y && y < this.h) {
						DStarLiteNode neighbour = this.graph[y][x];
						if (neighbour.getMazeCell().isBlocked())
							continue;
						this.updateNode(neighbour);

					}

				}
				if (this.step_by_step) {
					i.remove();
					this.execution_finished = false;
					return;
				}
			}
			this.blocked_cells = null;
		}

		if (this.unblocked_cells != null) {
			for (Iterator<MazeCell> i = this.unblocked_cells.iterator(); i.hasNext();) {
				MazeCell mazeCell = i.next();
				DStarLiteNode node = this.graph[mazeCell.getY()][mazeCell.getX()];
				assert (!node.getMazeCell().isBlocked());

				/* There is no need to initialize node. It has already been done on constructor and/or on "blocked_cells" iteration loop. */
				this.updateNode(node);

				for (int j = 0; j < this.neighborhood; j++) {
					int x, y;
					x = node.getMazeCell().getX() + Maze.delta_x[j];
					y = node.getMazeCell().getY() + Maze.delta_y[j];
					if (0 <= x && x < this.w && 0 <= y && y < this.h) {
						DStarLiteNode neighbour = this.graph[y][x];
						if (neighbour.getMazeCell().isBlocked())
							continue;
						this.updateNode(neighbour);

					}
				}
				if (this.step_by_step) {
					i.remove();
					this.execution_finished = false;
					return;
				}
			}
			this.unblocked_cells = null;
		}

		this.execution_finished = this.computeShortesPath();
		if (this.execution_finished) {
			this.unfinished_iteration = false;
			if (this.agent_position.g != Integer.MAX_VALUE) {
				this.has_solution = true;
				this.path_cost = this.agent_position.rhs;
				if (this.mark_path)
					this.markPath();
			}
		}
	}

	public int getMazeCellLastChangeIteration(MazeCell maze_cell) {
		return this.graph[maze_cell.getY()][maze_cell.getX()].last_change_iteration;
	}

	public int getMazeCellG(MazeCell maze_cell) {
		return this.graph[maze_cell.getY()][maze_cell.getX()].g;
	}

	public int getMazeCellRHS(MazeCell maze_cell) {
		return this.graph[maze_cell.getY()][maze_cell.getX()].rhs;
	}

	public int getMazeCellH(MazeCell maze_cell) {
		return this.heuristic.distanceToGoal(maze_cell, this.agent_position.getMazeCell());
	}

	public String getOpenListText() {
		StringBuilder stringBuilder = new StringBuilder();

		for (BinaryHeapElement e : this.open_list.asSortedList()) {
			stringBuilder.append(e.toString()).append('\n');
		}

		return stringBuilder.toString();
	}

	public boolean isInOpenList(MazeCell maze_cell) {
		return this.open_list.has(this.graph[maze_cell.getY()][maze_cell.getX()]);
	}

	public int getLastIteration() {
		return this.iteration;
	}

	public boolean hasExecutionFinished() {
		return this.execution_finished;
	}

	/* Private: */
	private static class DStarLiteNode extends BinaryHeapElement {
		/* Public: */
		public DStarLiteNode parent;
		public int g, rhs, k1, k2, last_change_iteration;

		public DStarLiteNode(MazeCell maze_cell) {
			this.g = this.rhs = Integer.MAX_VALUE;
			this.maze_cell = maze_cell;
		}

		@Override
		public boolean lessThanForHeap(BinaryHeapElement e) {
			DStarLiteNode dStarLiteNode = (DStarLiteNode) e;
			if (this.k1 == dStarLiteNode.k1)
				return this.k2 < dStarLiteNode.k2;
			return this.k1 < dStarLiteNode.k1;
		}

		public MazeCell getMazeCell() {
			return this.maze_cell;
		}

		void CalculateKey(int km, int h) {
			this.k2 = Math.min(this.g, this.rhs);
			this.k1 = this.k2 == Integer.MAX_VALUE ? Integer.MAX_VALUE : this.k2 + h + km;
		}

		@Override
		public String toString() {
			return String.format("%s [%2d %2d]", this.maze_cell.toString(), this.k1, this.k2);
		}

		@Override
		public Object clone() throws CloneNotSupportedException {
			DStarLiteNode clone = new DStarLiteNode(this.maze_cell);

			clone.parent = this.parent;
			clone.g = this.g;
			clone.rhs = this.rhs;
			clone.k1 = this.k1;
			clone.k2 = this.k2;

			return clone;
		}

		/* Private: */
		private MazeCell maze_cell;
	}

	private boolean step_by_step, mark_path, has_solution, unfinished_iteration, execution_finished;
	private BinaryHeap open_list;
	private DStarLiteNode graph[][], goal, agent_position, last_agent_position;
	private int w, h, path_cost, km, neighborhood, iteration;
	private Heuristic heuristic;
	private Set<MazeCell> unblocked_cells, blocked_cells;

	private void calculateKey(DStarLiteNode node) {
		int h = this.heuristic.distanceToGoal(node.getMazeCell(), this.agent_position.getMazeCell());
		node.CalculateKey(this.km, h);
	}

	private int getNodeCost(DStarLiteNode node) {
		assert !node.getMazeCell().isBlocked();
		return node.getMazeCell().getCost();
	}

	private boolean computeShortesPath() {
		try {
			for (int c = 0;; c++) {
				DStarLiteNode node, aux;

				/* Check the condition to continue. */
				if (this.open_list.size() == 0)
					break;
				aux = (DStarLiteNode) this.agent_position.clone();
				this.calculateKey(aux);
				node = (DStarLiteNode) this.open_list.peek();
				if (!(node.lessThanForHeap(aux) || this.agent_position.rhs != this.agent_position.g))
					break;

				if (this.step_by_step && c > 0) {
					return false;
				}

				node = (DStarLiteNode) this.open_list.pop();
				aux = (DStarLiteNode) node.clone();
				this.calculateKey(node);
				if (aux.lessThanForHeap(node)) {
					this.open_list.insert(node);

				} else if (node.g > node.rhs) {
					node.g = node.rhs;
					node.last_change_iteration = this.iteration;

					for (int i = 0; i < this.neighborhood; i++) {
						int x, y;
						x = node.getMazeCell().getX() + Maze.delta_x[i];
						y = node.getMazeCell().getY() + Maze.delta_y[i];
						if (0 <= x && x < this.w && 0 <= y && y < this.h) {
							DStarLiteNode neighbour = this.graph[y][x];
							if (neighbour.getMazeCell().isBlocked())
								continue;
							this.updateNode(neighbour);

						}
					}

				} else {
					node.g = Integer.MAX_VALUE;
					node.last_change_iteration = this.iteration;

					for (int i = 0; i < this.neighborhood; i++) {
						int x, y;
						x = node.getMazeCell().getX() + Maze.delta_x[i];
						y = node.getMazeCell().getY() + Maze.delta_y[i];
						if (0 <= x && x < this.w && 0 <= y && y < this.h) {
							DStarLiteNode neighbour = this.graph[y][x];
							if (neighbour.getMazeCell().isBlocked())
								continue;
							this.updateNode(neighbour);

						}
					}

					this.updateNode(node);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	private void markPath() {
		DStarLiteNode node, node_parent;

		node = this.agent_position;
		node_parent = node.parent;

		while (true) {
			node.getMazeCell().setNextMazeCell(node_parent.getMazeCell());
			node.getMazeCell().setPathFlag();
			node = node_parent;
			node_parent = node.parent;
			if (node_parent == null) {
				node.getMazeCell().setNextMazeCell(null);
				break;
			}
			node.getMazeCell().setNextMazeCell(node_parent.getMazeCell());
		}
	}

	private void updateNode(DStarLiteNode node) {
		if (node != this.goal) {

			node.rhs = Integer.MAX_VALUE;
			node.parent = null;

			for (int i = 0; i < this.neighborhood; i++) {
				int x, y;

				x = node.getMazeCell().getX() + Maze.delta_x[i];
				y = node.getMazeCell().getY() + Maze.delta_y[i];
				int cost = this.getNodeCost(node);

				if (0 <= x && x < this.w && 0 <= y && y < this.h) {
					DStarLiteNode neighbour = this.graph[y][x];

					/* The start cell cannot generate children. */
					if (neighbour.getMazeCell().isBlocked())
						continue;

					int aux = neighbour.g == Integer.MAX_VALUE ? Integer.MAX_VALUE : neighbour.g + cost;
					if (node.rhs > aux) {
						node.rhs = aux;
						node.parent = neighbour;
						node.last_change_iteration = this.iteration;
					}
				}
			}
		}
		if (this.open_list.has(node))
			this.open_list.delete(node);

		if (node.g != node.rhs) {
			this.calculateKey(node);
			this.open_list.insert(node);
		}
	}
}
