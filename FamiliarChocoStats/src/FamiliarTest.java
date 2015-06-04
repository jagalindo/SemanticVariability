import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;

import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.fileformats.AttributedReader;
import fr.familiar.attributedfm.AttributedFeatureModel;
import fr.familiar.attributedfm.reasoning.ChocoReasoner;

public class FamiliarTest {
	public static void main(String[] args) throws Exception{
		String dir ="C:\\Users\\jagalindo\\Dropbox\\Documentos\\Trabajo\\Research\\Papers\\1. Ongoing\\FOSD14-VariabilitySemantics\\Experiments\\datasets";
		runGeneration(dir);
	}
	public static void runGeneration(String path) throws Exception {
		
		String[] dirs = {  "concrete" };

		for (String d : dirs) {
			PrintWriter out = new PrintWriter(path + "\\familiar-" + d + ".csv");
			out.print("Model;NoConstraints;NoIntVariables;NoSetVariables;NoSolutions;NoVariables;NoNodes;NoDesicionVariables\r\n");

			File dir = new File("C:\\Users\\jagalindo\\workspaces\\ambiguieties\\FaMaChocoStats\\input\\models\\"+ d);
			File[] listFiles = dir.listFiles();
			for (File f : listFiles) {
				System.out.println(f.getName());
				AttributedReader reader = new AttributedReader();
				AttributedFeatureModel model = (AttributedFeatureModel) reader.parseFile(f.getAbsolutePath());

				ChocoReasoner reasoner = new ChocoReasoner();

				model.transformto(reasoner);
				Model problem = reasoner.getProblem();
				Solver solver = new CPSolver();
			
				solver.read(problem);
				solver.solve();
				int sol=0;
				while(solver.nextSolution()){
					sol++;
				}
	
				int ndvars = 0;
				Iterator<IntegerVariable> intVarIterator = problem
						.getIntVarIterator();
				while (intVarIterator.hasNext()) {
					if (intVarIterator.next().getOptions()
							.contains("cp:no_decision")) {
						ndvars++;
					}
				}
				int ic = solver.getNbIntConstraints();
				int niv = solver.getNbIntVars();
				int nsv = solver.getNbSetVars();
				//int sol = solver.getNbSolutions();
				int tv = solver.getNbTaskVars();
				int nc = solver.getNodeCount();
				out.println(f.getName() + ";" + ic + ";" + niv + ";" + nsv
						+ ";" + sol + ";" + tv + ";" + nc + ';' + ndvars);
				out.flush();
			}
			out.close();
		}
	}
}
