package husacct.define.presentation.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class KeyValueComboBox extends JComboBox{
	private static final long serialVersionUID = -2870095258058479405L;

	private HashMap<String, String> keyValuePair = new HashMap<String,String>();

	public KeyValueComboBox() {
		super();
		keyValuePair = new HashMap<String,String>();
	}
	
	public void setModel(Object[] keys, Object[] values){
		String[] tmpKeys = Arrays.asList(keys).toArray(new String[keys.length]);
		String[] tmpValues = Arrays.asList(values).toArray(new String[values.length]);
		
		setHashMap(tmpKeys, tmpValues);
		ComboBoxModel model = new DefaultComboBoxModel(tmpValues);
		super.setModel(model);
	}
	
	private void setHashMap(String[] keys, String[] values){
		HashMap<String, String> keyValuePair = new HashMap<String, String>();
		for (int i = 0; i<keys.length;i++){
			keyValuePair.put(keys[i], values[i]);
		}
		this.keyValuePair = keyValuePair;
	}
	
	public String getSelectedItemKey(){
		String selectedItemValue = "";
		if (super.getSelectedItem() != null) {
			selectedItemValue = super.getSelectedItem().toString();
		}
		return getHaskMapKeyFromValue(selectedItemValue);
	}
	
	public String getItemKeyAt(int i){
		String selectedItemValue = "";
		if (getItemAt(i) != null) {
			selectedItemValue = super.getItemAt(i).toString();
		}
		return getHaskMapKeyFromValue(selectedItemValue);
	}
	
	@SuppressWarnings("rawtypes")
	private String getHaskMapKeyFromValue(String value){
		String key = "";
		for (Iterator iterator = keyValuePair.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String tmpKey = (String)entry.getKey();
			if (keyValuePair.get(tmpKey).equals(value)){
				key = tmpKey;
				break;
			}
		}
		return key;
	}
}
