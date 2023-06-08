package org.cloudbus.cloudsim.network.datacenter;
import Tlbo.*;
//import net.sourceforge.jswarm_pso.FitnessFunction;
import eam.FitnessFunction;
public class TLBOMyFItness extends FitnessFunction{
	public TLBOMyFItness(double[][] td, double[] et, double[][] vd, double[][] vt){
		super(false);
		workFlowDataTrans = td;
		workFlowTaskExcution = et;
		vmData = vd;
		vmTransferCost = vt;
	}
	
	public double evaluate(double position[]) {
		double fitnessValue = 0;
		int[] intPosition = new int[position.length];
		for(int i = 0; i < position.length; i++ )
		{
			intPosition[i] = (int)position[i]; //convert position into integer
			//System.out.print(" value is "+position[i]);
		}
		
		double[] vmCost = new double[vmData.length];
		double taskCost=0;
		// calculate each task's total cost, and add to the vm's cost the task assigned to
		for(int i = 0; i < position.length; i++ )
		{
			int vmNum = intPosition[i];
			System.out.print(intPosition[i]+"+");

			 taskCost+= workFlowTaskExcution[i] / vmData[vmNum][0] * vmData[vmNum][1]; //add execution cost
		
			
			//System.out.println("workFlowTaskExcution[i]: " + workFlowTaskExcution[i]);
			//System.out.println("vmData[vmNum][0]" + vmData[vmNum][0]);
			//System.out.println("vmData[vmNum][1]" + vmData[vmNum][1]);
			double taskDataTransferCost = 0;
			for(int j = 0; j < workFlowDataTrans[i].length; j++)
			{
				if(workFlowDataTrans[i][j] != 0)
				{
					taskDataTransferCost += workFlowDataTrans[i][j]* vmTransferCost[vmNum][intPosition[j]]; //add the task output file transfer cost
					//System.out.println("Yaahooo"+taskDataTransferCost);
				}
			}
			//System.out.print("taskCost: pran " + taskCost);
			//System.out.println("taskDataTransferCost: " + taskDataTransferCost);
			vmCost[vmNum] = taskDataTransferCost + taskCost;
			//System.out.println("vmCost[vmNum]: " + vmCost[vmNum]);
		}
		//fitnessValue = 0;
		//for(int i = 0; i < vmCost.length; i++)
			//fitnessValue += vmCost[i];
		fitnessValue = max(vmCost);
		System.out.println("fitnessValue as max of vmCost: " + fitnessValue);
		return fitnessValue;
	}
	
	/**
	 * get max value in a array
	 * @param list input array
	 * @return the max vlaue
	 */
	private double max(double[] list)
	{
		double max = 0;
		for(int i = 0; i < list.length; i++ )
		{
			if (list[i] > max)
			{
				max = list[i];
			}	
		}
		return max;
	}
	
	private double min(double[] list)
	{
		double min=10;
		for(int i=0; i< list.length; i++)
		{
			if(list[i]<min)
				min=list[i];
		}
		return min;
	}
	
	private double[][] workFlowDataTrans;
	private double[] workFlowTaskExcution;
	private double[][] vmData;
	private double[][] vmTransferCost;
}
