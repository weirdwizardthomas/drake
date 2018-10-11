package weirdwizards.thedrake;

import com.sun.scenario.effect.Offset;

public class Troop {

    //attributes---------------------------
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;
    //-------------------------------------

    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
    }

    public Troop(String name, Offset2D pivot) {
        this.name = name;
        this.aversPivot = pivot;
        this.reversPivot = pivot;
    }

    public Troop(String name) {
        int pivotValue = 1;
        this.name = name;
        this.aversPivot =  pivotValue;
        this.reversPivot = pivotValue;
    }

    public String Name() {
        return this.name;
    }

    public Offset2D pivot(TroopFace face){
        if(face == TroopFace.AVERS)
            return this.aversPivot;

        if(face == TroopFace.REVERS)
            return this.reversPivot;
    }

}
