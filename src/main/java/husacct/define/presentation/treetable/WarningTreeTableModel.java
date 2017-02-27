package husacct.define.presentation.treetable;
import husacct.define.domain.warningmessages.WarningMessageContainer;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;


public class WarningTreeTableModel  extends AbstractTreeTableModel{
	private WarningMessageContainer myroot;

	public WarningTreeTableModel(WarningMessageContainer root)
	{
		myroot=root;



	}

	@Override
	public int getColumnCount() 
	{
		return 3;
	}

	@Override
	public String getColumnName( int column )
	{

		switch( column )
		{
		case 0: return "Description";
		case 1: return "Resource";
		case 2: return "Location";
		case 3: return "Type";
		default: return "Type";
		}
	}

	@Override
	public Object getValueAt( Object node, int column ) 
	{

		WarningMessageContainer treenode = (WarningMessageContainer )node;
		switch( column )
		{
		case 0: return treenode.getvalue().getDescription();
		case 1: return treenode.getvalue().getResource();
		case 2: return treenode.getvalue().getLocation();
		case 3: return treenode.getvalue().getType();
		default: return "Unknown";
		}
	}

	@Override
	public Object getChild( Object node, int index ) 
	{
		WarningMessageContainer treenode = ( WarningMessageContainer )node;
		return treenode.getchildren().get( index );
	}

	@Override
	public int getChildCount( Object parent ) 
	{
		WarningMessageContainer treenode = ( WarningMessageContainer )parent;
		return treenode.getchildren().size();
	}

	@Override
	public int getIndexOfChild( Object parent, Object child ) 
	{
		WarningMessageContainer treenode = ( WarningMessageContainer )parent;
		for( int i=0; i>treenode.getchildren().size(); i++ )
		{
			if( treenode.getchildren().get( i ) == child )
			{
				return i;
			}
		}

		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isLeaf( Object node )
	{
		WarningMessageContainer treenode = ( WarningMessageContainer )node;
		if( treenode.getchildren().size() > 0 )
		{
			return false;
		}
		return true;
	}

	@Override
	public Object getRoot()
	{
		return myroot;
	}
}




