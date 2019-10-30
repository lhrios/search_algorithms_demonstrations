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

package search_algoritms_demonstrations.d_star_lite_demonstration;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import search_algoritms_demonstrations.graphics.Misc;
import search_algoritms_demonstrations.graphics.TitledBorderPanel;
import search_algoritms_demonstrations.gui.GUIConstants;
import search_algoritms_demonstrations.maze.Maze;
import search_algoritms_demonstrations.maze.MazeCell;
import search_algoritms_demonstrations.maze.MazeDrawer;
import search_algoritms_demonstrations.maze.MazeDrawerLegend;
import search_algoritms_demonstrations.maze.MazeDrawingOptions;
import search_algoritms_demonstrations.search_algorithms.DStarLite;
import search_algoritms_demonstrations.search_algorithms.DiagonalDistanceHeuristic;
import search_algoritms_demonstrations.search_algorithms.DistanceCalculator;
import search_algoritms_demonstrations.search_algorithms.Heuristic;
import search_algoritms_demonstrations.search_algorithms.ManhattanDistanceHeuristic;

public class DStarLiteDemonstrationContainer extends Panel implements ActionListener, AdjustmentListener, ItemListener, MouseListener {
	/* Public: */
	public DStarLiteDemonstrationContainer() {
		this.options = new MazeDrawingOptions();
		this.options.min_cell_size = 40;

		GridBagConstraints c = new GridBagConstraints();

		c.insets = GUIConstants.DEFAULT_INSETS;
		this.setLayout(new GridBagLayout());

		this.d_star_lite_label = new Label("D* Lite", Label.CENTER);
		this.d_star_lite_label.setFont(GUIConstants.ALGORITHM_TITLE_FONT);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.d_star_lite_label, c);

		this.mazes_panel = new Container();
		this.mazes_panel.setLayout(new GridBagLayout());
		this.real_world_maze_cell_drawer = new DStarLiteRealWorldMazeCellDrawer(this.options);
		{
			Label maze_title = new Label("Real map", Label.CENTER);
			maze_title.setFont(GUIConstants.MAZE_TITLE_FONT);

			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0;
			c.weighty = 0;
			this.mazes_panel.add(maze_title, c);

			this.real_world_maze_drawer = new MazeDrawer(this.real_world, this.options, this.real_world_maze_cell_drawer);
			this.real_world_maze_drawer.addMouseListener(this);

			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			c.gridheight = 4;
			c.weightx = 1;
			c.weighty = 1;
			this.mazes_panel.add(this.real_world_maze_drawer, c);
			c.weightx = 0;
			c.weighty = 0;
			c.anchor = GridBagConstraints.CENTER;
		}

		this.robot_vision_maze_cell_drawer = new DStarLiteRobotVisionMazeCellDrawer(this.options);
		{
			Label maze_title = new Label("D* Lite (Robot perception)", Label.CENTER);
			maze_title.setFont(GUIConstants.MAZE_TITLE_FONT);

			c.fill = GridBagConstraints.BOTH;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 0;
			c.weighty = 0;
			this.mazes_panel.add(maze_title, c);

			this.robot_vision_maze_drawer = new MazeDrawer(this.real_world, this.options, this.robot_vision_maze_cell_drawer);
			this.robot_vision_maze_drawer.addMouseListener(this);

			c.fill = GridBagConstraints.BOTH;
			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 1;
			c.gridheight = 4;
			c.weightx = 1;
			c.weighty = 1;
			this.mazes_panel.add(this.robot_vision_maze_drawer, c);
			c.weightx = 0;
			c.weighty = 0;
			c.anchor = GridBagConstraints.CENTER;
		}

		/* Create open list content text area and solution cost label. */
		{
			this.last_iteration_label = new Label();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 2;
			c.gridy = GridBagConstraints.RELATIVE;
			c.gridwidth = 1;
			c.gridheight = 1;
			this.add(this.last_iteration_label, c);

			this.solution_cost_label = new Label();
			this.add(this.solution_cost_label, c);

			c.fill = GridBagConstraints.NONE;

			this.open_list_label = new Label("Sorted open list content:");
			c.gridx = 2;
			c.gridy = GridBagConstraints.RELATIVE;
			c.gridwidth = 1;
			c.gridheight = 1;
			this.add(this.open_list_label, c);
			this.open_list_text_area = new TextArea("", 1, GUIConstants.TEXT_AREA_COLUMNS, GUIConstants.TEXT_AREA_SCROLLBARS);
			this.open_list_text_area.setFocusable(false);
			this.open_list_text_area.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.open_list_text_area.setFont(GUIConstants.TEXT_AREA_FONT);
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1;
			c.gridx = 2;
			c.gridy = GridBagConstraints.RELATIVE;
			c.gridwidth = 1;
			c.gridheight = 1;
			this.add(this.open_list_text_area, c);

			c.fill = GridBagConstraints.NONE;
			c.weighty = 0;
		}

