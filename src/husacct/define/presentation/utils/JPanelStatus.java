package husacct.define.presentation.utils;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class JPanelStatus extends JPanel {

	private static final long serialVersionUID = -7360960342696885795L;
	private String defaultMessage = "Idle";
	private Stack<String> messages = new Stack<String>();
	private JLabel jLabelStatus;
	private JProgressBar jProgressBar;

	private static JPanelStatus instance;

	private JPanelStatus() {
		super();
		
		GridBagLayout jPanel1Layout = new GridBagLayout();
		jPanel1Layout.rowWeights = new double[] { 0.1 };
		jPanel1Layout.rowHeights = new int[] { 7 };
		jPanel1Layout.columnWeights = new double[] { 0.0, 0.1 };
		jPanel1Layout.columnWidths = new int[] { 787, 7 };
		setLayout(jPanel1Layout);
		setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

		jLabelStatus = new JLabel(defaultMessage);
		add(jLabelStatus, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

		jProgressBar = new JProgressBar();
		jProgressBar.setValue(0);

		add(jProgressBar, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		jProgressBar.setPreferredSize(new java.awt.Dimension(823, 14));
	}

	/**
	 * Because there can be only one instance of this jpanel the singleton pattern is implemented.
	 * 
	 * @return An StatusTask object
	 */
	public static JPanelStatus getInstance() {
		if (instance == null) {
			instance = new JPanelStatus();
		}
		return instance;
	}

	/**
	 * @see <code>getInstance()</code>
	 * @param newMessage
	 * @return
	 */
	public static JPanelStatus getInstance(String newMessage) {
		instance = JPanelStatus.getInstance();

		instance.setMessage(newMessage);
		return instance;
	}

	/**
	 * Sets the message in an local variable
	 * 
	 * @param newMessage The message that needs to show when the user calls <code>start()</code>
	 */
	private void setMessage(String newMessage) {
		if (messages.empty()) {
			messages.push(defaultMessage);
		}
		messages.push(newMessage);
	}

	/**
	 * This method shows the given message and starts the progressbar.
	 */
	public void start() {
//		logger.info("start() - Message: " + messages.peek());
//		logger.info("Message: " + messages.peek());
		jLabelStatus.setText(messages.peek());
		jProgressBar.setIndeterminate(true);

		repaint();
	}

	private void gotoLastTask() {
		if (messages.peek().equals(defaultMessage) || messages.peek().equals("") || messages.empty()) {
			jLabelStatus.setText(defaultMessage);
			jProgressBar.setIndeterminate(false);
		} else {
			start();
		}
	}

	/**
	 * This method will replace the message with an default message and stops the progressbar.
	 */
	public void stop() {
//		logger.info("stop() - Message: " + defaultMessage);

		// Remove the last message from the stack
		messages.pop();

		gotoLastTask();
	}
}
