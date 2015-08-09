package leapmotion.main;

import com.leapmotion.leap.Controller;

import leapmotion.controller.MainController;
import leapmotion.controller.NeuralTraning;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		MainController mainController = new MainController();
		Controller controller = new Controller();
		
		controller.addListener(mainController);
		
		try {
			System.in.read();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
