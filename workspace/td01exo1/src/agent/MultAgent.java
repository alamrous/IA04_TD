package agent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.tools.testagent.ReceiveCyclicBehaviour;


public class MultAgent extends Agent {
	private boolean test = false;
	
protected void setup(){
	System.out.println(getLocalName()+ "--> Installed");
	Object[] args = getArguments();
	String op = (String) args[0];
	addBehaviour(new MultBehaviour(this,op));
}

public class MultBehaviour extends Behaviour{


	public MultBehaviour(MultAgent multAgent, String op) {
		// TODO Auto-generated constructor stub
	}
	public boolean done(){
		return false;
	}
	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message = receive();
		if (message != null) {
			answer(message);
		}
		else
			block();
	}
	
	private void answer(ACLMessage message) {
		String par = message.getContent();
		ACLMessage reply = message.createReply();
		if (par.contains("x")) {
		String[] parameters = par.split("x");
		int n = Integer.parseInt(parameters[0].trim())
		* Integer.parseInt(parameters[1].trim());
		reply.setPerformative(ACLMessage.INFORM);
		reply.setContent(String.valueOf(n));
		}
		else {
		reply.setPerformative(ACLMessage.FAILURE);
		reply.setContent("unknown operator");
		}
		send(reply);
		}

}

}

