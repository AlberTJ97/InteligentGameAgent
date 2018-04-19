import java.util.Scanner;

import norsys.netica.*;
import norsys.neticaEx.aliases.Node;

public class Main {
	public static void main(String[] args) throws NeticaException {
		ProblemSpecification botProblem = new ProblemSpecification(args[1]);
		botProblem.computeSolution();
	}
}
