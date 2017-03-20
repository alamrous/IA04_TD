package agent;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.tools.testagent.ReceiveCyclicBehaviour;
import java.util.concurrent.TimeUnit;

public class MultAgent extends Agent {
	private boolean test = false;
	
protected void setup(){
	System.out.println(getLocalName()+ "--> Installed");
//	Object[] args = getArguments();
//	String op = (String) args[0];
	String op = getLocalName();
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
		//MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message = receive();
		if (message != null && message.getPerformative() == ACLMessage.REQUEST) {
			answer(message);
		}
		else
			block();
	}
	
	private void answer(ACLMessage message) {
		String par = message.getContent();

		ACLMessage reply = message.createReply();
	
				ObjectMapper mapper = new ObjectMapper();
				
				try {
					FactMessage ort = mapper.readValue(par, FactMessage.class);
					ort.setResultat(ort.getOp1()*ort.getOp2());
					String s = null;

					try {
						s = mapper.writeValueAsString(ort);
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent(s);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
//		if (par.contains("x")) {
//		String[] parameters = par.split("x");
//		int n = Integer.parseInt(parameters[0].trim())
//		* Integer.parseInt(parameters[1].trim());
//		reply.setPerformative(ACLMessage.INFORM);
//		reply.setContent(String.valueOf(n));
//		}
//		else {
//		reply.setPerformative(ACLMessage.FAILURE);
//		reply.setContent("unknown operator");
//		}
				try {
					long number = (long) (0.5 + (Math.random() * ((5 - 0.5) + 1)));
					TimeUnit.SECONDS.sleep(number);
					System.out.print("je suis l'agent "+this.getAgent().getLocalName()+" je fais l'opération "+par+"J'ai attendu "+(double)number+" \n");

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		send(reply);
		}

}

}

