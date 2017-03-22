package main;

import java.awt.List;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
public class SecondaryContainer {
	public static ContainerController TreeContainer = null;
public static void main(String[] args){
	String SECONDARY_PROPERTIES_FILE = "secd_container_ppt";
	Runtime rt = Runtime.instance();
	ProfileImpl p = null;
	try {
	p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
	 TreeContainer = rt.createAgentContainer(p);
	AgentController ac = TreeContainer.createNewAgent("MediatorAgent", "agent.MediatorAgent",null);
	ac.start();
	AgentController ac1 = TreeContainer.createNewAgent("RootAgent", "agent.RootAgent",null);
	ac1.start();
	
	} catch (Exception ex) {
	ex.printStackTrace();
	System.out.println("test probleme");
	}

}
}

