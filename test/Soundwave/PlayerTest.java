/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Soundwave;

import java.io.IOException;
import java.util.Arrays;
import javax.sound.sampled.LineUnavailableException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Temporal_Creator
 */
public class PlayerTest {
    private PureWave purewave;
    private WAVReader wavefile;
    private Player player;

    @Before
    public void setUp() throws LineUnavailableException, IOException {
        purewave = new PureWave("sine", 1, 800, 3, 44100);
        wavefile = new WAVReader(purewave);
        player = new Player(wavefile);
    }

    @After
    public void tearDown() {
        player = null;
        wavefile = null;
        purewave = null;
    }

    /**
     * Test of adjustVolume method, of class Player.
     */
    @Test
    public void testAdjustVolume() {
        System.out.println("adjustVolume");
        float percentage = -50.0F;
        byte[] testArray = Arrays.copyOf(wavefile.getByteBuffer(),
                                         wavefile.getByteBuffer().length);
        player.adjustVolume(percentage);
        assertEquals(testArray.length, wavefile.getByteBuffer().length);
    }
}
