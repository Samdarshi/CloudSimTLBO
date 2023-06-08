package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.TLBO_Datacenter_Broker_;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class TaskScheduling_using_TLBO extends CloudSimExample1
{
	/** The cloudlet list. */
	private static List<Cloudlet> cloudletList;

	/** The vmlist. */
	private static List<Vm> vmlist;

	

	/**
	 * Creates main() to run this example.
	 *
	 * @param args the args
	 */
	
		
		public static void main(String[] args) {

			Log.printLine("Starting CloudSimExample1...");

			try {
				// First step: Initialize the CloudSim package. It should be called
				// before creating any entities.
				int num_user = 1; // number of cloud users
				Calendar calendar = Calendar.getInstance();
				boolean trace_flag = false; // mean trace events

				// Initialize the CloudSim library
				CloudSim.init(num_user, calendar, trace_flag);

				// Second step: Create Datacenters
				// Datacenters are the resource providers in CloudSim. We need at
				// list one of them to run a CloudSim simulation
				Datacenter datacenter0 = createDatacenter("Datacenter_0");

				// Third step: Create Broker
				TLBO_Datacenter_Broker_ broker= createBroker();
				int brokerId = broker.getId();

				// Fourth step: Create one virtual machine
				vmlist = new ArrayList<Vm>();

				// VM description
				int vmid = 0;
				int mips = 1000;
				long size = 10000; // image size (MB)
				int ram = 512; // vm memory (MB)
				long bw = 1000;
				int pesNumber = 1; // number of cpus
				String vmm = "Xen"; // VMM name
				Vm[]vms=new Vm[4];
				Random r1=new Random();
				int lowlimit=990;
				int highlimit=1000;
	            for(int i=0;i<4;i++)
	            {
	             // create VM
	            // System.out.println("kah ho raja kaisan bare");
				   vms[i] = new Vm(i, brokerId, lowlimit+r1.nextInt(highlimit-lowlimit), pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
				   vmlist.add(vms[i]);
	            }
				// add the VM to the vmList
				
	                   System.out.printf("satyaaaaaaaaaaaaaaa");
				// submit vm list to the broker
				broker.submitVmList(vmlist);

				 Cloudlet[]clou=new Cloudlet[5];
				// Fifth step: Create one Cloudletpsopsodatacenterbroker
				cloudletList = new ArrayList<Cloudlet>();

				// Cloudlet properties
				int id = 0;
				long length = 10000;
				long fileSize = 300;
				long outputSize = 300;
				Random r2=new Random();
				UtilizationModel utilizationModel = new UtilizationModelFull();
				long lowerLimit = 9900L;
				long upperLimit = 10000L;
				for(int i=0;i<5;i++)
		         {
					 long number = lowerLimit+((long)(r2.nextDouble()*(upperLimit-lowerLimit)));
		            clou[i] = new Cloudlet(i,number, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
					clou[i].setUserId(brokerId);
					//clou[i].setVmId(vmid);
					cloudletList.add(clou[i]);
					System.out.println("i="+i+"length="+length);
				 }
					// add the cloudlet to the list
					
		          //broker.run();
					// submit cloudlet list to the broker
					broker.submitCloudletList(cloudletList);

				//broker.submitCloudletList(cloudletList);

				// Sixth step: Starts the simulation
				CloudSim.startSimulation();

				CloudSim.stopSimulation();

				//Final step: Print results when simulation is over
				List<Cloudlet> newList = broker.getCloudletReceivedList();
				printCloudletList(newList);

				for(int p=0;p<vmlist.size();p++)
		        {
		        	
		        	Log.printLine("makespan of Vm "+vmlist.get(p).getId()+"  "+makespan(newList,vmlist.get(p).getId()));
		        }
				
				
				// Print the debt of each user to each datacenter
				datacenter0.printDebts();

				Log.printLine("CloudSimExample1 finished!");
			} catch (Exception e) {
				e.printStackTrace();
				Log.printLine("Unwanted errors happen");
			}
		}

		private static double makespan(List<Cloudlet> newList, int id) 
		{
			double makespan=0;
			for(int i=0;i<newList.size();i++)
			{
			if(newList.get(i).getVmId()==id)
			
				if(newList.get(i).getFinishTime()>makespan)
					makespan=newList.get(i).getFinishTime();
			
			}
			return makespan;
		  
	    }

		/**
		 * Creates the datacenter.
		 *
		 * @param name the name
		 *
		 * @return the datacenter
		 */
		private static Datacenter createDatacenter(String name) {

			// Here are the steps needed to create a PowerDatacenter:
			// 1. We need to create a list to store
			// our machine
			List<Host> hostList = new ArrayList<Host>();

			// 2. A Machine contains one or more PEs or CPUs/Cores.
			// In this example, it will have only one core.
			List<Pe> peList = new ArrayList<Pe>();

			int mips = 4000;

			// 3. Create PEs and add these into a list.
			peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating

			// 4. Create Host with its id and list of PEs and add them to the list
			// of machines
			int hostId = 0;
			int ram = 2048; // host memory (MB)
			long storage = 1000000; // host storage
			int bw = 10000;

			hostList.add(
				new Host(
					hostId,
					new RamProvisionerSimple(ram),
					new BwProvisionerSimple(bw),
					storage,
					peList,
					new VmSchedulerTimeShared(peList)
				)
			); // This is our machine

			// 5. Create a DatacenterCharacteristics object that stores the
			// properties of a data center: architecture, OS, list of
			// Machines, allocation policy: time- or space-shared, time zone
			// and its price (G$/Pe time unit).
			String arch = "x86"; // system architecture
			String os = "Linux"; // operating system
			String vmm = "Xen";
			double time_zone = 10.0; // time zone this resource located
			double cost = 3.0; // the cost of using processing in this resource
			double costPerMem = 0.05; // the cost of using memory in this resource
			double costPerStorage = 0.001; // the cost of using storage in this
											// resource
			double costPerBw = 0.0; // the cost of using bw in this resource
			LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
														// devices by now

			DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
					arch, os, vmm, hostList, time_zone, cost, costPerMem,
					costPerStorage, costPerBw);

			// 6. Finally, we need to create a PowerDatacenter object.
			Datacenter datacenter = null;
			try {
				datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return datacenter;
		}

		// We strongly encourage users to develop their own broker policies, to
		// submit vms and cloudlets according
		// to the specific rules of the simulated scenario
		/**
		 * Creates the broker.
		 *
		 * @return the datacenter broker
		 */
		private static TLBO_Datacenter_Broker_ createBroker() 
		{

			TLBO_Datacenter_Broker_ broker = null;
			try {
				broker = new TLBO_Datacenter_Broker_("Broker");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return  broker;
		
		}

		/**
		 * Prints the Cloudlet objects.
		 *
		 * @param list list of Cloudlets
		 */
		private static void printCloudletList(List<Cloudlet> list) {
			int size = list.size();
			Cloudlet cloudlet;

			String indent = "    ";
			Log.printLine();
			Log.printLine("========== OUTPUT ==========");
			Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
					+ "Data center ID" + indent + "VM ID" + indent + "Time" + indent
					+ "Start Time" + indent + "Finish Time");

			DecimalFormat dft = new DecimalFormat("###.##");
			for (int i = 0; i < size; i++) {
				cloudlet = list.get(i);
				Log.print(indent + cloudlet.getCloudletId() + indent + indent);

				if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
					Log.print("SUCCESS");

					Log.printLine(indent + indent + cloudlet.getResourceId()
							+ indent + indent + indent + cloudlet.getVmId()
							+ indent + indent
							+ dft.format(cloudlet.getActualCPUTime()) + indent
							+ indent + dft.format(cloudlet.getExecStartTime())
							+ indent + indent
							+ dft.format(cloudlet.getFinishTime()));
				}
			}
		}



	}
