package embeddings.analogy;

import embeddings.Vector;


/**
 * @author Thijs D.
 *
 * Represents the multiplication analogy
 */
public class Multiplication extends Analogy {

	public Multiplication() {
		this.type="multiplication";
	}

	public final static double EPS = 0.001;
	
	@Override
	public double computeAnalogy(Vector v1, Vector v2, Vector v3, Vector checkVector) {
		double temp1 = (checkVector.cosineDist(v3)+1)/2; // cos(d,c)
		double temp2 = (checkVector.cosineDist(v2)+1)/2; // cos(d,b)
		double temp3 = (checkVector.cosineDist(v1)+1)/2; // cos(d,a)
		double result = temp1*temp2/(temp3+EPS); // cos(d,c)cos(d,b)/(cos(d,a)+eps)
		return result;
	}

}
