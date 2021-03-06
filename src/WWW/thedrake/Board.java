package WWW.thedrake;

import java.io.PrintWriter;

public final class Board {

	private final int dimension;
	private final BoardTile[][] board;

	public Board(int dimension) {
        if (dimension < 0)
            throw new IllegalArgumentException("The dimension needs to be positive.");

        this.dimension = dimension;
        board = new BoardTile[dimension][dimension];

        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                board[row][col] = BoardTile.EMPTY;
            }
        }
    }

    public Board(int dimension, BoardTile[][] board) {
        this.dimension = dimension;
        this.board = board;
    }

    public int dimension() {
		return dimension;
	} 
	
	public BoardTile at(BoardPos pos) {
	    return board[pos.i()][pos.j()];
	}

	public Board withTiles(TileAt ...ats) {

	    BoardTile[][] newBoard = new BoardTile[this.dimension()][this.dimension()];

	    for(int i = 0; i < this.dimension(); i++) {
            newBoard[i] = board[i].clone();
        }

	    for (TileAt tile : ats){
            newBoard[tile.pos.i()][tile.pos.j()] = tile.tile;
        }

        return new Board(this.dimension(), newBoard);
	}
	
	public PositionFactory positionFactory() {

		return new PositionFactory(this.dimension());
	}

	public void toJSON(PrintWriter writer) {
		writer.printf("\"board\":");
		writer.printf("{");
		writer.printf("\"dimension\":" + dimension() + ",");
		writer.printf("\"tiles\":[");
        int counter = 0;

//		for(BoardTile[] row : board)
//		{
//
//			for(BoardTile element : row)
//			{
//				counter++;
//				element.toJSON(writer);
//				if(counter < dimension()*dimension())
//					writer.printf(",");
//			}
//
//		}

		for (int j = 0; j<dimension; j++) {
            for(int i = 0; i<dimension; i++){
                counter++;
				board[i][j].toJSON(writer);
				if(counter < dimension()*dimension())
				    writer.printf(",");
            }
        }

        writer.printf("]}");
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

