package embeddings;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import logger.Logger;
import embeddings.analogy.Addition;
import embeddings.analogy.Analogy;
import embeddings.analogy.Direction;
import embeddings.analogy.Multiplication;


/**
 * @author Thijs D.
 *
 * Class responsible for solving an analogy task for a file containing queries.
 */
public class Solver {

	private String vector;
	private String query;
	private Analogy analogy;
	private int nbOfThreads;
	private int nbOfQueries;

	public Solver(Map<String, String> options) {
		verifyOptions(options);
		Logger.getInstance(analogy.getType(), vector, nbOfQueries,nbOfThreads);
	}

	/**
	 * Checks if all necessary options are filled in and changes the solver attributes so they are matching.
	 * @param options
	 */
	private void verifyOptions(Map<String, String> options) {
		if(options.containsKey("vector")){
			this.vector = options.get("vector");
		}
		else{
			throw new IllegalArgumentException("A vectors file should be given");
		}
		if(options.containsKey("query")){
			this.query=options.get("query");
		}
		else{
			throw new IllegalArgumentException("A query file should be given");
		}
		if(options.containsKey("analogy")){
			String ana = options.get("analogy");
			if(ana.equals("a")){
				this.analogy = new Addition();
			}
			else if(ana.equals("d")){
				this.analogy = new Direction();
			}
			else{
				this.analogy = new Multiplication();
			}
		}
		else{
			this.analogy = new Addition();
		}
		if(options.containsKey("threads")){
			this.nbOfThreads=Integer.parseInt(options.get("threads"));
		}
		else{
			this.nbOfThreads=1;
		}
		if(options.containsKey("numberOfQueries")){
			this.nbOfQueries=Integer.parseInt(options.get("numberOfQueries"));
		}
		else{
			this.nbOfQueries=0;
		}
		
	}

	/**
	 * Create different threads that will each take on a subset of the queries.
	 * @throws IOException
	 */
	public void solve() throws IOException {
		int nbOfLines = countLines(query);
		System.out.println("Total lines : "+nbOfLines);
		int linesCovered = 1;
		int linesPerThread = nbOfLines/nbOfThreads;
		int i = 0;
		while(linesCovered<nbOfLines-1 && i<nbOfThreads){
			i++;
			System.out.println("creating worker "+i);
			int stop = Math.min(linesCovered+nbOfQueries-1,Math.min(nbOfLines-1, (linesCovered+linesPerThread-1)));
			Worker worker = new Worker(linesCovered, stop, vector,analogy,query);
			Thread t = new Thread(worker,"worker"+i);
			t.start();
			linesCovered +=linesPerThread;
		}
		
	}
	
	
	/**
	 * @author http://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 * Counts the number of lines in the given file.
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}
	
	

}
