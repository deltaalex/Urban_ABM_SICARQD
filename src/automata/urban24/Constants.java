package automata.urban24;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Alexander
 */
public class Constants {

	public static int AGENTS = 1000;
	public static int NUM_POIS = 10; // number of POIS along mobility path (except home) // 10
	public static int NUM_SEEDS = 100; // initially infectious
	public final static int SIM_WIDTH = 1000; // 1000
	public final static int SIM_HEIGHT = 1000; // 1000
	private static int PARCEL_SIZE_X = 100; // 100
	private static int PARCEL_SIZE_Y = 100; // 100
	private static int PSx = SIM_WIDTH / PARCEL_SIZE_X;
	private static int PSy = SIM_HEIGHT / PARCEL_SIZE_Y;
	public static int MAX_SIMULATION = 5000;

	public final static int NODE_SPEED = 2; // 2
	public final static int POI_MIN_DIST = 2 * NODE_SPEED; // when within a POI
	public static int MAX_DIST = 800; // 500 = 50% of width
	public final static int INTERACT_RADIUS = 4 * NODE_SPEED; // 4*2 // infection transmission
	public final static int STAY_HOME = 10; // 365 max time to stay home

	public static float QUARANTINE_RATIO = 0.5f; // {0f,.2f,.4f,.6f,.8f,1f}
	public static RelapseScenario relapseScenario = RelapseScenario.SLOW; // QUICK, LONG, NEVER
	public static QuarantinePolicy quarantinePolicy = QuarantinePolicy.LATE; // NONE, LATE, EARLY

	public final static int NODE_SIZE = 4;
	public final static int PADDING = 50;
	// record only every number of iterations
	public final static int RECORD_STEP = 20;
	public final static boolean debugSaveHits = false;

	// public final static float INFECTIOUS_PROXIMITY = 3 * NODE_SPEED; // 3*2
	public final static int DAY = 20; // 20
	public final static float INCUBATING_PROBABILITY = 0.5f / DAY;// 50% chance to become I
	public final static float CONTAGIOUS_PROBABILITY = 0.182f / DAY; // 5.5 days avg (0-5.5)
	public final static float AWARE_PROBABILITY = 0.2f / DAY; // 5 days avg (5.5-10.5)
	public final static float RECOVERY_PROBABILITY = 0.0714f / DAY; // 14 days avg (10-24)
	public final static float DEATH_PROBABILITY = 0.034f; // ...no comment... 3.4%

	public final static float SUSCEPTIBLE_PROBABILITY_QUICK = 0.018f / DAY; // 2 months
	public final static float SUSCEPTIBLE_PROBABILITY_SLOW = 0.003f / DAY; // 1 year
	public final static float SUSCEPTIBLE_PROBABILITY_NEVER = 0f; // forever immune

	public final static int THRESHOLD_STOP = 2; // when outbreak is considered finished

	public static final Random rand = new Random(new Date().getTime());

	public final static ArrayList<String> edgelist = new ArrayList<String>();
//	public final static HashSet<ProximityEdge> edges = new HashSet<ProximityEdge>();

	// sum of distances from P0 to last POI and back to P0
	public final static ArrayList<Float> distances = new ArrayList<Float>();
	// distances from P0 to P1
	public final static ArrayList<Float> distancesP1 = new ArrayList<Float>();

	// count infectious numbers
	public static int countS, countI, countC, countA, countQ, countR, countD;
	public static int countInfected, countNewCases, countRelapsed;

	public static void saveStrings(String path, String[] lines) {
		try {
			File file = new File(path);
			PrintWriter pw = new PrintWriter(file);

			for (String line : lines) {
				pw.println(line);
			}

			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static int sign(float f) {
		if (f > 0)
			return 1;
		if (f < 0)
			return -1;
		return 0;
	}

	public static int randBetween(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}

	public static void updateParcelSize(int PS) {
		PARCEL_SIZE_X = PARCEL_SIZE_Y = PS;
		PSx = SIM_WIDTH / PARCEL_SIZE_X;
		PSy = SIM_HEIGHT / PARCEL_SIZE_Y;
	}

	public static int getParcelSizeX() {
		return PARCEL_SIZE_X;
	}

	public static int getParcelSizeY() {
		return PARCEL_SIZE_Y;
	}

	public static int getPSx() {
		return PSx;
	}

	public static int getPSy() {
		return PSy;
	}
}
