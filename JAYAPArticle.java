package org.cloudbus.cloudsim.network.datacenter;

//import net.sourceforge.jswarm_pso.Particle;
import JAYA_Schedule.*;

public class JAYAPArticle extends Particle 
{
	public JAYAPArticle(int d)
	{
		super(d);
		System.out.println("d:" + d);
	}
	
	public JAYAPArticle()
	{
		super(10);    //need to change the number of tasks here
	}
}