		/* Create the legend. */
		{
			this.legend_panel = new TitledBorderPanel("Legend:");
			this.legend_panel.setLayout(new GridBagLayout());

			this.legend_panel.add(new MazeDrawerLegend(this.options, 4, this.real_world_maze_cell_drawer, this.robot_vision_maze_cell_drawer));
		}

		/* Create widgets related with the maze. */
		{
			this.maze_widgets_panel = new TitledBorderPanel("Maze:");
			this.maze_widgets_panel.setLayout(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOW_LAYOUT_H_GAP, GUIConstants.FLOW_LAYOUT_V_GAP));

			this.new_maze_button = new Button("New maze");
			this.new_maze_button.addActionListener(this);
			this.new_maze_button.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.maze_widgets_panel.add(this.new_maze_button);

			this.cell_type_choice = new Choice();
			this.cell_type_choice.add("Set goal");
			this.cell_type_choice.add("Set start");
			this.cell_type_choice.add("Set wall");
			this.cell_type_choice.add("Set free cell (cost 1)");
			this.cell_type_choice.add("Set free cell (cost 2)");
			this.cell_type_choice.add("Set free cell (cost 3)");
			this.cell_type_choice.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.maze_widgets_panel.add(this.cell_type_choice);

			this.diagonals_checkbox = new Checkbox("Allow diagonals");
			this.diagonals_checkbox.addItemListener(this);
			this.maze_widgets_panel.add(this.diagonals_checkbox);

			this.uniform_cost_checkbox = new Checkbox("Uniform costs");
			this.uniform_cost_checkbox.addItemListener(this);
			this.maze_widgets_panel.add(this.uniform_cost_checkbox);

			this.probability_to_block_a_cell_label = new Label("Probability to block a cell:", Label.RIGHT);
			this.maze_widgets_panel.add(this.probability_to_block_a_cell_label);

