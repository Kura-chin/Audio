package SoundGUI.utility;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Multi-threaded method of Fourier Transform. Basic ideas borrowed from
 * MergeSort multi-threading codes.
 *
 * @see
 * <a href="https://courses.cs.washington.edu/courses/cse373/13wi/lectures/03-13/MergeSort.java">
 * https://courses.cs.washington.edu/courses/cse373/13wi/lectures/03-13/MergeSort.java</a>
 *
 */
class ThreadedFFT implements Runnable {
    private Complex[] complexArray;
    private int threadCount;
    public static Complex[] answer;

    /**
     * Constructor for ThreadedFFT class
     *
     * @param complexArray
     * @param threadCount
     */
    public ThreadedFFT(Complex[] complexArray, int threadCount) {
        this.complexArray = complexArray;
        this.threadCount = threadCount;
    }

    @Override
    public void run() {
        try {
            answer = Fourier.parallelFFT(complexArray, threadCount);
        } catch (InterruptedException ex) {
            Logger.getLogger(Fourier.class.getName()).log(Level.SEVERE,
                                                          null, ex);
        }
    }

    public Complex[] getArray() {
        return answer;
    }
}

/**
 * Compute the FFT of complexArray length N complex sequence assuming N is
 * complexArray power of 2
 *
 * Most of the codes in the Fourier class was based on codes on Princeton
 * University's algorithms class
 *
 * @see
 * <a href="http://introcs.cs.princeton.edu/java/97data/FFT.java.html">
 * http://introcs.cs.princeton.edu/java/97data/FFT.java.html</a>
 *
 *
 */
public class Fourier {
    /**
     * Compute the DFT of complexArray[]
     *
     * @param complexArray
     * @return output array
     */
    public static Complex[] dft(Complex[] complexArray) {
        int n = complexArray.length;
        Complex[] outArray = new Complex[n];
        for (int k = 0; k < n; k++) {  // For each output element
            double sumreal = 0;
            double sumimag = 0;
            for (int t = 0; t < n; t++) {  // For each input element
                double angle = 2 * Math.PI * t * k / n;
                sumreal += complexArray[t].real() * Math.cos(angle) + complexArray[t].imaginary() * Math.sin(
                        angle);
                sumimag += -complexArray[t].real() * Math.sin(angle) + complexArray[t].imaginary() * Math.cos(
                        angle);

            }
            outArray[k] = new Complex(sumreal, sumimag);
        }
        return outArray;
    }

    /**
     * compute the FFT of complexArray[], assuming its length is complexArray
     * power of 2
     *
     * @param complexArray
     * @return
     */
    public static Complex[] fft(Complex[] complexArray) {
        int N = complexArray.length;

        // base case
        if (N == 1) {
            return new Complex[]{complexArray[0]};
        }

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++) {
            even[k] = complexArray[2 * k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd = even;  // reuse the array
        for (int k = 0; k < N / 2; k++) {
            odd[k] = complexArray[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].add(wk.mult(r[k]));
            y[k + N / 2] = q[k].sub(wk.mult(r[k]));
        }
        return y;
    }

    /**
     * A Fourier FFT method with multi threading that calls main method
     *
     * @param complexArray input complex array
     * @return calls method parallelFFT(complexArray, cores)
     * @throws InterruptedException
     */
    public static Complex[] parallelFFT(Complex[] complexArray) throws InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        return parallelFFT(complexArray, cores);
    }

    /**
     * A Fourier FFT main method with multi threading that is called by helper
     * method
     *
     * @param complexArray input complex array
     * @param threadCount thread limit of the system
     * @return y the output complex array
     * @throws InterruptedException
     */
    public static Complex[] parallelFFT(Complex[] complexArray, int threadCount) throws InterruptedException {
        if (threadCount <= 1) {
            return Fourier.fft(complexArray);
        } else {
            int N = complexArray.length;

            // base case
            if (N == 1) {
                return new Complex[]{complexArray[0]};
            }

            // radix 2 Cooley-Tukey FFT
            if (N % 2 != 0) {
                throw new RuntimeException("N is not a power of 2");
            }

            // fft of even terms
            Complex[] even = new Complex[N / 2];
            for (int k = 0; k < N / 2; k++) {
                even[k] = complexArray[2 * k];
            }

            // fft of odd terms
            Complex[] odd = even;  // reuse the array
            for (int k = 0; k < N / 2; k++) {
                odd[k] = complexArray[2 * k + 1];
            }
            Complex[] q, r;

            ThreadedFFT evenArray = new ThreadedFFT(even,
                                                    threadCount / 2);
            ThreadedFFT oddArray = new ThreadedFFT(odd,
                                                   threadCount / 2);
            Thread oThread = new Thread(evenArray);
            Thread eThread = new Thread(oddArray);
            oThread.start();
            eThread.start();

            try {
                oThread.join();
                eThread.join();
            } catch (InterruptedException ie) {
            }

            q = evenArray.getArray();
            r = oddArray.getArray();

            // combine
            Complex[] y = new Complex[N];
            for (int k = 0; k < N / 2; k++) {
                double kth = -2 * k * Math.PI / N;
                Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
                y[k] = q[k].add(wk.mult(r[k]));
                y[k + N / 2] = q[k].sub(wk.mult(r[k]));
            }
            ThreadedFFT.answer = y;
            return y;
        }
    }

    /**
     * Compute the inverse FFT of fourierArray[], assuming its length is
     * complexArray power of 2
     *
     * @param fourierArray
     * @return
     */
    public static Complex[] ifft(Complex[] fourierArray) {
        int N = fourierArray.length;
        Complex[] y = new Complex[N];

        // take conjugate
        for (int i = 0; i < N; i++) {
            y[i] = fourierArray[i].conjugate();
        }

        // compute forward FFT
        y = fft(y);

        // take conjugate again
        for (int i = 0; i < N; i++) {
            y[i] = y[i].conjugate();
        }

        // divide by N
        for (int i = 0; i < N; i++) {
            y[i] = y[i].mult(1.0 / N);
        }
        return y;
    }

    /**
     * Return array that contain peaks of the fourierArray
     *
     * @param fourierArray
     * @return
     */
    public static double[] calPeak(Complex[] fourierArray) {
        int len = fourierArray.length / 2;
        double[] outArray = new double[len];
        for (int i = 0; i < len; i++) {
            outArray[i] = fourierArray[i].abs();
        }
        return outArray;
    }

    /**
     * Returns Fourier frequency based on given index and padSampleLength
     *
     * @param wavefile
     * @param index
     * @param padSampleLength
     * @return
     */
    public static double fourierFrequency(WAVReader wavefile, int index,
                                          int padSampleLength) {
        double frequency = (index + 1 / 2) * wavefile.getAudioFormat().getSampleRate() / padSampleLength;
        return frequency;
    }
}
