/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Soundwave;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Temporal_Creator
 */
public class FourierTest {
    static final double EPSILON = 1.0E-12;
    Complex[] x = new Complex[4];
    Complex comp1;
    Complex comp2;
    Complex comp3;
    Complex comp4;

    @Before
    public void setUp() {
        comp1 = new Complex(-0.03480425839330703);
        comp2 = new Complex(0.07910192950176387);
        comp3 = new Complex(0.7233322451735928);
        comp4 = new Complex(0.1659819820667019);
        x[0] = comp1;
        x[1] = comp2;
        x[2] = comp3;
        x[3] = comp4;
    }

    @After
    public void tearDown() {
        x[0] = null;
        x[1] = null;
        x[2] = null;
        x[3] = null;
        comp1 = null;
        comp2 = null;
        comp3 = null;
        comp4 = null;
    }

    /**
     * Test of fft method, of class Fourier.
     */
    @Test
    public void testFft() {
        System.out.println("fft");
        Complex expResult = new Complex(0.9336118983487516);
        int length = 4;
        Complex[] result = Fourier.fft(x);
        assertEquals(length, result.length);
        assertEquals(expResult, result[0]);
    }

    /**
     * Test of calPeak method, of class Fourier.
     */
    @Test
    public void testCalPeak() {
        System.out.println("calPeak");
        double expResult = 0.03480425839330703;
        double[] result = Fourier.calPeak(x);
        assertEquals(expResult, result[0], EPSILON);
    }
}
