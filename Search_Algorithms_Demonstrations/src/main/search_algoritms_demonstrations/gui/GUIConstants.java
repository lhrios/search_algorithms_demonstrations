package search_algoritms_demonstrations.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.TextArea;

public class GUIConstants {
	/* Public: */
	public static final Insets DEFAULT_INSETS = new Insets(1, 1, 1, 1);

	public static final int SCROLL_BAR_WIDTH = 250;
	public static final int SCROLL_BAR_HEIGHT = 15;

	public static final int DEFAULT_PROBABILITY = 25;
	public static final int MIN_PROBABILITY = 0;
	public static final int MAX_PROBABILITY = 100;
	public static final int SCROLL_BAR_VALUE = 1;

	public static final Font TEXT_AREA_FONT = new Font("monospaced", Font.PLAIN, 12);
	public static final int TEXT_AREA_COLUMNS = 20;
	public static final int TEXT_AREA_SCROLLBARS = TextArea.SCROLLBARS_VERTICAL_ONLY;

	public static final int FLOW_LAYOUT_H_GAP = 8;
	public static final int FLOW_LAYOUT_V_GAP = 2;

	public static final Color COMPONENT_BACKGROUND_COLOR = Color.LIGHT_GRAY;

	public static final Font ALGORITHM_TITLE_FONT = new Font("", Font.BOLD | Font.ITALIC, 40);
	public static final Font MAZE_TITLE_FONT = new Font("", Font.BOLD | Font.ITALIC, 20);
}
