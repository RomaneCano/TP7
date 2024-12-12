package champollion;

public class ServicePrevu {
    private int volumeCM;
    private int volumeTD;
    private int volumeTP;
    private UE ue;

    public ServicePrevu(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        this.ue = ue;
        this.volumeCM = volumeCM;
        this.volumeTD = volumeTD;
        this.volumeTP = volumeTP;
    }

    public int getVolumeCM() { return volumeCM; }

    public int getVolumeTD() { return volumeTD; }

    public int getVolumeTP() { return volumeTP; }

    public UE getUe() { return ue; }

}
