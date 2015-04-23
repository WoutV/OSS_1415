package embeddings.analogy;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import embeddings.Vector;


/**
 * @author Thijs D.
 * 
 * Class representing an analogy strategy.
 *
 */
public abstract class Analogy {
	
	protected String type;

	/**
	 * Computes the word which is the closest according to the chosen analogy and the given other vectors.
	 */
	public String findClosest(Vector v1, Vector v2, Vector v3,
			String vectors, List<String> words) throws FileNotFoundException, IOException {
				double best = 0.0;
				String bestWord="NoWordFound!";
				FileInputStream fs;
				fs = new FileInputStream(vectors);
				BufferedReader br = new BufferedReader(new InputStreamReader(fs));
				String line;
				while((line=br.readLine())!=null){
					try{
					String[] elements = line.split(" ");
					Vector checkVector =  new Vector(line);
					double result = computeAnalogy(v1, v2, v3, checkVector);
						if(result>best && !words.contains(elements[0].toLowerCase())){
							bestWord = elements[0];
							best = result;							
							//System.out.println("New best word: "+ bestWord); // Print new best word
						}
					}
					catch(IllegalArgumentException iae){
						System.out.println("Vectors niet evengroot");
					}
			
				}
				br.close();
				System.out.println("Found best word");
				return bestWord;
			}
	/**
	 * Computes how analog the four given vectors are.
	 * @param v1
	 * @param v2
	 * @param v3
	 * @param v4
	 */
	public abstract double computeAnalogy(Vector v1,Vector v2,Vector v3,Vector v4);
	
	public String getType(){
		return this.type;
	}
}
