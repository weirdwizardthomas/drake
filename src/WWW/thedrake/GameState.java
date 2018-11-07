package WWW.thedrake;

import java.io.PrintWriter;
import java.util.Optional;

public class GameState implements JSONSerializable{
	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;
	
	public GameState(Board board, Army blueArmy, Army orangeArmy) {
		this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}
	
	public GameState(Board board, Army blueArmy, Army orangeArmy, PlayingSide sideOnTurn, GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
	}
	
	public Board board() {
		return board;
	}
	
	public PlayingSide sideOnTurn() {
		return sideOnTurn;
	}
	
	public GameResult result() {
		return result;
	}
	
	public Army army(PlayingSide side) {
		if(side == PlayingSide.BLUE) {
			return blueArmy;
		}
		
		return orangeArmy;
	}
	
	public Army armyOnTurn() {
		return army(sideOnTurn);
	}
	
	public Army armyNotOnTurn() {
		if(sideOnTurn == PlayingSide.BLUE)
			return orangeArmy;
		
		return blueArmy;
	}
	
	public Tile tileAt(BoardPos pos) {
		if (blueArmy.boardTroops().at(pos).isPresent()) {
			return blueArmy.boardTroops().at(pos).get();
		}

		if (orangeArmy.boardTroops().at(pos).isPresent()) {
			return orangeArmy.boardTroops().at(pos).get();
		}

		return board.at(pos);
	}
	
	private boolean canStepFrom(TilePos origin) {
		if(result != GameResult.IN_PLAY) {
			return false;
		}

		if ((!blueArmy.boardTroops().at(origin).isPresent()) && (!orangeArmy.boardTroops().at(origin).isPresent())) {
			return false;
		}

		if (sideOnTurn == PlayingSide.BLUE && (blueArmy.boardTroops().at(origin).isPresent()) && !blueArmy.boardTroops().isPlacingGuards()){
			return true;
		}

		if (sideOnTurn == PlayingSide.ORANGE && (orangeArmy.boardTroops().at(origin).isPresent()) && !orangeArmy.boardTroops().isPlacingGuards()){
			return true;
		}

		return false;
	}

	private boolean canStepTo(TilePos target) {
        if(result != GameResult.IN_PLAY) {
            return false;
        }

        if(blueArmy.boardTroops().at(target).isPresent()){
            return false;
        }

        if(orangeArmy.boardTroops().at(target).isPresent()){
            return false;
        }

        BoardPos boardPos;
        try {
            boardPos = new BoardPos(board.dimension(), target.i(), target.j());
        }
        catch (UnsupportedOperationException e){
            return false;
        }

	    return board.at(boardPos).canStepOn();
    }
	
	private boolean canCaptureOn(TilePos target) {

        if(result != GameResult.IN_PLAY) {
            return false;
        }

        if (sideOnTurn == PlayingSide.BLUE){
            return orangeArmy.boardTroops().at(target).isPresent();
        }
        else if (sideOnTurn == PlayingSide.ORANGE){
            return blueArmy.boardTroops().at(target).isPresent();
        }

		return false;
	}
	
	public boolean canStep(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canStepTo(target);
	}
	
	public boolean canCapture(TilePos origin, TilePos target)  {
		return canStepFrom(origin) && canCaptureOn(target);
	}
	
	public boolean canPlaceFromStack(TilePos target) {
        if(result != GameResult.IN_PLAY) {
            return false;
        }

        Army army;
        if (sideOnTurn == PlayingSide.BLUE){
            army = blueArmy;
        }
        else {
            army = orangeArmy;
        }

        if (sideOnTurn == PlayingSide.BLUE){
            if(blueArmy.stack().isEmpty()){
                return false;
            }

            if (!blueArmy.boardTroops().isLeaderPlaced()){
                int j;
                try {
                    j = target.j();
                }
                catch (UnsupportedOperationException e){
                    return false;
                }
                return j == 0 && canStepTo(target);
            }
            else if (blueArmy.boardTroops().isPlacingGuards()){
                return blueArmy.boardTroops().leaderPosition().isNextTo(target) && canStepTo(target);
            }
            else{
                return canStepTo(target);
            }
        }
        else if (sideOnTurn == PlayingSide.ORANGE) {
            if(orangeArmy.stack().isEmpty()){
                return false;
            }

            if (!orangeArmy.boardTroops().isLeaderPlaced()){
                int j;
                try {
                    j = target.j();
                }
                catch (UnsupportedOperationException e){
                    return false;
                }
                return j == board.dimension()-1 && canStepTo(target);
            }
            else if (orangeArmy.boardTroops().isPlacingGuards()){
                return orangeArmy.boardTroops().leaderPosition().isNextTo(target) && canStepTo(target);
            }
            else{
                return canStepTo(target);
            }
        }

		return false;
	}

	public GameState stepOnly(BoardPos origin, BoardPos target) {
		if(canStep(origin, target))
			return createNewGameState(
					armyNotOnTurn(),
					armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

		throw new IllegalArgumentException();
	}

	public GameState stepAndCapture(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;

			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target), 
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState captureOnly(BoardPos origin, BoardPos target) {
		if(canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;
			
			if(armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;
			
			return createNewGameState(
					armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState placeFromStack(BoardPos target) {
		if(canPlaceFromStack(target)) {
			return createNewGameState(
					armyNotOnTurn(), 
					armyOnTurn().placeFromStack(target), 
					GameResult.IN_PLAY);
		}
		
		throw new IllegalArgumentException();
	}
	
	public GameState resign() {
		return createNewGameState(
				armyNotOnTurn(), 
				armyOnTurn(), 
				GameResult.VICTORY);
	}
	
	public GameState draw() {
		return createNewGameState(
				armyOnTurn(), 
				armyNotOnTurn(), 
				GameResult.DRAW);
	}
	
	private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
		if(armyOnTurn.side() == PlayingSide.BLUE) {
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		}
		
		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result); 
	}

	@Override
	public void toJSON(PrintWriter writer) {
        writer.printf("\"result\":");
        result.toJSON(writer);
        writer.printf(",");
        board.toJSON(writer);
        writer.printf(",");
        blueArmy.toJSON(writer);
    }

}
