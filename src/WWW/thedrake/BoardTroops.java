package WWW.thedrake;

import java.io.PrintWriter;
import java.util.*;

public class BoardTroops {
	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;
	
	public BoardTroops(PlayingSide playingSide) { 
	this.playingSide = playingSide;
	this.troopMap = Collections.emptyMap();
	this.leaderPosition = TilePos.OFF_BOARD;
	this.guards = 0;
	}
	
	public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap,	TilePos leaderPosition, int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
	    return Optional.ofNullable(this.troopMap.get(pos));

	}
	
	public PlayingSide playingSide() {
		return this.playingSide;
	}
	
	public TilePos leaderPosition() {
		return this.leaderPosition;
	}

	public int guards() {
		return this.guards;
	}
	
	public boolean isLeaderPlaced() {
		return this.leaderPosition != TilePos.OFF_BOARD;
	}
	
	public boolean isPlacingGuards() {
		return isLeaderPlaced() && guards < 2;
	}	
	
	public Set<BoardPos> troopPositions() {
		return troopMap.keySet();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {

	    if(this.at(target).isPresent())
	        throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
        //add the new trooper
        newMap.put(target,new TroopTile(troop,playingSide(),TroopFace.AVERS));

        int newGuards = guards();
        if(isPlacingGuards())
            newGuards++;

        TilePos newLeaderPosition = leaderPosition();

        //determine leader's position
	    if(!isLeaderPlaced())
            newLeaderPosition = target;

	    return new BoardTroops(playingSide(),newMap, newLeaderPosition, newGuards);
	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {

	    if(!isLeaderPlaced() || isPlacingGuards())
	        throw new IllegalStateException();

	    //target is occupied OR origin is vacant
	    if(this.at(target).isPresent() || !this.at(origin).isPresent())
	        throw new IllegalArgumentException();

	    Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
        TilePos newLeaderPosition = this.leaderPosition();

	    //flip the OG trooper
	    TroopTile movedTrooper = this.troopMap.get(origin).flipped();
        newMap.put(target, movedTrooper);
        newMap.remove(origin);
	    //determine whether a leader's getting re-placed or not
	    if(this.leaderPosition().equals(origin))
	        newLeaderPosition = target;

        return new BoardTroops(this.playingSide(),newMap,newLeaderPosition,this.guards());
	}

	public BoardTroops troopFlip(BoardPos origin) {
		if(!isLeaderPlaced()) {
			throw new IllegalStateException(
					"Cannot move troops before the leader is placed.");			
		}
		
		if(isPlacingGuards()) {
			throw new IllegalStateException(
					"Cannot move troops before guards are placed.");			
		}
		
		if(!at(origin).isPresent())
			throw new IllegalArgumentException();
		
		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}
	
	public BoardTroops removeTroop(BoardPos target) {
        if(!isLeaderPlaced() || isPlacingGuards())
            throw new IllegalStateException();

        //trying to remove from an unoccupied tile
        if(!at(target).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newMap = new HashMap<>(troopMap);
        newMap.remove(target);

        TilePos newLeaderPosition = leaderPosition();

        //removing leader
        if(leaderPosition().equals(target))
            newLeaderPosition = TilePos.OFF_BOARD;


        return new BoardTroops(playingSide(),newMap, newLeaderPosition, guards());
    }

	public void toJSON(PrintWriter writer) {
		writer.printf("\"boardTroops\":{");

		playingSide.toJSON(writer);
		writer.printf(",");

		writer.printf("\"leaderPosition\":");
		leaderPosition.toJSON(writer);
		writer.printf(",");

		writer.printf("\"guards\":" + guards() + ",");
		troopMaptoJSON(writer);
		writer.printf("}");

	}

	private void troopMaptoJSON(PrintWriter writer){

		writer.printf("\"troopMap\":{");

        Map<BoardPos, TroopTile> sortedTroopMap = new TreeMap<>(troopMap);

		int counter = 0;
		for(BoardPos current : sortedTroopMap.keySet()) {
			counter++;

			current.toJSON(writer);
			writer.printf(":{");
			troopMap.get(current).toJSON(writer);

			writer.printf("}");
			if(counter < troopMap.size())
				writer.printf(",");
		}

		writer.printf("}");
	}
}
