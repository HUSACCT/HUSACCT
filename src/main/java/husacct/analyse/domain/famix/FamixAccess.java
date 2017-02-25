package husacct.analyse.domain.famix;

class FamixAccess extends FamixAssociation {

    private String accesses;
    private String accessedIn;
	/**
* The is accessed l value. Is a predicate telling whether the value was
* accessed as Lvalue, i.e. a location value or a value on the left side of
* an assignment. When the predicate is true, the memory location denoted by
* the variable might change its value; false means that the contents of the
* memory location is read; null means that it is unknown. Note that LValue
* is the inverse of RValue.
*/
    private boolean isAccessedLValue = false;

    public String getAccesses() {
        return accesses;
    }

    public void setAccesses(String accesses) {
        this.accesses = accesses;
    }

    public String getAccessedIn() {
        return accessedIn;
    }

    public void setAccessedIn(String accessedIn) {
        this.accessedIn = accessedIn;
    }

    public boolean isAccessedLValue() {
        return isAccessedLValue;
    }

    public void setAccessedLValue(boolean isAccessedLValue) {
        this.isAccessedLValue = isAccessedLValue;
    }
}
