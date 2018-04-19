import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import norsys.netica.Learner;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Node;
import norsys.netica.NodeList;

public class ProblemSolution {
	private final String SOLUTION_PHRASE = "NUESTRO BOT EN EL SIGUIENTE ESTADO VA A: ";
	// Node that we are interested on
	private int nodeToInspect;
	// Start node
	private int startNode;
	// Lisf of nodes of our problem
	private NodeList nodes;
	private double solution;
	
	public ProblemSolution(NodeList nodes) {
		this.nodes = nodes;
	}
	
	public void setNodeToInspect(String nodeToInspect) throws ArrayIndexOutOfBoundsException, NeticaException {
		// Get the position of the node we are interested on know the values
		this.nodeToInspect = searchNodeByName(nodes, nodeToInspect);
	}
	
	public void setStartNode(int startNodeNumber) throws ArrayIndexOutOfBoundsException, NeticaException {
		this.startNode = startNodeNumber;
	}
	
	public void setNodeList(NodeList nodes) throws ArrayIndexOutOfBoundsException, NeticaException {
		this.nodes = nodes;
	}
	
	public void askForNodeValues() throws ArrayIndexOutOfBoundsException, NeticaException {
		// The value that user puts on the prompt
		String nodeValue;
		String continueAsking;
		Scanner scanner = new Scanner(System.in);
		boolean stop = false;
		
			for(int i = 0; i < nodes.size(); i++) {
				
				// Ignore the node to inspect, because we're interested on know it's values
				if(i != startNode && i != nodeToInspect) {

					// Get the name of the node on the network
					System.out.println("¿" + nodes.getNode(i).getTitle() + "? ");
					System.out.print("Posibles estados: {");

					//For each node we iterate on the possible states
					for(int j = 0; j < nodes.getNode(i).getNumStates(); j++) {

						// Get the attribute name
						System.out.print(nodes.getNode(i).state(j).getName());

						//If it is last node
						if(j + 1 >= nodes.getNode(i).getNumStates())
							System.out.println("}");

						else
							System.out.print(", ");
					}

					System.out.print(" > ");

					// To do thing easier, we automatically puts the first character
					// on mayus, if user forgets it
					nodeValue = scanner.nextLine();

					//If the nodeValue introduced isn't recognized, we ask for the
					//same attribute value again
					setNodeState(nodes.getNode(i), nodeValue);				
				}
			}

			solution = nodes.getNode(nodeToInspect).getBeliefs()[0];
	}
	
	public void showSolution() throws ArrayIndexOutOfBoundsException, NeticaException {
		System.out.println();
		System.out.println("-- Soluciones -- ");
		for(int k = 0; k < nodes.getNode(nodeToInspect).getBeliefs().length; k++) {
			System.out.print(nodes.getNode(nodeToInspect).state(k).getName() + " ");
			System.out.println(nodes.getNode(nodeToInspect).getBeliefs()[k]);
		}
		System.out.println();
		System.out.println(SOLUTION_PHRASE + nodes.getNode(nodeToInspect).state(getMaxValue()).getName());
	}
	
	private int getMaxValue() throws ArrayIndexOutOfBoundsException, NeticaException {
		int nodeWithMaxValue = -1;
		double maxValue = -1;
		
		for(int i = 0; i < nodes.getNode(nodeToInspect).getBeliefs().length; i++) {
			if(nodes.getNode(nodeToInspect).getBeliefs()[i] > maxValue) {
				maxValue = nodes.getNode(nodeToInspect).getBeliefs()[i];
				nodeWithMaxValue = i;
			}
		}
		
		return nodeWithMaxValue;
	}
	public static int searchNodeByName(NodeList nodes, String nodeName) throws ArrayIndexOutOfBoundsException, NeticaException {
		for(int i = 0; i < nodes.size(); i++)
			if(nodes.getNode(i).getTitle().toLowerCase().equals(nodeName.toLowerCase()))
				return i;
		
		return -1;
	}
	
	public static void setNodeState(Node node, String nodeAttribute) {
		// We try to change the nodeVale of the node with the value
		// of nodeValue that we have received at the prompt
		try {
			//node.retractFindings();
			node.finding().enterState(nodeAttribute);
		} catch (NeticaException e) {
			System.err.println("Estado no reconcido saliendo del programa...");
			System.exit(0);
		}
	}
	
	public int getStartNode() {
		return startNode;
	}
	
	public double getSolution() {
		return solution;
	}
	
	public NodeList getNodes() {
		return nodes;
	}
	
	public int getNodeToInspect() {
		return nodeToInspect;
	}
}
