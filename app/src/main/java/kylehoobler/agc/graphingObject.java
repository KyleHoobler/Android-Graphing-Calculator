package kylehoobler.agc;

class graphingObject {

    private Equation eq;
    private boolean isSelected;
    private int color;

    public graphingObject(Equation e, boolean isSelected, int color){
        this.eq = e;
        this.isSelected = isSelected;
        this.color = color;
    }

    public Equation getEq() {
        return eq;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public void setSelected(boolean x){
        isSelected = x;
    }
}
