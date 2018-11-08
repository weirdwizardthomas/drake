package WWW.thedrake;

import java.io.PrintWriter;

public enum TroopFace {
    AVERS, REVERS;

    public void toJSON(PrintWriter writer) {
        writer.printf("\"face\":\"" + this.toString() + "\"");
    }
}
