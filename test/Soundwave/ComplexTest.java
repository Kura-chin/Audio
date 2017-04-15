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
public class ComplexTest {
    static final double EPSILON = 1.0E-12;
    Complex comp1;
    Complex comp2;

    @Before
    public void setUp() {
        comp1 = new Complex(3, 4);
        comp2 = new Complex(5, 12);
    }

    @After
    public void tearDown() {
        comp1 = null;
        comp2 = null;
    }

    /**
     * Test of abs method, of class Complex.
     */
    @Test
    public void testAbs() {
        System.out.println("abs");
        double expResult = 5.0;
        double result = comp1.abs();
        assertEquals(expResult, result, EPSILON);
    }

    /**
     * Test of add method, of class Complex.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Complex expResult = new Complex(8, 16);
        Complex result = comp1.add(comp2);
        assertEquals(expResult, result);
    }

    /**
     * Test of sub method, of class Complex.
     */
    @Test
    public void testSub() {
        System.out.println("sub");
        Complex expResult = new Complex(-2, -8);
        Complex result = comp1.sub(comp2);
        assertEquals(expResult, result);
    }

    /**
     * Test of mult method, of class Complex.
     */
    @Test
    public void testMult_Complex() {
        System.out.println("mult_comp");
        Complex expResult = new Complex(-33, 56);
        Complex result = comp1.mult(comp2);
        assertEquals(expResult, result);
    }

    /**
     * Test of mult method, of class Complex.
     */
    @Test
    public void testMult_double() {
        System.out.println("mult_const");
        double alpha = 5.0;
        Complex expResult = new Complex(15, 20);
        Complex result = comp1.mult(alpha);
        assertEquals(expResult, result);
    }

    /**
     * Test of conjugate method, of class Complex.
     */
    @Test
    public void testConjugate() {
        System.out.println("conjugate");
        Complex expResult = new Complex(3, -4);
        Complex result = comp1.conjugate();
        assertEquals(expResult, result);
    }

    /**
     * Test of reciprocal method, of class Complex.
     */
    @Test
    public void testReciprocal() {
        System.out.println("reciprocal");
        Complex expResult = new Complex((double) 3 / 25, (double) -4 / 25);
        Complex result = comp1.reciprocal();
        assertEquals(expResult, result);
    }

    /**
     * Test of div method, of class Complex.
     */
    @Test
    public void testDiv() {
        System.out.println("div");
        Complex expResult = new Complex(2.52, 0.64);
        Complex result = comp2.div(comp1);
        assertEquals(expResult.real(), result.real(), EPSILON);
        assertEquals(expResult.imaginary(), result.imaginary(), EPSILON);
    }

}
