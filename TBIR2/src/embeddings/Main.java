package embeddings;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class Main {

	public static void main(String[] args) {
		Map<String,String> options = getOptions(args);
		Solver solver = new Solver(options);
		try {
			solver.solve();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Parses the different options and returns them as a hashmap with as key the option name and value the chosen option value.
	 * @param args Provided arguments
	 * @return HashMap containing name and value of the options.
	 */
	private static Map<String,String> getOptions(String[] args){
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("vector", args[0]);
		int size = args.length;
		int i;
		for(i=0;i<size;i++){
			if("-a".equals(args[i])){
				i++;
				if(i<size){
					result.put("analogy", args[i]);
				}
				else{
					System.out.println("You need to provide a valid analogy model.");
					break;
				}
			}
			else if ("-q".equals(args[i])){
				i++;
				if(i<size){
					result.put("query", args[i]);
				}
				else{
					System.out.println("You need to provide a valid query.");
					break;
				}
			}
			else if ("-t".equals(args[i])){
				i++;
				if(i<size){
					result.put("threads", args[i]);
				}
				else{
					System.out.println("You need to provide a valid number of threads.");
					break;
				}
			}
			
			else if ("-nq".equals(args[i])){
				i++;
				if(i<size){
					result.put("numberOfQueries", args[i]);
				}
				else{
					System.out.println("You need to provide a valid number of queries.");
					break;
				}
			}
			
			if ("-h".equals(args[i].toLowerCase())
					|| "--help".equals(args[i].toLowerCase())) {
				printHelp();			
			}
		}
		return result;
	}
	
	private static void printHelp(){
		System.out.println(
				"This java program allows a user to process analogy questions given a vector dataset.\n"
				+ "\n"
				+ "usage: java -jar analogy.jar vectorFilename.txt [options]\n"
				+ "vectorFilename.txt refers to the file name of the .txt file with the wordvectors.\n"
				+ "\n"
				+ "Options:\n"
				+ "-h --help                 Prints this message.\n"
				+ "-q                     	 Path to query file.\n"
				+ "-nq                       Number of queries to be processed.\n"
				+ "-a		          		 Specifies the chosen analogy: options are m (for multiplication), a (for addition) and d (for direction)"
				+ "Example Usage:\n"
				+ "java -jar analogy.jar google.txt -q questions-words.txt -a a -nq -100 -t 4");
	}
}
