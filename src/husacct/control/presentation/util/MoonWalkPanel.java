package husacct.control.presentation.util;

import husacct.common.Resource;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class MoonWalkPanel extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;
    private Logger logger = Logger.getLogger(MoonWalkPanel.class);
    private JLabel label;

    public MoonWalkPanel() {
        setVisible(false);
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        Icon icon = new ImageIcon(Resource.get(Resource.BLACKCAT));
        label = new JLabel(icon);
        add(label);
        setBackground(new Color(172, 181, 189));
    }

    @Override
    public void run() {
        try {
            setVisible(true);
            while (label.getX() > -100 && !Thread.interrupted()) {
                Thread.sleep(10);
                label.setBounds(label.getX() - 3, label.getY(), label.getWidth(), label.getHeight());
            }
            setVisible(false);
        } catch (InterruptedException e) {
            logger.debug("Poof! :(");
        }
    }
}
