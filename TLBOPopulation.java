package Tlbo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.cloudbus.cloudsim.network.datacenter.TLBOMyFItness;
//import net.sourceforge.*;
//import net.sourceforge.jswarm_pso.Particle;
//import net.sourceforge.jswarm_pso.Particle;
/**
 * A swarm of particles
 * @author Pablo Cingolani <pcingola@users.sourceforge.net>
 */
public class TLBOPopulation implements Iterable<Particle> {

	public static int DEFAULT_NUMBER_OF_PARTICLES = 25;
	
	/** Best fitness so far (global best) */
	double bestFitness;
	
	/** Index of best particle so far */
	//int bestParticleIndex;
	
	/** Best position so far (global best) */
	double bestPosition[];

	double fin1[][];
	
	double finMerge[][];
	/** A sample particles: Build other particles based on this one */
	Particle sampleParticle;

	/** Fitness function for this swarm */
	TLBOMyFItness fitnessFunction;
	
	/** Maximum position (for each dimension) */
	double xmax[];
	
	/** Minimum position (for each dimension) */
	double xmin[];
	
	/** How many times 'particle.evaluate()' has been called? */
	int numberOfEvaluations;
	
	/** Number of particles in this swarm */
	int numberOfParticles;
	
	/** Particles in this population */
	Particle particles[];
	
	/** Particle update strategy */
	ParticleUpdate particleUpdate;
	
	
	/** Variables update */
	//VariablesUpdate variableseUpdate;
	
	/** A collection used for 'Iterable' interface */
	ArrayList<Particle> particlesList;
	
	int Dim;
	
	double fin[][];

	//-------------------------------------------------------------------------
	// Constructors
	//-------------------------------------------------------------------------

	/**
	 * Create a Swarm and set default values
	 * @param numberOfParticles : Number of particles in this swarm (should be greater than 0). 
	 * If unsure about this parameter, try Swarm.DEFAULT_NUMBER_OF_PARTICLES or greater
	 * @param sampleParticle : A particle that is a sample to build all other particles
	 * @param fitnessFunction : Fitness function used to evaluate each particle
	 */
	public TLBOPopulation(int numberOfParticles, Particle sampleParticle, TLBOMyFItness fitnessFunction, int dim) {
		if (sampleParticle == null) throw new RuntimeException("Sample particle can't be null!");
		if (numberOfParticles <= 0) throw new RuntimeException("Number of particles should be greater than zero.");

		
		numberOfEvaluations = 0;
		this.numberOfParticles = numberOfParticles;
		this.sampleParticle = sampleParticle;
		this.fitnessFunction = fitnessFunction;
		this.Dim = dim;
		bestFitness = Double.NaN;

		// Set up particle update strategy (default: ParticleUpdateSimple) 
		particleUpdate = new ParticleUpdateSimple(sampleParticle);

		// Set up variablesUpdate strategy (default: VariablesUpdate)
		//variablesUpdate = new VariablesUpdate();

		//neighborhood = null;
		//neighborhoodIncrement = 0.0;
		particlesList = null;
	}

	//-------------------------------------------------------------------------
	// Methods
	//-------------------------------------------------------------------------

	/**
	 * Evaluate fitness function for every particle 
	 * Warning: particles[] must be initialized and fitnessFunction must be set
	 */
	
	
	
