package leapmotion.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.encog.Encog;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.simple.TrainAdaline;
import org.encog.neural.pattern.ADALINEPattern;
import org.encog.neural.pattern.FeedForwardPattern;
import org.encog.persist.EncogDirectoryPersistence;

public class NeuralTraning {

	private List<MLData> listMLData;
	private final int MATRIX_SIZE = 6400;
	private int j=0;

	public NeuralTraning() {

		listMLData = new ArrayList<>();
	}

	public void readData() {
 
		for (int i = 0; i < 10; i++) {
			String file = "D:\\numberTemplate\\yeni" + i + ".txt";
			FileReader fileReader;
			String line;
			try {
				fileReader = new FileReader(file);
				BufferedReader br = new BufferedReader(fileReader);
				ArrayList<Double> tempList = new ArrayList<>();
				double[] tempArrayList = new double[MATRIX_SIZE];

				int k = 0;
				while ((line = br.readLine()) != null) {
					char[] tempArray = line.toCharArray();

					for (char cTemp : tempArray) { 
						tempArrayList[k] = Double.parseDouble(String
								.valueOf(cTemp));
						k++; 
					}

				}

				// mldata olustur.

				MLData tempMLData = new BasicMLData(tempArrayList);
				listMLData.add(tempMLData);

				br.close();
 

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.toString());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.toString());
			}
		}
		
		trainingNetwork();
	}

	public void trainingNetwork() {

		int inputNeurons = MATRIX_SIZE * 1;
		int outputNeurons = 10;

		FeedForwardPattern ffPattern = new FeedForwardPattern();
		ffPattern.addHiddenLayer(15);
		ffPattern.addHiddenLayer(15);
		ffPattern.setInputNeurons(inputNeurons);
		ffPattern.setOutputNeurons(outputNeurons);
		BasicNetwork network = (BasicNetwork) ffPattern.generate();
		
		/*
		ADALINEPattern pattern = new ADALINEPattern();
		pattern.setInputNeurons(inputNeurons);
		pattern.setOutputNeurons(outputNeurons);
		BasicNetwork network = (BasicNetwork) pattern.generate();
		 */
		// train it
		MLDataSet training = generateTraining();
		MLTrain train = new Backpropagation(network, training, 0.7, 0.7);

		int epoch = 1;
		do {
			train.iteration();
			System.out
					.println("Epoch #" + epoch + " Error:" + train.getError());
			epoch++;
		} while (train.getError() > 0.0007);

		//
		System.out.println("Error:" + network.calculateError(training));

		EncogDirectoryPersistence.saveObject(new File(
				"D:\\numberTemplate\\training.eg"), network);

	}

	public MLDataSet generateTraining() {
		MLDataSet result = new BasicMLDataSet();
		for (int i = 0; i < 10; i++) {
			BasicMLData ideal = new BasicMLData(10);

			// setup input
			MLData input = new BasicMLData(listMLData.get(i));

			// setup ideal
			for (int j = 0; j < 10; j++) {
				if (j == i)
					ideal.setData(j, 1);
				else
					ideal.setData(j, -1);
			}

			// add training element
			result.add(input, ideal);
		}
		return result;
	}

	
	
	
	// Eðitilen Yapay Sinir Aðýný gelen veri ile Test ettirme.
	public void test(int[] testData) {

		BasicNetwork network = (BasicNetwork) EncogDirectoryPersistence
				.loadObject(new File("D:\\numberTemplate\\training.eg"));
		
		String a = "";
		for(int doub : testData)
			a = a + doub;
		try {
			
			 

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		MLData tempMLData = dosyadanOku();
		j++;
		int output = network.winner(tempMLData);
		
		System.out.println("sonuc. : " + output);
		Encog.getInstance().shutdown();
		
		
	}
	
 

	private MLData dosyadanOku(){
		
		String file = "D:\\numberTemplate\\yeni" + j + ".txt";
		FileReader fileReader;
		MLData tempMLData =null;
		String line;
		try {
			fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			ArrayList<Double> tempList = new ArrayList<>();
			double[] tempArrayList = new double[MATRIX_SIZE];

			int k = 0;
			while ((line = br.readLine()) != null) {
				char[] tempArray = line.toCharArray();

				for (char cTemp : tempArray) { 
					tempArrayList[k] = Double.parseDouble(String
							.valueOf(cTemp));
					k++; 
				}
			}

			// mldata olustur.

			tempMLData = new BasicMLData(tempArrayList);

			br.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
		return tempMLData;
	}
	
}
