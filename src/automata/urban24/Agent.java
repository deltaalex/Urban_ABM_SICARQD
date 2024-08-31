package automata.urban24;

/**
 * Entity carrying infection between its 'home' and 'away' locations.
 *
 * @author Alexander
 */
public class Agent {
    int id;
    // home and temporary destination
    PVector2D home, destination;
    // pois
    PVector2D pois[] = new PVector2D[Constants.NUM_POIS];
    // current position
    PVector2D pos;
    // poi id to visit next
    int k_poi = 0;
    // stay home duration [5,360]
    int stayHome = Constants.randBetween(5, Constants.STAY_HOME);
    // movement
    float dx, dy, d, vx, vy;
    // infectious state
    State state = State.SUS;

    Agent(int id, float x, float y) {
        this.id = id;
        this.home = new PVector2D(x, y);

        // generate pois
        for (int i = 0; i < Constants.NUM_POIS; ++i) {
            // this.pois[i] = new PVector2D(random(max(0, home.x-MAX_DIST/2),
            // min(home.x+MAX_DIST/2, width)),
            // random(max(0, home.y-MAX_DIST/2), min(home.y+MAX_DIST/2, height)));

            // only parcels in vicinity, bounded by +/-MAX_DIST/2
            PVector2D bounds[] = getParcelBounds(home);
            pois[i] = new PVector2D(
                    Constants.randBetween((int) (bounds[0].x), (int) (bounds[1].x + 1)) * Constants.getParcelSizeX(),
                    Constants.randBetween((int) (bounds[0].y), (int) (bounds[1].y + 1)) * Constants.getParcelSizeY());
        }

        resetAgentPath();
    }

    void update() {
        // update infectious state
        updateInfectiousState();

        // update movement
        if (stayHome > 0) {
            stayHome--;
            // delay(DELAY);
        }
        // move to next target
        else {
            // current destination is reached, generate next destination
            if (pos.distance(destination) < Constants.POI_MIN_DIST) {

                // if cycle finished, reset
                if (destination.equals(home)) {
                    resetAgentPath();
                } else {

                    k_poi++;
                    // if no furhter poi exists, return home
                    if (k_poi >= pois.length) {
                        destination = home;
                    }
                    // else pick next poi
                    else {
                        destination = pois[k_poi];
                    }

                    // update vx, vy
                    updateAgentDestination();
                }
            }
            // update position
            else {
                pos.x += vx;
                pos.y += vy;
            }
        }
    }

    void resetAgentPath() {
        this.pos = home.copy();
        this.stayHome = Constants.randBetween(5, Constants.STAY_HOME);
        this.k_poi = 0;
        this.destination = pois[0]; // [0] // crash point!

        // fill(random(120, 255), random(120, 255), random(120, 255));

        updateAgentDestination();
    }

    void  updateAgentDestination() {
        // define agent speed/direction
        dx = destination.x - pos.x;
        dy = destination.y - pos.y;
        d = Math.abs(dx / dy);

        vx = (float) (d * Constants.NODE_SPEED / Math.sqrt(d * d + 1) * Constants.sign(dx));
        vy = (float) (Constants.NODE_SPEED / Math.sqrt(d * d + 1) * Constants.sign(dy));
    }

    PVector2D[] getParcelBounds(PVector2D home) {
        PVector2D[] bounds = new PVector2D[2];
        float xmin = Math.max(0, home.x - Constants.MAX_DIST / 2);
        float xmax = Math.min(home.x + Constants.MAX_DIST / 2, Constants.SIM_WIDTH);
        float ymin = Math.max(0, home.y - Constants.MAX_DIST / 2);
        float ymax = Math.min(home.y + Constants.MAX_DIST / 2, Constants.SIM_HEIGHT);
        bounds[0] = new PVector2D();
        bounds[1] = new PVector2D();

        for (int x = 0; x <= Constants.getPSx(); ++x)
            if (x * Constants.getParcelSizeY() >= xmin) {
                bounds[0].x = x;
                break;
            }

        for (int x = Constants.getPSx(); x >= 0; --x)
            if (x * Constants.getParcelSizeX() <= xmax) {
                bounds[1].x = x;
                break;
            }

        for (int y = 0; y <= Constants.getPSy(); ++y)
            if (y * Constants.getParcelSizeY() >= ymin) {
                bounds[0].y = y;
                break;
            }

        for (int y = Constants.getPSy(); y >= 0; --y)
            if (y * Constants.getParcelSizeY() <= ymax) {
                bounds[1].y = y;
                break;
            }

        // println("P:"+bounds[0].x+"<->"+bounds[1].x+" x
        // "+bounds[0].y+"<->"+bounds[1].y);

        return bounds;
    }

    // S-I-C-A-R-Q-D transitions
    void updateInfectiousState() {

        // if susceptible - leave as it is

        // if recovered - move to S with probability based on relapse scenario
        if (state.equals(State.REC) && Constants.rand.nextFloat() < Constants.relapseScenario.getRelapseProbability()) {
            state = State.SUS;
            Constants.countRelapsed++;
        }

        // if quarantined || aware - move to recovered with probability
        if ((state.equals(State.QRT) || state.equals(State.AWR))
                && Constants.rand.nextFloat() < Constants.RECOVERY_PROBABILITY) {
            state = State.REC;
            if (Constants.rand.nextFloat() < Constants.DEATH_PROBABILITY) {
                state = State.DEAD;
            }
        }

        // if contagious - move to A with probability (and Q-early)
        if (state.equals(State.CTG) && Constants.rand.nextFloat() < Constants.AWARE_PROBABILITY) {
            state = State.AWR;
            // if any (late/early) quarantine is enabled
            if ((Constants.quarantinePolicy == QuarantinePolicy.LATE /* || quarantine == Quarantine.EARLY */)
                    && Constants.rand.nextFloat() < Constants.QUARANTINE_RATIO) {
                state = State.QRT;
            }
        }

        // if incubating - move to C with probability (and Q-early)
        if (state.equals(State.INC) && Constants.rand.nextFloat() < Constants.CONTAGIOUS_PROBABILITY) {
            state = State.CTG;
            // if early quarantine is enabled
            if (Constants.quarantinePolicy == QuarantinePolicy.EARLY
                    && Constants.rand.nextFloat() < Constants.QUARANTINE_RATIO) {
                state = State.QRT;
            }
        }
    }

    public boolean isSusceptible() {
        return state.equals(State.SUS);
    }

    public boolean isRecovered() {
        return state.equals(State.REC);
    }

    public boolean isInfectious() {
        return /* state.equals(State.INC) || */ state.equals(State.CTG) || state.equals(State.AWR);
    }
}