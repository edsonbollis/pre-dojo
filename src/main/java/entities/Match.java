package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class Match {
	

	private Long matchNumber;
	private List<Kill> kills;
	private HashMap<String,User> usersHash;
	private HashMap<String,Weapon> weaponsHash;
	private HashMap<String,List<Kill>> userKiller;
	private HashMap<String,List<Kill>> userKilled;
	
	public Match(){
		super();
		this.kills = new ArrayList<Kill>();
		this.usersHash = new HashMap<String,User>();
		this.weaponsHash = new HashMap<String,Weapon>();
		this.userKiller = new HashMap<String,List<Kill>>();
		this.userKilled = new HashMap<String,List<Kill>>();
	}
	
	
	public Match(Long matchNumber, List<Kill> kills,  HashMap<String, User> usersHash,
			HashMap<String, Weapon> weaponsHash, HashMap<String, List<Kill>> userKiller,
			HashMap<String, List<Kill>> userKilled) {
		super();
		this.matchNumber = matchNumber;
		this.kills = kills;
		this.usersHash = usersHash;
		this.weaponsHash = weaponsHash;
		this.userKiller = userKiller;
		this.userKilled = userKilled;
	}


	public Long getMatchNumber() {
		return matchNumber;
	}
	public void setMatchNumber(Long matchNumber) {
		this.matchNumber = matchNumber;
	}
	public List<Kill> getKills() {
		return kills;
	}
	public void setKills(List<Kill> kills) {
		this.kills = kills;
	}
	public HashMap<String, User> getUsersHash() {
		return usersHash;
	}

	public void setUsersHash(HashMap<String, User> usersHash) {
		this.usersHash = usersHash;
	}

	public HashMap<String, Weapon> getWeaponsHash() {
		return weaponsHash;
	}

	public void setWeaponsHash(HashMap<String, Weapon> weaponsHash) {
		this.weaponsHash = weaponsHash;
	}


	public HashMap<String, List<Kill>> getUserKiller() {
		return userKiller;
	}


	public void setUserKiller(HashMap<String, List<Kill>> userKiller) {
		this.userKiller = userKiller;
	}


	public HashMap<String, List<Kill>> getUserKilled() {
		return userKilled;
	}


	public void setUserKilled(HashMap<String, List<Kill>> userKilled) {
		this.userKilled = userKilled;
	}


	@Override
	public String toString() {
		return "Match [matchNumber=" + matchNumber + ", kills=" + kills + ", usersHash="
				+ usersHash + ", weaponsHash=" + weaponsHash + ", userKiller=" + userKiller + ", userKilled="
				+ userKilled + "]";
	}
	
	
	
}
