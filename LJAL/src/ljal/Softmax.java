package ljal;

import java.util.Random;

/**
 * Implementation of the softmax action selection algorithm
 */
public class Softmax implements SelectionAlgorithm {
	
	public Softmax(AdaptiveParameter tau) {
		m_tau = tau;
	}
	
	@Override
	public int selectAction(double[] EV, int t) {
		double tau = m_tau.at(t);
		
		double maxEV = Double.NEGATIVE_INFINITY;
		for (double ev : EV) {
			maxEV = ev > maxEV ? ev : maxEV;
		}
		
		double[] probabilities = new double[EV.length];
		for (int i = 0; i < EV.length; ++i) {
			probabilities[i] = Math.exp((EV[i] - maxEV) / tau);
		}
		
		return randomWeightedIndex(probabilities);
	}
	
	private int randomWeightedIndex(double[] weights) {
		double totalWeight = 0.0;
		for (double weight : weights) {
			totalWeight += weight;
		}
		
		Random random = new Random();
		double r = random.nextDouble() * totalWeight;
		
		for (int i = 0; i < weights.length; ++i) {
			if (r <= weights[i]) {
				return i;
			}
			else {
				r -= weights[i];
			}
		}
		
		return weights.length - 1; // Shouldn't happen, but well, Java wants a return, and I don't trust fp precision...
	}
	
	private AdaptiveParameter m_tau; 
	
}
