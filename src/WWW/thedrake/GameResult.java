package WWW.thedrake;

import java.io.PrintWriter;

public enum GameResult {
	VICTORY, DRAW, IN_PLAY;

	public void toJSON(PrintWriter writer) {
		writer.printf("\"" + this.toString() + "\"");
	}
}
