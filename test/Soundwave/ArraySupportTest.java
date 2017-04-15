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
 * @author Hung Giang
 */
public class ArraySupportTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of powerOf2 method, of class ArraySupport.
     */
    @Test
    public void testPowerOf2() {
        System.out.println("powerOf2");
        int a = 5;
        int expResult = 3;
        int result = ArraySupport.powerOf2(a);
        assertEquals(expResult, result);
    }

    /**
     * Test of pad_0 method, of class ArraySupport.
     */
    @Test
    public void testPad_0_byteArr() {
        System.out.println("pad_0");
        byte[] array = new byte[5];
        byte[] expResult = new byte[8];
        byte[] result = ArraySupport.pad_0(array);
        assertEquals(expResult.length, result.length);
    }

    /**
     * Test of pad_0 method, of class ArraySupport.
     */
    @Test
    public void testPad_0_shortArr() {
        System.out.println("pad_0");
        short[] array = new short[6];
        short[] expResult = new short[8];
        short[] result = ArraySupport.pad_0(array);
        assertEquals(expResult.length, result.length);
    }

    /**
     * Test of doubleSearch method, of class ArraySupport.
     */
    @Test
    public void testDoubleSearch() {
        System.out.println("doubleSearch");
        double[] array = new double[]{0.05, 0.06, 0.07, 0.08};
        double n = 0.07;
        int expResult = 2;
        int result = ArraySupport.doubleSearch(array, n);
        assertEquals(expResult, result);
    }

}
