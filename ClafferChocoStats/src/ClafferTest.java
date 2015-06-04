import java.io.File;
import java.io.PrintWriter;

import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.measure.IMeasures;
import org.chocosolver.solver.variables.Variable;
import org.clafer.ast.AstModel;
import org.clafer.compiler.ClaferCompiler;
import org.clafer.compiler.ClaferSolver;
import org.clafer.scope.Scope;

import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.FAMAAttributedFeatureModel;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.fileformats.AttributedReader;

public class ClafferTest {
	public static void main(String[] args) throws Exception{
		String dir ="C:\\Users\\jagalindo\\Dropbox\\Documentos\\Trabajo\\Research\\Papers\\1. Ongoing\\FOSD14-VariabilitySemantics\\Experiments\\datasets";
		runGeneration(dir);
	}
	public static void runGeneration(String path) throws Exception {

		String[] dirs = { "concrete" };

		for (String d : dirs) {
			PrintWriter out = new PrintWriter(path + "\\claffer-" + d + ".csv");
			out.print("Model;NoConstraints;NoIntVariables;NoSetVariables;NoSolutions;NoVariables;NoNodes;NoDesicionVariables\r\n");
			File dir = new File("C:\\Users\\jagalindo\\workspaces\\ambiguieties\\FaMaChocoStats\\input\\models\\"	+ d);

			File[] listFiles = dir.listFiles();
			for (File f : listFiles) {
				System.out.println(f);
				AttributedReader reader = new AttributedReader();
				FAMAAttributedFeatureModel model = (FAMAAttributedFeatureModel) reader
						.parseFile(f.getAbsolutePath());

				FamaTranslatorToClaffer t = new FamaTranslatorToClaffer();
				AstModel translate = t.translate(model);

				ClaferSolver csolver = ClaferCompiler.compile(translate, Scope
						.defaultScope(1).intLow(-3).intHigh(2));
				 int products=0;//csolver.allInstances().length;
				 while (csolver.find()) {
					 products++;
			        }
				System.out.println(products);
//				Solver solver = csolver.getInternalSolver();
//
//				solver.findSolution();
//				IMeasures m = solver.getMeasures();
//				long sol = m.getSolutionCount();
//				int ic = solver.getNbCstrs();
//				int tv = solver.getNbVars();
//
//				long nc = m.getNodeCount();
//				int niv = solver.retrieveIntVars().length;
//				int nsv = solver.retrieveSetVars().length;
//				out.println(f.getName() + ";" + ic + ";" + niv + ";" + nsv
//						+ ";" + sol + ";" + tv + ";" + nc + ";0;"+products);
//				out.flush();
			}
			out.close();
		}
	}

}
