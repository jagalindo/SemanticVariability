import org.clafer.ast.AstConcreteClafer;
import org.clafer.ast.AstModel;
import org.clafer.ast.Asts;
import org.clafer.compiler.ClaferCompiler;
import org.clafer.compiler.ClaferSolver;
import org.clafer.scope.Scope;

public class test {

	public static void main(String[] args) {
		
		
		AstModel model = Asts.newModel();

		 AstConcreteClafer installation = model.addChild("Installation").withCard(Asts.Mandatory);
	        // withCard(Mandatory) and withCard(1, 1) is the same. Pick the one you find more readable.
	        AstConcreteClafer status = installation.addChild("Status").withCard(1, 1).withGroupCard(1, 1);
	            AstConcreteClafer ok = status.addChild("Ok").withCard(Asts.Optional);
	            // withCard(Optional) and withCard(0, 1) is the same.
	            AstConcreteClafer bad = status.addChild("Bad").withCard(0, 1);
	            // Note that ok and bad have an explicit optional cardinality, whereas
	            // it was implicit in the oringal model.
	        AstConcreteClafer time = installation.addChild("Time").withCard(1, 1).refTo(Asts.IntType);
	            time.addConstraint(Asts.greaterThan(Asts.joinRef(Asts.$this()), Asts.constant(2)));
	            // Note that joinRef is explicit whereas it was implicit in the original model.
	      
	
	
	            ClaferSolver solver = ClaferCompiler.compile(model, 
	                  Scope.defaultScope(1).intLow(-16).intHigh(16));
	                    // intLow is the "suggested" lowest integer for solving. intHigh is the "suggested"
	                    // highest integer.
	                // find will return true when the solver finds another instance.
	                while (solver.find()) {
	                    // Print the solution in a format similar to ClaferIG.
	                    System.out.println(solver.instance());
	                }
	
	}
}
