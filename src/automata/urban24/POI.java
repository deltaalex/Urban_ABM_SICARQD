package automata.urban24;

import java.util.ArrayList;

public class POI {
	// coordinates
	PVector2D pos;
	// currently visiting agents
	ArrayList<Agent> agents;

	POI(float x, float y) {
		pos = new PVector2D(x * Constants.getParcelSizeX(), y * Constants.getParcelSizeY());
		agents = new ArrayList<Agent>();
		//System.out.println(pos.x+","+pos.y);
	}

	void clearAgents() {
		agents.clear();
	}

	void addAgent(Agent agent) {
		agents.add(agent);
	}
}