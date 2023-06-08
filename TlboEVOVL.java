package org.cloudbus.cloudsim.network.datacenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import Tlbo.*;
//import net.sourceforge.jswarm_pso.Swarm;
//import net.sourceforge.jswarm_pso.example_2.SwarmShow2D;
import org.cloudbus.cloudsim.Datacenter;

import JAYA_Sd.Population;

public class TlboEVOVL {
	public TlboEVOVL(double[][] td, double[] et, Datacenter dc, int dim)
	{
		workFlowDataTrans = td; //workflowDataTransferMap
		workFlowTaskExecution = et;//workflowExecutionMI
		datacenter = dc;
		Dim = dim;
		setVmData();
	}
	
	
	public ArrayList<Integer> runTLBO()
	{
		// Create a swarm (using 'MyParticle' as sample particle and 'MyFitnessFunction' as fitness function)
		TLBOPopulation pop = new TLBOPopulation(20,//Swarm.DEFAULT_NUMBER_OF_PARTICLES, 
	               new TLBOPArticle(workFlowTaskExecution.length/*10*/), 
	               new TLBOMyFItness(workFlowDataTrans, workFlowTaskExecution, vmData, vmTransferCost),Dim);
		System.out.println("Population Created");
		//System.out.println("workFlowTaskExecution.length: " + workFlowTaskExecution.length);
		
		
		
		//System.out.println("length: "+vmData.length);
		//pop.setMaxPosition(vmData.length-1);//vmData.length);  //change
		//pop.setMinPosition(0);
		
		int numberOfIterations = 10;
		boolean showGraphics = false;
		
		
			// Optimize (and time it)
			for (int i = 0; i < numberOfIterations; i++)
			{
				//System.out.println("new");
				pop.evolve();
		
				//System.out.println("hello "+swarm.getBestFitness());
			}
		
		
		// transfer double to int
		double[] bestPosition = pop.getBestPosition();
		ArrayList<Integer> intBestPosition = new ArrayList<Integer>();
		for(int i = 0; i < bestPosition.length; i++ )
		{
			//length of bestPosition is currently 10
			intBestPosition.add((int)bestPosition[i]);
		}
		return intBestPosition;
	}
	
	
	
	/**
	 * Get VM information(MIPS, execution cost, bandwidth cost) into an array
	 */
	private void setVmData()
	{
	    List<NetworkVm> vmList = datacenter.getVmList();
	    int sizeOfVm = vmList.size();
	    System.out.println("sizeOfVm: " + sizeOfVm);
	    Iterator<NetworkVm> it = vmList.iterator();
	    int count = 0;
	    
	    //vmData is vm * {mips,executionCost}; shows mips and executionCost of each VM
	    vmData = new double[sizeOfVm][2];
	    
	    //vmTransferCost
	    vmTransferCost = new double[sizeOfVm][sizeOfVm];
	    
	    while(it.hasNext())
	    {
	    	NetworkVm tmp = it.next();
	    	vmData[count][0] = tmp.getMips();
	    	
	    	vmData[count][1] = tmp.getExecutionCost();
	    	ArrayList<Double> vmBandwidthCost = new ArrayList<Double>();
	    	vmBandwidthCost = tmp.getBandwidthCost();//transferCost per second
	    	//System.out.println("gul: "+ vmBandwidthCost);
	    	for (int i = 0; i < vmBandwidthCost.size(); i++)
	    	{
	    		
	    		vmTransferCost[count][i] = vmBandwidthCost.get(i);
	    		//System.out.println("count is "+count+ " and cost is "+vmTransferCost[count][i]);
	    	}
	    	count++;
	    }
	}
	
	
	
	private double[][] vmData;
	private double[][] vmTransferCost;
	private Datacenter datacenter;
	private double[][] workFlowDataTrans;
	private double[] workFlowTaskExecution;
	public int Dim;
}
