package main;

import view.View;


/**
 * Hello world!
 * 
 */
public class App {
	
	//public static Logger logger = Logger.getLogger(App.class);
	/**
	 * @param args
	 *           run application
	 */
	public static void main(String[] args) {

		try {
			// Initalize Spring context in Desktop Application
			View view = (View) SingleConstructorContext
					.getClassPathXmlApplicationContext().getBean(
							"view");
			view.createView();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}
