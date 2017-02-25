package husacct.define.task.components;

import husacct.define.domain.warningmessages.WarningMessage;


import java.awt.Component;
import java.util.ArrayList;

public  class WarningComponent  {

private WarningMessage decourWarningMessage;
	private ArrayList<WarningComponent> warnings = new ArrayList<WarningComponent>();	

public WarningComponent(WarningMessage msg) {
	decourWarningMessage=msg;
}

public void addWarning(WarningComponent msg)
{
warnings.add(msg);
}



public boolean isleaf() {
	if (warnings.size()>0) {
		return true;
	} else {
return false;
	}
	
}

public int getChildrenSize()
{
return warnings.size();	
}

public WarningComponent getWarningComponent(int index)
{
 return	warnings.get(index);

}

public int getWarningComponentIndex(WarningComponent index)
{
 return	warnings.indexOf(warnings);

}


public WarningMessage getWarningValue()
{
	decourWarningMessage.generateMessage();
	return decourWarningMessage;
}

public void addChild(WarningComponent w) {
	warnings.add(w);
	
}

}
