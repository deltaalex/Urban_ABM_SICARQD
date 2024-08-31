package automata.urban24;

public enum RelapseScenario {
	QUICK, SLOW, NEVER;

	public float getRelapseProbability() {
		switch (this) {
		case QUICK:
			return Constants.SUSCEPTIBLE_PROBABILITY_QUICK;
		case SLOW:
			return Constants.SUSCEPTIBLE_PROBABILITY_SLOW;
		case NEVER:
			return Constants.SUSCEPTIBLE_PROBABILITY_NEVER;				
		default:
			return 0f;
		}
	}
}