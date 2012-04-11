package husacct.analyse.presentation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class SortedListModelV2 extends AbstractListModel implements TableModel  {

	  

			SortedSet<Object> model;

			  public SortedListModelV2() {
			    model = new TreeSet<Object>();
			  }

			  public int getSize() {
			    return model.size();
			  }

			  public Object getElementAt(int index) {
			    return model.toArray()[index];
			  }

			  public void add(Object element) {
			    if (model.add(element)) {
			      fireContentsChanged(this, 0, getSize());
			    }
			  }

			  public void addAll(Object elements[]) {
			    Collection<Object> c = Arrays.asList(elements);
			    model.addAll(c);
			    fireContentsChanged(this, 0, getSize());
			  }

			  public void clear() {
			    model.clear();
			    fireContentsChanged(this, 0, getSize());
			  }

			  public boolean contains(Object element) {
			    return model.contains(element);
			  }

			  public Object firstElement() {
			    return model.first();
			  }

			  public Iterator iterator() {
			    return model.iterator();
			  }

			  public Object lastElement() {
			    return model.last();
			  }

			  public boolean removeElement(Object element) {
			    boolean removed = model.remove(element);
			    if (removed) {
			      fireContentsChanged(this, 0, getSize());
			    }
			    return removed;
			  }

			@Override
			public void addTableModelListener(TableModelListener arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public Class<?> getColumnClass(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getColumnCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String getColumnName(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getRowCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getValueAt(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isCellEditable(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void removeTableModelListener(TableModelListener arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void setValueAt(Object arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
 
}
