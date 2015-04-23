package embeddings.analogy;

import embeddings.Vector;


/**
 * @author Thijs D.
 *
 * Represents the addition analogy
 */
public class Addition extends Analogy {

	public Addition() {
		this.type="addition";
	}

	public double computeAnalogy(Vector v1, Vector v2, Vector v3,  Vector checkVector) {
		Vector temp1 = v3.subtract(v1); // c-a
		Vector temp2 = temp1.add(v2); // c-a+b
		double result = checkVector.cosineDist(temp2); // cos(d',c-a+b)
		return result;
	}


}
