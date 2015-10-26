package dao;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import entities.Kill;
import entities.Match;
import entities.User;
import entities.Weapon;

@Component("dao")
public class Dao {
	
	private Match match;
	

	String SINTAX_TITLE = "\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\s-\\sNew\\smatch\\s\\d+\\shas\\sstarted";
	String SINTAX_FOOTER = "\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\s-\\sMatch\\s\\d+\\shas\\sended";
	String SINTAX_KILL = "\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\s-\\s\\S+\\skilled\\s\\S+\\susing\\s\\S+";
	String SINTAX_KILL_BY_WORD = "\\d\\d/\\d\\d/\\d\\d\\d\\d\\s\\d\\d:\\d\\d:\\d\\d\\s-\\s<\\S+>\\skilled\\s\\S+\\sby\\s\\S+";
	String SPLITTER = "\\s";
	String FORMAT_DATE = "dd/MM/yyyy HH:mm:ss";
	
	
	/**
	 * @param path:String
	 * @return Match 
	 * 
	 * O método carrega os dados da partida e os retorna;
	 * @throws IOException 
	 * @throws ParseException 
	 * 
	 */
	public Boolean loadData(File file) throws IOException, ParseException{
		
		List<String> list = FileUtils.readLines(file);
		
		match = new Match();
		
		
		// Se o arquivo tiver menos que duas linhas está incorréto e retorna null
		if(list.size() < 2 ){
			return null;
		}
		
		//se o arquivo não tem o cabeçalho nem o rodape está incorreto e retorna null
		if(!list.get(0).matches(SINTAX_TITLE) || !list.get(list.size() - 1).matches(SINTAX_FOOTER)){
		  	return null;
		} else {
			String s[] = list.get(0).split(SPLITTER);
			Long matchNumber = Long.parseLong(s[5]);
			match.setMatchNumber(matchNumber);
		}
		
		
		// adiciona as linhas kill no objeto Match
		for(int i = 1; i < list.size() - 1; i++){
			
			// verifica se a sintaxe de cada linha está correta
			if(!list.get(i).matches(SINTAX_KILL) && !list.get(i).matches(SINTAX_KILL_BY_WORD)){
			  	return null;
			} 
				
				// separa as palavras da linha em um vetor
				String s[] = list.get(i).split(SPLITTER);
				
				Kill kill = new Kill();
				
				// adicionar o primeiro usuário (nome do matador)
				String userName = s[3];
				
				if(match.getUsersHash().containsKey(userName) ){
					kill.setKiller(match.getUsersHash().get(userName));
				} else {
					kill.setKiller(new User(userName));
					match.getUsersHash().put(userName, kill.getKiller());
				}
				
				// adicionar o segundo usuário (nome do morto)
				userName = s[5];
				
				if(match.getUsersHash().containsKey(userName) ){
					kill.setKilled(match.getUsersHash().get(userName));
				} else {
					kill.setKilled(new User(userName));
					match.getUsersHash().put(userName, kill.getKilled());
				}
				
				// adicionar a arma utilizada (nome da arma)
				String weaponName = s[7];
				
				if(match.getWeaponsHash().containsKey(weaponName) ){
					kill.setWeapon(match.getWeaponsHash().get(weaponName));
				} else {
					kill.setWeapon(new Weapon(weaponName));
					match.getWeaponsHash().put(weaponName, kill.getWeapon());
				}
				
				//adicionar o tempo do evento kill
				Date date = new SimpleDateFormat(FORMAT_DATE).parse(s[0] + " " + s[1]);
				
				kill.setDate(date);
				
				// Gera a lista geral de Kill
				match.getKills().add(kill);
				
				
				// Gera as listas de Kills dos usuários
				if(!match.getUserKiller().containsKey(kill.getKiller().getName())){
					match.getUserKiller().put(kill.getKiller().getName(),new ArrayList<Kill>());
				}
				match.getUserKiller().get(kill.getKiller().getName()).add(kill);
				
				// Gera as listas de mortes dos usuários
				if(!match.getUserKilled().containsKey(kill.getKilled().getName())){
					match.getUserKilled().put(kill.getKilled().getName(),new ArrayList<Kill>());
				}
				match.getUserKilled().get(kill.getKilled().getName()).add(kill);
				
		}
		if(match.getKills().size() > 0){
			return true;
		} else {
			return false;
		}
	}
	
	public List<Kill> kills(){
		if(match == null){
			return null;
		} else {
			return match.getKills();
		}
	}
	
	public List<Kill> killsForUser(User user){
		if(match == null){
			return null;
		} else if(match.getUserKiller() == null ){
			return null;
		} else {
			return match.getUserKiller().get(user.getName());
		}
	}
	
	public List<Kill> userDeaths(User user){
		if(match == null){
			return null;
		} else if(match.getUserKilled() == null ){
			return null;
		} else {
			return match.getUserKilled().get(user.getName());
		}
	}
	
	public Collection<User> users(){
		if(match == null){
			return null;
		} else {
			return match.getUsersHash().values();
		}
	}
	
}
