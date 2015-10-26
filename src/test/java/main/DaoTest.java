package main;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import dao.Dao;
import entities.Match;
import entities.User;

public class DaoTest  extends AbstractServiceTest{
	
	@Autowired
	Dao dao;
	
	@Value("${test.path}")
	String pathFileNormal;
	
	@Value("${test.path_normal_whithout_data}")
	String pathNormalWhithoutData;
	
	@Value("${test.path_whithout_footer}")
	String pathFileWithoutFooter;
	
	@Value("${test.path_whithout_title}")
	String pathFileWithoutTitle;
	
	@Value("${test.path_sintax_error}")
	String pathFileSintaxError;
	
	
	
	 /**
	 * @throws Exception
	 * 
	 * Verificar o comportamento do objeto de acesso a dados quando o arquivo passado não é terminado corretamente
	 * 
	 */
	@Test
	public void retriveDataTestWhithoutFooter() throws Exception{
		
		 File file = new File(pathFileWithoutFooter);
		 assertNull(dao.loadData(file));
		 
	}
	 
	/**
	 * @throws Exception
	 * 
	 * Verificar o comportamento do objeto de acesso a dados quando o arquivo passado não é iniciado corretamente
	 * 
	 */
	 @Test
	public void retriveDataTestWhithoutTitle() throws Exception{
			 
		 File file = new File(pathFileWithoutTitle);
		 assertNull(dao.loadData(file));
		
	}
	 
	 /**
		 * @throws Exception
		 * 
		 * Verificar o comportamento do objeto de acesso a dados quando o arquivo passado não é terminado corretamente
		 * 
		 */
	 @Test
		public void retriveDataSintaxError() throws Exception{
				 
				 File file = new File(pathFileSintaxError);
				 assertNull(dao.loadData(file));
			
		}
	 
	 
	 /**
		 * @throws Exception
		 * 
		 * Verificar se o comportamento do objeto de acesso a dados é correto se a estrutura estiver correta mas não existirem motes
		 * 
		 */
	 @Test
		public void retriveDataKillsWhithoutData() throws Exception{
				 
			 File file = new File(pathNormalWhithoutData);
			 Boolean bol = dao.loadData(file);
			 assertNotNull(bol);
			 assertEquals(false,bol.booleanValue());
			 assertEquals(0,dao.kills().size());
			
		}
	 
	 
	 /**
		 * @throws Exception
		 * 
		 * Verificar se o comportamento do objeto de acesso a dados é correto em relação ao carregamento das kills e seus índices
		 * 
		 */
	 @Test
		public void retriveDataKills() throws Exception{
				 
			 File file = new File(pathFileNormal);
			 Boolean bol = dao.loadData(file);
			 assertNotNull(bol);
			 assertEquals(true,bol.booleanValue());
			 assertEquals(17,dao.kills().size());
			 assertEquals(3,dao.killsForUser(new User("Roman")).size());
			 assertEquals(5,dao.userDeaths(new User("Nick")).size());
			
		}
	 
	 
	 

}
