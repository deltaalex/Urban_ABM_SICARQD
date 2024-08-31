package automata.urban24;

public class UrbanMobility {
    private AgentModel agentModel;
    private Recorder recorder = new Recorder();
    private int simCounter = 0;

    public static void main(String[] args) {
        Constants.MAX_SIMULATION = 20*500;
        Constants.AGENTS = 2500;
        Constants.NUM_POIS = 10;
        Constants.NUM_SEEDS = 100;
        Constants.MAX_DIST = 800;
        Constants.relapseScenario = RelapseScenario.QUICK;
        Constants.quarantinePolicy = QuarantinePolicy.LATE;
        Constants.QUARANTINE_RATIO = 0.2f;

        new UrbanMobility().simulate();
    }

    public UrbanMobility() {
        Constants.countInfected = Constants.countNewCases = Constants.countRelapsed = 0;
        agentModel = new AgentModel();

        agentModel.setupMultiAgents();
    }

    long elapsed = 0, fpsCount = 0, fps = 0;

    public void simulate() {
        while (true) {

            // fps
            if (System.currentTimeMillis() - elapsed >= 1000) {
                fps = fpsCount;
                fpsCount = 0;
                elapsed = System.currentTimeMillis();
                // System.out.println("FPS:" + fps);
            } else {
                fpsCount++;
            }

            // drawSinglePathDemo();
            agentModel.drawMultiAgents();
            // interact only in fixed POIS
            agentModel.updateMultiAgents1(); // 150 FPS, 5-25 hits/frame (~+15%)
            // interact anywhere on the map
            // agentModel.updateMultiAgents2(); // 125 FPS, 60-120 hits/frame

            recorder.record();

            // stop simulation
            if (simCounter >= Constants.MAX_SIMULATION /* || Constants.countInfected <= Constants.THRESHOLD_STOP */) {
                saveSimulationStats();
                return;
            }

            simCounter++;
        }
    }

    private void saveSimulationStats() {
        if (Constants.debugSaveHits) {
            Constants.edgelist.clear();
            Constants.saveStrings("output/hits.csv", agentModel.numberOfHits.toArray(new String[]{}));
        }

        // System.out.println("Avg hits: " + (1f * agentModel.avgHits / simCounter));
        // System.out.println("Max hits: " + (1f * agentModel.maxHits));

        // System.out.println((1f * agentModel.avgHits / simCounter) + "," + (1f *
        // agentModel.maxHits));
        System.out.print((1f * agentModel.peakInfection / Constants.AGENTS) + ","
                + (1f * Constants.countNewCases / Constants.AGENTS) + ",");

        // save recorder data; uses edgelist
        recorder.saveToCSV();
    }

}
