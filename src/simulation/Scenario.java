package simulation;

import java.util.Random;

public class Scenario {
	private static Random r;				//Used when creating scenario objects with random properties
	private static int lastScenarioId=0;	//used to assign a distinctive id to every scenario object
	private int scenarioId;
	
	static {
		r = new Random(0);
	}
	
	protected float innerCircleRadius;
	protected float outerCircleRadius;
	protected float initialAngleWithNormalInRadians;
	protected float initialAngularSpeedInRadians;
	protected float gravity;
	protected int maxDuration;
	
	/**
	 * Create a scenario object
	 * @param innerCircleRadius Radius of the inner circle
	 * @param outerCircleRadius Radius of the outer circle controlled by the agent
	 * @param initialAngleWithNormalInDegrees Angle between the y axis and the initial position
	 * @param initialAngularSpeedInDegrees Initial angular speed
	 * @param gravity Gravity of the environment
	 * @param maxDuration Time when the simulation terminates and stops simulating the scenario
	 */
	public Scenario(float innerCircleRadius,
			float outerCircleRadius,
			float initialAngleWithNormalInDegrees,
			float initialAngularSpeedInDegrees,
			float gravity,
			int maxDuration) {
		
		if (Math.abs(initialAngleWithNormalInDegrees)>90) {
			throw new IllegalArgumentException("Initial angle of the scneraio must be between -90 and 90: "+initialAngleWithNormalInDegrees);
		}
		
		scenarioId = lastScenarioId++;
		this.innerCircleRadius = innerCircleRadius;
		this.outerCircleRadius = outerCircleRadius;
		this.initialAngleWithNormalInRadians = (float) Math.toRadians(initialAngleWithNormalInDegrees);
		this.initialAngularSpeedInRadians = (float) Math.toRadians(initialAngularSpeedInDegrees);
		this.gravity = gravity;
		this.maxDuration = maxDuration;
	}
	
	/**
	 * Create a scenario object with random values
	 * @param gravity Gravity of the environment
	 * @param maxDuration Time when the simulation terminates and stops simulating the scenario
	 */
	public Scenario(float gravity, int maxDuration) {
		scenarioId = lastScenarioId++;
		this.innerCircleRadius = r.nextFloat()*5+5; //between 5 and 10
		this.outerCircleRadius = r.nextFloat()*4+1;	//between 1 and 5
		this.initialAngleWithNormalInRadians = (float) Math.toRadians(r.nextFloat()*160-80); //between -80 and 80 degrees
		this.initialAngularSpeedInRadians = (float) Math.toRadians(r.nextFloat()*200-100);	//between -100 and 100
		this.gravity = gravity;
		this.maxDuration = maxDuration;
	}
	
	/**
	 * 
	 * @return Distinctive id of the scenario object
	 */
	public int getScenarioId() {
		return scenarioId;
	}
	
	/**
	 * Returns a string with values defining the scenario, used when printing to Csv file 
	 * @return 
	 */
	public String toStringForCsv() {
		return String.format("%d, %f, %f, %f, %f, %f, %d",
				scenarioId,
				innerCircleRadius,
				outerCircleRadius,
				Math.toDegrees(initialAngleWithNormalInRadians),
				Math.toDegrees(initialAngularSpeedInRadians),
				gravity,
				maxDuration
				);
	}
	
	public String toString() {
		return String.format("Ri=%f, Ro=%f, TetaI=%f, AngSpeedI=%f, g=%f, tmax=%f",
				innerCircleRadius,
				outerCircleRadius,
				Math.toDegrees(initialAngleWithNormalInRadians),
				Math.toDegrees(initialAngularSpeedInRadians),
				gravity,
				maxDuration
				);
	}
}
