package SoundGUI.utility;

import SoundGUI.model.PureWave;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.sound.sampled.*;

public class WAVReader {
    // File chose by the user
    private File file;
    // A AudioStream attribute of a wave
    private AudioInputStream audioStream;
    // A AudioFormat attribute of a wave
    private AudioFormat audioFormat;
    //Byte buffer of the wave form
    private byte[] byteBuffer;

    /**
     * Construct a WAVReader class with given file name
     *
     * @param fileName
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    public WAVReader(String fileName) throws UnsupportedAudioFileException, IOException {
        this.file = new File(fileName);
        this.audioStream = AudioSystem.getAudioInputStream(this.file);
        this.audioFormat = this.audioStream.getFormat();
        this.generateByteArray();
        this.audioStream = AudioSystem.getAudioInputStream(this.file);
    }

    /**
     * Construct a WAVReader object with a pure wave
     *
     * @param pureWave
     */
    public WAVReader(PureWave pureWave) {
        this.audioStream = pureWave.getAudioStream();
        this.audioFormat = this.audioStream.getFormat();
        this.byteBuffer = pureWave.getByteBuffer();
    }

    /**
     * Get the file name
     *
     * @return fileName
     */
    public String getFileName() {
        return this.file.getName();
    }

    /**
     * Get the byte buffer for the wave
     *
     * @return byteBuffer
     */
    public byte[] getByteBuffer() {
        return byteBuffer;
    }

    /**
     * Get AudioInputStream of the wave
     *
     * @return audioStream
     */
    public AudioInputStream getAudioStream() {
        return audioStream;
    }

    /**
     * Get the format of the audio
     *
     * @return audioFormat
     */
    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public long getLengthTime() {
        return (long) (audioStream.getFrameLength() / audioFormat.getFrameRate());
    }

