package WWW.thedrake;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
		return Optional.of(this.troopMap.get(pos));
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
		return isLeaderPlaced() && guards <= 2;
	}	
	
	public Set<BoardPos> troopPositions() {
		return troopMap.keySet();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {

	    Optional<TroopTile> position = this.at(target);

	    if(!position.isPresent())
            throw new IllegalArgumentException("That position is already occupied.");

	    //initialise a new map
		Map<BoardPos, TroopTile> newMap = Collections.emptyMap();

		//fill with previous data
		for(BoardPos i :troopPositions())
			newMap.put(i,this.troopMap.get(i));

		TroopTile tmp = new TroopTile(troop, this.playingSide, TroopFace.AVERS);

		//place the trooper
		newMap.put(target,tmp);

		//determine whether a leader was placed
	    if(this.leaderPosition == TilePos.OFF_BOARD)
			return new BoardTroops(this.playingSide, this.troopMap, target, this.guards);
	    else
	    	return new BoardTroops(this.playingSide, this.troopMap, this.leaderPosition, this.guards);

	}
	
	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		// TODO
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
        // TODO
	}	
}
