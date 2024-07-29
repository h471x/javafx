package app.models;

public class FlightRecord {

    private final String numPssrt;
    private final int numVol;

    public FlightRecord(String numPssrt, int numVol) {
        this.numPssrt = numPssrt;
        this.numVol = numVol;
    }

    public String getNumPssrt() {
        return numPssrt;
    }

    public int getNumVol() {
        return numVol;
    }
}

