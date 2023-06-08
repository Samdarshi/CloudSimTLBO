/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.network.datacenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.predicates.PredicateType;

/**
 * This class allows to simulate Edge JAYASwitch for Datacenter network. It interacts with other
 * switches in order to exchange packets.
 * 
 * Please refer to following publication for more details:
 * 
 * Saurabh Kumar Garg and Rajkumar Buyya, NetworkCloudSim: Modelling Parallel Applications in Cloud
 * Simulations, Proceedings of the 4th IEEE/ACM International Conference on Utility and Cloud
 * Computing (UCC 2011, IEEE CS Press, USA), Melbourne, Australia, December 5-7, 2011.
 * 
 * @author Saurabh Kumar Garg
 * @since CloudSim Toolkit 3.0
 */
public class JAYAEdgeSwitch extends JAYASwitch {


	/**
	 * Constructor for Edge JAYASwitch We have to specify switches that are connected to its downlink
	 * and uplink ports, and corresponding bandwidths. In this JAYASwitch downlink ports are connected
	 * to hosts not to a JAYASwitch.
	 * 
	 * @param name Name of the JAYASwitch
	 * @param level At which level JAYASwitch is with respect to hosts.
	 * @param dc Pointer to Datacenter
	 */
	public JAYAEdgeSwitch(String name, int level, JAYANetworkDatacenter dc) {
		super(name, level, dc);
		hostlist1 = new HashMap<Integer, JAYANetworkHost>();
		uplinkswitchpktlist = new HashMap<Integer, List<NetworkPacket>>();
		packetTohost = new HashMap<Integer, List<NetworkPacket>>();
		uplinkbandwidth = NetworkConstants.BandWidthEdgeAgg;
		downlinkbandwidth = NetworkConstants.BandWidthEdgeHost;
		switching_delay = NetworkConstants.SwitchingDelayEdge;
		numport = NetworkConstants.EdgeSwitchPort;
		uplinkswitches = new ArrayList<JAYASwitch>();
	}

	/**
	 * Send Packet to JAYASwitch connected through a uplink port
	 * 
	 * @param ev Event/packet to process
	 */
	@Override
	protected void processpacket_up(SimEvent ev) {
		// packet coming from down level router/host.
		// has to send up
		// check which JAYASwitch to forward to
		// add packet in the JAYASwitch list
		//
		// int src=ev.getSource();
		NetworkPacket hspkt = (NetworkPacket) ev.getData();
		int recvVMid = hspkt.pkt.reciever;
		CloudSim.cancelAll(getId(), new PredicateType(CloudSimTags.Network_Event_send));
		schedule(getId(), switching_delay, CloudSimTags.Network_Event_send);

		// packet is recieved from host
		// packet is to be sent to aggregate level or to another host in the same level

		int hostid = dc.VmtoHostlist.get(recvVMid);
		JAYANetworkHost hs = hostlist1.get(hostid);
		hspkt.recieverhostid = hostid;

		// packet needs to go to a host which is connected directly to JAYASwitch
		if (hs != null) {
			// packet to be sent to host connected to the JAYASwitch
			List<NetworkPacket> pktlist = packetTohost.get(hostid);
			if (pktlist == null) {
				pktlist = new ArrayList<NetworkPacket>();
				packetTohost.put(hostid, pktlist);
			}
			pktlist.add(hspkt);
			return;

		}
		// otherwise
		// packet is to be sent to upper JAYASwitch
		// ASSUMPTION EACH EDGE is Connected to one aggregate level JAYASwitch
		// if there are more than one Aggregate level JAYASwitch one need to modify following code

		JAYASwitch sw = uplinkswitches.get(0);
		List<NetworkPacket> pktlist = uplinkswitchpktlist.get(sw.getId());
		if (pktlist == null) {
			pktlist = new ArrayList<NetworkPacket>();
			uplinkswitchpktlist.put(sw.getId(), pktlist);
		}
		pktlist.add(hspkt);
		return;

	}

	/**
	 * Send Packet to hosts connected to the JAYASwitch
	 * 
	 * @param ev Event/packet to process
	 */
	@Override
	protected void processpacketforward(SimEvent ev) {
		// search for the host and packets..send to them

		if (uplinkswitchpktlist != null) {
			System.out.println("uplinkswitchpktlist != null");
			for (Entry<Integer, List<NetworkPacket>> es : uplinkswitchpktlist.entrySet()) {
				int tosend = es.getKey();
				List<NetworkPacket> hspktlist = es.getValue();
				if (!hspktlist.isEmpty()) {
					// sharing bandwidth between packets
					double avband = uplinkbandwidth / hspktlist.size();
					System.out.println("uplinkbandwidth: " + uplinkbandwidth);
					System.out.println("hspktlist.size(): " + hspktlist.size());
					System.out.println("avband: " + avband);
					Iterator<NetworkPacket> it = hspktlist.iterator();
					while (it.hasNext()) {
						NetworkPacket hspkt = it.next();
						double delay = 1000 * hspkt.pkt.data / avband;
						System.out.println("delay: " + delay);
						this.send(tosend, delay, CloudSimTags.Network_Event_UP, hspkt);
					}
					hspktlist.clear();
				}
			}
		}
		if (packetTohost != null) {
			System.out.println("packetTohost != null");
			for (Entry<Integer, List<NetworkPacket>> es : packetTohost.entrySet()) {
				List<NetworkPacket> hspktlist = es.getValue();
				if (!hspktlist.isEmpty()) {
					Iterator<NetworkPacket> it = hspktlist.iterator();
					while (it.hasNext()) {
						NetworkPacket hspkt = it.next();
						// hspkt.recieverhostid=tosend;
						// hs.packetrecieved.add(hspkt);
						int recvVMid = hspkt.pkt.reciever;
						int hostid = dc.VmtoHostlist.get(recvVMid);
						JAYANetworkHost hs = hostlist1.get(hostid);
						double avband = hs.bandwidth / hspktlist.size();
						System.out.println("downlinkbandwidth: " + hs.bandwidth);
						System.out.println("hspktlist.size(): " + hspktlist.size());
						System.out.println("avband: " + avband);
						System.out.println("hspkt.pkt.data: " + hspkt.pkt.data);
						System.out.println("delay: " + hspkt.pkt.data / avband);
						this.send(getId(), hspkt.pkt.data / avband, CloudSimTags.Network_Event_Host, hspkt);
					}
					hspktlist.clear();
				}
			}
		}

		// or to JAYASwitch at next level.
		// clear the list

	}

}
