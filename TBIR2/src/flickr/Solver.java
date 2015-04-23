package flickr;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logger.Logger;
import embeddings.Vector;
import flickr.model.Model;
import flickr.model.ModelType;
import flickr.model.SentenceEmbedModel;
import flickr.model.SentenceProbModel;
import flickr.model.TotalEmbedModel;
import flickr.model.TotalProbModel;

/**
 * @author Thijs D
 * 
 * This class is responsible for creating models and queries out of the given file and starting workers that will
 * check how the chosen models perform.
 */
public class Solver {
	
	private String train;
	private ModelType modelType;
	private String vectorsFile;
	private int nbOfThreads=1;
	private int nbOfQueries=0;
	private String test;
	private String imageNamesFile;
	private String imagesVectorFile;

	/**
	 * Creates a solver based on a map containing options.
	 * @param options
	 * @throws IOException
	 */
	public Solver(Map<String, String> options){
		setOptions(options);
	}
	
	public void solve() throws IOException{
		Logger.getInstance(vectorsFile,modelType.toString(),nbOfThreads,nbOfQueries);
		List<Model>models = createModels();
		List<Query>queries = createQueries();
		Map<String,Vector>images = createImages();
		prepareModels(models);
		solve(queries,models,images);		
	}

	private Map<String,Vector> createImages() {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * Configures the solver based on the given options.
	 * @param options
	 */
	private void setOptions(Map<String, String> options) {
		train=options.get("train");
		test=options.get("test");
		imagesVectorFile=options.get("images");
		imageNamesFile=options.get("names");
		modelType = ModelType.fromString(options.get("model"));

		if(options.containsKey("vectors")){
			this.vectorsFile=options.get("vectors");
		}
		if(options.containsKey("threads")){
			this.nbOfThreads=Integer.parseInt(options.get("threads"));
		}
		if(options.containsKey("nbOfQueries")){
			this.nbOfQueries=Integer.parseInt(options.get("nbOfQueries"));
		}
		
		
	}
	
	/**
	 * Creates different threads that will each take on a subset of the queries.
	 * @param images 
	 * @param models 
	 * @param queries 
	 * @throws IOException
	 */
	public void solve(List<Query> queries, List<Model> models, Map<String,Vector> images) throws IOException {
		int totalQueries = queries.size();
		int queriesCovered = 0;
		int queriesPerThread = totalQueries/nbOfThreads;
		int i = 0;
		if(nbOfQueries==0){
			nbOfQueries=queriesPerThread;
		}
		while(queriesCovered<totalQueries-1 && i<nbOfThreads){
			i++;
			System.out.println("creating worker "+i);
			List<Query> threadQueries = new ArrayList<Query>();
			for(int j=queriesCovered;j<(Math.min(queriesCovered+nbOfQueries,queries.size()-1));j++){
				threadQueries.add(queries.get(j));
			}
			System.out.println("Worker starts at "+queriesCovered+" until "+(queriesCovered+nbOfQueries));
			Worker worker = new Worker(threadQueries, models, images);
			Thread t = new Thread(worker,"worker"+i);
			t.start();
			queriesCovered +=queriesPerThread;
		}
		
	}
	
	/**
	 * Selects queries out of each model.
	 * @return 
	 */
	private List<Query> createQueries() {
		//TODO
		return null;
	}
	
	/**
	 *  Creates a model for each image in the dataset.
	 * @return 
	 */
	private List<Model> createModels() {
		System.out.println("Creating models...");
		List<Model> models = new ArrayList<Model>();
		FileInputStream fs;
		try {
			fs = new FileInputStream(train);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			String line;
			int i = 0;
			Model currentModel=null;
			while((line=br.readLine())!=null){
				String[] elements = line.split("	");
				String image = elements[0].split("#")[0];
				String sentence = elements[1];
				if(i%5==0){
					currentModel = createModel(image,vectorsFile);
					models.add(currentModel);
				}
				currentModel.addSentence(sentence);
				i++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return models;
	}
	
	
	/**
	 * Returns the right model based on the model type of this solver.
	 * @param image Title of the image
	 * @param vectorsFile File containing vectors (this is optional but required for the embeddings model)
	 * @return a new Model based on the modeltype
	 */
	private Model createModel(String image,String vectorsFile){
		if(modelType==ModelType.P_TOTAL){
			return new TotalProbModel(image);
		}
		else if (modelType==ModelType.P_AGG || modelType==ModelType.P_MAX){
			return new SentenceProbModel(image,modelType);
		}
		else if (modelType==ModelType.E_TOTAL){
			return new TotalEmbedModel(image, vectorsFile);
		}
		else if(modelType==ModelType.E_AGG||modelType==ModelType.E_MAX){
			return new SentenceEmbedModel(image, vectorsFile,modelType);
		}
		else{
			return new TotalProbModel(image);
		}
	}
	
	/**
	 * 'Trains' the models i.e. generates the probabilities for the unigram models or prepares the vectors for an embeddings model.
	 * @param models 
	 */
	private void prepareModels(List<Model> models) {
		double i = 1;
		for(Model m:models){
			if(i%10==0){
				System.out.println("Creating models progress:"+100*(i/(models.size()))+"%");
			}
			i++;
			m.generateValues();
		}

	}

	
}