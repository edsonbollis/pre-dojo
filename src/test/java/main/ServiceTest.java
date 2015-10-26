package main;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.AssertThrows;

import dao.Dao;
import entities.Match;
import entities.User;
import services.Service;

public class ServiceTest  extends AbstractServiceTest{
	
	@Autowired
	Service service;
	
	@Value("${test.path}")
	String pathFileNormal;
	
	
	
	/**
	 * @throws Exception
	 * 
	 * Testa de integração com DAO
	 *  Passar um arquivos inexiste e veriricar se retorna erro
	 *  
	 */
	@Test
	public void integrationTest(){
		
		try{
		Collection<User> collection = service.generateRank(new File(".\teste"));
		assertNull(collection);
		}catch(Exception e){
			throw new AssertionError("Integration Error");
		}
		
		
		 
	}
	
	
	
	 /**
	 * @throws Exception
	 * 
	 * Testa a geração do ranking verificando os usuário da lista de testes
	 * Rank
	 * 
	 * * User    Killer  Killed
	 * Ronin    7       3
     * Roman 	3   	4
     * Nick 	2		5
   	 * Edgar    1       0
     * Suelen   1       3
     * Leo		0		2
	 * 
	 */
	@Test
	public void generateRankTest() throws Exception{
		
		Collection<User> collection = service.generateRank(new File(pathFileNormal));
		
		assertNotNull(collection);
		assertEquals(6, collection.size());
		Iterator<User> it = collection.iterator();
		assertEquals("Ronin", it.next().getName());
		assertEquals("Roman", it.next().getName());
		assertEquals("Nick", it.next().getName());
		assertEquals("Edgar", it.next().getName());
		assertEquals("Suelen", it.next().getName());
		assertEquals("Leo", it.next().getName());
		 
	}
	
	
	
	/**
	 * @throws Exception
	 * 
	 * Testa se o usuário tem o número de mortes e assassinatos correto
	 * Rank
	 * 
	 * * User    Killer  Killed
	 * Ronin    7       4
	 * 
	 */
	@Test
	public void killsDeathTest() throws Exception{
		
		Collection<User> collection = service.generateRank(new File(pathFileNormal));
		
		assertNotNull(collection);
		assertEquals(6, collection.size());
		Iterator<User> it = collection.iterator();
		User user = it.next();
		assertEquals("Ronin", user.getName());
		assertEquals(7, service.getKilledForUser(user).size());
		assertEquals(3, service.getUserDeaths(user).size());
		 
	}
	
	
	/**
	 * @throws Exception
	 * 
	 * Testa a arma predileta do usuário
	 * Rank
	 * 
	 * * User    Predilect Weapon 
	 * Ronin       AK47
	 * Roman 	   M16
     * Nick 	   AK47
	 * Edgar       M16
	 * Suelen      AWP
	 */
	
	@Test
	public void predilectWeaponTest() throws Exception{
		
		Collection<User> collection = service.generateRank(new File(pathFileNormal));
		
		assertNotNull(collection);
		Iterator<User> it = collection.iterator();
		User user = it.next();
		assertNotNull(service.predilectWeapon(user));
		assertEquals("AK47", service.predilectWeapon(user).getName());
		user = it.next();
		assertNotNull(service.predilectWeapon(user));
		assertEquals("M16", service.predilectWeapon(user).getName());
		user = it.next();
		assertNotNull(service.predilectWeapon(user));
		assertEquals("AK47", service.predilectWeapon(user).getName());
		user = it.next();
		assertNotNull(service.predilectWeapon(user));
		assertEquals("M16", service.predilectWeapon(user).getName());
		user = it.next();
		assertNotNull(service.predilectWeapon(user));
		assertEquals("AWP", service.predilectWeapon(user).getName());
		user = it.next();
		assertNull(service.predilectWeapon(user));
		 
	}
	
	
	
	/**
	 * @throws Exception
	 * 
	 * Testa a sequência de maior assassinato do usuário
	 * Rank
	 * 
	 *   User    Kills 
	 * Ronin       5
	 * Roman 	   2   	
     * Nick 	   2		
   	 * Edgar       1       
     * Suelen      1       
     * Leo		   0
	 */
	
	@Test
	public void majorKillsSequence() throws Exception{
		
		Collection<User> collection = service.generateRank(new File(pathFileNormal));
		
		assertNotNull(collection);
		Iterator<User> it = collection.iterator();
		User user = it.next();
		assertNotNull(service.majorKillsSequence(user));
		assertEquals(5, service.majorKillsSequence(user).size());
		user = it.next();
		assertNotNull(service.majorKillsSequence(user));
		assertEquals(2, service.majorKillsSequence(user).size());
		user = it.next();
		assertNotNull(service.majorKillsSequence(user));
		assertEquals(2, service.majorKillsSequence(user).size());
		user = it.next();
		assertNotNull(service.majorKillsSequence(user));
		assertEquals(1, service.majorKillsSequence(user).size());
		user = it.next();
		assertNotNull(service.majorKillsSequence(user));
		assertEquals(1, service.majorKillsSequence(user).size());
		user = it.next();
		assertNotNull(service.majorKillsSequence(user));
		assertEquals(0, service.majorKillsSequence(user).size());
		
	}
	
	/**
	 * Testa se o usuário morreu durante a partida
	 */
	@Test
	public void userDeathTest(){
		Collection<User> collection = service.generateRank(new File(pathFileNormal));
		
		assertNotNull(collection);
		Iterator<User> it = collection.iterator();
		User user = it.next();
		assertNotNull(service.userDeath(user));
		assertEquals(true, service.userDeath(user));
		user = it.next();
		assertNotNull(service.userDeath(user));
		assertEquals(true, service.userDeath(user));
		user = it.next();
		assertNotNull(service.userDeath(user));
		assertEquals(true, service.userDeath(user));
		user = it.next();
		assertNotNull(service.userDeath(user));
		assertEquals(false, service.userDeath(user));
		user = it.next();
		assertNotNull(service.userDeath(user));
		assertEquals(true, service.userDeath(user));
		user = it.next();
		assertNotNull(service.userDeath(user));
		assertEquals(true, service.userDeath(user));
	}
	
	/**
	 * Testa se o usuário conseguiu cinco mortes em um minuto
	 */
	@Test
	public void numberDeathsInTimeTest(){
		Collection<User> collection = service.generateRank(new File(pathFileNormal));
		
		assertNotNull(collection);
		Iterator<User> it = collection.iterator();
		User user = it.next();
		assertNotNull(service.numberDeathsInTime(user));
		assertEquals(true, service.numberDeathsInTime(user));
		user = it.next();
		assertNotNull(service.numberDeathsInTime(user));
		assertEquals(false, service.numberDeathsInTime(user));
		user = it.next();
		assertNotNull(service.numberDeathsInTime(user));
		assertEquals(false, service.numberDeathsInTime(user));
		user = it.next();
		assertNotNull(service.numberDeathsInTime(user));
		assertEquals(false, service.numberDeathsInTime(user));
		user = it.next();
		assertNotNull(service.numberDeathsInTime(user));
		assertEquals(false, service.numberDeathsInTime(user));
		user = it.next();
		assertNotNull(service.numberDeathsInTime(user));
		assertEquals(false, service.numberDeathsInTime(user));
	}
	 

}
