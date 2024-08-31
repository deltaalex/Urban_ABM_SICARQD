package automata.urban24;

/* save infected numbers each iteration */
public class Recorder {
    int[] susceptible = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] incubating = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] contagious = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] aware = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] quarantined = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] recovered = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] dead = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] total = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int[] newCases = new int[Constants.MAX_SIMULATION / Constants.RECORD_STEP];
    int k = 0;
    int day = 0;

    public void record() {
        if (k == 0) {
            susceptible[day] = Constants.countS;
            incubating[day] = Constants.countI;
            contagious[day] = Constants.countC;
            aware[day] = Constants.countA;
            quarantined[day] = Constants.countQ;
            recovered[day] = Constants.countR;
            dead[day] = Constants.countD;
            total[day] = Constants.countInfected;
            newCases[day] = Constants.countNewCases;

            k = Constants.RECORD_STEP;
            day++;
        } else {
            k--;
        }
    }

    public void saveToCSV() {
        Constants.edgelist.clear();

        double maxTotal = getMaxTotal(total);
        double endTotal = getEndTotal(total, 10);
        double endDead = (1f * dead[day - 1] / Constants.AGENTS);
        double endRecov = getEndRecov(recovered, 10);
        int duration = day;

        Constants.edgelist.add("S,I,C,A,Q,R,D,T,N=," + Constants.countNewCases + ",MaxTotal=," + maxTotal
                + ",EndTotal=," + endTotal + ",EndDead=," + endDead + ",Relap=," + Constants.countRelapsed + ",Recov=,"
                + endRecov + ",Dur=," + duration);
        for (int i = 0; i < day; ++i) {
            Constants.edgelist
                    .add((1f * susceptible[i] / Constants.AGENTS) + "," + (1f * incubating[i] / Constants.AGENTS) + ","
                            + (1f * contagious[i] / Constants.AGENTS) + "," + (1f * aware[i] / Constants.AGENTS) + ","
                            + (1f * quarantined[i] / Constants.AGENTS) + "," + (1f * recovered[i] / Constants.AGENTS)
                            + "," + (1f * dead[i] / Constants.AGENTS) + "," + (1f * total[i] / Constants.AGENTS) + ","
                            + (1f * newCases[i] / Constants.AGENTS));
        }

        Constants.saveStrings("output/sq-" + Constants.getParcelSizeX() + "-" + Constants.relapseScenario + "-"
                        + Constants.quarantinePolicy + "-" + Constants.QUARANTINE_RATIO + "-" + Constants.AGENTS + ".csv",
                Constants.edgelist.toArray(new String[]{}));
    }

    private double getMaxTotal(int[] total) {
        int max = -1;
        for (int i = 0; i < day; ++i) {
            if (total[i] > max)
                max = total[i];
        }
        return (1f * max / Constants.AGENTS);
    }

    private double getEndTotal(int[] total, int k) {
        double sum = 0.0;
        if (day - k >= 0) {
            for (int i = day - k; i < day; ++i) {
                sum += total[i];
            }
            sum /= k;

        } else if (day - k / 2 >= 0) {
            for (int i = day - k / 2; i < day; ++i) {
                sum += total[i];
            }
            sum /= k / 2;
        } else {
            sum = total[day - 1];
        }
        return (1f * sum / Constants.AGENTS);
    }

    private double getEndRecov(int[] recovered, int k) {
        double sum = 0.0;
        if (day - k >= 0) {
            for (int i = day - k; i < day; ++i) {
                sum += recovered[i];
            }
            sum /= k;

        } else if (day - k / 2 >= 0) {
            for (int i = day - k / 2; i < day; ++i) {
                sum += recovered[i];
            }
            sum /= k / 2;
        } else {
            sum = recovered[day - 1];
        }
        return (1f * sum / Constants.AGENTS);
    }
}