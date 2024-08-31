package automata.urban24;

import java.util.ArrayList;

public class AgentModel {
	// all agents on this map
	Agent agents[] = new Agent[Constants.AGENTS];
	// all fixed pois on the map located at parcel intersections
	POI fixedPois[][];
	// infectious model
	SICARQD sicarqd;
	// SICARQD2 sicarqd2;
	// save number of hits
	ArrayList<String> numberOfHits = new ArrayList<>();
	// save number of infected
	int[] numberOfInfected = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
	float avgHits = 0;
	int maxHits = 0, peakInfection = 0;

	public void setupMultiAgents() {
		// create mesh of fixed pois on map
		fixedPois = new POI[Constants.getPSx() + 1][Constants.getPSy() + 1];
		for (int i = 0; i < fixedPois.length; ++i)
			for (int j = 0; j < fixedPois[i].length; ++j) {
				fixedPois[i][j] = new POI(i, j);
			}

		// generate agents
		for (int i = 0; i < Constants.AGENTS; ++i) {
			agents[i] = new Agent(i, Constants.randBetween(Constants.PADDING, Constants.SIM_WIDTH - Constants.PADDING),
					Constants.randBetween(Constants.PADDING, Constants.SIM_HEIGHT - Constants.PADDING));
		}

		// infect random seeds
		for (int i = 0; i < Constants.NUM_SEEDS; ++i) {
			agents[Constants.rand.nextInt(Constants.AGENTS)].state = State.INC;
		}

		sicarqd = new SICARQD(agents);
		// sicarqd2 = new SICARQD2();
	}

	public void drawMultiAgents() {
		// background(30);

		for (int i = 0; i < Constants.AGENTS; ++i) {
			agents[i].update();
		}

		// text("Hits:" + hits, 5, 60);
		numberOfHits.add(String.valueOf(hits));
		avgHits += hits;
		if (maxHits < hits)
			maxHits = hits;
		if (peakInfection < Constants.countInfected)
			peakInfection = Constants.countInfected;
	}

	int hits = 0;

	// N agents inside each poi interact with each other
	// O(P+N+P* (N/P)^2 ) = O(N + N/P)
	public void updateMultiAgents1() {
		hits = 0;

		// clear all pois (O(P))
		for (int j = 0; j < fixedPois.length; ++j) {
			for (int k = 0; k < fixedPois[j].length; ++k) {
				fixedPois[j][k].clearAgents();
			}
		}

		// add agents to neighboring pois (O(N))
		for (int i = 0; i < agents.length; ++i) {
			checkIfAgentInsidePOI(agents[i]);
		}

		// clear stats for this iteration
		// sicarqd2.resetInfectedCount();

		// all agents interact only inside each poi
		// for each fixed poi (O(P))*
		for (int j = 0; j < fixedPois.length; ++j) {
			for (int k = 0; k < fixedPois[j].length; ++k) {
				// for each agent pair inside each poi[][] (O((N/P)^2))
				for (int i1 = 0; i1 < fixedPois[j][k].agents.size() - 1; ++i1) {
					for (int i2 = i1 + 1; i2 < fixedPois[j][k].agents.size(); ++i2) {
						if (fixedPois[j][k].agents.get(i1).pos
								.distance(fixedPois[j][k].agents.get(i2).pos) <= Constants.INTERACT_RADIUS) {
							hits++;

							// infect the other node with probability
//							if (fixedPois[j][k].agents.get(i1).isInfectious()
//									&& fixedPois[j][k].agents.get(i2).isSusceptible()
//									&& Constants.rand.nextFloat() < Constants.INCUBATING_PROBABILITY) {
//
//								fixedPois[j][k].agents.get(i2).state = State.INC;
//								Constants.countNewCases++;
//							}
//							if (fixedPois[j][k].agents.get(i2).isInfectious()
//									&& fixedPois[j][k].agents.get(i1).isSusceptible()
//									&& Constants.rand.nextFloat() < Constants.INCUBATING_PROBABILITY) {
//
//								fixedPois[j][k].agents.get(i1).state = State.INC;
//								Constants.countNewCases++;
//							}
						}
						// sicarqd2.updateCounter(fixedPois[j][k].agents.get(i1));
					}
				}
			}
		}
		// update infected count
		// sicarqd2.updateInfectedCount();

		sicarqd.update();
	}

	int checkIfAgentInsidePOI(Agent agent) {
		for (int j = 0; j < fixedPois.length; ++j) {
			for (int k = 0; k < fixedPois[j].length; ++k) {
				if (agent.pos.distance(fixedPois[j][k].pos) <= Constants.INTERACT_RADIUS) {
					fixedPois[j][k].addAgent(agent);
					return 1;
				}
			}
		}
		return 0;
	}

	// O((N^2)/2): all agents interact with all other agents ANYWHERE on the map
	public void updateMultiAgents2() {
		hits = 0;
		// clear stats for this iteration
		// sicarqd2.resetInfectedCount();

		for (int i = 0; i < agents.length - 1; ++i) {
			for (int j = i + 1; j < agents.length; ++j) {
				if (agents[i].isInfectious() && agents[j].isSusceptible()) {
					if (agents[i].pos.distance(agents[j].pos) <= Constants.INTERACT_RADIUS) {
						hits++;

						// infect the other node with probability
						if (Constants.rand.nextFloat() < Constants.INCUBATING_PROBABILITY) {
							agents[j].state = State.INC;
							//Constants.countNewCases++;
							// sicarqd2.updateCounter(agents[j]);
						}
					}
				} else if (agents[j].isInfectious() && agents[i].isSusceptible()) {
					if (agents[i].pos.distance(agents[j].pos) <= Constants.INTERACT_RADIUS) {

						if (Constants.rand.nextFloat() < Constants.INCUBATING_PROBABILITY) {
							agents[i].state = State.INC;
							//Constants.countNewCases++;
							// sicarqd2.updateCounter(agents[i]);
						}
					}
				}
			}

		}

		// sicarqd2.updateInfectedCount();
		sicarqd.update();
	}
}
