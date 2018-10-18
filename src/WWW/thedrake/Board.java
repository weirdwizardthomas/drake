package WWW.thedrake;

public class Board {
	
	public Board(int dimension) {
		// Místo pro váš kód


		// tmp
	}
		
	public int dimension() {
		// Místo pro váš kód


		// tmp

		return 1;
	} 
	
	public BoardTile at(BoardPos pos) {
		// Místo pro váš kód


		// tmp
		return new BoardTile() {
			@Override
			public boolean canStepOn() {
				return false;
			}

			@Override
			public boolean hasTroop() {
				return false;
			}
		};
	}
		
	public Board withTiles(TileAt ...ats) {
		// Místo pro váš kód


		// tmp
		return this;
	}
	
	public PositionFactory positionFactory() {
		// Místo pro váš kód


		// tmp
		return new PositionFactory(1);
	}
	
	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;
		
		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}
}

