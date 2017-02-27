package husacct.define.presentation.utils;

public class DataHelper {
    private long id;
    private Object key;
    private String value;

    public long getId() {
	return id;
    }

    public Object getKey() {
	return key;
    }

    public String getValue() {
	return value;
    }

    // TODO remove id and replace with key
    public void setId(long id) {
	this.id = id;
    }

    public void setKey(Object key) {
	this.key = key;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public String toString() {
	return getValue();
    }

}
