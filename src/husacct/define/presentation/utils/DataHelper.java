package husacct.define.presentation.utils;

public class DataHelper {

    private long id;
    private Object key;
    private String value;

    //TODO remove id and replace with key
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return getValue();
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }
}
