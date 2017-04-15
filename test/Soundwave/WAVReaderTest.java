/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Soundwave;

import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Temporal_Creator
 */
public class WAVReaderTest {
    static final double EPSILON = 1.0E-12;
    private PureWave purewave;
    private WAVReader wavefile;

    @Before
    public void setUp() {
        purewave = new PureWave("sine", 1, 800, 3, 44100);
        wavefile = new WAVReader(purewave);
    }

    @After
    public void tearDown() {
        wavefile = null;
        purewave = null;
    }

    /**
     * Test of downSampling method, of class WAVReader.
     */
    @Test
    public void testDownSampling() throws IOException, UnsupportedAudioFileException {
        System.out.println("downSampling");
        float sampleRate = 400.0F;
        wavefile.downSampling(sampleRate);
        assertEquals(400.0F, wavefile.getAudioFormat().getSampleRate(), EPSILON);
    }

    /**
     * Test of addEcho method, of class WAVReader.
     */
    @Test
    public void testAddEcho() {
        System.out.println("addEcho");
        double delayTime = 1000.0;
        double decayValue = 0.08;
        byte[] testArray = Arrays.copyOf(wavefile.getByteBuffer(),
                                         wavefile.getByteBuffer().length);
        wavefile.addEcho(delayTime, decayValue);
        assertEquals(testArray.length, wavefile.getByteBuffer().length);
    }

    /**
     * Test of saveToWAV method, of class WAVReader.
     */
    @Test
    public void testSaveToWAV() throws Exception {
        System.out.println("saveToWAV");
        String fileName = "outAudio.wav";
        wavefile.saveToWAV(fileName);
        WAVReader testWave = new WAVReader(fileName);
        assertArrayEquals(wavefile.getByteBuffer(), testWave.getByteBuffer());
    }
}
