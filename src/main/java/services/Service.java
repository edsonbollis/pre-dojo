package services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.Dao;
import entities.Kill;
import entities.User;
import entities.Weapon;

@Component("service")
public class Service {
	
	@Autowired
	Dao dao;
	
	private static final Integer NUMBER_USER_DETH = 5;
	private static final Long VARIATION_OF_TIME = 60000l;
	
	
	private static final String WORD = "<WORLD>";
	
	
	/**
	 * @return Collection<Usuario>
	 * 
	 * retorna uma lista de usu�rios ordenada 
	 * em primeiro lugar pelo n�mero de assassinatos 
	 * e, como crit�rio de desenpate, o n�mero de mortes
	 * 
	 * pontua��o = - (numero de assassinatos do usu�rio * numero de mortes da partida - numero de mortes do usu�rio)
	 * 
	 *  Isso gera uma arvore de busca inversamente ordenada para ser retornada
	 * 
	 */
	public Collection<User> generateRank(File file){
		
		try{
			// Gera a arvore de busca
			TreeMap<Integer, User> ordenate = new TreeMap<Integer, User>();
			
			Boolean bol = dao.loadData(file);
			if(bol == null || !bol){
				return null;
			}
			
			Collection<User> users = dao.users();
			
			int totalDeaths = dao.kills().size() + 1;
			
			int pontuation, killsForUser, userDeath; 
			
			// monta o Rank
			for (User user : users) {
				
				if(user.getName().equals(WORD)){
					continue;
				}
				
				if(dao.killsForUser(user) != null){
					killsForUser = dao.killsForUser(user).size();
				} else {
					killsForUser = 0;
				}
				
				if(dao.userDeaths(user) != null){
					userDeath = dao.userDeaths(user).size();
				} else {
					userDeath = 0;
				}
				
				pontuation = -(killsForUser*totalDeaths - userDeath) ;
				
				ordenate.put(pontuation, user);
			}
			
			return ordenate.values();
			
		} catch(Exception e){
			return null;
		}
		
		
	}
	
	/**
	 * @param user: User
	 * @return Weapon
	 * 
	 * Escolhe a arma que o usu�rio mais utilizou ao assassinar outros usu�rios
	 * Se houver impates a primeira arma utilizada que matou mais usu�rios � retornada
	 * 
	 */
	public Weapon predilectWeapon(User user){
		
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		Integer itr = null;
		Weapon weapon = null;
		Integer max = new Integer(0);
		
		if(dao.killsForUser(user) == null){
			return null;
		}
		
		//Escolhe a arma mais utilizada e grava as mortes por outras armas no hash
		for (Kill kill : dao.killsForUser(user)) {
			if(hashMap.containsKey(kill.getWeapon().getName())){
				itr = hashMap.get(kill.getWeapon().getName());
				hashMap.put(kill.getWeapon().getName(), itr++);
			} else {
				itr = new Integer(1);
				hashMap.put(kill.getWeapon().getName(), itr);
			}
			
			if(itr > max){
				weapon = kill.getWeapon();
				max = itr;
			}
		}
		return weapon;
	}
	
	
	/**
	 * @param user: User
	 * @return List<Kill>
	 * 
	 * gera a maior lista contendo uma sequencia de mortes sem que o usu�rio tenha morrido
	 * 
	 */
	public List<Kill> majorKillsSequence(User user){
		
		TreeMap<Long,Kill> treeMap = new TreeMap<Long, Kill>();
		
		// Colocando os assassinatos e mortes do usu�rio em ordem temporal
		// Estou supondo que o arquivo n�o tenha linhas duplicadas, ou seja, mesmo millissegundo 
		//(ent�o quando o tempo estiver duplicado os eventos ocorreram em millisegundos distintos)
		
		//Adi��o dos tempos de assassinato do usu�rio
		List<Kill> listKillsAux = dao.killsForUser(user);
		if(listKillsAux != null){
			for (Kill kill : listKillsAux) {
				if(treeMap.containsKey(kill.getDate().getTime())){
					Long j = kill.getDate().getTime();
					while(treeMap.containsKey(j)){
						j = j+1;
					}
					treeMap.put(j,kill);											
				} else {
					treeMap.put(kill.getDate().getTime(),kill);
				}
			}
		}
		 //adi��o dos tempos de morte do usu�rio
		listKillsAux = dao.userDeaths(user);
		if(listKillsAux != null){
			for (Kill kill : dao.userDeaths(user)) {
				if(treeMap.containsKey(kill.getDate().getTime())){
					Long j = kill.getDate().getTime();
					while(treeMap.containsKey(j)){
						j = j+1;
					}
					treeMap.put(j,kill);											
				} else {
					treeMap.put(kill.getDate().getTime(),kill);
				}
			}
		}
		
		List<Kill> killsReturn = new ArrayList<Kill>();
		List<Kill> killsAux = new ArrayList<Kill>();
		// c�lculo do n�mero de mortes sucess�vas 
		for (Kill kill : treeMap.values()) {
			
			if(kill.getKiller().getName().equals(user.getName())){
				killsAux.add(kill);
			} else {
				if(killsReturn.size() < killsAux.size()){
					killsReturn = killsAux;
				}
				killsAux = new ArrayList();
			}
			
		}
		
		if(killsReturn.size() < killsAux.size()){
			killsReturn = killsAux;
		}
		
		return killsReturn;
	}
	
	/**
	 * @param user: User
	 * @return Boolean
	 * 
	 * Verifica se um usu�rio sofreu mortes durante o jogo
	 * 
	 */
	public Boolean userDeath(User user){
		if(dao.userDeaths(user) == null || dao.userDeaths(user).size() == 0){
			return false;
		} else {
			return true;
		}
	}
	
	
	
public Boolean numberDeathsInTime(User user){
		
		TreeMap<Long,Kill> treeMap = new TreeMap<Long, Kill>();
		
		
		// Colocando os assassinatos e mortes do usu�rio em ordem temporal
		// Estou supondo que o arquivo n�o tenha linhas duplicadas, ou seja, mesmo millissegundo 
		//(ent�o quando o tempo estiver duplicado os eventos ocorreram em millisegundos distintos)
		
		//Adi��o dos tempos de assassinato do usu�rio
		List<Kill> listKillsAux = dao.killsForUser(user);
		if(listKillsAux != null){
			for (Kill kill : listKillsAux) {
				if(treeMap.containsKey(kill.getDate().getTime())){
					Long j = kill.getDate().getTime();
					while(treeMap.containsKey(j)){
						j = j+1;
					}
					treeMap.put(j,kill);											
				} else {
					treeMap.put(kill.getDate().getTime(),kill);
				}
			}
		}
		
		if(treeMap.size() < NUMBER_USER_DETH){
			return false;
		}
		
		List<Long> list = new ArrayList<Long>(); 
		
		list.addAll(treeMap.keySet());
		
		
		// c�lculo do n�mero de mortes no tempo 
		for(int i = 0; i + NUMBER_USER_DETH -1 < list.size(); i++ ){
			if(list.get(i + NUMBER_USER_DETH -1) - list.get(i)  <= VARIATION_OF_TIME){
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	public List<Kill> getKilledForUser(User user){
		return dao.killsForUser(user);
	}
	
	public List<Kill> getUserDeaths(User user){
		return dao.userDeaths(user);
	}
	
	
	
	
	
	
	
}
