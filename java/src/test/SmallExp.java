package test;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import es.us.isa.FAMA.models.FAMAfeatureModel.FAMAFeatureModel;
import es.us.isa.FAMA.models.FAMAfeatureModel.fileformats.XMLReader;
import es.us.isa.FAMA.models.variabilityModel.parsers.WrongFormatException;
import es.us.isa.Sat4jReasoner.Sat4jReasoner;
import es.us.isa.Sat4jReasoner.questions.Sat4jProductsQuestion;

public class SmallExp {

	public static void main(String[] args) throws WrongFormatException, InterruptedException {

		SmallExp exp=new SmallExp();
		ExecutorService executor = Executors.newSingleThreadExecutor();
	//	ExecutorService executor = Executors.newFixedThreadPool(20);

		File dir = new File("tmp");
		File[] listFiles = dir.listFiles();
		for (File f : listFiles) {
			//for (int i = 0; i < 10; i++) {
		        Future<String> future = executor.submit(exp.new Task(f));

			        try {
			            System.out.println(future.get(30, TimeUnit.MINUTES));
			        } catch (TimeoutException | ExecutionException e) {
						System.out.println(f.getName() + ";NA;NA");
			        }

			    }
			//}
			
        executor.shutdownNow();

			

		}

	
	
	class Task implements Callable<String> {
		
		File f;
		public Task(File f){
			this.f=f;
		}
		
		@Override
	    public String call() throws Exception {
	    	long start = System.currentTimeMillis();
			XMLReader reader = new XMLReader();
			FAMAFeatureModel fm = (FAMAFeatureModel) reader.parseFile(f
					.getAbsolutePath());

			Sat4jReasoner sat = new Sat4jReasoner();
			Sat4jProductsQuestion pq = new Sat4jProductsQuestion();
			fm.transformTo(sat);
			sat.ask(pq);
			long end = System.currentTimeMillis();
	    	
	    	return (f.getName() + ";" + end+";"+start);
	    }
	}

}
