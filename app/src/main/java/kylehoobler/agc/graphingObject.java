package kylehoobler.agc;

import com.jjoe64.graphview.series.LineGraphSeries;

class graphingObject {

    private Equation eq;
    private boolean isSelected;
    private int color;
    private LineGraphSeries LineGraphSeriess;

    public graphingObject(Equation e, boolean isSelected, int color) {
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

    public void setColor(int color) {
        this.color = color;
    }

    public void setSelected(boolean x) {
        isSelected = x;
    }

    public LineGraphSeries getLineGraphSeriess() {
        return LineGraphSeriess;
    }

    public void setLineGraphSeriess(LineGraphSeries LineGraphSeriess) {
        this.LineGraphSeriess = LineGraphSeriess;
    }
}
