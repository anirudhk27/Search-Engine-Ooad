package util;

public class Tuple {
    private int documentId;
    private int Ftd;

    public Tuple(int documentId, int termDocumentFrequency) {
        this.documentId = documentId;
        this.Ftd = termDocumentFrequency;
    }

    public int getFtd() {
        return this.Ftd;
    }

    public int getDocumentId() {
        return documentId;
    }

    @Override
    public String toString() {
        return "<" + this.documentId + "," + this.Ftd + ">";
    }
}
