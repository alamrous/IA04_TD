/**
 * 
 */
package agent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import main.SecondaryContainer; 

/**
 * @author ia04p007
 *
 */
public class RootAgent extends Agent{
	/**
	 * 
	 */
	
	private int value;
	private boolean isValueSet= false;
	private Integer agentLeft = null;
	private Integer agentRight = null;
	
protected void setup(){
	System.out.println(getLocalName()+ "--> Installed");
//	Object[] args = getArguments();
//	String op = (String) args[0];
	String op = getLocalName();
	addBehaviour(new InsertBehaviour(this,op));
	addBehaviour(new ExistBehaviour(this,op));
	addBehaviour(new AfficheBehaviour(this,op));

}
/**
 * @return the agentRight
 */
public Integer getAgentRight() {
	return agentRight;
}
/**
 * @param agentRight the agentRight to set
 */
public void setAgentRight(Integer agentRight) {
	this.agentRight = agentRight;
}
/**
 * @return the agentLeft
 */
public Integer getAgentLeft() {
	return agentLeft;
}
/**
 * @param agentLeft the agentLeft to set
 */
public void setAgentLeft(Integer agentLeft) {
	this.agentLeft = agentLeft;
}
/**
 * @return the value
 */
public int getValue() {
	return value;
}
/**
 * @param value the value to set
 */
public void setValue(int value) {
	this.value = value;
	this.isValueSet= true;
}
public class AfficheBehaviour extends Behaviour{

	/**s
	 * @param rootAgent
	 * @param op
	 */
	public AfficheBehaviour(RootAgent rootAgent, String op) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate mTemplate = MessageTemplate.MatchConversationId("affiche");
		ACLMessage message = receive(mTemplate);
		if (message != null) {
			String arbre =message.getContent();

			ACLMessage replyMsg = message.createReply();
			if(isValueSet)
			{
				arbre = arbre.replace("valeur", Integer.toString(value));
				
				if(agentLeft != null)
				{
					arbre =arbre.replace("left", "((left)value(right))");
				}
				else{
					arbre =arbre.replace("(left)", "");
				}
				if(agentRight != null)
				{
					arbre =arbre.replace("right", "((left)value(right))");
				}
				else{
					arbre =arbre.replace("(right)", "");
				}
			}
			else 
			{
				replyMsg.setContent("Arbre vide");
			}
			System.out.println(arbre);
	
		}
		
		else
			block();
		
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}}
public class ExistBehaviour extends Behaviour{

	/**
	 * @param rootAgent
	 * @param op
	 */
	public ExistBehaviour(RootAgent rootAgent, String op) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate mTemplate = MessageTemplate.MatchConversationId("exist");
		ACLMessage message = receive(mTemplate);
		if (message != null) {
				System.out.println("Je suis l'agent "+getLocalName()+" Je réponds à "+message.getAllReceiver().toString());
				

				ACLMessage reply = message.createReply();
				if (isValueSet == false)
				{
						System.out.println("\n Cette valeur n'existe pas");

						//setValue(Integer.parseInt(message.getContent()));
					 	reply.addReceiver(new AID("MediatorAgent", AID.ISLOCALNAME));
						reply.setPerformative(ACLMessage.FAILURE);
						reply.setContent("Cette valeur n'existe pas");
				}
				
				else 
				{
					message.removeReceiver(new AID(getLocalName(), AID.ISLOCALNAME));

					if(value >  Integer.parseInt(message.getContent()))
					{
						
						if (agentRight == null)
						{
							System.out.println("\n Je n'ai pas de fils droit");

							//agentRight = Integer.parseInt(message.getContent());
							reply.setPerformative(ACLMessage.FAILURE);
							reply.setContent("Cette valeur n'existe pas");

								
						}
						else 
						{
							//l'agent right existe d�j� lui envoyer un msg pour qu'il ait le mm comportement
							message.addReceiver(new AID(agentRight.toString(),AID.ISLOCALNAME));
							send(message);


						}
					}
					else if(value <  Integer.parseInt(message.getContent()))
					{

						
						if (agentLeft == null)
						{
							System.out.println("\n Je n'ai pas de fils gauche");

							reply.setPerformative(ACLMessage.FAILURE);
							reply.setContent("Cette valeur n'existe pas");
							
							
						}
						else 
						{
							//l'agent right existe d�j� lui envoyer un msg pour qu'il ait le mm comportement
							message.addReceiver(new AID(agentLeft.toString(),AID.ISLOCALNAME));
							send(message);

						}
					

					}
					else if (value == Integer.parseInt(message.getContent()))
					{
						System.out.println("\n J'ai");

						reply.setPerformative(ACLMessage.CONFIRM);
						reply.setContent("Cette valeur existe");
					 	reply.addReceiver(new AID("MediatorAgent", AID.ISLOCALNAME));
					 	
					}
				}
				
				send(reply);
			}
		
		else
			block();
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
public class InsertBehaviour extends Behaviour{

	/**
	 * @param rootAgent
	 * @param op
	 */
	public InsertBehaviour(RootAgent rootAgent, String op) {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate mTemplate = MessageTemplate.MatchConversationId("insert");
		ACLMessage message = receive(mTemplate);
		if (message != null) {
			System.out.println("Je suis l'agent "+getLocalName());

			ACLMessage reply = message.createReply();
			if (isValueSet == false)
			{
					System.out.println("\n Je stocke la valeur");

					setValue(Integer.parseInt(message.getContent()));
				 	reply.addReceiver(new AID("MediatorAgent", AID.ISLOCALNAME));
					reply.setPerformative(ACLMessage.CONFIRM);
			}
			
			else 
			{
				message.removeReceiver(new AID(getLocalName(), AID.ISLOCALNAME));
				
				if(value >  Integer.parseInt(message.getContent()))
				{
					
					if (agentRight == null)
					{
						System.out.println("\n Je crée un fils droit");

						agentRight = Integer.parseInt(message.getContent());
						reply.setPerformative(ACLMessage.CONFIRM);
						try {
							AgentController aController=  SecondaryContainer.TreeContainer.createNewAgent(message.getContent(), "agent.RootAgent",null);
							aController.start();
						//On renvoie le même message reçu par mediator à l'agent right
							message.addReceiver(new AID(message.getContent(),AID.ISLOCALNAME));
							send(message);
							
						} catch (StaleProxyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					else 
					{
						//l'agent right existe d�j� lui envoyer un msg pour qu'il ait le mm comportement
						message.addReceiver(new AID(agentRight.toString(),AID.ISLOCALNAME));
						send(message);


					}
				}
				else if(value <  Integer.parseInt(message.getContent()))
				{

					
					if (agentLeft == null)
					{
						System.out.println("\n Je crée un fils gauche");
						agentLeft = Integer.parseInt(message.getContent());
						reply.setPerformative(ACLMessage.CONFIRM);
						try {
							AgentController aController=  SecondaryContainer.TreeContainer.createNewAgent(message.getContent(), "agent.RootAgent",null);
							aController.start();
						//On renvoie le même message reçu par mediator à l'agent right
							message.addReceiver(new AID(message.getContent(),AID.ISLOCALNAME));
							send(message);
							
						} catch (StaleProxyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					else 
					{
						//l'agent right existe d�j� lui envoyer un msg pour qu'il ait le mm comportement
						message.addReceiver(new AID(agentLeft.toString(),AID.ISLOCALNAME));
						send(message);


					}
				

				}
				else 
				{
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("Num�ro d�j� ins�r� \n");
				}
			}
			
			send(reply);
		}
		else
			block();
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}}
}
