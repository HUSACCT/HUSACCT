package husacct.analyse.domain.famix;

class FamixArgument extends FamixObject {

    private int index;
    private boolean isReceiver;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isReceiver() {
        return isReceiver;
    }

    public void setReceiver(boolean isReceiver) {
        this.isReceiver = isReceiver;
    }
}
