package SoundGUI.model;

import SoundGUI.utility.WAVReader;

public class SoundModel {
    private WAVReader wavefile;
//    private PureWave pureWave;

    public SoundModel() {
    }

    public void loadWaveFile(WAVReader wavefile) {
        this.wavefile = wavefile;
    }

    public WAVReader getWavefile() {
        return wavefile;
    }
}
