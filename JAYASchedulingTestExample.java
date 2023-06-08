package org.cloudbus.cloudsim.examples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.jswarm_pso.drawGraph;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.network.datacenter.JAYAEdgeSwitch;
import org.cloudbus.cloudsim.network.datacenter.JAYANetworkDatacenter;
import org.cloudbus.cloudsim.network.datacenter.JAYANetworkVmAllocationPolicy;
import org.cloudbus.cloudsim.network.datacenter.LeaderSwitch;
import org.cloudbus.cloudsim.network.datacenter.JAYANetdatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkCloudlet;
import org.cloudbus.cloudsim.network.datacenter.EdgeSwitch;
import org.cloudbus.cloudsim.network.datacenter.JAYANetdatacenterBroker;
import org.cloudbus.cloudsim.network.datacenter.NetworkConstants;
import org.cloudbus.cloudsim.network.datacenter.NetworkDatacenter;
import org.cloudbus.cloudsim.network.datacenter.JAYANetworkHost;
import org.cloudbus.cloudsim.network.datacenter.NetworkVm;
import org.cloudbus.cloudsim.network.datacenter.JAYANetworkHost;
import org.cloudbus.cloudsim.network.datacenter.TLBONetworkVmAllocationPolicy;
import org.cloudbus.cloudsim.network.datacenter.ExampleWorkflow;
import org.cloudbus.cloudsim.network.datacenter.NetworkCloudletSpaceSharedScheduler;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 * A demo showing how to schedule workflow using PSO algorithm.
 */

public class JAYASchedulingTestExample {

	/**
	 * Creates main() to run this example
	 */
	private static List<NetworkVm> vmlist;
	public static double bandwidth[];
	public static void main(String[] args) {
		
		Log.printLine("satya deo @2015rcs11 ");
		Log.printLine("Starting IEAM scheduing...");

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
			JAYANetworkDatacenter datacenter0 = createDatacenter("Datacenter_0");

			// Third step: Create Broker
			JAYANetdatacenterBroker broker = createBroker();
			broker.setLinkDC(datacenter0);

			// Fourth step: Starts the simulation
			
			CloudSim.startSimulation();

			/*
			 * // Final step: Print results when simulation is over
			 * List<Cloudlet> newList = broker.getCloudletReceivedList();
			 */
			CloudSim.stopSimulation();
			
			//get the list of cloudlets
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			printCloudletList(newList);
			System.out.println("numberofcloudlet " + (newList.size()) +  " Data transfered "
					+ NetworkConstants.totaldatatransfer);
			// Print the debt of each user to each datacenter
			//datacenter0.printDebts();
			//PrintStream out=new PrintStream(new FileOutputStream("output.txt"));
			//System.setOut(out);
			
			//System.out.println("Total execution cost: " + JAYANetdatacenterBroker.totalExeCost);
			//System.out.println("Total transfer cost:" + JAYANetdatacenterBroker.totalTransferCost);
			JAYANetdatacenterBroker.totalCost = JAYANetdatacenterBroker.totalExeCost + JAYANetdatacenterBroker.totalTransferCost;
			System.out.println("Total cost: " + JAYANetdatacenterBroker.totalCost);
			Log.printLine("CloudSim simulation over!");
			Log.printLine("Graph Plotted");
			List <Double> l=new ArrayList<Double>();
			l.add(JAYANetdatacenterBroker.totalExeCost);
			//drawGraph g=new drawGraph(l);
			//g.createAndShowGui();
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}

	private static JAYANetworkDatacenter createDatacenter(String name) {

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store
		// our machine
		List<JAYANetworkHost> hostList = new ArrayList<JAYANetworkHost>();

		// 2. A Machine contains one or more PEs or CPUs/Cores.
		// In this example, it will have only one core.
		List<Pe> peList = new ArrayList<Pe>();

		int mips = 1000;

		// 3. Create PEs and add these into a list.
		peList.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store
																// Pe id and
																// MIPS Rating
		//peList.add(new Pe(1, new PeProvisionerSimple(mips)));
		// 4. Create Host with its id and list of PEs and add them to the list
		// of machines
		int ram = 2048; // host memory (MB)
		long storage = 1000000; // host storage
		int bw = 10000;
		int hostNumber = 4;

		for (int index = 0; index < hostNumber; index++) {
			hostList.add(new JAYANetworkHost(index, new RamProvisionerSimple(ram),
					new BwProvisionerSimple(bw), storage, peList,
					new VmSchedulerTimeShared(peList))); // This is our machine
		}
		
		System.out.println("HOst created");
		// This is our machine

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
		LinkedList<Storage> storageList = new LinkedList<Storage>(); 

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
				arch, os, vmm, hostList, time_zone, cost, costPerMem,
				costPerStorage, costPerBw);

		// 6. Finally, we need to create a PowerDatacenter object.
		JAYANetworkDatacenter datacenter = null;
		try {
			datacenter = new JAYANetworkDatacenter(name, characteristics,
					new JAYANetworkVmAllocationPolicy(hostList), storageList, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Create Internal Datacenter network
		CreateNetwork(hostNumber, datacenter);
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
	private static JAYANetdatacenterBroker createBroker() {
		JAYANetdatacenterBroker broker = null;
		try {
			broker = new JAYANetdatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects
	 * 
	 * @param list
	 *            list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
				+ "Data center ID" + indent + "VM ID" + indent + "Time"
				+ indent + "Start Time" + indent + "Finish Time");

		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
				Log.print("SUCCESS");

				DecimalFormat dft = new DecimalFormat("###.##");
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

	static void CreateNetwork(int numhost, JAYANetworkDatacenter dc) {

		// Edge Switch
		JAYAEdgeSwitch edgeswitch[] = new JAYAEdgeSwitch[1];

		for (int i = 0; i < 1; i++) {
			edgeswitch[i] = new JAYAEdgeSwitch("Edge" + i,
					NetworkConstants.EDGE_LEVEL, dc);
			// edgeswitch[i].uplinkswitches.add(null);
			dc.Switchlist.put(edgeswitch[i].getId(), edgeswitch[i]);
			// aggswitch[(int)
			// (i/Constants.AggSwitchPort)].downlinkswitches.add(edgeswitch[i]);
		}

		double bw[] = {80, 90, 100, 70};//, 60};
		bandwidth=new double[numhost];
		for(int i=0; i<bandwidth.length;i++)
			bandwidth[i]=bw[i%4];
		int i = 0;
		for (Host hs : dc.getHostList()) {
			JAYANetworkHost hs1 = (JAYANetworkHost) hs;
			hs1.bandwidth = bandwidth[i++] * 1024 * 1024;
			int switchnum = (int) (hs.getId() / numhost);//NetworkConstants.EdgeSwitchPort);
			edgeswitch[switchnum].hostlist1.put(hs.getId(), hs1);
			dc.HostToSwitchid.put(hs.getId(), edgeswitch[switchnum].getId());
			hs1.sw = edgeswitch[switchnum];
			List<JAYANetworkHost> hslist = hs1.sw.fintimelistHost1.get(0D);
			if (hslist == null) {
				hslist = new ArrayList<JAYANetworkHost>();
				hs1.sw.fintimelistHost1.put(0D, hslist);
			}
			hslist.add(hs1);

		}

	}
}
