package org.cloudbus.cloudsim.network.datacenter;

//import net.sourceforge.jswarm_pso.Particle;
import Tlbo.*;

public class TLBOPArticle extends Particle 
{
	public TLBOPArticle(int d)
	{
		super(d);
		System.out.println("d:" + d);
	}
	
	public TLBOPArticle()
	{
		super(10);    //need to change the number of tasks here
	}
}
