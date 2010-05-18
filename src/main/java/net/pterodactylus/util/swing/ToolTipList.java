
package net.pterodactylus.util.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import net.pterodactylus.util.logging.Logging;

/**
 * Extension of {@link JList} that retrieves the tool tip text to display from
 * the cell renderer. This can be used to let a custom {@link ListCellRenderer}
 * return e.g. a {@link JPanel} with multiple components that can return
 * different tool tips.
 *
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public class ToolTipList extends JList {

	/** The logger. */
	private static final Logger logger = Logging.getLogger(ToolTipList.class);

	/** The underlying list model. */
	private final ListModel listModel;

	/**
	 * Creates a new tool tip list.
	 *
	 * @param listModel
	 *            The underlying list model
	 */
	public ToolTipList(ListModel listModel) {
		super(listModel);
		this.listModel = listModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getToolTipText(MouseEvent event) {
		logger.log(Level.FINEST, "Getting Tooltip for " + event + "…");
		int row = locationToIndex(event.getPoint());
		logger.log(Level.FINEST, "Mouse is over row " + row + ".");
		if (row < 0) {
			return null;
		}
		Object value = listModel.getElementAt(row);
		logger.log(Level.FINEST, "Getting Tooltip for “" + value + "”…");
		if (value == null) {
			return null;
		}
		Component cell = getCellRenderer().getListCellRendererComponent(this, value, row, false, false);
		logger.log(Level.FINEST, "CellRenderer gave us Cell " + cell + ".");
		if (cell == null) {
			return null;
		}
		int cellX = cell.getX();
		int cellY = cell.getY();
		cell.setSize(new Dimension(-cellX, -cellY));
		cell.setLocation(0, 0);
		Point cellLocation = indexToLocation(row);
		Point mousePosition = event.getPoint();
		mousePosition.translate(-(int) cellLocation.getX(), -(int) cellLocation.getY());
		logger.log(Level.FINEST, "Mouse Position translates to " + mousePosition + ".");
		Component toolTipComponent = cell.getComponentAt(mousePosition);
		cell.setLocation(cellX, cellY);
		logger.log(Level.FINEST, "Component under Mouse is " + toolTipComponent + ".");
		if (toolTipComponent instanceof JComponent) {
			return ((JComponent) toolTipComponent).getToolTipText();
		}
		return null;
	}

}
