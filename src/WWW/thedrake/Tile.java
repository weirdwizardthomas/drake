package WWW.thedrake;

import java.io.PrintWriter;
import java.util.List;

public interface Tile {
	public boolean canStepOn();
	public boolean hasTroop();
	public List<Move> movesFrom(BoardPos pos, GameState state);

    void toJSON(PrintWriter writer);
}
