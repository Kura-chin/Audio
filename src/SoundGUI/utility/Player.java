package SoundGUI.utility;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.*;
import javax.swing.SwingWorker;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

public class Player extends SwingWorker {
    // A instance variable for wavefile
    private WAVReader wavefile;
    // A AudioInputStream attribute of a wave
    private AudioInputStream audioStream;
    // A AudioFormat attribute of a wave
    private AudioFormat audioFormat;
    // A audio info attribute of a wave
    private DataLine.Info audioInfo;
    // A SourceDataLine of a wave
    private SourceDataLine audioLine;
    boolean playCompleted;
    // byte array for a given wave
    private byte[] byteArray;
    // Thread that will be used for multithreading
    private Thread t;

    private boolean isPlaying;
    private int sampleAdjustment = 0;
    private int markStart = 0;

    /**
     * Constructor for player class
     *
     * @param wavefile
     * @throws LineUnavailableException
     * @throws IOException
     */
    public Player(WAVReader wavefile) throws LineUnavailableException, IOException {
        this.wavefile = wavefile;
        this.audioStream = wavefile.getAudioStream();
        this.audioFormat = wavefile.getAudioFormat();
        this.byteArray = wavefile.getByteBuffer();
        this.audioInfo = new DataLine.Info(SourceDataLine.class,
                                           this.audioFormat);
        this.isPlaying = false;
    }

    /**
     * Get AudioInputStream of the wave form
     *
     * @return audioStream
     */
    public AudioInputStream getStream() {
        return this.audioStream;
    }

    /**
     * Get AudioFormat of the wave form
     *
     * @return audioFormat
     */
    public AudioFormat getFormat() {
        return this.audioFormat;
    }

    /**
     * A main method for playing wave form
     *
     * This code is based on Oracle's official Java doc
     *
     * @see
     * <a href=https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/DataLine.html>
     * https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/DataLine.html</a>
     * @throws LineUnavailableException
     * @throws IOException
     */
    public void play() throws LineUnavailableException, IOException {
        isPlaying = true;
        if (this.audioLine == null) {
            this.audioLine = (SourceDataLine) AudioSystem.getLine(audioInfo);
            this.audioLine.open(this.audioFormat);
            this.audioLine.start();
            this.audioLine.write(byteArray, 0, byteArray.length);
            this.audioLine.drain();
            this.audioLine.stop();
            this.audioLine.close();
            this.audioLine = null;
        } else {
            this.audioLine.start();
            this.audioLine.write(byteArray, 0, byteArray.length);
            this.audioLine.drain();
            this.audioLine.stop();
            this.audioLine.close();
            this.audioLine = null;
        }
    }

    public void pause() {
        if (isPlaying) {
            isPlaying = false;
            audioLine.stop();
            audioLine.flush();
//            try {
//                setSamplePosition(getSamplePosition());     // rewind past the flushed bytes
//            }
//            audioData.analyzeExcerpts();
        }
    }

    public void stop() {
        isPlaying = false;
        audioLine.stop();
        audioLine.flush();
        this.audioLine.close();
        this.audioLine = null;
    }

    /**
     * Accesses the current position (index into the audio file's samples) at
     * which the audio is playing or paused.
     *
     * @return
     */
    public int getSamplePosition() {
        return audioLine.getFramePosition() + sampleAdjustment;
    }

    /**
     * If paused, sets the current audio position (index into the audio file's
     * samples) to the given index.
     *
     * @param sampleNum the index into the audio file's samples at which to set
     * the position
     * @throws IOException if there is a problem accessing the audio file
     * @throws InternalException if the player is playing
     */
    public void setSamplePosition(int sampleNum) throws InternalException, IOException {
        if (isPlaying) {
            throw new InternalException("Can't set audio position while playing");
        }
        closeAudio();
        sampleAdjustment = sampleNum;
        int bytePosition = sampleNum * audioStream.getFormat().getFrameSize();
        audioStream.skip(bytePosition);
    }

    /**
     * If audioStream and/or audioLine are open, closes them. Otherwise does
     * nothing.
     *
     * @throws IOException if there is a problem closing audioStream or
     * audioLine
     */
    private void closeAudio() throws IOException {
        if (audioLine != null) {
            audioLine.close();
        }
        if (audioStream != null) {
            audioStream.close();
        }
    }

    /**
     * A method to adjust volume of the waveform by manipulating the byte array
     *
     * @param percentage
     */
    public void adjustVolume(float percentage) {
        ByteOrder endianType;
        if (this.audioFormat.isBigEndian()) {
            endianType = ByteOrder.BIG_ENDIAN;
        } else {
            endianType = ByteOrder.LITTLE_ENDIAN;
        }
        short[] shortArray = new short[this.byteArray.length / 2];
        ByteBuffer.wrap(this.byteArray).order(endianType).asShortBuffer().get(
                shortArray);
        for (int i = 0; i < shortArray.length; i++) {
//            this.byteArray[i] = (byte) ((float) (this.byteArray[i]) + (float) (this.byteArray[i]) * percentage / 100);
            shortArray[i] = (short) ((float) (shortArray[i]) + (float) (shortArray[i]) * percentage / 100);
        }
        ByteBuffer.wrap(this.byteArray).order(endianType).asShortBuffer().put(
                shortArray);
    }

    /**
     * A run method that play the audio
     *
     * This code was based on information on tutorialspoint.com
     *
     * @see
     * <a href=http://www.tutorialspoint.com/java/java_multithreading.htm>
     * http://www.tutorialspoint.com/java/java_multithreading.htm</a>
     */
//    @Override
//    public void run() {
//        try {
//            this.play();
//        } catch (LineUnavailableException | IOException ex) {
//            Logger.getLogger(WAVReadinClient.class.getName()).log(
//                    Level.SEVERE, null, ex);
//        }
//    }
    /**
     * A start method that utilizes multi-threading to play audio
     *
     * This code was based on information on tutorialspoint.com
     *
     * @see
     * <a href=http://www.tutorialspoint.com/java/java_multithreading.htm>
     * http://www.tutorialspoint.com/java/java_multithreading.htm</a>
     */
    public void start() {
        System.out.println("Playing ");
        if (t == null) {
            t = new Thread(this);
            t.start();
            t = null;
        }
    }

    @Override
    protected Object doInBackground() throws Exception {
        play();
        return null;
    }
}
