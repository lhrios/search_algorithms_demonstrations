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

package search_algoritms_demonstrations.a_star_demonstration;

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

import search_algoritms_demonstrations.graphics.Misc;
import search_algoritms_demonstrations.graphics.TitledBorderPanel;
import search_algoritms_demonstrations.gui.GUIConstants;
import search_algoritms_demonstrations.maze.Maze;
import search_algoritms_demonstrations.maze.MazeCell;
import search_algoritms_demonstrations.maze.MazeDrawer;
import search_algoritms_demonstrations.maze.MazeDrawerLegend;
import search_algoritms_demonstrations.maze.MazeDrawingOptions;
import search_algoritms_demonstrations.search_algorithms.AStar;
import search_algoritms_demonstrations.search_algorithms.DiagonalDistanceHeuristic;
import search_algoritms_demonstrations.search_algorithms.Heuristic;
import search_algoritms_demonstrations.search_algorithms.ManhattanDistanceHeuristic;
import search_algoritms_demonstrations.search_algorithms.NullHeuristic;
import search_algoritms_demonstrations.search_algorithms.TieBreakingStrategy;

public class AStarDemonstrationContainer extends Panel implements ActionListener, AdjustmentListener, ItemListener, MouseListener {
	/* Public: */
	public AStarDemonstrationContainer() {
		GridBagConstraints c = new GridBagConstraints();

		c.insets = GUIConstants.DEFAULT_INSETS;
		this.setLayout(new GridBagLayout());

		this.a_star_label = new Label("A*", Label.CENTER);
		this.a_star_label.setFont(GUIConstants.ALGORITHM_TITLE_FONT);

		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.a_star_label, c);

		{
			this.options = new MazeDrawingOptions();

			this.maze_drawer = new MazeDrawer(this.maze, this.options, this.a_star_maze_cell_drawer);
			this.maze_drawer.addMouseListener(this);

			c.fill = GridBagConstraints.BOTH;
			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 1;
			c.gridheight = 3;
			c.weightx = 1;
			c.weighty = 1;
			this.add(this.maze_drawer, c);
			c.weightx = 0;
			c.weighty = 0;
		}

