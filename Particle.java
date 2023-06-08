package Tlbo;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.cloudbus.cloudsim.network.datacenter.NetDatacenterBroker;

/**
 * Basic (abstract) particle
 * 
 * @author Pablo Cingolani <pcingola@users.sourceforge.net>
 */
public abstract class Particle {

	/** Best fitness function so far */
	double bestFitness;
	/** Best particles's position so far */
	double bestPosition[];
	/** current fitness */
	double currentFitness;
	/** Position */
	double position[];
	
	double xmin[];
	
	double xmax[];

	
	private double [] exeCos;

	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	/**
	 * Constructor 
	 */
	public Particle() {
		throw new RuntimeException("You probably need to implement your own 'Particle' class");
	}

	/**
	 * Constructor 
	 * @param dimension : Particle's dimension
	 */
	public Particle(int dimension) {
		allocate(dimension);
		
	}

	
	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------


	public double[] getPosition() {
		return position;
	}
	
	public double[] getBestPosition() {
		return bestPosition;
	}

	/** Allocate memory */
	public void allocate(int dimension) {
		position = new double[dimension];
		bestPosition = new double[dimension];
		bestFitness = Double.NaN;
		currentFitness = Double.NaN;
		//xmin = new double[dimension];
		//xmax = new double[dimension];
		
		
		//for (int i = 0; i < dimension; i++)
		//{
			//xmin[i] = 0;
			//xmax[i] = 7;
		//}
	}

	/**
	 * Apply position and velocity constraints (clamp)
	 * @param minPosition : Minimum position
	 * @param maxPosition : Maximum position
	 * @param minVelocity : Minimum velocity
	 * @param maxVelocity : Maximum velocity
	 */
	/*
	public void applyConstraints(double[] minPosition, double[] maxPosition, double[] minVelocity, double[] maxVelocity) {
		//---
		// Every constraint is set? (do all of them it one loop)
		//---
		if ((minPosition != null) && (maxPosition != null) && (minVelocity != null) && (maxVelocity != null)) for (int i = 0; i < position.length; i++) {
			if (!Double.isNaN(minPosition[i])) position[i] = (minPosition[i] > position[i] ? minPosition[i] : position[i]);
			if (!Double.isNaN(maxPosition[i])) position[i] = (maxPosition[i] < position[i] ? maxPosition[i] : position[i]);
			if (!Double.isNaN(minVelocity[i])) velocity[i] = (minVelocity[i] > velocity[i] ? minVelocity[i] : velocity[i]);
			if (!Double.isNaN(maxVelocity[i])) velocity[i] = (maxVelocity[i] < velocity[i] ? maxVelocity[i] : velocity[i]);
		}
		else {
			//---
			// Position constraints are set? (do both of them in the same loop)
			//---
			if ((minPosition != null) && (maxPosition != null)) for (int i = 0; i < position.length; i++) {
				if (!Double.isNaN(minPosition[i])) position[i] = (minPosition[i] > position[i] ? minPosition[i] : position[i]);
				if (!Double.isNaN(maxPosition[i])) position[i] = (maxPosition[i] < position[i] ? maxPosition[i] : position[i]);
			}
			else {
				//---
				// Do it individually
				//---
				if (minPosition != null) for (int i = 0; i < position.length; i++)
					if (!Double.isNaN(minPosition[i])) position[i] = (minPosition[i] > position[i] ? minPosition[i] : position[i]);
				if (maxPosition != null) for (int i = 0; i < position.length; i++)
					if (!Double.isNaN(maxPosition[i])) position[i] = (maxPosition[i] < position[i] ? maxPosition[i] : position[i]);
			}

		}
	}
*/
	/** Copy position[] to positionCopy[] */
	public void copyPosition(double positionCopy[]) {
		for (int i = 0; i < position.length; i++)
			positionCopy[i] = position[i];
	}

	/** Copy position[] to bestPosition[] */
	public void copyPosition2Best() {
		for (int i = 0; i < position.length; i++)
			bestPosition[i] = position[i];
	}

	public double getBestFitness() {
		return bestFitness;
	}

	public int getDimension() {
		return position.length;
	}

	public double getFitness() {
		return currentFitness;
	}


	/**
	 * Initialize a particles's position and velocity vectors 
	 * @param maxPosition : Vector stating maximum position for each dimension
	 * @param minPosition : Vector stating minimum position for each dimension
	 * @param maxVelocity : Vector stating maximum velocity for each dimension
	 * @param minVelocity : Vector stating minimum velocity for each dimension
	 */
	public void init(double xmax[], double xmin[]) {
		for (int i = 0; i < position.length; i++) {
			//if (Double.isNaN(maxPosition[i])) throw new RuntimeException("maxPosition[" + i + "] is NaN!");
			//if (Double.isInfinite(maxPosition[i])) throw new RuntimeException("maxPosition[" + i + "] is Infinite!");

			//if (Double.isNaN(minPosition[i])) throw new RuntimeException("minPosition[" + i + "] is NaN!");
			//if (Double.isInfinite(minPosition[i])) throw new RuntimeException("minPosition[" + i + "] is Infinite!");



			// Initialize using uniform distribution
			//System.out.println("hello"+ NetDatacenterBroker.executionCost[1]);
	//		exeCos = new double[NetDatacenterBroker.numberOfVms];
		//	for(int j = 0; j < NetDatacenterBroker.numberOfVms; j++)
			//{
				//exeCos[j] = (NetDatacenterBroker.workflowExecutionMI[i]/NetDatacenterBroker.mips[j])*NetDatacenterBroker.executionCost[j];
				//System.out.println("hehe "+exeCos[j]);
			//}
			
//			int minCostVM = minimum(exeCos);
			//System.out.println("lulu "+ minCostVM);
			position[i] = (xmax[i] - xmin[i]+1) * (Math.random()) + xmin[i];
			//System.out.println(position[i]);
			bestPosition[i] = Double.NaN;
		}
	}

	public int minimum(double [] exeCost)
	{
		int min = 0;
		for(int i = 1; i < exeCost.length; i++)
		{
			if (exeCost[i] < exeCost[min])
			{
				min = i;
			}
		}
		return min;
	}
	/**
	 * Create a new instance of this particle 
	 * @return A new particle, just like this one
	 */
	
	public Object selfFactory() {
		Class cl = this.getClass();
		Constructor cons;

		try {
			cons = cl.getConstructor((Class[]) null);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}

		try {
			return cons.newInstance((Object[]) null);
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException(e1);
		} catch (InstantiationException e1) {
			throw new RuntimeException(e1);
		} catch (IllegalAccessException e1) {
			throw new RuntimeException(e1);
		} catch (InvocationTargetException e1) {
			throw new RuntimeException(e1);
		}
	}

	public void setBestFitness(double bestFitness) {
		this.bestFitness = bestFitness;
	}
	/**
	 * Set fitness and best fitness accordingly.
	 * If it's the best fitness so far, copy data to bestFitness[]
	 * @param fitness : New fitness value
	 * @param maximize : Are we maximizing or minimizing fitness function?
	 */
	public void setFitness(double fitness, boolean maximize) {
		this.currentFitness = fitness;
		if ((maximize && (fitness > bestFitness)) // Maximize and bigger? => store data
				|| (!maximize && (fitness < bestFitness)) // Minimize and smaller? => store data too
				|| Double.isNaN(bestFitness)) {
			copyPosition2Best();
			bestFitness = fitness;
		}
	}

}
