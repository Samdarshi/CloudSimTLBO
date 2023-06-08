package org.cloudbus.cloudsim.network.datacenter;

//import net.sourceforge.jswarm_pso.Particle;
import JAYA_Sd.*;

public class MY_particle extends Particle 
{
	public MY_particle(int d)
	{
		super(d);
		System.out.println("d:" + d);
	}
	
	public MY_particle()
	{
		super(10);    //need to change the number of tasks here
	}
}
