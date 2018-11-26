package WWW.thedrake;

import java.io.PrintWriter;
import java.util.List;

public class Troop {

    //attributes---------------------------
    private final String name;
    private final Offset2D aversPivot;
    private final Offset2D reversPivot;
    private final List<TroopAction> aversActions;
    private final List<TroopAction> reversActions;
    //-------------------------------------

    //Constructors-------------------------
    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.name = name;
        this.aversPivot = pivot;
        this.reversPivot = pivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }

    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions) {
        this.aversActions = aversActions;
        this.reversActions = reversActions;
        int pivotValue = 1;
        this.name = name;
        this.aversPivot = new Offset2D(pivotValue, pivotValue);
        this.reversPivot = new Offset2D(pivotValue, pivotValue);
    }

    //Other-methods------------------------
    public String name() {
        return this.name;
    }

    public Offset2D pivot(TroopFace face){
        if(face == TroopFace.AVERS)
            return this.aversPivot;
        else
            return this.reversPivot;
    }

    public List<TroopAction> actions(TroopFace face){
     if (face == TroopFace.AVERS){
         return aversActions;
     }
     else {
         return reversActions;
     }
    }

    public void toJSON(PrintWriter writer) {
        writer.printf("\"troop\":\"" + name() + "\"");
    }
}
