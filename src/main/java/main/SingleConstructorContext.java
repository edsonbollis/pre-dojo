package main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class SingleConstructorContext {
	
	private static final String ARQUIVO_CONTEXT = "applicationContextSMI.xml";

	private static ClassPathXmlApplicationContext context = null;
	
	
	public static ClassPathXmlApplicationContext  getClassPathXmlApplicationContext(){
		
		
		if(context == null){
			context = new ClassPathXmlApplicationContext(ARQUIVO_CONTEXT);
			context.start();
		}
		
		return context;
		
	}
	
	
	
}
