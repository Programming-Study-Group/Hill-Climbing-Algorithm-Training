package executable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import simulation.Scenario;
import simulation.Simulation;

public class Main {
	
	
	//Test for writing state of several -random- scenario simulations every frame to a csv file
	public static void writingAngleToCsvEveryFrameTest(int testId, float dt) throws FileNotFoundException {
		Simulation sim = new Simulation();
		int nofScenarios = 10;
		Scenario[] scenarios = new Scenario[nofScenarios];
		
		File detailsFile = new File(String.format("data/angle-over-time-test/test-%d/details.csv",testId));
		PrintStream detailsPs = new PrintStream(detailsFile);
		detailsPs.println("Scenario Id,Inner Circle Radius,Outer Circle Radius,Initial Angle With Normal In Degrees,Initial Angular Speed In Degrees,Gravity,Max Duration,dt");
		
		for (int i=0; i<nofScenarios; i++) {
			scenarios[i] = new Scenario(10, 10);
			File file = new File(String.format("data/angle-over-time-test/test-%d/scenario-%d.csv",testId,i));
			PrintStream ps = new PrintStream(file);
			detailsPs.println(scenarios[i].toStringForCsv()+ ", "+dt);
			sim.getCostScore(null, scenarios[i], dt, ps);	
		}
		detailsPs.close();
	}
}
