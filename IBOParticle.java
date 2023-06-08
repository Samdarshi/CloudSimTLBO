package org.cloudbus.cloudsim.network.datacenter;

import IBO_pkg.*;

public class IBOParticle extends Particle 
{
	public IBOParticle(int d)
	{
		super(d);
		System.out.println("d:" + d);
	}
	
	public IBOParticle()
	{
		super(10);    //need to change the number of tasks here
	}
}
