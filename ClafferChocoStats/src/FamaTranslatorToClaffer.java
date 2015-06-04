import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.clafer.ast.AstBoolExpr;
import org.clafer.ast.AstCompare;
import org.clafer.ast.AstConcreteClafer;
import org.clafer.ast.AstExpr;
import org.clafer.ast.AstModel;
import org.clafer.ast.AstSetExpr;
import org.clafer.ast.Asts;

import es.us.isa.FAMA.main.FaMaMain;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.AttributedFeature;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.FAMAAttributedFeatureModel;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.Relation;
import es.us.isa.FAMA.models.FAMAAttributedfeatureModel.fileformats.AttributedReader;
import es.us.isa.FAMA.models.featureModel.Cardinality;
import es.us.isa.FAMA.models.featureModel.Constraint;
import es.us.isa.FAMA.models.featureModel.GenericFeature;
import es.us.isa.FAMA.models.featureModel.extended.GenericAttribute;
import es.us.isa.util.Node;
import es.us.isa.util.Tree;

public class FamaTranslatorToClaffer {

	Map<String, AstConcreteClafer> clafferAtts = new HashMap<String, AstConcreteClafer>();

	FAMAAttributedFeatureModel famamodel;

	AstModel translate(FAMAAttributedFeatureModel model) throws IOException {
		famamodel = model;
		AstModel newmodel = Asts.newModel();
		AstConcreteClafer newFeat = newmodel
				.addChild(model.getRoot().getName()).withCard(1, 1);
		generateTree(model.getRoot(), newFeat);
		generateConstratins(model, newFeat);
		return newmodel;
	}

	private void generateConstratins(FAMAAttributedFeatureModel model,
			AstConcreteClafer newFeat) {

		for (Constraint c : model.getConstraints()) {
			Tree<String> ast = c.getAST();
			Node<String> rootElement = ast.getRootElement();
			AstExpr ctc = processNode(rootElement);
			newFeat.addConstraint((AstBoolExpr) ctc);
		}

	}