	public void evaluate1() {
		if (particles == null) throw new RuntimeException("No particles in this swarm! May be you need to call Swarm.init() method");
		if (fitnessFunction == null) throw new RuntimeException("No fitness function in this swarm! May be you need to call Swarm.setFitnessFunction() method");
		// Initialize
		if (Double.isNaN(bestFitness)) {
			bestFitness = (fitnessFunction.isMaximize() ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
		}

		//---
		// Evaluate each particle (and find the 'best' one)
		//---
		fin = new double[numberOfParticles][Dim+1];
		int fitnessIndex = Dim;
		for (int i = 0; i < particles.length; i++) {
			// Evaluate particle
			double fit = fitnessFunction.evaluate(particles[i].getPosition());
			//System.out.println("hellwell");
			particles[i].setFitness(fit, false);
			//System.out.println("set");
			numberOfEvaluations++; // Update counter
			/*
			 * fill out the fin matrix of size #of particles * {post, fitnessValue}
			 */
			double [] position = new double[particles[i].getPosition().length];
			position = particles[i].getPosition(); //working
			//for(int l = 0; l< position.length; l++)
				//System.out.print(position[l]+ "ahem");
			int[] intPosition = new int[position.length];
			
			for(int k = 0; k < position.length; k++ )
			{
				intPosition[k] = (int)position[k]; //convert position into integer
				//System.out.println(intPosition[k]+ " helllll");
				fin[i][k] = intPosition[k];
				//System.out.print(" value is "+position[i]);
			}
			fin[i][fitnessIndex] = fit;
			
			
			// Update 'best global' position
			if (fitnessFunction.isBetterThan(bestFitness, fit)) {
				bestFitness = fit; // Copy best fitness, index, and position vector
				
			}

		}
		
		
		//sort fin matrix based on fitness value
		Arrays.sort(fin, new Comparator<double[]>(){
			public int compare(double[] s1, double[] s2){
				if(s1[10]<s2[10])
					return 1;
				else if(s1[10]>s2[10])
					return -1;
				else
					return 0;
			}
		});
	
		
		
	}

	
	
	public void evaluate() {
		if (particles == null) throw new RuntimeException("No particles in this swarm! May be you need to call Swarm.init() method");
		if (fitnessFunction == null) throw new RuntimeException("No fitness function in this swarm! May be you need to call Swarm.setFitnessFunction() method");
		// Initialize
		if (Double.isNaN(bestFitness)) {
			bestFitness = (fitnessFunction.isMaximize() ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
		}

		/* Adaptation starts here */
		//---
		// Evaluate each particle (and find the 'best' one)
		//---
		double fitad[] = new double[numberOfParticles];
		double sum =0;
		double favg = 0;
	
		for (int i = 0; i < particles.length; i++) {
			fitad[i] = particles[i].getFitness();	
		}
		//take mean of all fitness values
		for(int i =0; i< fitad.length; i++)
		{
			sum += fitad[i];
		}
		//System.out.println("sum: "+sum);
		favg = sum/fitad.length;
		//System.out.println("favg:"+fitad.length);
		int ind = 0;
		while(fitad[ind]<favg)
		{
			ind++;
		}
		//now calculate new positions
		
		
		
		
		for (int i = 0; i < particles.length; i++) {
			//calcualte c
			
		double c = particles[i].getFitness() / favg; //individual population fitness / avg fitness of whole population
			//System.out.println("hello "+ c);
			//calculate mb
		int Dim = particles[i].getDimension();
		double mb[]= new double [Dim];
		for(int j =0; j< Dim; j++)
		{
			mb[j] = particles[0].position[j] - particles[numberOfParticles-1].position[j];
		}
		//clamping
		if(i == 0)
		{
			//System.out.println("ahem" + particles[i].getFitness());
		for(int j = 0; j<Dim; j++)
			{	
			particles[i].position[j] = (c*particles[i].position[j] + Math.random());
		if(particles[i].position[j] < 0)
		{
			//System.out.println("ahem");
			particles[i].position[j] = 0;//(7 - 0+1) * (Math.random()) + 0;
		}
		else if(particles[i].position[j] > 7)
			particles[i].position[j] = 7;//(7 - 0+1) * (Math.random()) + 0;//7;
		}
		}
		else
		{
		
			for(int j = 0; j<Dim; j++)
			{
				particles[i].position[j] = particles[i].position[j] + mb[j] * Math.random();
				if(particles[i].position[j] < 0)
				{
					//System.out.println("ahem");
					particles[i].position[j] = 0;//(7 - 0+1) * (Math.random()) + 0;//0;
				}
				else if(particles[i].position[j] > 7)
					particles[i].position[j] = 7;//(7 - 0+1) * (Math.random()) + 0;//7;
				
			
			}
		
		
		}
		
		
		/*
		// Evaluate particle
			double fit = fitnessFunction.evaluate(particles[i]);
			//System.out.println("hellwell");

			numberOfEvaluations++; // Update counter

			// Update 'best global' position
			if (fitnessFunction.isBetterThan(bestFitness, fit)) {
				bestFitness = fit; // Copy best fitness, index, and position vector
				
			}
		*/
		}
	
		}
	
	
	/* Selection method starts here */
	
	public void selection()
	{
		fin1 = new double[numberOfParticles][Dim+1];
		finMerge = new double[numberOfParticles*2][Dim+1];
		
		for(int i =0; i<numberOfParticles; i++)
		{
			for(int j = 0; j<Dim; j++)
			{
				//System.out.println("hello");
				fin1[i][j] = particles[i].position[j];
				//System.out.println("hello");
				finMerge[i][j] = fin1[i][j];
			}
		}
		
		
		for(int i =0; i<numberOfParticles; i++)
		{
			double fit = fitnessFunction.evaluate(particles[i].getPosition());
			particles[i].setFitness(fit, false);
			fin1[i][Dim] = fit;
			finMerge[i][Dim] = fit;
		}
		
		//merge fin and fin1 to finMerge
		for(int i = numberOfParticles,j = 0; i< (2*numberOfParticles);j++,i++)
		{
			for(int k = 0; k<Dim+1; k++ )
			{
				finMerge[i][k]= fin[j][k];
			}
		}
		
		
		//copy fin1 to fin
		for(int i =0; i<numberOfParticles; i++)
		{
			for(int j = 0; j<Dim+1; j++)
			{
				fin[i][j] = fin1[i][j];
			}
		}
		
		//sort finMerge
		Arrays.sort(finMerge, new Comparator<double[]>(){
			public int compare(double[] s1, double[] s2){
				if(s1[Dim]>s2[Dim])
					return 1;
				else if(s1[Dim]<s2[Dim])
					return -1;
				else
					return 0;
			}
		});
	
		//select top particles based on fitness value
		for(int i =0; i<numberOfParticles; i++)
		{
			for(int j = 0; j<Dim; j++)
			particles[i].position[j]= finMerge[i][j];
		}
		
		
	}
	
	
	
	
	/**
	 * Make an iteration: 
	 * 	- evaluates the swarm 
	 * 	- updates positions and velocities
	 * 	- applies positions and velocities constraints 
	 */
	//variables to be used in eam
	int flag=0;
	public int count =2;
	public int rj =0;
	public int f2 = 0;
	public int c1 = 0;
	public int d1 = 0;
	public void evolve() {
		// Initialize (if not already done) and is called for the first time
		if (particles == null){
			//System.out.println("ahem");
			init(); //define initial positions and velocity for every particle
			evaluate1();
		}
		else
		{
		evaluate(); // Evaluate particles
		//update(); // Update positions and velocities
		selection();
		}
		//variablesUpdate.update(this);
	}

	public double getBestFitness() {
		return bestFitness;
	}

	//public Particle getBestParticle() {
	//	return particles[bestParticleIndex];
	//}

	//public int getBestParticleIndex() {
	//	return bestParticleIndex;
	//}

	public double[] getBestPosition() {
		return particles[0].position;
	}

	public TLBOMyFItness getFitnessFunction() {
		return fitnessFunction;
	}

	

	public double[] getxmax() {
		return xmax;
	}

	public double[] getxmin(){
		return xmin;
	}

	
	public int getNumberOfEvaluations() {
		return numberOfEvaluations;
	}

	public int getNumberOfParticles() {
		return numberOfParticles;
	}

	public Particle getParticle(int i) {
		return particles[i];
	}

	public Particle[] getParticles() {
		return particles;
	}

	//public ParticleUpdate getParticleUpdate() {
		//return particleUpdate;
	//}

	public Particle getSampleParticle() {
		return sampleParticle;
	}
	
	
	//public VariablesUpdate getVariablesUpdate() {
		//return variablesUpdate;
	//}

	/**
	 * Initialize every particle
	 * Warning: maxPosition[], minPosition[], maxVelocity[], minVelocity[] must be initialized and setted
	 */
	
	public void init() {
		// Init particles
		particles = new Particle[numberOfParticles];
		
		xmin = new double[Dim];
		xmax = new double[Dim];
		
		for(int i = 0; i < Dim; i++)
		{
			xmax[i] = 7;
			xmin[i] = 0;
		}
		
		
		//System.out.println("gul");
		// Check constraints (they will be used to initialize particles)
		if (xmax == null) throw new RuntimeException("maxPosition array is null!");
		if (xmin == null) throw new RuntimeException("maxPosition array is null!");
		
		// Init each particle
		for (int i = 0; i < numberOfParticles; i++) {
			particles[i] = (Particle) sampleParticle.selfFactory(); // Create a new particles (using 'sampleParticle' as reference)
			
			particles[i].init(xmax, xmin); // Initialize it
		}
	}

	/**
	 * Iterate over all particles
	 */
	public Iterator<Particle> iterator() {
		if (particlesList == null) {
			particlesList = new ArrayList<Particle>(particles.length);
			for (int i = 0; i < particles.length; i++)
				particlesList.add(particles[i]);
		}

		return particlesList.iterator();
	}
/*
	public void setBestParticleIndex(int bestParticle) {
		bestParticleIndex = bestParticle;
	}
*/
	public void setBestPosition(double[] bestPosition) {
		this.bestPosition = bestPosition;
	}

	public void setFitnessFunction(TLBOMyFItness fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}


	/**
	 * Sets every maxPosition[] to 'maxPosition'
	 * @param maxPosition
	 */
	/*public void setMaxPosition(double xmax) {
		if (sampleParticle == null) throw new RuntimeException("Need to set sample particle before calling this method (use Swarm.setSampleParticle() method)");
		int dim = sampleParticle.getDimension();
		this.xmax = new double[dim];
		for (int i = 0; i < dim; i++)
			this.xmax[i] = xmax;
	}
*/
	public void setMaxPosition(double[] xmax) {
		this.xmax = xmax;
	}

	

	/**
	 * Sets every minPosition[] to 'minPosition'
	 * @param minPosition
	 */
	/*public void setMinPosition(double minPosition) {
		if (sampleParticle == null) throw new RuntimeException("Need to set sample particle before calling this method (use Swarm.setSampleParticle() method)");
		int dim = sampleParticle.getDimension();
		this.xmin = new double[dim];
		for (int i = 0; i < dim; i++)
			this.xmin[i] = xmin;
	}
*/
	public void setMinPosition(double[] xmin) {
		this.xmin = xmin;
	}

	
	public void setNumberOfEvaluations(int numberOfEvaliations) {
		this.numberOfEvaluations = numberOfEvaliations;
	}

	public void setNumberOfParticles(int numberOfParticles) {
		this.numberOfParticles = numberOfParticles;
	}

	public void setParticles(Particle[] particle) {
		particles = particle;
		particlesList = null;
	}




	/**
	 * Show a swarm in a graph 
	 * @param graphics : Grapics object
	 * @param foreground : foreground color
	 * @param width : graphic's width
	 * @param height : graphic's height
	 * @param dim0 : Dimention to show ('x' axis)
	 * @param dim1 : Dimention to show ('y' axis)
	 * @param showVelocity : Show velocity tails?
	 */
	
	/** Swarm size (number of particles) */
	public int size() {
		return particles.length;
	}
	
	/** Printable string */
	/*@Override
	public String toString() {
		String str = "";

		if (particles != null) str += "Swarm size: " + particles.length + "\n";

		if ((minPosition != null) && (maxPosition != null)) {
			str += "Position ranges:\t";
			for (int i = 0; i < maxPosition.length; i++)
				str += "[" + minPosition[i] + ", " + maxPosition[i] + "]\t";
		}

		if ((minVelocity != null) && (maxVelocity != null)) {
			str += "\nVelocity ranges:\t";
			for (int i = 0; i < maxVelocity.length; i++)
				str += "[" + minVelocity[i] + ", " + maxVelocity[i] + "]\t";
		}

		if (sampleParticle != null) str += "\nSample particle: " + sampleParticle;

		if (particles != null) {
			str += "\nParticles:";
			for (int i = 0; i < particles.length; i++) {
				str += "\n\tParticle: " + i + "\t";
				str += particles[i].toString();
			}
		}
		str += "\n";

		return str;
	}
*/
	/**
	 * Return a string with some (very basic) statistics 
	 * @return A string
	 */
	/*
	public String toStringStats() {
		String stats = "";
		if (!Double.isNaN(bestFitness)) {
			stats += "Best fitness: " + bestFitness + "\nBest position: \t[";
			for (int i = 0; i < bestPosition.length; i++)
				stats += bestPosition[i] + (i < (bestPosition.length - 1) ? ", " : "");
			stats += "]\nNumber of evaluations: " + numberOfEvaliations + "\n";
		}
		return stats;
	}
*/
	/**
	 * Update every particle's position and velocity, also apply position and velocity constraints (if any)
	 * Warning: Particles must be already evaluated
	 */
/*	public void update() {
		// Initialize a particle update iteration
		particleUpdate.begin(this);

		// For each particle...
		for (int i = 0; i < particles.length; i++) {
			// Update particle's position and speed
			particleUpdate.update(this, particles[i]);

			// Apply position and velocity constraints
			particles[i].applyConstraints(xmin, xmax);
		}

		// Finish a particle update iteration
		particleUpdate.end(this);
	}
	*/
}
