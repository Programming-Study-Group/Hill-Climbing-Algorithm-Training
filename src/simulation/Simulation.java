package simulation;

import java.io.PrintStream;

import agent.Agent;

public class Simulation {
	
	public Simulation() {
		
	}
	
	/*
	public void runGeneration(Generation generation) {
		
	}
	*/
	
	/**
	 * Used to calculate cost score of an agent with multiple scenarios
	 * @param agent Agent being evaluated
	 * @param scenario Scenario used to evaluate the agent
	 * @param dt Time interval between every frame
	 * @return Score of the agent
	 */
	public double getCostScore(Agent agent, Scenario[] scenarios, float dt) {
		if (agent==null) {
			throw new IllegalArgumentException("Agent object is not initialized.");
		}
		
		double costScore = 0;
		for (Scenario scenario: scenarios) {
			if (scenario==null) {
				throw new IllegalArgumentException("Scenario object is not initialized.");
			}
			
			costScore += getCostScore(agent, scenario, dt);
		}
		return costScore;
	}
	
	/**
	 * Calculates and returns cost score of the agent using the scenario. Doesn't print anything to console nor to any file
	 * @param agent Agent being evaluated
	 * @param scenario Scenario used to evaluate the agent
	 * @param dt Time interval between every frame
	 * @return Score of the agent
	 */
	public double getCostScore(Agent agent, Scenario scenario, float dt) {
		if (agent==null) {
			throw new IllegalArgumentException("Agent object is not initialized.");
		} else if (scenario==null) {
			throw new IllegalArgumentException("Scenario object is not initialized.");
		} else if (Math.abs(dt*scenario.initialAngularSpeedInRadians)>Math.PI/2) {
			throw new IllegalArgumentException(String.format("Values of dt and initial angular speed are too extreme for an accurate simulation. dt: %.2f, angularSpeed: %.2f", dt, Math.toDegrees(scenario.initialAngularSpeedInRadians)));
		}
		
		float angleWithNormalInRadians = scenario.initialAngleWithNormalInRadians;
		float angularSpeedInRadians = scenario.initialAngularSpeedInRadians;
		float d = scenario.innerCircleRadius+scenario.outerCircleRadius;
		double costScore = 0;
		
		for (float t=0; t<scenario.maxDuration; t+=dt) {
			
			//update speed with acceleration
			double linearAcceleration = scenario.gravity * Math.sin(angleWithNormalInRadians); //+ decisionOfAgent();		//TODO Add decision of agent every frame
			double angularAcceleration = linearAcceleration/d;
			angularSpeedInRadians += angularAcceleration*dt;
			
			//update angle with speed
			angleWithNormalInRadians += angularSpeedInRadians*dt;
			
			//check if ball is out of range (when angle becomes higher than 90)
			if (angleWithNormalInRadians>1.57) { //ball crosses the boundary
				angleWithNormalInRadians -= 2*(angleWithNormalInRadians-1.57);
				angularSpeedInRadians = -angularSpeedInRadians;
			} else if (angleWithNormalInRadians<-1.57) {
				angleWithNormalInRadians += 2*(-1.57-angleWithNormalInRadians);
				angularSpeedInRadians = -angularSpeedInRadians;
			}
			
			//update score
			costScore+= Math.pow(angleWithNormalInRadians, 2);
		}
		return costScore;
	}

	/**
	 * Calculates and returns cost score of the agent using the scenario. Used when printing state of the simulation every frame
	 * @param agent Agent being evaluated
	 * @param scenario Scenario used to evaluate the agent
	 * @param dt Time interval between every frame
	 * @param ps PrintStream object used to print data every frame
	 * @return Score of the agent
	 */
	public double getCostScore(Agent agent, Scenario scenario, float dt, PrintStream ps) {
		if (agent==null) {
			throw new IllegalArgumentException("Agent object is not initialized.");
		} else if (scenario==null) {
			throw new IllegalArgumentException("Scenario object is not initialized.");
		} else if (Math.abs(dt*scenario.initialAngularSpeedInRadians)>Math.PI/2) {
			throw new IllegalArgumentException(String.format("Values of dt and initial angular speed are too extreme for an accurate simulation. dt: %.2f, angularSpeed: %.2f", dt, Math.toDegrees(scenario.initialAngularSpeedInRadians)));
		}
		
		float angleWithNormalInRadians = scenario.initialAngleWithNormalInRadians;
		float angularSpeedInRadians = scenario.initialAngularSpeedInRadians;
		float d = scenario.innerCircleRadius+scenario.outerCircleRadius;
		double costScore = 0;
		
		ps.println("time,angle");
		
		for (float t=0; t<scenario.maxDuration; t+=dt) {
			
			//update speed with acceleration
			double linearAcceleration = scenario.gravity * Math.sin(angleWithNormalInRadians); //+ decisionOfAgent();		//TODO Add decision of agent every frame
			double angularAcceleration = linearAcceleration/d;
			angularSpeedInRadians += angularAcceleration*dt;
			
			//update angle with speed
			angleWithNormalInRadians += angularSpeedInRadians*dt;
			
			//check if ball is out of range (when angle becomes higher than 90)
			if (angleWithNormalInRadians>1.57) { 
				angleWithNormalInRadians -= 2*(angleWithNormalInRadians-1.57);
				angularSpeedInRadians = -angularSpeedInRadians;
			} else if (angleWithNormalInRadians<-1.57) {
				angleWithNormalInRadians += 2*(-1.57-angleWithNormalInRadians);
				angularSpeedInRadians = -angularSpeedInRadians;
			}
			
			//update score
			costScore+= Math.pow(angleWithNormalInRadians, 2);
			
			//print to console/csv
			//System.out.println(String.format("Time: %.2f\tAngle: %.3f\tAngular Speed: %.3f", t, Math.toDegrees(angleWithNormalInRadians),angularSpeedInRadians));
			ps.println(String.format("%.3f, %.4f", t, Math.toDegrees(angleWithNormalInRadians)));
		}
		return costScore;
	}
}