    /**
     * Generate byte array
     *
     * This code was based on information on stackoverflow.com
     *
     * @see
     * <a href=http://stackoverflow.com/questions/10397272/wav-file-convert-to-byte-array-in-java>
     * http://stackoverflow.com/questions/10397272/wav-file-convert-to-byte-array-in-java</a>
     * @return @throws IOException
     */
    private void generateByteArray() throws IOException {
        if (this.file != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read;
            byte[] buff = new byte[1024];
            while ((read = this.audioStream.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
            out.flush();
            byte[] audioBytes = out.toByteArray();
            this.byteBuffer = audioBytes;
        }
    }

    /**
     * Generate Short Array (only use for 16 bits wave file)
     *
     * @return shortBuffer
     */
    public short[] generateShortArray() {
        short[] shortBuffer;
        if (this.audioFormat.getSampleSizeInBits() == 8) {
            shortBuffer = new short[this.getByteBuffer().length];
            for (int i = 0; i < shortBuffer.length; i++) {
                shortBuffer[i] = (short) ((int) this.getByteBuffer()[i] & 0xff);
                shortBuffer[i] -= 127;
            }
        } else {
            shortBuffer = new short[this.getByteBuffer().length / 2];
            if (this.getAudioFormat().isBigEndian()) {
                ByteBuffer.wrap(byteBuffer).order(ByteOrder.BIG_ENDIAN).asShortBuffer().get(
                        shortBuffer);
            } else if (!this.getAudioFormat().isBigEndian()) {
                ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(
                        shortBuffer);
            }
        }
        return shortBuffer;
    }

    /**
     * Generate byte buffer from short buffer
     *
     * @param shortBuffer
     */
    public void fromShortToByte(short[] shortBuffer) {
        byte[] bytesCopy = new byte[shortBuffer.length * 2];
        if (this.getAudioFormat().isBigEndian()) {
            ByteBuffer.wrap(bytesCopy).order(ByteOrder.BIG_ENDIAN).asShortBuffer().put(
                    shortBuffer);
        } else if (!this.getAudioFormat().isBigEndian()) {
            ByteBuffer.wrap(bytesCopy).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(
                    shortBuffer);
        }
        System.arraycopy(bytesCopy, 0, this.byteBuffer, 0,
                         this.byteBuffer.length);
    }

    /**
     * Down sample a wave with given sample frequency
     *
     * This code was based on information on stackoverflow.com
     *
     * @throws java.io.IOException
     * @throws javax.sound.sampled.UnsupportedAudioFileException
     * @see
     * <a href=http://stackoverflow.com/questions/21732090/java-downsampling-from-22050-to-8000-gives-zero-bytes>
     * http://stackoverflow.com/questions/21732090/java-downsampling-from-22050-to-8000-gives-zero-bytes</a>
     * @param sampleRate
     */
    public void downSampling(float sampleRate) throws IOException, UnsupportedAudioFileException {
        this.audioFormat = new AudioFormat(
                this.audioFormat.getEncoding(), sampleRate,
                this.audioFormat.getSampleSizeInBits(),
                this.audioFormat.getChannels(),
                this.audioFormat.getFrameSize(),
                sampleRate,
                this.audioFormat.isBigEndian());
        this.audioStream = AudioSystem.getAudioInputStream(audioFormat,
                                                           audioStream);
        File output = new File("out.wav");
        int nWrittenBytes = AudioSystem.write(audioStream,
                                              AudioFileFormat.Type.WAVE,
                                              output);
        System.out.println("Downsampled file save to out.wav");
    }

    /**
     * Add echo to the give waveform
     *
     * This code was based on information on stackoverflow.com and java2s.com
     *
     * @see
     * <a href=http://stackoverflow.com/questions/5625573/byte-array-to-short-array-and-back-again-in-java>
     * http://stackoverflow.com/questions/5625573/byte-array-to-short-array-and-back-again-in-java</a>
     * <a href=http://www.java2s.com/Code/Java/Development-Class/Anexampleofplayingasoundwithanechofilter.htm>
     * http://www.java2s.com/Code/Java/Development-Class/Anexampleofplayingasoundwithanechofilter.htm</a>
     *
     * @param delayTime
     * @param decayValue
     */
    public void addEcho(double delayTime, double decayValue) {
        short[] shortBuffer = new short[byteBuffer.length / 2];
        int[] intBuffer = new int[this.byteBuffer.length];
        //byte to short
        ByteBuffer.wrap(byteBuffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(
                shortBuffer);
        int sampleDelay = (int) (this.audioFormat.getFrameRate() * delayTime * this.audioFormat.getChannels());
        //short to int
        for (int i = 0; i < shortBuffer.length - sampleDelay; i++) {
            intBuffer[i] = (int) shortBuffer[i];
        }

        for (int i = 0; i < this.byteBuffer.length - sampleDelay; i++) {
            int value = (int) ((float) intBuffer[i] * decayValue + (float) intBuffer[i + sampleDelay]);
            intBuffer[i + sampleDelay] = value;
        }
        //int to short
        for (int i = 0; i < shortBuffer.length - sampleDelay; i++) {
            shortBuffer[i + sampleDelay] = (short) intBuffer[i + sampleDelay];
        }
        //short to byte
        byte[] bytesCopy = new byte[shortBuffer.length * 2];
        ByteBuffer.wrap(bytesCopy).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(
                shortBuffer);
        System.arraycopy(bytesCopy, 0, this.byteBuffer, 0,
                         this.byteBuffer.length);
    }

    /**
     * Hard coded constants for the effect of reverb
     */
    private static final double DEFAULT_DELAY_TIME = 500 * 0.001;
    private static final double DEFAULT_DECAY_VALUE = 0.4;
    private static final int REPEAT_NUM = 2;

    /**
     * Add reverb sound effect to the wave
     *
     * @param wavefile
     * @see
     * <a href="http://codes.google.com/p/dep-jp/">
     * http://codes.google.com/p/dep-jp/</a>
     * In the directory of
     * <source/browse/trunk/andeb1/sound/t.egg/examples/SoundFilter/src/jp/andeb/sound/filter/Reverb.java>
     */
    public void reverb(WAVReader wavefile) {
        double delayTime = DEFAULT_DELAY_TIME;
        double decayValue = DEFAULT_DECAY_VALUE;
        for (int i = 1; i < REPEAT_NUM; i++) {
            wavefile.addDelay(delayTime, decayValue);
            decayValue = Math.pow(decayValue, i);
        }
        System.out.println("Reverb done.");
    }

    /**
     * A helper function for the reverb
     *
     * @param delayTime
     * @param decayValue
     */
    public void addDelay(double delayTime, double decayValue) {
        int sampleDelay = (int) (this.audioFormat.getFrameRate() * delayTime);
        for (int i = 0; i < this.byteBuffer.length - sampleDelay; i++) {
            this.byteBuffer[i + sampleDelay] += (byte) ((float) this.byteBuffer[i] * decayValue);
        }
    }

    /**
     * Prints out the information of the wave
     *
     * This code was based on information on oracle's Java API docs
     *
     * @see
     * <a href=https://docs.oracle.com/javase/8/docs/api/>
     * https://docs.oracle.com/javase/8/docs/api/</a>
     * @throws IOException
     */
    public void displayInformation() throws IOException {
        System.out.format("Number of channels: %d\n"
                          + "Bits per sample: %d\n"
                          + "Sample rate: %f\n"
                          + "Frame rate: %f\n"
                          + "Length of waveform in frames: %d\n"
                          + "Length of waveform in bytes: %d\n"
                          + "Byte order: %s\n", audioFormat.getChannels(),
                          audioFormat.getSampleSizeInBits(),
                          audioFormat.getSampleRate(),
                          audioFormat.getFrameRate(),
                          audioStream.getFrameLength(),
                          this.audioFormat.getFrameSize() * this.audioStream.getFrameLength(),
                          (audioFormat.isBigEndian() ? "Big endian" : "Little endian"));

    }

    /**
     * Save a file with given fileName
     *
     * @param fileName
     * @throws IOException
     */
    public void saveToWAV(String fileName) throws IOException {
        File out = new File(fileName);
        AudioSystem.write(this.audioStream, AudioFileFormat.Type.WAVE,
                          out);
        System.out.println("Save new audio to " + fileName);
    }
}