	public AstExpr processNode(Node<String> node) {

		if (node.getData().equals("IMPLIES")) {
			AstBoolExpr i = (AstBoolExpr) processNode(node.getChildren().get(0));
			AstBoolExpr d = (AstBoolExpr) processNode(node.getChildren().get(1));
			return Asts.implies(i, d);

		} else if (node.getData().equals("IFF")) {
			AstBoolExpr i = (AstBoolExpr) processNode(node.getChildren().get(0));
			AstBoolExpr d = (AstBoolExpr) processNode(node.getChildren().get(1));
			return Asts.ifOnlyIf(i, d);

		} else if (node.getData().equals("AND")) {
			AstBoolExpr i = (AstBoolExpr) processNode(node.getChildren().get(0));
			AstBoolExpr d = (AstBoolExpr) processNode(node.getChildren().get(1));
			return Asts.and(i, d);

		} else if (node.getData().equals("OR")) {
			AstBoolExpr i = (AstBoolExpr) processNode(node.getChildren().get(0));
			AstBoolExpr d = (AstBoolExpr) processNode(node.getChildren().get(1));
			return Asts.or(i, d);

		} else if (node.getData().equals(">=")) {
			AstSetExpr i = (AstSetExpr) processNode(node.getChildren().get(0));
			AstSetExpr d = (AstSetExpr) processNode(node.getChildren().get(1));
			return Asts.greaterThanEqual(i, d);
		} else if (node.getData().equals(">")) {
			AstSetExpr i = (AstSetExpr) processNode(node.getChildren().get(0));
			AstSetExpr d = (AstSetExpr) processNode(node.getChildren().get(1));
			return Asts.greaterThan(i, d);
		} else if (node.getData().equals("==")) {
			AstSetExpr i = (AstSetExpr) processNode(node.getChildren().get(0));
			AstSetExpr d = (AstSetExpr) processNode(node.getChildren().get(1));
			return Asts.equal(i, d);

		} else if (node.getData().equals("!=")) {
			AstSetExpr i = (AstSetExpr) processNode(node.getChildren().get(0));
			AstSetExpr d = (AstSetExpr) processNode(node.getChildren().get(1));
			return Asts.notEqual(i, d);

		} else if (node.getData().equals("<=")) {
			AstSetExpr i = (AstSetExpr) processNode(node.getChildren().get(0));
			AstSetExpr d = (AstSetExpr) processNode(node.getChildren().get(1));
			return Asts.lessThanEqual(i, d);
		} else if (node.getData().equals("<")) {
			AstSetExpr i = (AstSetExpr) processNode(node.getChildren().get(0));
			AstSetExpr d = (AstSetExpr) processNode(node.getChildren().get(1));
			return Asts.lessThan(i, d);
			// requires excludes y lo terminales enteros atributos

		} else if (node.getData().equals("EXCLUDES")) {
			AstBoolExpr i = (AstBoolExpr) processNode(node.getChildren().get(0));
			AstBoolExpr d = (AstBoolExpr) processNode(node.getChildren().get(1));
			return Asts.and(Asts.implies(i, Asts.not(d),
					Asts.implies(d, Asts.not(i))));
		} else if (node.getData().equals("REQUIRES")) {

			AstBoolExpr i = (AstBoolExpr) processNode(node.getChildren().get(0));
			AstBoolExpr d = (AstBoolExpr) processNode(node.getChildren().get(1));
			return Asts.implies(i, d);

		} else if (node.getData().equals("Attribute")) {
			// String attName = getAttributeName(node.getData());
			// res = attVars.get(p).get(attName);
			List<Node<String>> children = node.getChildren();
			AstConcreteClafer astConcreteClafer = clafferAtts.get(children.get(
					0).getData()
					+ "." + children.get(1).getData());
			return Asts.joinRef(astConcreteClafer);
		} else if (isFeature(node.getData())) {
			AstConcreteClafer astConcreteClafer = clafferAtts.get(node
					.getData());
			return Asts.some(astConcreteClafer);
		} else {

			Integer i = Integer.parseInt(node.getData());
			if (i != null) {
				return Asts.constant(i);
			}

		}
		return null;

	}

	private boolean isFeature(String data) {
		for (GenericFeature f : famamodel.getFeatures()) {
			if (f.getName().equals(data)) {
				return true;
			}
		}
		return false;
	}

	private void generateTree(AttributedFeature root, AstConcreteClafer newFeat)
			throws IOException {

		// addchilds
		Iterator<Relation> relIt = root.getRelations();
		while (relIt.hasNext()) {
			Relation rel = relIt.next();
			if (rel.getNumberOfDestination() == 1) {
				if (rel.isMandatory()) {
					AstConcreteClafer f = newFeat.addChild(
							rel.getDestinationAt(0).getName()).withCard(1, 1);
					clafferAtts.put(rel.getDestinationAt(0).getName(), f);
					generateTree(rel.getDestinationAt(0), f);
				} else if (rel.isOptional()) {
					AstConcreteClafer f = newFeat.addChild(
							rel.getDestinationAt(0).getName()).withCard(0, 1);
					clafferAtts.put(rel.getDestinationAt(0).getName(), f);
					generateTree(rel.getDestinationAt(0), f);
				}
			} else if (rel.getNumberOfDestination() > 1) {
				Cardinality card = rel.getCardinalities().next();
				newFeat.withGroupCard(card.getMin(), card.getMax());

				Iterator<AttributedFeature> destination = rel.getDestination();
				while (destination.hasNext()) {
					AttributedFeature next = destination.next();
					AstConcreteClafer f = newFeat.addChild(next.getName())
							.withCard(0, 1);
					clafferAtts.put(next.getName(), f);

					generateTree(next, f);
				}
			}
		}

		// addatributes
		for (GenericAttribute att : root.getAttributes()) {
			AstConcreteClafer attr = newFeat.addChild(att.getFullName()).refTo(
					Asts.IntType);// we
			// do
			// not
			// specify
			// domain
			// in
			// claffer
			clafferAtts.put(att.getFullName(), attr);

		}

	}

}
