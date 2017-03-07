package agent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.tools.testagent.ReceiveCyclicBehaviour;


public class FactAgent extends Agent {
	private boolean test = false;
	
protected void setup(){
	String op = getLocalName();
	System.out.println(op+" Hello World");
	addBehaviour(new DisplayMsgBehaviour(this,op));}

public class DisplayMsgBehaviour extends Behaviour{


	public DisplayMsgBehaviour(FactAgent factAgent, String op) {
		// TODO Auto-generated constructor stub
	}
	public boolean done(){
		return false;
	}
	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message = receive();
		if(message != null){
			System.out.println("Contact "+message.getContent());
			test = true;
		}else
			block();
	}

}

}

