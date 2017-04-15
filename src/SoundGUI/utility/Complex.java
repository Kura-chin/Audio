package SoundGUI.utility;

/**
 * Class for dealing with complex math operation
 *
 * This class was based on information on Princeton University's algorithms
 * class website
 *
 * @see
 * <a href=http://introcs.cs.princeton.edu/java/97data/Complex.java.html>
 * http://introcs.cs.princeton.edu/java/97data/Complex.java.html</a>
 */
public class Complex {
    private final double re;   // the real part
    private final double im;   // the imaginary part

    /**
     * create a new object with the given real and imaginary parts
     *
     * @param real
     * @param imag
     */
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    /**
     * create a new object with the given real
     *
     * @param real
     */
    public Complex(double real) {
        re = real;
        im = 0;
    }

    /**
     * return a string representation of the invoking Complex object
     *
     * @return String representation
     */
    public String toString() {
        if (im == 0) {
            return re + "";
        }
        if (re == 0) {
            return im + "i";
        }
        if (im < 0) {
            return re + " - " + (-im) + "i";
        }
        return re + " + " + im + "i";
    }

    /**
     * return abs/modulus/magnitude and angle/phase/argument
     *
     * @return abs
     */
    public double abs() {
        return Math.hypot(re, im);
    }  // Math.sqrt(re*re + im*im)

    /**
     * return a new Complex object whose value is (this + b)
     *
     * @param b
     * @return a new Complex object
     */
    public Complex add(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    /**
     * return a new Complex object whose value is (this - b)
     *
     * @param b
     * @return new Complex(real, imag)
     */
    public Complex sub(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    /**
     * return a new Complex object whose value is (this * b)
     *
     * @param b Complex type
     * @return new Complex(real, imag)
     */
    public Complex mult(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    /**
     * scalar multiplication return a new object whose value is (this * alpha)
     *
     * @param alpha
     * @return new Complex
     */
    public Complex mult(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /**
     * return a new Complex object whose value is the conjugate of this
     *
     * @return new Complex object
     */
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    /**
     * return a new Complex object whose value is the reciprocal of this
     *
     * @return a new Complex object
     */
    public Complex reciprocal() {
        double scale = re * re + im * im;
        return new Complex(re / scale, -im / scale);
    }

    /**
     * return the real or imaginary part
     *
     * @return double re
     */
    public double real() {
        return re;
    }

    /**
     * Return imaginary part
     *
     * @return im
     */
    public double imaginary() {
        return im;
    }

    /**
     * return a / b
     *
     * @param b
     * @return a / b
     */
    public Complex div(Complex b) {
        Complex a = this;
        return a.mult(b.reciprocal());
    }
}
