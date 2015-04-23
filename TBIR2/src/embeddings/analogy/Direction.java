package embeddings.analogy;

import embeddings.Vector;


/**
 * @author Thijs D.
 *
 * Represents the direction analogy
 */
public class Direction extends Analogy {
	public Direction() {
		this.type="direction";
	}

	@Override
	public double computeAnalogy(Vector v1, Vector v2, Vector v3, Vector checkVector) {
		Vector temp1 = v1.subtract(v2); // a-b		
		Vector temp2 = v3.subtract(checkVector); // c-d
		double result = temp1.cosineDist(temp2); // cos(a-b,c-d)
		return result;
		}

}
