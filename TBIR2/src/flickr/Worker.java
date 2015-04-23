package flickr;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import logger.Logger;
import embeddings.Vector;
import flickr.model.Model;


/**
 * @author Thijs D
 * 
 * A thread which ranks the models for each query.
 */
public class Worker implements Runnable{

	private List<Model> models;
	private List<Query> queries;
	private Logger logger;
	private Map<String, Vector> images;


	public Worker(List<Query> queries, List<Model> models,Map<String,Vector> images) {
		this.queries=queries;
		this.models=models;
		this.logger=Logger.getInstance();
		this.images=images;
	}


	private String getBestTraining(Query q) throws IOException{
		System.out.println("Started Ranking...");
		String bestImage="";
		double bestRanking=0;
		double progress=0.0;
		progress++;
		System.out.println("Ranking training images progress: "+100*(1.0*progress/queries.size())+"%");
		for(Model m: models){
			double rank = m.rank(q);
			if(rank>bestRanking){
				bestRanking=rank;
				bestImage = m.getImage();
			}
		}
		//System.out.println("#########################");
		//System.out.println("Query for image "+q.getImage());
		//ranking.printHighest(5);

		return bestImage;
	}


	private void logResults(List<Ranking> rankings) {
		//TODO uitbreiden

	}


	@Override
	public void run() {
		try {
			List<Ranking> rankings = new ArrayList<Ranking>();
			//TODO ranking metrics 
			for(Query q: queries){
				String image = getBestTraining(q);
				Vector best = images.get(image);
				rankings.add(getClosestImages(best));				
			}
				logResults(rankings);

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	private Ranking getClosestImages(Vector best) {
		System.out.println("Started Ranking the images...");
		int progress=0;
		Ranking ranking = new Ranking();
		for(Entry<String, Vector> image:images.entrySet()){
			progress++;
			System.out.println("Ranking the images progress: "+100*(1.0*progress/images.size())+"%");
			double rank = image.getValue().cosineDist(best);
			RankElement el = new RankElement(image.getKey(),rank);
				ranking.addElement(el);
		}	
		return ranking;
	}


}
