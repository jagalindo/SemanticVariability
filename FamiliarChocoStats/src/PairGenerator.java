import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import es.us.isa.ChocoReasoner.pairwise.Pair;
import fr.familiar.attributedfm.AttributedFeatureModel;
import fr.familiar.attributedfm.Configuration;
import fr.familiar.attributedfm.Feature;
import fr.familiar.attributedfm.Product;
import fr.familiar.attributedfm.reasoning.ChocoReasoner;


public class PairGenerator {
	public AttributedFeatureModel model;
	public Collection<Product> products;
	
	public PairGenerator(AttributedFeatureModel model){
		this.model=model;
	}

	public Collection<Pair>getPairs(){
		Collection<? extends Feature> features = model.getFeatures();
		
		//1st generate all pairs
		ArrayList<Pair> res = new ArrayList<Pair>();
		for(Feature f: features){
			for(Feature f2:features){
				if(f instanceof Feature && f2 instanceof Feature && !f.getName().equals(f2.getName())){
					Pair p = new Pair((Feature)f, (Feature)f2);
					if(!res.contains(p)){
						res.add(p);
					}
				}
			}
		}
		Iterator<Pair> pairs=res.listIterator();
		while(pairs.hasNext()){
			Pair p =pairs.next();
			
			ChocoReasoner reasoner = new ChocoReasoner();
			model.transformto(reasoner);
			Configuration c = new Configuration();
			c.addElement(p.featurea, 1);
			c.addElement(p.featureb, 1);
			reasoner.applyStagedConfiguration(c);
			
			Model problem = reasoner.getProblem();
	

			CPSolver solver = new CPSolver();
			solver.read(problem);
			try {
				solver.propagate();
			}catch (Exception e){
				pairs.remove();
			}	
			
			
		}

		return res;
		
	}
	
	
	
	private boolean pairsContained(Pair p, Collection<Product> products) {
		for(Product prod : products){
			if(prod.getFeatures().contains(p.featurea)&&prod.getFeatures().contains(p.featureb)){
				return true;
			}
		}
		return false;
	}
}
