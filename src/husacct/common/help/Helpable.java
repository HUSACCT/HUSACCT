package husacct.common.help;


import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public  class Helpable{
	Component component;
	public Helpable(Component comp) {

		this.component = comp;
	}
	public MouseListener getListener() {
		
		return new HelpMouseListener(this.component);
		
	}
	
	public void doListener(MouseEvent e) {
		new HelpMouseListener(this.component).mouseClicked(e);
	}

}
