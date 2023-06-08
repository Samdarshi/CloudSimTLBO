package Tlbo;



//import eam.Particle;
//import eam.ParticleUpdate;
//import eam.TLBOPopulation;

/**
* Particle update strategy
* 
* Every Swarm.evolve() itereation the following methods are called
* 		- begin(Swarm) : Once at the begining of each iteration
* 		- update(Swarm,Particle) : Once for each particle
* 		- end(Swarm) : Once at the end of each iteration
* 
* @author Pablo Cingolani <pcingola@users.sourceforge.net>
*/
public class ParticleUpdateSimple extends ParticleUpdate {

	/** Random vector for local update */
	double rand1[];
	/** Random vector for global update */
	double rand2[];
	/** Random vector for neighborhood update */
	//double rneighborhood[];

	/**
	 * Constructor 
	 * @param particle : Sample of particles that will be updated later
	 */
	public ParticleUpdateSimple(Particle particle) {
		super(particle);
		rand1 = new double[particle.getDimension()];
		rand2 = new double[particle.getDimension()];
		//rneighborhood = new double[particle.getDimension()];
		System.out.println("rand111="+rand1+"rand222="+rand2);
	}

	/** 
	 * This method is called at the begining of each iteration
	 * Initialize random vectors use for local and global updates (rlocal[] and rother[])
	 */
	@Override
	public void begin(TLBOPopulation pop) {
		int i, dim = pop.getSampleParticle().getDimension();
		for (i = 0; i < dim; i++) {
			rand1[i] = Math.random();
			rand2[i] = Math.random();
			//rneighborhood[i] = Math.random();
			System.out.println("rand1="+rand1+"rand2="+rand2);
		}
	}

	/** This method is called at the end of each iteration */
	@Override
	public void end(TLBOPopulation pop) {
	}

	/** Update particle's velocity and position */
	@Override
	public void update(TLBOPopulation pop, Particle particle) {
		double position[] = particle.getPosition();
		@SuppressWarnings("unused")
		double globalBestPosition[] = pop.getBestPosition();
		@SuppressWarnings("unused")
		double particleBestPosition[] = particle.getBestPosition();
		//double neighBestPosition[] = swarm.getNeighborhoodBestPosition(particle);

		// Update velocity and position
		for (int i = 0; i < position.length; i++) {
			// Update position
					//	position[i] += velocity[i];
						//System.out.println("gulpul");
						// Update velocity
		//	velocity[i] = swarm.getInertia() * velocity[i] // Inertia
			//		+ rlocal[i] * swarm.getParticleIncrement() * (particleBestPosition[i] - position[i]) // Local best
					//+ rneighborhood[i] * swarm.getNeighborhoodIncrement() * (neighBestPosition[i] - position[i]) // Neighborhood best					
				//	+ rglobal[i] * swarm.getGlobalIncrement() * (globalBestPosition[i] - position[i]); // Global best
					}
	}
}