			this.probability_to_block_a_cell_scrollbar =
					new Scrollbar(Scrollbar.HORIZONTAL, GUIConstants.DEFAULT_PROBABILITY, GUIConstants.SCROLL_BAR_VALUE, GUIConstants.MIN_PROBABILITY,
							GUIConstants.MAX_PROBABILITY);
			this.probability_to_block_a_cell_scrollbar.addAdjustmentListener(this);
			this.probability_to_block_a_cell_scrollbar.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.probability_to_block_a_cell_scrollbar.setPreferredSize(new Dimension(GUIConstants.SCROLL_BAR_WIDTH, GUIConstants.SCROLL_BAR_HEIGHT));
			this.maze_widgets_panel.add(this.probability_to_block_a_cell_scrollbar);
		}

		/* Create widgets related with the algorithm. */
		{
			this.algorithm_widgets_panel = new TitledBorderPanel("Algorithm:");
			this.algorithm_widgets_panel.setLayout(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOW_LAYOUT_H_GAP, GUIConstants.FLOW_LAYOUT_V_GAP));

			this.restart = new Button("Restart");
			this.restart.addActionListener(this);
			this.restart.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.algorithm_widgets_panel.add(this.restart);

			this.next_step_button = new Button("Next step");
			this.next_step_button.addActionListener(this);
			this.next_step_button.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.algorithm_widgets_panel.add(this.next_step_button);

			this.step_by_step_button = new Button("Finish current execution");
			this.step_by_step_button.addActionListener(this);
			this.step_by_step_button.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.step_by_step_button.setEnabled(false);
			this.algorithm_widgets_panel.add(this.step_by_step_button);

			this.step_by_step_checkbox = new Checkbox("Step by step execution");
			this.step_by_step_checkbox.addItemListener(this);
			this.algorithm_widgets_panel.add(this.step_by_step_checkbox);
		}

		/* Create widgets related with the robot control. */
		{
			this.robot_panel = new TitledBorderPanel("Robot:");
			this.robot_panel.setLayout(new FlowLayout(FlowLayout.CENTER, GUIConstants.FLOW_LAYOUT_H_GAP, GUIConstants.FLOW_LAYOUT_V_GAP));

			this.perception_range_label = new Label("Robot perception range:", Label.RIGHT);
			this.robot_panel.add(this.perception_range_label);

			this.robot_perception_range_choice = new Choice();
			this.robot_perception_range_choice.add("1");
			this.robot_perception_range_choice.add("2");
			this.robot_perception_range_choice.add("3");
			this.robot_perception_range_choice.add("4");
			this.robot_perception_range_choice.add("5");
			this.robot_perception_range_choice.add(Misc.INFINITY_SYMBOL);
			this.robot_perception_range_choice.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.robot_perception_range_choice.addItemListener(this);
			this.robot_panel.add(this.robot_perception_range_choice);

			this.move_button = new Button("Move");
			this.move_button.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.move_button.addActionListener(this);
			this.robot_panel.add(this.move_button);
		}

		c.weightx = c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 5;
		this.add(this.mazes_panel, c);

		c.weightx = c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.maze_widgets_panel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = 1;
		c.gridheight = 2;
		this.add(this.legend_panel, c);

		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 1;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.robot_panel, c);

		c.gridx = 1;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.algorithm_widgets_panel, c);

		this.real_world_maze_cell_drawer.setRobotPerceptionRange(this.getRobotPerceptionRange());
		this.generateNewDStarLite(true);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (!e.getValueIsAdjusting() && !this.step_by_step_button.isEnabled()) {
			this.generateNewDStarLite(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.next_step_button) {
			this.d_star_lite.solve();
			this.afterSolve();

		} else if (e.getSource() == this.new_maze_button) {
			this.generateNewDStarLite(true);

		} else if (e.getSource() == this.step_by_step_button) {
			assert this.must_execute_step_by_step;
			this.d_star_lite.setStepByStep(false);
			this.d_star_lite.solve();
			this.afterSolve();
			this.d_star_lite.setStepByStep(true);

		} else if (e.getSource() == this.move_button) {
			Set<MazeCell> blocked_cells = new HashSet<MazeCell>();
			Set<MazeCell> unblocked_cells = new HashSet<MazeCell>();
			int perceptionRange = this.getRobotPerceptionRange();

			for (Iterator<MazeCell> i = this.blocked_cells.iterator(); i.hasNext();) {
				MazeCell mazeCell = i.next();
				if (DistanceCalculator.diagonalDistance(mazeCell.getX(), mazeCell.getY(), this.robot_cell.getX(), this.robot_cell.getY()) <= perceptionRange) {
					MazeCell robot_vision_maze_cell = this.robot_vision.getMazeCell(mazeCell.getX(), mazeCell.getY());
					robot_vision_maze_cell.copyConfiguration(mazeCell);
					blocked_cells.add(robot_vision_maze_cell);
					i.remove();
				}
			}

			for (Iterator<MazeCell> i = this.unblocked_cells.iterator(); i.hasNext();) {
				MazeCell mazeCell = i.next();
				if (DistanceCalculator.diagonalDistance(mazeCell.getX(), mazeCell.getY(), this.robot_cell.getX(), this.robot_cell.getY()) <= perceptionRange) {
					MazeCell robot_vision_maze_cell = this.robot_vision.getMazeCell(mazeCell.getX(), mazeCell.getY());
					robot_vision_maze_cell.copyConfiguration(mazeCell);
					unblocked_cells.add(robot_vision_maze_cell);
					i.remove();
				}
			}

			if (!unblocked_cells.isEmpty() || !blocked_cells.isEmpty()) {
				this.d_star_lite.informNewStart(this.robot_cell);
				this.d_star_lite.informBlockedCells(blocked_cells);
				this.d_star_lite.informUnblockedCells(unblocked_cells);

				this.robot_vision.cleanPath();
				this.d_star_lite.solve();

			} else if (this.robot_cell != this.robot_vision.getGoal() && this.d_star_lite.hasSolution() && this.d_star_lite.hasExecutionFinished()) {
				this.robot_cell.unsetPathFlag();
				this.robot_cell = this.robot_cell.getNextMazeCell();

				this.robot_vision_maze_cell_drawer.setRobotCell(this.robot_cell);
				this.real_world_maze_cell_drawer.setRobotCell(this.real_world.getMazeCell(this.robot_cell.getX(), this.robot_cell.getY()));

			}
			this.afterSolve();

		} else if (e.getSource() == this.restart) {
			this.generateNewDStarLite(false);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItemSelectable() == this.uniform_cost_checkbox) {
			this.generateNewDStarLite(true);

		} else if (e.getItemSelectable() == this.diagonals_checkbox) {
			this.generateNewDStarLite(false);

		} else if (e.getItemSelectable() == this.step_by_step_checkbox) {
			this.must_execute_step_by_step = !this.must_execute_step_by_step;
			this.d_star_lite.setStepByStep(this.must_execute_step_by_step);

		} else if (e.getItemSelectable() == this.robot_perception_range_choice) {
			this.real_world_maze_cell_drawer.setRobotPerceptionRange(this.getRobotPerceptionRange());
			this.drawMaze();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		/* If we are in the middle of a step by step execution. */
		if (this.step_by_step_button.isEnabled())
			return;

		switch (e.getButton()) {
			case MouseEvent.BUTTON1: {
				MazeCell maze_cell;
				if (e.getComponent() == this.real_world_maze_drawer) {
					maze_cell = this.real_world_maze_drawer.getMazeCellFromDrawing(e.getX(), e.getY());

				} else {
					assert e.getComponent() == this.robot_vision_maze_drawer;
					maze_cell = this.robot_vision_maze_drawer.getMazeCellFromDrawing(e.getX(), e.getY());
					if (maze_cell != null) {
						maze_cell = this.real_world.getMazeCell(maze_cell.getX(), maze_cell.getY());
					}
				}

				if (maze_cell == null)
					return;

				switch (this.cell_type_choice.getSelectedIndex()) {
					case 0:
						if (maze_cell != this.real_world.getStart()) {
							this.real_world.setGoal(maze_cell);
							this.robot_vision.setGoal(this.robot_vision.getMazeCell(maze_cell.getX(), maze_cell.getY()));
							this.generateNewDStarLite(false);
						}
					break;
					case 1:
						if (maze_cell != this.real_world.getGoal()) {
							this.real_world.setStart(maze_cell);
							this.robot_vision.setStart(this.robot_vision.getMazeCell(maze_cell.getX(), maze_cell.getY()));
							this.generateNewDStarLite(false);
						}
					break;
					case 2:
						if (maze_cell != this.real_world.getStart() && maze_cell != this.real_world.getGoal()
								&& this.real_world.getMazeCell(this.robot_cell.getX(), this.robot_cell.getY()) != maze_cell) {
							maze_cell.block();
							this.blocked_cells.add(maze_cell);
							this.unblocked_cells.remove(maze_cell);
						}
					break;
					case 3:
						maze_cell.setCost(1);
						this.blocked_cells.remove(maze_cell);
						this.unblocked_cells.add(maze_cell);
					break;
					case 4:
						maze_cell.setCost(this.getMaxCost() == 1 ? 1 : 2);
						this.blocked_cells.remove(maze_cell);
						this.unblocked_cells.add(maze_cell);
					break;
					case 5:
						maze_cell.setCost(this.getMaxCost() == 1 ? 1 : 3);
						this.blocked_cells.remove(maze_cell);
						this.unblocked_cells.add(maze_cell);
					break;
				}
				this.drawMaze();
			}
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/* Private: */
	private static final long serialVersionUID = 1L;
	private static final int MAZE_W = 10, MAZE_H = 9;

	private DStarLite d_star_lite;
	private DStarLiteRealWorldMazeCellDrawer real_world_maze_cell_drawer;
	private DStarLiteRobotVisionMazeCellDrawer robot_vision_maze_cell_drawer;
	private boolean must_execute_step_by_step;
	private Button next_step_button, new_maze_button, step_by_step_button, move_button, restart;
	private Container maze_widgets_panel, algorithm_widgets_panel, legend_panel, robot_panel, mazes_panel;
	private Checkbox diagonals_checkbox, uniform_cost_checkbox, step_by_step_checkbox;
	private Choice cell_type_choice, robot_perception_range_choice;
	private Label open_list_label, probability_to_block_a_cell_label, solution_cost_label, d_star_lite_label, last_iteration_label, perception_range_label;
	private MazeDrawer real_world_maze_drawer, robot_vision_maze_drawer;
	private MazeDrawingOptions options;
	private Maze real_world, robot_vision;
	private MazeCell robot_cell; /* Points to {@link robot_vision}. */
	private Scrollbar probability_to_block_a_cell_scrollbar;
	private TextArea open_list_text_area;
	private Set<MazeCell> blocked_cells = new HashSet<MazeCell>();
	private Set<MazeCell> unblocked_cells = new HashSet<MazeCell>();

	private void disableInterfaceForStepByStepExecution() {
		this.restart.setEnabled(false);
		this.move_button.setEnabled(false);
		this.new_maze_button.setEnabled(false);
		this.diagonals_checkbox.setEnabled(false);
		this.uniform_cost_checkbox.setEnabled(false);
		this.cell_type_choice.setEnabled(false);
		this.next_step_button.setEnabled(true);
		this.step_by_step_button.setEnabled(true);
		this.step_by_step_checkbox.setEnabled(false);
		this.robot_perception_range_choice.setEnabled(false);
	}

	private void enableInterfaceAfterStepByStepExecution() {
		this.restart.setEnabled(true);
		this.move_button.setEnabled(true);
		this.new_maze_button.setEnabled(true);
		this.diagonals_checkbox.setEnabled(true);
		this.uniform_cost_checkbox.setEnabled(true);
		this.cell_type_choice.setEnabled(true);
		this.next_step_button.setEnabled(false);
		this.step_by_step_button.setEnabled(false);
		this.step_by_step_checkbox.setEnabled(true);
		this.robot_perception_range_choice.setEnabled(true);
	}

	private int getMaxCost() {
		return (this.uniform_cost_checkbox.getState() ? 1 : 3);
	}

	private int getNeighborhood() {
		return (this.diagonals_checkbox.getState() ? Maze.N_DIRECTIONS_WITH_DIAGONALS : Maze.N_DIRECTIONS_WITHOUT_DIAGONALS);
	}

	private Heuristic getHeuristic() {
		return (this.diagonals_checkbox.getState() ? DiagonalDistanceHeuristic.getInstance() : ManhattanDistanceHeuristic.getInstance());
	}

	private float getProbabilityToBlockACell() {
		return (this.probability_to_block_a_cell_scrollbar.getValue()) / 100.f;
	}

	private void generateNewDStarLite(boolean create_new_maze) {
		if (create_new_maze) {
			this.real_world = new Maze(System.currentTimeMillis(), MAZE_W, MAZE_H, this.getProbabilityToBlockACell(), this.getMaxCost(), false);
			this.robot_vision = this.real_world.clone();

			this.real_world_maze_drawer.setMaze(this.real_world);
			this.robot_vision_maze_drawer.setMaze(this.robot_vision);

			this.real_world_maze_cell_drawer.setMaze(this.real_world);
			this.robot_vision_maze_cell_drawer.setMaze(this.robot_vision);

		} else {
			/* Make sure the changes are copied. */
			this.blocked_cells.clear();
			this.unblocked_cells.clear();

			this.robot_vision.copy(this.real_world);
			assert this.real_world.equals(this.robot_vision);

			this.robot_vision.cleanPath();
		}

		this.robot_cell = this.robot_vision.getStart();
		this.robot_vision_maze_cell_drawer.setRobotCell(this.robot_cell);
		this.real_world_maze_cell_drawer.setRobotCell(this.real_world.getMazeCell(this.robot_cell.getX(), this.robot_cell.getY()));

		this.d_star_lite = new DStarLite(this.robot_vision, true, this.must_execute_step_by_step, this.getHeuristic(), this.getNeighborhood());
		this.d_star_lite.solve();

		this.real_world_maze_cell_drawer.setDStarLite(this.d_star_lite);
		this.robot_vision_maze_cell_drawer.setDStarLite(this.d_star_lite);

		this.afterSolve();
	}

	private void drawMaze() {
		this.real_world_maze_drawer.repaint();
		this.robot_vision_maze_drawer.repaint();
	}

	private void updateOpenListText() {
		this.open_list_text_area.setText(this.d_star_lite.getOpenListText());

	}

	private void updateSolutionCostLabel() {
		if (this.d_star_lite.hasExecutionFinished()) {
			this.solution_cost_label.setText(String.format("Solution cost: %s", this.d_star_lite.hasSolution() ? Integer.toString(this.d_star_lite.getPathCost())
					: Misc.INFINITY_SYMBOL));
		} else {
			this.solution_cost_label.setText("");
		}
	}

	private void updateLastIterationLabel() {
		if (this.d_star_lite.hasExecutionFinished()) {
			this.last_iteration_label.setText(String.format("Last iteration: %d", this.d_star_lite.getLastIteration()));
		} else {
			this.last_iteration_label.setText("");
		}
	}

	private int getRobotPerceptionRange() {
		return this.robot_perception_range_choice.getSelectedItem().equals(Misc.INFINITY_SYMBOL) ? Integer.MAX_VALUE : Integer.valueOf(this.robot_perception_range_choice
				.getSelectedItem());
	}

	private void afterSolve() {
		if (this.d_star_lite.hasExecutionFinished()) {
			this.enableInterfaceAfterStepByStepExecution();

		} else if (this.must_execute_step_by_step) {
			this.disableInterfaceForStepByStepExecution();
		}

		this.drawMaze();
		this.updateOpenListText();
		this.updateSolutionCostLabel();
		this.updateLastIterationLabel();
	}
}
