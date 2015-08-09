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

	public void onInit(Controller controller) { // Program baþladýðýnda çalýan
												// method.

		System.out.println("Program Baþarýyla Baþlatýldý.");

		training = new NeuralTraning();
		
		/****************************			//Eðitim Kümesi oluþturulmak istenildiðince açýlacak
		 training.readData(); 					
		 *******************************/ 	
		training.readData();
		 matrix = new Matrix(training);
		listCoordinat = new ArrayList<>();

	}

	public void onConnect(Controller controller) { // Cihazla baðlantý
													// saðlandýðýnda çalýþan
													// method.

		System.out.println("Cihazla Baðlantý Saðlandý.");

		// Burada Cihazýn tanýmasýný istediðimiz Gesturelari Ekliyorz.
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);

		controller.config().setFloat("Gesture.Swipe.MinLenght", 1000000.0f);
		controller.config().save();
		System.out.println(controller.config().getFloat(
				"Gesture.Swipe.MinLenght"));

	}

	public void onDisconnect(Controller controller) { // Cihaz ile baðlantý
														// koparýldýðýnda
														// çalýþan method.

		System.out.println("Cihaz Ýle Baðlantý Koptu.");
	}

	public void onExit(Controller controller) { // Program kapatýldýðýnda
												// çalýþan method.

		System.out.println("Program Sonlandýrýldý.");
	}

	public void onFrame(Controller controller) { // Cihaz kendini her
													// yenilediðinde çalýþan
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

							System.out.println("Type SWÝPE ID =" + swipe.id());
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
