package WWW.thedrake;

public class TroopTile implements Tile {

    //Attributes---------------------------
    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;
    //-------------------------------------


    //Constructor--------------------------
    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    //Getters------------------------------
    public PlayingSide side(){
        return this.side;
    }

    public Troop troop(){
        return this.troop;
    }

    public TroopFace face(){ return this.face;}

    //------------------------------------
    public boolean canStepOn(){
        return false; //occupied by a unit
    }

    @Override
    public boolean hasTroop() {
        return true; //occupied by a unit
    }

    //New-tile-constructor-----------------
    public TroopTile flipped(){
        TroopFace swap;
        if(this.face == TroopFace.AVERS)
            swap = TroopFace.REVERS;
        else
            swap = TroopFace.AVERS;
            return new TroopTile(this.troop, this.side, swap);
    }
}
