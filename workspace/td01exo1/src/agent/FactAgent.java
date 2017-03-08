package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
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
//						On crée un tableau qui va contenir les valeur de 1,2,...,number_to_factorize
						int tab_of_number[] = new int[2];
						for (i=2; i<=number_to_factorize; i++){
//							tab_of_number[0]=resultat;
//							tab_of_number[1]=i;
							ACLMessage calcul_to_sendMessage=new ACLMessage(ACLMessage.REQUEST);
							calcul_to_sendMessage.addReceiver(new AID("MultAgent",AID.ISLOCALNAME));
							calcul_to_sendMessage.setContent(Integer.toString(resultat, 10)+"x"+Integer.toString(i, 10));
							send(calcul_to_sendMessage);
						}


					}
				}
			} else
				block();
		}

	}

}
