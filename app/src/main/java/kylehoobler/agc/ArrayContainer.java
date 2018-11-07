package kylehoobler.agc;

import java.util.ArrayList;

class ArrayContainer {

    private ArrayList<Equation> x;

    public ArrayContainer(){
        x = new ArrayList<>();
    }

    public ArrayContainer(ArrayList<Equation> e){
        x = e;
    }

    public void addItem(Equation e){
        x.add(e);
    }

    public ArrayList<Equation> getList() {
        return x;
    }
}
