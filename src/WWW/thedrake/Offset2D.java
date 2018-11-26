package WWW.thedrake;

public class Offset2D {


    //Attributes---------------------------
    public final int x;
    public final int y;
    //-------------------------------------

    //Constructors-------------------------
    public Offset2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Other-methods------------------------
    public boolean equalsTo(int x, int y)
    {
        return this.x == x && this.y == y;
    }

    public Offset2D yFlipped(){
        return new Offset2D(this.x, -this.y);
    }

}
