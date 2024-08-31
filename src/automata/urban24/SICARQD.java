package automata.urban24;

import java.util.Date;
import java.util.Random;

/** implements infectious lifecycle for the human population */
public class SICARQD {
	Agent agents[];
	private Random random = new Random(new Date().getTime());

	SICARQD(Agent nodes[]) {
		this.agents = nodes;
	}

	public void update() {
		Constants.countS = Constants.countI = Constants.countC = Constants.countA = Constants.countQ = Constants.countR = Constants.countD = Constants.countInfected = 0;

		// for each pair of nodes (ni, nj)
		for (int i = 0; i < agents.length - 1; ++i) {
			for (int j = i + 1; j < agents.length; ++j) {

				// if the two nodes are adjacent (<24 points distance)
				if (agents[i].pos.distance(agents[j].pos) <= Constants.INTERACT_RADIUS) {

					// infect the other node with probability
					if (agents[i].isInfectious() && agents[j].isSusceptible()
							&& random.nextFloat() < Constants.INCUBATING_PROBABILITY) {
						agents[j].state = State.INC;
						Constants.countNewCases++;
					}
					if (agents[j].isInfectious() && agents[i].isSusceptible()
							&& random.nextFloat() < Constants.INCUBATING_PROBABILITY) {
						agents[i].state = State.INC;
						Constants.countNewCases++;
					}
				}
			}
			updateCounter(agents[i]);
		}
		Constants.countInfected = Constants.countI + Constants.countC + Constants.countA + Constants.countQ;
	}

	private void updateCounter(Agent node) {
		switch (node.state) {
		case SUS:
			Constants.countS++;
			break;
		case INC:
			Constants.countI++;
			break;
		case CTG:
			Constants.countC++;
			break;
		case AWR:
			Constants.countA++;
			break;
		case QRT:
			Constants.countQ++;
			break;
		case REC:
			Constants.countR++;
			break;
		case DEAD:
			Constants.countD++;
			break;
		default:
			break;
		}
	}
}