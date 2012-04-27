package husacct.define.presentation.jframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import husacct.define.task.ValueInputController;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public abstract class AbstractValuesJFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 545921025909901624L;
	
	protected ValueInputController inputController;
	
	protected JButton cancelButton;
	protected JButton saveButton;
	
	public AbstractValuesJFrame() {
		inputController = new ValueInputController();
	}
	
	public abstract void initUI();
	protected abstract void addButtons();
	public abstract void actionPerformed(ActionEvent event);
	protected abstract void cancelButtonAction(ActionEvent event);
	protected abstract void saveButtonAction(ActionEvent event);
}
