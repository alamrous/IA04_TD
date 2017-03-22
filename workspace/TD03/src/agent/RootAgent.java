/**
 * 
 */
package agent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
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
			ACLMessage reply = message.createReply();
			reply.setPerformative(ACLMessage.CONFIRM);
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
		ACLMessage reply = message.createReply();

		if (message != null) {
			if (isValueSet != false)
			{
				 value = Integer.parseInt(message.getContent());
					reply.setPerformative(ACLMessage.CONFIRM);
			}
			
			else 
			{
				if(value >  Integer.parseInt(message.getContent()))
				{
					if (agentRight == null)
					{
						agentRight = Integer.parseInt(message.getContent());
						reply.setPerformative(ACLMessage.CONFIRM);
						try {
							AgentController aController=  SecondaryContainer.TreeContainer.createNewAgent(message.getContent(), "agent.RootAgent",null);
							aController.start();
							//Envoie de message à l'agent créé pour qu'il s'attribue la value
						} catch (StaleProxyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					else 
					{
						//l'agent right existe déjà lui envoyer un msg pour qu'il ait le mm comportement
					}
				}
				else if(value <  Integer.parseInt(message.getContent()))
				{
//					if (agentLeft == null) {
//						agentLeft = new RootAgent();
//
//						agentLeft.setValue(Integer.parseInt(message.getContent()));
//						reply.setPerformative(ACLMessage.CONFIRM);
//					}

				}
				else 
				{
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("Numéro déjà inséré \n");
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
