package kylehoobler.agc;

public class ErrorItem extends EquationPart {

    public ErrorItem(){
        this.setDisplayItem("An Error has Occurred.");
    }

    public ErrorItem(String message){
        this.setDisplayItem(message);
    }
}
