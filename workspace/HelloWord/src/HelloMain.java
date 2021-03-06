
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
public class HelloMain {
public static void main(String[] args){
	String SECONDARY_PROPERTIES_FILE = "secd_container_ppt";
	Runtime rt = Runtime.instance();
	ProfileImpl p = null;
	try {
	p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
	ContainerController cc = rt.createAgentContainer(p);
	AgentController ac = cc.createNewAgent("Hello3", "agent.HelloWord",null);
	ac.start();
	AgentController ac2 = cc.createNewAgent("Hello4", "agent.HelloWord",null);
	ac2.start();
	} catch (Exception ex) {
	ex.printStackTrace();
	}

}
}

