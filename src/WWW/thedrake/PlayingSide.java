package WWW.thedrake;

import java.io.PrintWriter;

public enum PlayingSide {
    ORANGE, BLUE;

    public void toJSON(PrintWriter writer) {
        writer.printf("\"side\":\"" + this.toString() + "\"");
    }
}
