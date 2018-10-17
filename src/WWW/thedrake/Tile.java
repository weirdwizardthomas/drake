package WWW.thedrake;

public interface Tile {

    //Tile is vacant and can be stepped on
    public boolean canStepOn();

    //Tile contains a unit
    public boolean hasTroop();
}
