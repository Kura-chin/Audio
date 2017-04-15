package SoundGUI.model;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.*;

public class PureWave {
    // A string that represents wave type
    private String waveType;
    // A double that represents amplitude of the wave
    private double amp;
    // A double that represents frequency of the wave
    private double frequency;
    // A double that represents length of the wave in seconds
    private double lengthInTime;
    // A double that represents sample rate of the wave
    private double sampleRate;
    // A float array that represents sample point of the wave
    private float[] samplePoint;
    // A byte array that represents byte buffer of the wave
    private byte[] byteBuffer;
    // A AudioFormat attribute of a wave
    private AudioFormat audioFormat;
    // A AudioInputStream attribute of a wave
    private AudioInputStream audioStream;

    /**
     * Constructor to initialize a pure wave
     *
     * @param wave_type
     * @param amp
     * @param frequency
     * @param length_in_time
     * @param sampleRate
     */
    public PureWave(String wave_type, double amp, double frequency,
                    double length_in_time, double sampleRate) {
        this.waveType = wave_type;
        this.amp = amp;
        this.frequency = frequency;
        this.lengthInTime = length_in_time;
        this.sampleRate = sampleRate;
        this.samplePoint = new float[(int) (this.lengthInTime * this.sampleRate)];
        this.byteBuffer = new byte[this.samplePoint.length * 2];
        this.generateArray();
        this.generateByteArray();
        this.toAudioStream();
    }

    /**
     * Get the type of the wave
     *
     * @return waveType
     */
    public String getWaveType() {
        return this.waveType;
    }

    /**
     * Get the value of the amplitude
     *
     * @return amp
     */
    public double getAmplitude() {
        return this.amp;
    }

    /**
     * Get the value of the frequency
     *
     * @return frequency
     */
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * Get the length of the wave in seconds
     *
     * @return lengthInTime
     */
    public double getTime() {
        return this.lengthInTime;
    }

    /**
     * Get the value of the sample rate
     *
     * @return sampleRate
     */
    public double getSampleRate() {
        return this.sampleRate;
    }

    /**
     * Get the array of the sample point
     *
     * @return samplePoint
     */
    public float[] getSamplePoint() {
        return this.samplePoint;
    }

    /**
     * Get the AudioFormat of of wave
     *
     * @return audioFormat
     */
    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    /**
     * Get the AudioInputStream of of wave
     *
     * @return audioStream
     */
    public AudioInputStream getAudioStream() {
        return audioStream;
    }

    /**
     * Get the byte array of the byte buffer
     *
     * @return byteBuffer
     */
    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    /**
     * Generate array given the user input
     */
    private void generateArray() {
        if (this.getWaveType().equalsIgnoreCase("Sine")) {
            for (int sample = 0; sample < this.samplePoint.length; sample++) {
                double time = sample / sampleRate;
                double w_t = Math.sin(2 * Math.PI * frequency * time);
                this.samplePoint[sample] = (float) ((float) amp * w_t);
            }
        } else if (this.getWaveType().equalsIgnoreCase("Square")) {
            for (int sample = 0; sample < this.samplePoint.length; sample++) {
                double time = sample / sampleRate;
                double w_t = Math.sin(2 * Math.PI * frequency * time);
                if (w_t < 0) {
                    this.samplePoint[sample] = (float) ((float) amp * (-1));
                } else if (w_t == 0) {
                    this.samplePoint[sample] = (float) (0);
                } else if (w_t > 0) {
                    this.samplePoint[sample] = (float) ((float) amp * 1);
                }
            }
//        } else if (this.getWaveType() == "Triangle") {
//            for (int sample = 0; sample < this.samplePoint.length; sample++) {
//                double time = sample / sampleRate;
//                double w_t = Math.sin(2 * Math.PI * frequency * time);
//                this.samplePoint[sample] = (float) ((float) amp * w_t);
//            }
        } else if (this.getWaveType().equalsIgnoreCase("Sawtooth")) {
            for (int sample = 0; sample < this.samplePoint.length; sample++) {
                double time = sample / this.sampleRate;
                double period = (double) 1 / this.getFrequency();
                this.samplePoint[sample] = (float) ((float) amp * 2 * (time / period - (int) (1 / 2 + time / period)));
            }
        }
    }

    /**
     * Generate byte array of the given byte buffer
     *
     * This code was based on information on stackoverflow.com
     *
     * @see
     * <a href=http://stackoverflow.com/questions/31931618/i-want-to-convert-byte-into-float-but-just-the-first-4-numbers-are-correct>
     * http://stackoverflow.com/questions/31931618/i-want-to-convert-byte-into-float-but-just-the-first-4-numbers-are-correct</a>
     */
    private void generateByteArray() {
        int bufferIndex = 0;
        for (int i = 0; i < this.byteBuffer.length; i++) {
            int x = (int) (this.samplePoint[bufferIndex++] * 32767.0);
            this.byteBuffer[i] = (byte) x;
            i++;
            this.byteBuffer[i] = (byte) (x >>> 8);
        }
    }

    /**
     * Generate audio format and audio stream
     *
     * This code was based on information on oracle's Java API docs
     *
     * @see
     * <a href=https://docs.oracle.com/javase/8/docs/api/>
     * https://docs.oracle.com/javase/8/docs/api/</a>
     */
    private void toAudioStream() {
        boolean bigEndian = false;
        boolean signed = true;
        int bits = 16;
        int channels = 1;
        this.audioFormat = new AudioFormat((float) this.sampleRate, bits,
                                           channels,
                                           signed, bigEndian);
        ByteArrayInputStream bais = new ByteArrayInputStream(this.byteBuffer);
        this.audioStream = new AudioInputStream(bais, audioFormat,
                                                this.samplePoint.length);
    }

}
