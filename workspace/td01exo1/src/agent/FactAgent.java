package agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import java.lang.Math;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.tools.testagent.ReceiveCyclicBehaviour;
import jade.util.leap.Collection;

public class FactAgent extends Agent {
	private boolean test = false;

	protected void setup() {
		String op = getLocalName();
		System.out.println(op + " Hello World");
		addBehaviour(new DisplayMsgBehaviour(this, op));
	}

	public class DisplayMsgBehaviour extends Behaviour {

		public DisplayMsgBehaviour(FactAgent factAgent, String op) {
			// TODO Auto-generated constructor stub
		}

		public boolean done() {
			return false;
		}

		public void initializeAgentList(ArrayList<String> Agentlist){
			Agentlist.add("MultAgent1");
			Agentlist.add("MultAgent2");
			Collections.shuffle(Agentlist);
		}
		public int sendOperationToAvailableAgent(ArrayList<Integer> list, FactMessage factMessage, int count, ObjectMapper mapper, ArrayList<String> Agentlist){
			count= 0;
			while (Agentlist.size() > 0) {
				if (list.size() < 2)
					break;
				factMessage.setOp1(list.get(0));
				factMessage.setOp2(list.get(1));
				list.remove(0);
				list.remove(0);
				String s = null;
				count = count+1;
				System.out.println("Itération du count"+count);
				try {
					s = mapper.writeValueAsString(factMessage);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.print("Message  à "+Agentlist.get(0)+" contenu:"+s + "\n");
				ACLMessage calcul_to_sendMessage = new ACLMessage(ACLMessage.REQUEST);
				calcul_to_sendMessage.addReceiver(new AID(Agentlist.get(0), AID.ISLOCALNAME));
				Agentlist.remove(0);
				calcul_to_sendMessage.setContent(s);
				send(calcul_to_sendMessage);
			}
return count;
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
//		initialisation des variables : resultat qui est � 0, le nombre � factoriser
			int resultat = 0;
			int number_to_factorize = resultat;
			int i = number_to_factorize;
			int count = 0;

			ObjectMapper mapper = new ObjectMapper();

			//Initialisation de la liste des agents 
			ArrayList<String> Agentlist = new ArrayList<>();
			initializeAgentList(Agentlist);
			
//			On recup�re le message et on fait un test
			ACLMessage message = receive();
			if (message != null && message.getPerformative() == ACLMessage.INFORM) {
				{
					resultat = 1;
					number_to_factorize = Integer.parseInt(message.getContent());
//					Si le nombre est 0, on ne fait pas le calcul est on renvoie 1
					if (number_to_factorize > 1 )  {
						ArrayList<Integer> list = new ArrayList<>();
						for (i=1; i<= number_to_factorize; i++)
						{
							list.add(i);
						}
						FactMessage factMessage = new FactMessage();
						while(list.size() > 1 )
						{
							count=sendOperationToAvailableAgent(list,factMessage,count,mapper,Agentlist);
							System.out.println("Nb Messages à recevoir :"+count+"\n");
							
							MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
								
							while ( count >0)
							{											
								message = receive(messageTemplate);
													
								if(message == null )
								{
									if(count > 0)
										continue;
									}
								else  
								{
									System.out.println("J'ai reçu la réponse de "+message.getSender().getLocalName());
									try {
										factMessage = mapper.readValue(message.getContent(), FactMessage.class);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									list.add(factMessage.getResultat());

									count = count -1 ;
									System.out.println("Plus que "+count+" réponses");
									
								}
							}
						
							

							//list.add(factMessage.getResultat());
							initializeAgentList(Agentlist);
						}


						resultat= list.get(0);
					}
					System.out.print("Le r�sultat de l'op�ration est :"+resultat);
				
				}
			} else
				block();
		}

	}

}
