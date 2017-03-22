/**
 * 
 */
package agent;

import java.security.acl.Acl;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author ia04p007
 *
 */
public class MediatorAgent extends Agent{
	private boolean test = false;

	protected void setup() {
		String op = getLocalName();
		System.out.println(op + " Hello World");
		addBehaviour(new MediateBehaviour(this, op));
	}
public class MediateBehaviour extends Behaviour{

	/**
	 * @param mediatorAgent
	 * @param op
	 */
	public MediateBehaviour(MediatorAgent mediatorAgent, String op) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message = receive();
		if (message != null && message.getPerformative() == ACLMessage.REQUEST) {
			String par = message.getContent();
	 		ACLMessage messageToRoot = new ACLMessage(ACLMessage.REQUEST);
			messageToRoot.addReceiver(new AID("RootAgent", AID.ISLOCALNAME));
			MessageTemplate mt;


			if (message.getContent().contains(" ")) {
				 		String[] parameters = par.split(" ");
						messageToRoot.setConversationId(parameters[0]);
						messageToRoot.setContent(parameters[1]);
						send(messageToRoot);
						 mt = MessageTemplate.MatchConversationId(parameters[0]);
						messageToRoot=receive(mt);
						while(messageToRoot == null )
						{
							messageToRoot=receive(mt);
						}
						if (messageToRoot.getPerformative() == ACLMessage.CONFIRM)
						{
							System.out.println("J'ai réussi l'opération "+parameters[0]+" Sur le nombre "+parameters[1]);
							return;
						}
						System.out.println(messageToRoot.getContent());
				 	
		}
		else
			if (par.contains("affiche"))
			{
			messageToRoot.setConversationId("Affiche");
			send(messageToRoot);
			 mt = MessageTemplate.MatchConversationId("Affiche");
			messageToRoot = receive(mt);
			while(messageToRoot == null )
			{
				messageToRoot=receive(mt);
			}
			System.out.println(messageToRoot.getContent());

			
			}
			else 
			{
				block();
			}
		
	}
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
}
