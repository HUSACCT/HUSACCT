package husacct.analyse.domain.famix;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

abstract class FamixObject {

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("(" + this.getClass().getSimpleName() + "\r\n");
        ArrayList<Field> fields = getFields(this.getClass());
        for (Field field : fields) {
            string.append(" (" + field.getName() + " \"" + getFieldValue(field, this) + "\")\r\n");

        }
        string.append(')');
        return string.toString();
    }

    public ArrayList<Field> getFields(java.lang.Class<? extends java.lang.Object> theClass) {
        ArrayList<Field> fields = new ArrayList<Field>();
        if (theClass.getSuperclass() != null) {
            fields.addAll(getFields(theClass.getSuperclass()));
        }
        Field[] myFields = theClass.getDeclaredFields();
        for (Field field : myFields) {
            fields.add(field);
        }
        return fields;
    }

    private String getFieldValue(Field field, Object object) {
    	String returnVariable;
        String fieldName = field.getName();
        String methodName = getFieldMethodName(fieldName);
        try {
            Method method = this.getClass().getMethod(methodName, (Class<?>) null);
            java.lang.Object response = method.invoke(this, (Object) null);
            if (response == null) {
                returnVariable = "null";
            }
            returnVariable = response.toString();
        } catch (Exception e) {
            returnVariable = "-";
        }
		return returnVariable;
    }

    private String getFieldMethodName(String fieldName) {
        if (fieldName.startsWith("is")) {
            return fieldName;
        } else if (fieldName.startsWith("has")) {
            return "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
