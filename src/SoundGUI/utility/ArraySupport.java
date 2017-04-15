package SoundGUI.utility;

import static java.lang.Double.compare;

public class ArraySupport {

    /**
     * Return the 2's power
     *
     * @param a
     * @return 2's power
     */
    public static int powerOf2(int a) {
        double log2_e = Math.log(Math.E) / Math.log(2.0);
        int n = (int) (log2_e * Math.log(a));
        if (Math.pow(2, n) == a) {
            return n;
        } else {
            return n + 1;
        }
    }

    /**
     * Pad 0 to the given byte array
     *
     * @param array: byte array
     * @return byte array
     */
    public static byte[] pad_0(byte[] array) {
        int newLength = (int) Math.pow(2, powerOf2(array.length));
        byte[] newArray = new byte[newLength];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    /**
     * Pad 0 to the given short array
     *
     * @param array
     * @return short array
     */
    public static short[] pad_0(short[] array) {
        int newLength = (int) Math.pow(2, powerOf2(array.length));
        short[] newArray = new short[newLength];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    /**
     * Generate Complex Array from byte array
     *
     * @param array byte array
     * @return complexArray
     */
    public static Complex[] generateComplexArray(byte[] array) {
        Complex[] complexArray = new Complex[array.length];
        for (int i = 0; i < complexArray.length; i++) {
            Complex complex = new Complex((float) array[i] / 127);
            complexArray[i] = complex;
        }
        return complexArray;
    }

    /**
     * Generate Complex Array from short array
     *
     * @param array short array
     * @return complexArray
     */
    public static Complex[] generateComplexArray(short[] array) {
        Complex[] complexArray = new Complex[array.length];
        for (int i = 0; i < complexArray.length; i++) {
            Complex complex = new Complex((float) array[i] / 32767);
            complexArray[i] = complex;
        }
        return complexArray;
    }

    /**
     * Search through the double array and return index
     *
     * @param array double array
     * @param n double
     * @return index of the given double n
     */
    public static int doubleSearch(double[] array, double n) {
        for (int i = 0; i < array.length; i++) {
            if (compare(n, array[i]) == 0) {
                return i;
            } else {
            }
        }
        return -1;
    }
}
