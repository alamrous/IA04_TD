package agent;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import java.lang.Math;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.tools.testagent.ReceiveCyclicBehaviour;

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

		@Override
		public void action() {
			// TODO Auto-generated method stub
//		initialisation des variables : resultat qui est à 0, le nombre à factoriser
			int resultat = 0;
			int number_to_factorize = resultat;
			int i = number_to_factorize;
//			On recupère le message et on fait un test
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
						while(list.size() > 1)
						{
							factMessage.setOp1(list.get(0));
							factMessage.setOp2(list.get(1));	
							list.remove(0);
							list.remove(0);
							ObjectMapper mapper = new ObjectMapper();
							String s = null;
	
							try {
								s = mapper.writeValueAsString(factMessage);
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						System.out.print(s+"\n");	
						ACLMessage calcul_to_sendMessage=new ACLMessage(ACLMessage.REQUEST);
							calcul_to_sendMessage.addReceiver(new AID("MultAgent"+(int)(1+(Math.random())*2),AID.ISLOCALNAME));
							calcul_to_sendMessage.setContent(s);
							send(calcul_to_sendMessage);
							message = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
							if (message != null )	
							{
								message = receive();
							}
							try {
								factMessage = mapper.readValue(message.getContent(), FactMessage.class);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							list.add(factMessage.getResultat());
						}
//						On crée un tableau qui va contenir les valeur de 1,2,...,number_to_factorize
						/*for (i=2; i<=number_to_factorize; i++){
							factMessage.setOp1(resultat);
							factMessage.setOp2(i);	
							ObjectMapper mapper = new ObjectMapper();
							String s = null;
	
							try {
								s = mapper.writeValueAsString(factMessage);
							} catch (JsonProcessingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						
							
							ACLMessage calcul_to_sendMessage=new ACLMessage(ACLMessage.REQUEST);
							calcul_to_sendMessage.addReceiver(new AID("MultAgent",AID.ISLOCALNAME));
							calcul_to_sendMessage.setContent(s);
							send(calcul_to_sendMessage);
							message = receive();
							while (message == null || message.getPerformative() != ACLMessage.INFORM)	
							{
								message = receive();
							
							}
							try {
								factMessage = mapper.readValue(message.getContent(), FactMessage.class);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							resultat = factMessage.getResultat();
						}*/

						resultat= list.get(0);

					}
					System.out.print("Le résultat de l'opération est :"+resultat);
				
				}
			} else
				block();
		}

	}

}
