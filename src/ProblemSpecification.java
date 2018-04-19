import java.util.Scanner;

import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.NodeList;
import norsys.netica.Streamer;

public class ProblemSpecification {
	// Represents all the Netica network
	private Net neticaNetwork;
	// Problem nodes
	private NodeList networkNodes;
	// Class to solve the problem
	private ProblemSolution solution;
	
	public ProblemSpecification(String fileName) throws NeticaException {
		super();
		Environ env = new Environ(null);
		this.neticaNetwork = new Net(new Streamer(fileName));
		this.networkNodes = neticaNetwork.getNodes();
		this.solution = new ProblemSolution(networkNodes);
		askForStartNode();
	}
	
	private void askForStartNode() throws ArrayIndexOutOfBoundsException, NeticaException {
		int startNodeNumber;
		String startNodeValue;
		Scanner scanner = new Scanner(System.in);
		String startNode;
		
		System.out.print("Elige el nodo de partida -> {");
		for(int i = 0; i < networkNodes.size(); i++) {
			System.out.print(networkNodes.getNode(i).getTitle());
			
			if(i+1 >= networkNodes.size())
				System.out.println("}");
			else
				System.out.print(", ");
		}
		
		System.out.print(" > ");
		startNode = scanner.nextLine();
		
		// Gets the node number knowing the node name
		startNodeNumber = ProblemSolution.searchNodeByName(networkNodes, startNode);

		System.out.print("Elige uno de los posibles estados para " + startNode + ": {");

		//For each node we iterate on the possible states
		for(int j = 0; j < networkNodes.getNode(startNodeNumber).getNumStates(); j++) {

			// Get the attribute name
			System.out.print(networkNodes.getNode(startNodeNumber).state(j).getName());

			//If it is last node
			if(j + 1 >= networkNodes.getNode(startNodeNumber).getNumStates())
				System.out.println("}");

			else
				System.out.print(", ");
		}
		
		System.out.print(" > ");

		// To do thing easier, we automatically puts the first character
		// on mayus, if user forgets it
		startNodeValue = (scanner.next());

		//If the nodeValue introduced isn't recognized, we ask for the
		//same attribute value again
		ProblemSolution.setNodeState(networkNodes.getNode(startNodeNumber), startNodeValue);	
		solution.setStartNode(startNodeNumber);
	}
	
	public Net getNetwork() {
		return neticaNetwork;
	}
	
	public NodeList getNodes() {
		return networkNodes;
	}
	
	public void computeSolution() throws ArrayIndexOutOfBoundsException, NeticaException {
		Scanner scanner = new Scanner(System.in);
		String nodeToInspect;
		System.out.println();
		System.out.print("Elige el nodo que quieres observar -> {");
		for(int i = 0; i < networkNodes.size(); i++) {
			System.out.print(networkNodes.getNode(i).getTitle());
			
			if(i+1 >= networkNodes.size())
				System.out.println("}");
			else
				System.out.print(", ");
		}
		
		System.out.print(" > ");
		nodeToInspect = scanner.nextLine();
		solution.setNodeToInspect(nodeToInspect);
		neticaNetwork.compile();
		System.out.println();
		
		
		boolean stop = false;
		String continueAsking = "y";
		
		while(stop == false) {
			// Asks for the rest of nodes and its attributes
			solution.askForNodeValues();
			
			// If user wants to continue calculating next state
			if(continueAsking.equals("n")) {
				stop = true;
				break;
			} else {
				int nodestart = solution.getStartNode();

				NodeList solList = solution.getNodes();
				Node newNode = null;
				
				// If user wants to continue calculating next states, we update the originNode values
				// an clears all data input
				for(int k = 0; k < solList.getNode(solution.getNodeToInspect()).getBeliefs().length; k++) {
					String name = solList.getNode(solution.getStartNode()).state(k).getName();
					
					solList.getNode(nodestart).setCPTable(solList.getNode(solution.getNodeToInspect()).getBeliefs());
					newNode = solList.getNode(nodestart);
				}
				
				neticaNetwork.retractFindings();
				networkNodes.set(solution.getNodeToInspect(), newNode);
				solution.setNodeList(networkNodes);
			}
			System.out.print("¿Quiere calcular otra solucion? (s/n)\n");

			System.out.print(" > ");
			continueAsking = scanner.nextLine();
			System.out.println();
			
			if(continueAsking.equals("n"))
				stop = true;
		}
		
		solution.showSolution();
	}
}
