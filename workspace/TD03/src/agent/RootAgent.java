/**
 * 
 */
package agent;


import java.security.acl.Acl;
import java.util.Spliterators.AbstractIntSpliterator;

import javax.xml.ws.Response;

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
	private boolean waitLeftstatus = false;
	private boolean waitRightstatus = false;


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
	public void sendMessageToSon(String sonName,ACLMessage messageReceived){
		ACLMessage messageToSend = new ACLMessage(messageReceived.getPerformative());
		messageToSend.setConversationId(messageReceived.getConversationId());
		messageToSend.addReceiver(new AID(sonName, AID.ISLOCALNAME));
		messageToSend.setContent(messageReceived.getContent());
		send(messageToSend);
	}
	public void sendMessageToSonWithId(String sonName,ACLMessage messageReceived, String conversationId)
	{
		ACLMessage messageToSend = new ACLMessage(messageReceived.getPerformative());
		messageToSend.setConversationId(conversationId);
		messageToSend.addReceiver(new AID(sonName, AID.ISLOCALNAME));
		messageToSend.setContent("(left)valeur(right)");
		send(messageToSend);	
	}
	public void replyToMediator(ACLMessage replyMessage,Integer performative,String content){
		ACLMessage messageToSend = new ACLMessage(performative);
		messageToSend.setConversationId(replyMessage.getConversationId());
		messageToSend.addReceiver(new AID("MediatorAgent", AID.ISLOCALNAME));
		messageToSend.setContent(content);
		send(messageToSend);
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
			String conversation_id = "affiche";
			System.out.println(getLocalName().contains("RootAgent"));

			if (getLocalName().contains("RootAgent") == false) conversation_id= conversation_id +getLocalName();
			System.out.println(conversation_id);
			MessageTemplate mTemplate = MessageTemplate.MatchConversationId(conversation_id);
			//Le message qui va être renvoyé une fois la chaine crée
			ACLMessage message = receive(mTemplate);
			if (message != null) {
				String arbre =message.getContent();

				ACLMessage reply = message.createReply();
				if(isValueSet)
				{
					arbre = arbre.replace("valeur", Integer.toString(value));
					reply.setPerformative(ACLMessage.INFORM);

				
						if (agentLeft != null) {
							sendMessageToSonWithId(agentLeft.toString(), message, "affiche" + agentLeft.toString());
							waitLeftstatus = true;
							ACLMessage response = null ; 
							
							while (response == null) {
								response = receive(	MessageTemplate.MatchConversationId("affiche" + agentLeft.toString()));
							}
								arbre = arbre.replace("left", response.getContent());
								waitLeftstatus = false;
							
						} else {
							arbre = arbre.replace("(left)", "");
							

						}
						if (agentRight != null) {
							sendMessageToSonWithId(agentRight.toString(), message, "affiche" + agentRight.toString());
						waitRightstatus = true;
						ACLMessage response= null; 
						while (response == null) {
								response = receive(
										MessageTemplate.MatchConversationId("affiche" + agentRight.toString()));
							}
								waitRightstatus= false;
								
								arbre = arbre.replace("right", response.getContent());
								
							
						} else {
							arbre = arbre.replace("(right)", "");
		
						} 
						reply.setContent(arbre);
					
						send(reply);
					
				}
				else 
				{
					System.out.println("arbre vide");
					reply.setContent("Arbre vide");
					reply.setPerformative(ACLMessage.INFORM);
					send(reply);
				}

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

				ACLMessage reply = message.createReply();
				if(value != Integer.parseInt(message.getContent()))
				{
					//Cas 1 : Il s'agit d'une feuille 
					if(agentRight == null && agentLeft == null)
					{
						replyToMediator(reply, ACLMessage.FAILURE, "Cette valeur n'existe pas");
						return;
					}
					//Si la valeur est plus petite
					if(value >  Integer.parseInt(message.getContent()))
					{

						if (agentRight == null)
						{
							replyToMediator(reply, ACLMessage.FAILURE, "Cette valeur n'existe pas");

						}
						else 
						{
							sendMessageToSon(agentRight.toString(), message);

						}
					}
					else 
					{

						if (agentLeft == null)
						{
							replyToMediator(reply, ACLMessage.FAILURE, "Cette valeur n'existe pas");

						}
						else 
						{
							sendMessageToSon(agentLeft.toString(), message);

						}


					}
				}
				else 
				{
					replyToMediator(reply, ACLMessage.CONFIRM, "Cette valeur existe");

				}

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
					setValue(Integer.parseInt(message.getContent()));

					replyToMediator(reply, ACLMessage.CONFIRM, "");


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
							try {
								AgentController aController=  SecondaryContainer.TreeContainer.createNewAgent(message.getContent(), "agent.RootAgent",null);
								aController.start();
			

							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					
						sendMessageToSon(agentRight.toString(), message);

					}
					else if(value <  Integer.parseInt(message.getContent()))
					{


						if (agentLeft == null)
						{
							System.out.println("\n Je crée un fils gauche");
							agentLeft = Integer.parseInt(message.getContent());
							try {
								AgentController aController=  SecondaryContainer.TreeContainer.createNewAgent(message.getContent(), "agent.RootAgent",null);
								aController.start();
				
							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						sendMessageToSon(agentLeft.toString(), message);

					}
					else 
					{
						replyToMediator(reply, ACLMessage.FAILURE, "Numéro Déjà Inséré");


					}
				}

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
