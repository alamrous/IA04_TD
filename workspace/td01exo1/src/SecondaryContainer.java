
import java.awt.List;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
public class SecondaryContainer {
public static void main(String[] args){
	String SECONDARY_PROPERTIES_FILE = "secd_container_ppt";
	Runtime rt = Runtime.instance();
	ProfileImpl p = null;
	try {
	p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
	ContainerController cc = rt.createAgentContainer(p);
	AgentController ac = cc.createNewAgent("FactAgent", "agent.FactAgent",null);
	ac.start();
	AgentController ac2 = cc.createNewAgent("MultAgent2", "agent.MultAgent",null);
	ac2.start();
	AgentController ac3 = cc.createNewAgent("MultAgent1", "agent.MultAgent",null);
	ac3.start();
	} catch (Exception ex) {
	ex.printStackTrace();
	System.out.println("test probleme");
	}

}
}

