package leapmotion.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.leapmotion.leap.Finger;

public class Matrix {

	private List<String> listNumbers;
	int i = 20;
	private NeuralTraning training;
	private final int MATRIX_SIZE = 6400;
	int xGlobal = 400 , yGlobal = 400;

	public Matrix(NeuralTraning pTraining) {

		listNumbers = new ArrayList<>();
		this.training = pTraining;
	}

	public void createMatrix(List<Finger> listFinger) {
		if (listFinger.size() > 0) {
			try {
				int tempMinX = 400;
				int tempMaxX = 0;
				int tempMinY = 400;
				int tempMaxY = 0;

				for (Finger finger : listFinger) {
					int x = (int) finger.tipPosition().getX();
					int y = (int) finger.tipPosition().getY();
					x = Math.abs(x);
					y = Math.abs(y);

					if (tempMinX > x) {
						tempMinX = x;
					}
					if (tempMaxX < x) {
						tempMaxX = x;
					}
					if (tempMinY > y) {
						tempMinY = y;
					}
					if (tempMaxY < y) {
						tempMaxY = y;
					}
				}
 
				int[][] matrix = new int[xGlobal][yGlobal];

				System.out.println("x min:" + tempMinX + "\ny min:" + tempMinY);
				System.out.println("x:" + tempMaxX + "\ny:" + tempMaxY); 

				int tempBetweenX = tempMaxX - tempMinX;
				int tempSpaceX = (xGlobal - tempBetweenX) / 2;

				int tempBetweenY = tempMaxY - tempMinY;
				int tempSpaceY = (yGlobal - tempBetweenY) / 2;

				System.out.println("Tempspace x: " + tempSpaceX
						+ "  :  TempSpace Y: " + tempSpaceY);

				for (Finger finger : listFinger) {
					int x = (int) finger.tipPosition().getX();
					int y = (int) finger.tipPosition().getY();
					x = Math.abs(x);
					y = Math.abs(y);

					matrix[y - tempMinY + tempSpaceY][x - tempMinX + tempSpaceX] = 1;

					for (int i = 0; i < 45; i++) {
						x++;

						y = (int) finger.tipPosition().getY();
						y = Math.abs(y);
						for (int j = 0; j < 45; j++) {
							y++;
							matrix[y - tempMinY + tempSpaceY][x - tempMinX
									+ tempSpaceX] = 1;

						}
					}
				}
				System.out.println("Baþarýlý Bir Þekilde 400 X 400 Matrix oluþturuldu.");
				String write = "";
				/*
				 * for(int[] rowMatrix : matrix){ for(int number : rowMatrix){
				 * write = write + number; } write = write + "\n"; }
				 */

				// dosyayaEkle(write);

				resizeMatrix(matrix, 5, 2);

			} catch (Exception e) {
				System.out.println(e.toString() + " createMatrix ");
			}
		}
	}

	// Matrix'i parametreler oranýnda küçültüyor. (Normalizasyon)
	private void resizeMatrix(int orjinalMatrix[][], int tolerans,
			int tolerans1Sayisi) {
		int ySize = orjinalMatrix.length;
		int xSize = orjinalMatrix[0].length;

		int[][] arraySutunKucult = new int[ySize][xSize / tolerans];

		for (int i = 0; i < ySize; i++) {
			int count1 = 0;
			for (int j = 0; j < xSize; j++) {

				if (orjinalMatrix[i][j] == 1) {
					count1++;
				}
				if (j % tolerans == 0) {
					if (count1 > tolerans1Sayisi) {
						arraySutunKucult[i][j / tolerans] = 1;
					}
					count1 = 0;
				}
			}
		}

		int[][] arrayFinaly = new int[ySize / tolerans][xSize / tolerans];

		for (int i = 0; i < xSize / tolerans; i++) { // ---->
			int count1 = 0;
			for (int j = 0; j < ySize; j++) {

				if (arraySutunKucult[j][i] == 1) {
					count1++;
				}
				if (j % tolerans == 0) {
					if (count1 > tolerans1Sayisi) {
						arrayFinaly[j / tolerans][i] = 1;
					}
					count1 = 0;
				}
			}
		}

		try {

			String write = "";
			for (int[] rowMatrix : arrayFinaly) {
				for (int number : rowMatrix) {
					write = write + number;
				}
				write = write + "\n";
			}
			dosyayaEkle(write);

		} catch (Exception e) {
			System.out.println(e.toString() + " resizeMatrix ");
		}

		// ------------------------------------- Kayýtlý eðitim verilerinden
		// cekerek karsýlastýrma.
		 /*
		  String file = "D:\\numberTemplate\\yeni8.txt"; 
		  double [] tempArrayList = new double[MATRIX_SIZE]; 
		  
		  FileReader fileReader; 
		  String line;
		  try { fileReader = new FileReader(file);
		   BufferedReader br = new
		  BufferedReader(fileReader);
		   int k =0; 
		   while (( line = br.readLine()) != null) {
		    char[] tempArray = line.toCharArray();
		   
		   for(char cTemp : tempArray){ 
			   tempArrayList[k] = Double.parseDouble(String.valueOf(cTemp));
			   k++;
			   }
		  
		   } }catch(Exception e){} 
		   training.test(tempArrayList);
		  */
		// --------------------------------------

		resizeMatrixOneDimension(arrayFinaly);
	}

	// Gönderilen 2 boyutlu matrisi tek boyutlu arraya çeviriyor.
	private void resizeMatrixOneDimension(int[][] pMatrix) {

		ArrayList<Double> tempList = new ArrayList<>();
		int[] temp = new int[MATRIX_SIZE];
		try {

			int i = 0;
			for (int[] tempMatrix : pMatrix) {
				for (int value : tempMatrix) {
					temp[i] = value;
					i++;
				}
			}
		} catch (Exception e) {
			System.out.println("createRowResizeMatrix   " + e.toString());
		}

		training.test(temp);
	}

	
	private void dosyayaEkle(String metin) throws Exception {
		try {

			File dosya = new File("D:\\numberTemplate\\yeni" + i + ".txt");
			FileWriter yazici = new FileWriter(dosya, true);
			BufferedWriter yaz = new BufferedWriter(yazici);
			yaz.write(metin);
			yaz.close();
			System.out.println("Ekleme Ýþlemi Baþarýlý");
			i++;

		} catch (Exception hata) {
			hata.printStackTrace();
		}
	}
}
