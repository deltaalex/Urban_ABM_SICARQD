package simulator;

import automata.urban24.Constants;
import automata.urban24.QuarantinePolicy;
import automata.urban24.RelapseScenario;
import automata.urban24.UrbanMobility;

public class TestUrbanMobility {

    public static void main(String[] args) {
        Constants.AGENTS = 1000;
        Constants.NUM_POIS = 10;
        Constants.NUM_SEEDS = 10;
        Constants.MAX_DIST = 800;
        Constants.relapseScenario = RelapseScenario.SLOW;
        Constants.quarantinePolicy = QuarantinePolicy.LATE;
        Constants.QUARANTINE_RATIO = 0.5f;

        int agents[] = new int[]{100, 250, 500, 1000, 1500, 2500};
        int parcelSizes[] = new int[]{1000, 500, 250, 100, 50, 25};
        int personalPOIs[] = new int[]{1, 2, 5, 10, 20, 50};
        float quarantineRatios[] = new float[]{0, .2f, .4f, .6f, .8f, 1f};
        int maxDistances[] = new int[]{100, 200, 300, 400, 500, 1000};

        for (int agent : agents) {
            Constants.AGENTS = agent;

            System.out.print("\nA=" + agent + ",");

//            for (int parcel : parcelSizes) {
//                Constants.updateParcelSize(parcel);

//            for (int ppois : personalPOIs) {
//                Constants.NUM_POIS = ppois;

//			for (float qr : quarantineRatios) {
//				Constants.QUARANTINE_RATIO = qr;

            for (int maxD : maxDistances) {
                Constants.MAX_DIST = maxD;

                // reset simulator
                UrbanMobility urbanMobility = new UrbanMobility();
                urbanMobility.simulate();
            }
        }
    }
}