		/* Create open list content text area and solution cost label. */
		{
			this.solution_cost_label = new Label();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = GridBagConstraints.RELATIVE;
			c.gridwidth = 1;
			c.gridheight = 1;
			this.add(this.solution_cost_label, c);
			c.fill = GridBagConstraints.NONE;

			this.open_list_label = new Label("Sorted open list content:");
			c.gridx = 1;
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
			c.gridx = 1;
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

			this.legend_panel.add(new MazeDrawerLegend(this.options, Integer.MAX_VALUE, this.a_star_maze_cell_drawer));
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

			this.next_step_button = new Button("Next step");
			this.next_step_button.addActionListener(this);
			this.next_step_button.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.algorithm_widgets_panel.add(this.next_step_button);

			this.step_by_step_button = new Button("Start a step by step execution");
			this.step_by_step_button.addActionListener(this);
			this.step_by_step_button.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.algorithm_widgets_panel.add(this.step_by_step_button);

			this.tie_breaking_strategy_label = new Label("Tie breaking strategy:", Label.RIGHT);
			this.algorithm_widgets_panel.add(this.tie_breaking_strategy_label);

			this.tie_breaking_strategy_choice = new Choice();
			this.tie_breaking_strategy_choice.add("Do not break ties");
			this.tie_breaking_strategy_choice.add("In favor of highest g-values");
			this.tie_breaking_strategy_choice.add("In favor of smallest g-values");
			this.tie_breaking_strategy_choice.setBackground(GUIConstants.COMPONENT_BACKGROUND_COLOR);
			this.tie_breaking_strategy_choice.addItemListener(this);
			this.algorithm_widgets_panel.add(this.tie_breaking_strategy_choice);

			this.heuristic_checkbox = new Checkbox("Use a heuristic");
			this.heuristic_checkbox.setState(true);
			this.heuristic_checkbox.addItemListener(this);
			this.algorithm_widgets_panel.add(this.heuristic_checkbox);
		}

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.legend_panel, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.maze_widgets_panel, c);

		c.gridx = 0;
		c.gridy = GridBagConstraints.RELATIVE;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		this.add(this.algorithm_widgets_panel, c);

		this.generateNewAStar(true);
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (!e.getValueIsAdjusting())
			this.generateNewAStar(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.next_step_button) {
			this.a_star.solve();
			this.drawMaze();
			this.updateOpenListText();
			if (this.a_star.hasExecutionFinished()) {
				this.enableInterfaceAfterStepByStepExecution();
				this.must_execute_step_by_step = false;
			}
			this.updateSolutionCostLabel();

		} else if (e.getSource() == this.new_maze_button) {
			this.generateNewAStar(true);

		} else if (e.getSource() == this.step_by_step_button) {
			if (this.must_execute_step_by_step) {
				this.must_execute_step_by_step = false;
				this.enableInterfaceAfterStepByStepExecution();
				this.a_star.setStepByStep(false);
				this.a_star.solve();
				this.drawMaze();
				this.updateOpenListText();

			} else {
				this.must_execute_step_by_step = true;
				this.disableInterfaceForStepByStepExecution();
				this.generateNewAStar(false);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getItemSelectable() == this.uniform_cost_checkbox) {
			this.generateNewAStar(true);
		} else if (e.getItemSelectable() == this.heuristic_checkbox || e.getItemSelectable() == this.tie_breaking_strategy_choice
				|| e.getItemSelectable() == this.diagonals_checkbox) {
			this.generateNewAStar(false);
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
		if (this.must_execute_step_by_step)
			return;

		switch (e.getButton()) {
			case MouseEvent.BUTTON1: {
				MazeCell maze_cell;
				maze_cell = this.maze_drawer.getMazeCellFromDrawing(e.getX(), e.getY());
				if (maze_cell == null)
					return;

				switch (this.cell_type_choice.getSelectedIndex()) {
					case 0:
						if (maze_cell != this.maze.getStart())
							this.maze.setGoal(maze_cell);
					break;
					case 1:
						if (maze_cell != this.maze.getGoal())
							this.maze.setStart(maze_cell);
					break;
					case 2:
						if (maze_cell != this.maze.getStart() && maze_cell != this.maze.getGoal())
							maze_cell.block();
					break;
					case 3:
						maze_cell.setCost(1);
					break;
					case 4:
						maze_cell.setCost(this.getMaxCost() == 1 ? 1 : 2);
					break;
					case 5:
						maze_cell.setCost(this.getMaxCost() == 1 ? 1 : 3);
					break;
				}
				this.generateNewAStar(false);
			}
			break;
			case MouseEvent.BUTTON3:
			break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/* Private: */
	private static final long serialVersionUID = 1L;
	private static final int MAZE_W = 28, MAZE_H = 11;

	private AStar a_star;
	private AStarMazeCellDrawer a_star_maze_cell_drawer = new AStarMazeCellDrawer(new MazeDrawingOptions());
	private boolean must_execute_step_by_step = false;
	private Button next_step_button, new_maze_button, step_by_step_button;
	private Container maze_widgets_panel, algorithm_widgets_panel, legend_panel;
	private Checkbox heuristic_checkbox, diagonals_checkbox, uniform_cost_checkbox;
	private Choice cell_type_choice, tie_breaking_strategy_choice;
	private Label open_list_label, probability_to_block_a_cell_label, tie_breaking_strategy_label, solution_cost_label, a_star_label;
	private MazeDrawer maze_drawer;
	private Maze maze;
	private MazeDrawingOptions options;
	private Scrollbar probability_to_block_a_cell_scrollbar;
	private TextArea open_list_text_area;

	private void disableInterfaceForStepByStepExecution() {
		this.new_maze_button.setEnabled(false);
		this.heuristic_checkbox.setEnabled(false);
		this.diagonals_checkbox.setEnabled(false);
		this.uniform_cost_checkbox.setEnabled(false);
		this.cell_type_choice.setEnabled(false);
		this.tie_breaking_strategy_choice.setEnabled(false);
		this.next_step_button.setEnabled(true);
		this.probability_to_block_a_cell_scrollbar.setEnabled(false);
		this.step_by_step_button.setLabel("Finish current execution");
	}

	private void enableInterfaceAfterStepByStepExecution() {
		this.new_maze_button.setEnabled(true);
		this.heuristic_checkbox.setEnabled(true);
		this.diagonals_checkbox.setEnabled(true);
		this.uniform_cost_checkbox.setEnabled(true);
		this.cell_type_choice.setEnabled(true);
		this.tie_breaking_strategy_choice.setEnabled(true);
		this.next_step_button.setEnabled(false);
		this.probability_to_block_a_cell_scrollbar.setEnabled(true);
		this.step_by_step_button.setLabel("Start a step by step execution");
	}

	private boolean mustUseHeuristic() {
		return this.heuristic_checkbox.getState();
	}

	private Heuristic selectProperHeuristic() {
		if (this.mustUseHeuristic()) {
			if (this.diagonals_checkbox.getState())
				return DiagonalDistanceHeuristic.getInstance();
			else
				return ManhattanDistanceHeuristic.getInstance();
		} else {
			return NullHeuristic.getInstance();
		}
	}

	private int getMaxCost() {
		return (this.uniform_cost_checkbox.getState() ? 1 : 3);
	}

	private TieBreakingStrategy getTieBreakingStrategy() {
		switch (this.tie_breaking_strategy_choice.getSelectedIndex()) {
			default:
			case 0:
				return TieBreakingStrategy.NONE;
			case 1:
				return TieBreakingStrategy.HIGHEST_G_VALUES;
			case 2:
				return TieBreakingStrategy.SMALLEST_G_VALUES;
		}
	}

	private int getNeighborhood() {
		return (this.diagonals_checkbox.getState() ? Maze.N_DIRECTIONS_WITH_DIAGONALS : Maze.N_DIRECTIONS_WITHOUT_DIAGONALS);
	}

	private float getProbabilityToBlockACell() {
		return (this.probability_to_block_a_cell_scrollbar.getValue()) / 100.f;
	}

	private void generateNewAStar(boolean create_new_maze) {
		if (create_new_maze) {
			this.maze = new Maze(System.currentTimeMillis(), MAZE_W, MAZE_H, this.getProbabilityToBlockACell(), this.getMaxCost(), false);
			this.a_star_maze_cell_drawer.setMaze(this.maze);
			this.maze_drawer.setMaze(this.maze);
		} else {
			this.maze.cleanPath();
		}

		this.a_star = new AStar(this.maze, true, this.must_execute_step_by_step, this.getTieBreakingStrategy(), this.selectProperHeuristic(), this.getNeighborhood());
		this.a_star.solve();
		if (this.a_star.hasExecutionFinished()) {
			this.enableInterfaceAfterStepByStepExecution();
		}
		this.a_star_maze_cell_drawer.setAStar(this.a_star);

		this.updateSolutionCostLabel();
		this.drawMaze();
		this.updateOpenListText();
	}

	private void drawMaze() {
		this.maze_drawer.repaint();
	}

	private void updateOpenListText() {
		this.open_list_text_area.setText(this.a_star.getOpenListText());
	}

	private void updateSolutionCostLabel() {
		if (this.a_star.hasExecutionFinished()) {
			this.solution_cost_label.setText(String.format("Solution cost: %s", this.a_star.hasSolution() ? Integer.toString(this.a_star.getPathCost())
					: Misc.INFINITY_SYMBOL));
		} else {
			this.solution_cost_label.setText("");
		}
	}
}
