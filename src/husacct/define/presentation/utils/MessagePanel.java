package husacct.define.presentation.utils;

import java.awt.BorderLayout;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MessagePanel extends JPanel {

    private static final long serialVersionUID = -7360960342696885795L;
    private static MessagePanel instance;
    private String defaultMessage = ""; //ServiceProvider.getInstance().getLocaleService().getTranslatedString("IdleMessage");
    private JLabel jLabelStatus;
    private Stack<String> messages = new Stack<String>();


    /**
     * Because there can be only one instance of this jpanel the singleton pattern is implemented.
     * @return an StatusTask object
     */
    public static MessagePanel getInstance() {
		if (instance == null) {
		    instance = new MessagePanel();
		}
		return instance;
    }

    /**
     * @see <code>getInstance()</code>
     * @param newMessage
     * @return
     */
    public static MessagePanel getInstance(String newMessage) {
		instance = MessagePanel.getInstance();
		instance.setMessage(newMessage);
		return instance;
    }

    private MessagePanel() {
		super();
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		jLabelStatus = new JLabel(defaultMessage);
		add(jLabelStatus, BorderLayout.CENTER);
    }

    private void gotoLastTask() {
		if (messages.empty() || messages.peek().equals("") || messages.peek().equals(defaultMessage)) {
		    jLabelStatus.setText(defaultMessage);
		} else {
		    start();
		}
    }

    /**
     * Sets the message in a local variable
    * @param newMessage: The message that needs to show when the user calls <code>start()</code>
     */
    private void setMessage(String newMessage) {
		if (messages.empty()) {
		    messages.push(defaultMessage);
		}
		messages.push(newMessage);
    }

    /**
     * This method shows the given message and starts the progress bar.
     */
    public void start() {
		// logger.info("start() - Message: " + messages.peek());
		// logger.info("Message: " + messages.peek());
		jLabelStatus.setText(messages.peek());
		repaint();
    }

    /**
     * This method will replace the message with an default message and stops the progress bar.
     */
    public void stop() {
		// logger.info("stop() - Message: " + defaultMessage);
		// Remove the last message from the stack
		messages.pop();
		gotoLastTask();
    }
}
