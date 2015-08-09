package leapmotion.controller;

import java.util.ArrayList;
import java.util.List;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Finger.Type;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;

public class MainController extends Listener {

	private int gestureId = 0;
	private boolean motionActive = false;
	private Matrix matrix;
	private List<Finger> listCoordinat;
	private NeuralTraning training;

	public MainController() {
	}

	public void onInit(Controller controller) { // Program ba�lad���nda �al�an
												// method.

		System.out.println("Program Ba�ar�yla Ba�lat�ld�.");

		training = new NeuralTraning();
		
		/****************************			//E�itim K�mesi olu�turulmak istenildi�ince a��lacak
		 training.readData(); 					
		 *******************************/ 	
		training.readData();
		 matrix = new Matrix(training);
		listCoordinat = new ArrayList<>();

	}

	public void onConnect(Controller controller) { // Cihazla ba�lant�
													// sa�land���nda �al��an
													// method.

		System.out.println("Cihazla Ba�lant� Sa�land�.");

		// Burada Cihaz�n tan�mas�n� istedi�imiz Gesturelari Ekliyorz.
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);

		controller.config().setFloat("Gesture.Swipe.MinLenght", 1000000.0f);
		controller.config().save();
		System.out.println(controller.config().getFloat(
				"Gesture.Swipe.MinLenght"));

	}

	public void onDisconnect(Controller controller) { // Cihaz ile ba�lant�
														// kopar�ld���nda
														// �al��an method.

		System.out.println("Cihaz �le Ba�lant� Koptu.");
	}

	public void onExit(Controller controller) { // Program kapat�ld���nda
												// �al��an method.

		System.out.println("Program Sonland�r�ld�.");
	}

	public void onFrame(Controller controller) { // Cihaz kendini her
													// yeniledi�inde �al��an
													// method.

		Frame frame = controller.frame();

		/*
		 * for(Finger finger : frame.fingers()){
		 * if(finger.type()==Type.TYPE_INDEX)
		 * System.out.println("Finger Type : " + finger.type()
		 * +"  Finger Cordinate X= " + finger.tipPosition().getX()+" Y= "+
		 * finger.tipPosition().getY());
		 * 
		 * }
		 */

		for (Hand hand : frame.hands()) {

			for (Gesture gesture : frame.gestures()) {

				// if(gesture.type() == Gesture.Type.TYPE_SCREEN_TAP){
				// ScreenTapGesture screenTag = new ScreenTapGesture(gesture);
				// System.out.println("Type Screen Tap");
				// }
				if (hand.isLeft()) {
					if (gesture.type() == Gesture.Type.TYPE_SWIPE) {
						SwipeGesture swipe = new SwipeGesture(gesture);

						if (gestureId != swipe.id() && gestureId < swipe.id()) {

							System.out.println("Type SW�PE ID =" + swipe.id());
							System.out.println("oldu");
							motionActive = !motionActive;
						}

						if (gestureId < swipe.id()) {
							gestureId = swipe.id();
						}
					}
				}

			}
		}

		if (motionActive) {

			for (Hand hand : frame.hands()) {
				if (hand.isRight()) {
					for (Finger finger : hand.fingers()) {
						if (finger.type() == Type.TYPE_INDEX) {
							System.out.println("Finger Type : " + finger.type()
									+ "  Finger Cordinate X= "
									+ finger.tipPosition().getX() + " Y= "
									+ finger.tipPosition().getY());
							listCoordinat.add(finger);

						}
					}
				}
			}
		}

		if (listCoordinat.size() > 1 && !motionActive) {
			matrix.createMatrix(listCoordinat);
			listCoordinat = null;
			listCoordinat = new ArrayList<>();
		}

	}
}
