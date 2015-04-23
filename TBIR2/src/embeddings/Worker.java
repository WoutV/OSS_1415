package embeddings;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import logger.Logger;
import embeddings.analogy.Analogy;

/**
 * @author Thijs D.
 *
 * This class represents a worker thread responsible for processing analogy queries and finding the closest word.
 */
public class Worker implements Runnable{

	private int start;
	private int stop;
	private String vectors;
	private Analogy analogy;
	private String query;
	private Logger logger;

	public Worker(int start, int stop, String vectors, Analogy analogy,String query) {
		this.start=start;
		this.stop = stop;
		this.vectors= vectors;
		this.analogy = analogy;
		this.query=query;
		this.logger=Logger.getInstance();
	}

	@Override
	public void run() {
		System.out.println("A worker has started for start "+start+", stop "+stop);
		int currentLine = start;
		double successes = 0.0;
		FileInputStream fs;
		try {
			fs = new FileInputStream(query);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			for(int i = 0; i < start; ++i){ //move to startpoint in file
				br.readLine();
			}
			while(currentLine<stop){ // read each query until stop
				String query = br.readLine();
				String[] queryWords = query.split(" ");
				System.out.println("This worker is at " + 100*(currentLine-start)*1.0/(1.0*(stop-start)) + "%");
				String queryResult = processOneQuery(queryWords[0],queryWords[1],queryWords[2]);
				Boolean check = checkResult(queryResult,queryWords[3]);
		    // LOG EACH WORD INDIVIDUALLY
			//	logger.log("Line "+currentLine+":  found "+check+ " result for :"+queryWords[0]+" "+queryWords[1] +" "+queryWords[2]+ " = "+ queryResult +" and this should be: "+queryWords[3]);
				if(check){successes++;}
				currentLine++;
			}
			br.close();

		} catch (Exception e) {
			System.out.println("An error occured: \n");
			e.printStackTrace();
		}
		logSuccess(successes);
	}

	private void logSuccess(double successes) {
		logger.log("#############################################");
		logger.log("Worker started at "+start+" finished with " + successes+ " successes from query "+start+" until "+ stop +". \nRecall: " + successes/(stop-start+1));
		logger.log("#############################################");
		logger.close();
	}

	/**
	 * @return True if the result of the query on the system is the correct result.
	 */
	private Boolean checkResult(String queryResult, String queryWords) {
		return queryResult.equalsIgnoreCase(queryWords);
	}

	/**
	 * Processes the query consisting of the three given words.
	 * @return the word closest to the given query according to the chosen analogy
	 * @throws Exception
	 */
	private String processOneQuery(String word1, String word2, String word3) throws Exception {
		Vector v1 = getVector(word1,vectors);
		Vector v2 = getVector (word2,vectors);
		Vector v3 = getVector(word3,vectors);
		List<String> words = new ArrayList<String>();
		words.add(word1.toLowerCase());
		words.add(word2.toLowerCase());
		words.add(word3.toLowerCase());
		return analogy.findClosest(v1, v2, v3, vectors,words);		
	}

	/**
	 * Returns the wordvector for the given word.
	 * @param word
	 * @return
	 * @throws Exception
	 */
	public static Vector getVector(String word, String file) throws Exception{
		FileInputStream fs;
		fs = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fs));
		String line;
		while((line=br.readLine())!=null){
			String[] elements = line.split(" ");
			if(elements[0].equalsIgnoreCase(word)){
				br.close();
				return new Vector(line);
			}
		}
		br.close();
		throw new IllegalArgumentException("given word '"+word+"' does not exist in vocabulary");		
	}

}
