package SoundGUI.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import static java.util.Arrays.sort;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.sound.sampled.*;

public class WAVReadinClient {
    // A instance variable for wavefile
    private WAVReader wavefile;

    /**
     * A construct for WAVReadinClient class
     *
     * @param wavefile
     */
    public WAVReadinClient(WAVReader wavefile) {
        this.wavefile = wavefile;
    }

    /**
     * Enumerate choices in the menu for easy future use and management
     */
    enum MenuChoices {
        EXIT("Exit"), PLAY("Play the waveform"), SHOW_INFO(
                "Show audio format information"), DOWNSAMPLE(
                "Perform Downsapling of the audio"), ADJUST_VOLUME(
                "Adjust the volume"), ADD_DELAY("Add a single decay"), ADD_REVERB(
                "Add reverb effect"), PERFORM_FFT(
                "Perform a discrete Fourier transform"), PERFORM_FFT_MULTI(
                "Perform a discrete Fourier transform using either single or multithreaded approach"), SAVE(
                "Save the file to a new WAV format file");

        /**
         * A string that represents the meaning of each option/choice
         */
        private final String label;

        // Contruct choices with string information
        MenuChoices(String s) {
            this.label = s;
        }

        /**
         * Overriding the toString() method to give us an easy way to look at
         * the guts of the object.
         *
         * @return the string representing the label of the option
         */
        @Override
        public String toString() {
            return label;
        }

        /**
         * Give us an easy way to print out the number of the choice
         *
         * @return String
         */
        public String printNum() {
            return String.format("%d. ", this.ordinal());
        }
    }

    /**
     * Prints the menu that contains multiple tasks to perform on the waveform
     * in the memory
     *
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.InterruptedException
     */
    public void printMenu() throws LineUnavailableException, IOException, UnsupportedAudioFileException, FileNotFoundException, InterruptedException {
        Player player = new Player(this.wavefile);
        System.out.println("---Waveform loaded/generated---");
        System.out.println("Please choose an option below:");
        for (MenuChoices choices : MenuChoices.values()) {
            System.out.println(choices.printNum() + choices);
        }
        System.out.format("Your choice(0 - %d): ",
                          MenuChoices.values().length - 1);
        while (true) {
            try {
                Scanner in = new Scanner(System.in);
                int choice = in.nextInt();
                if (choice > MenuChoices.values().length - 1 || choice < 0) {
                    System.out.format("Please enter from 0 to %d. \n",
                                      MenuChoices.values().length - 1);
                    System.out.println("Try again");
                } else {
                    handleChoice(choice, this.wavefile, player);
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter integer only.");
                System.out.println("Try again");
            }
        }
    }

    /**
     * Switch to different method given the user input
     *
     * @param choice the user's choice
     * @param wavefile
     * @param player
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.InterruptedException
     */
    public static void handleChoice(int choice, WAVReader wavefile,
                                    Player player) throws LineUnavailableException, IOException, UnsupportedAudioFileException, FileNotFoundException, InterruptedException {
        switch (choice) {
            case 0:
                System.out.println("Goodbye");
                System.exit(0);
            case 1:
                player.start();
                break;
            case 2:
                wavefile.displayInformation();
                break;
            case 3:
                downSampling(wavefile);
                break;
            case 4:
                adjustVolume(player);
                break;
            case 5:
                addDelay(wavefile);
                break;
            case 6:
                wavefile.reverb(wavefile);
                break;
            case 7:
                fourierTransform(wavefile);
                break;
            case 8:
                fourierTransformMultiOptions(wavefile);
                break;
            case 9:
                saveWAVFile(wavefile);
                break;
            default:
                break;
        }
    }

    /**
     * Static method for asking user input for down sampling
     *
     * @param wavefile
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    public static void downSampling(WAVReader wavefile) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        while (true) {
            System.out.println(
                    "Please enter a desired sample frequency: ");
            try {
                Scanner in = new Scanner(System.in);
                float sampleRate = in.nextFloat();
                wavefile.downSampling(sampleRate);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter a float. Try again.");
            }
        }
    }

    /**
     * Adjust volume of the input/generated wave
     *
     * @param player
     * @throws LineUnavailableException
     */
    public static void adjustVolume(Player player) throws LineUnavailableException {
        while (true) {
            System.out.println(
                    "Please enter a percentage +/- value to increase or decrease the volume.");
            System.out.println("e.g. 10 means increase the volume 10%");
            System.out.println("     -10 means decrease the volume 10%");
            try {
                Scanner in = new Scanner(System.in);
                float percentage = in.nextFloat();
                if (percentage <= -100) {
                    System.out.println(
                            "Please enter a percentage larger than -100: ");
                } else {
                    player.adjustVolume(percentage);
                    System.out.println(
                            "Volume adjusted.");
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter only number.");
            }
        }
    }

    /**
     * Add delay/echo to the input/generated wave with given user input
     *
     * @param wavefile
     */
    public static void addDelay(WAVReader wavefile) {
        double delayTime;
        double decay;
        while (true) {
            System.out.println("Please enter delay time in ms(<= 1000ms): ");
            Scanner in = new Scanner(System.in);
            delayTime = in.nextDouble();
            System.out.println("Enter a decay value that is larger than 0");
            System.out.println("e.g. 0.5 is a good value to try out");
            decay = in.nextDouble();
            if (delayTime > 1000) {
                System.out.println(
                        "Please enter delay time that smaller than 1000ms ");
            }
            if (decay < 0.0) {
                System.out.println("Please enter dacay that is larger than 0");
            } else {
                wavefile.addEcho(delayTime * 0.001, decay);
                System.out.println("Echo added.");
                break;
            }
        }
    }

    /**
     * Perform Fourier Transform on given wave file using single thread
     *
     * @param wavefile the wavefile in memory
     * @throws FileNotFoundException
     * @throws java.lang.InterruptedException
     */
    public static void fourierTransform(WAVReader wavefile) throws FileNotFoundException, InterruptedException {
        while (true) {
            try {
                System.out.println("Please enter number of peaks needed: ");
                Scanner in = new Scanner(System.in);
                int numPeak = in.nextInt();
                short[] shortBuffer = wavefile.generateShortArray();
                byte[] byteBuffer = wavefile.getByteBuffer();
                Complex[] complexBuffer;
                int bitsize = wavefile.getAudioFormat().getSampleSizeInBits();
                switch (bitsize) {
                    case 8:
                        byte[] byteArray = ArraySupport.pad_0(byteBuffer);
                        complexBuffer = ArraySupport.generateComplexArray(
                                byteArray);
                        break;
                    case 16:
                        short[] shortArray = ArraySupport.pad_0(shortBuffer);
                        complexBuffer = ArraySupport.generateComplexArray(
                                shortArray);
                        break;
                    default:
                        return;
                }
                long startTime = System.nanoTime();
                Complex[] fourierData = Fourier.fft(complexBuffer);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println("Single Thread runtime: " + duration);
                displayPeaks(fourierData, numPeak, wavefile);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.");
            }
        }
    }

    /**
     * Call by method fourierTransform to display peaks
     *
     * @param fourierData
     * @param numPeak
     * @param wavefile the wavefile in memory
     */
    private static void displayPeaks(Complex[] fourierData, int numPeak,
                                     WAVReader wavefile) {
        double[] fourierPeaks = Fourier.calPeak(fourierData);
        double[] sortedPeaks = new double[fourierPeaks.length];
        System.arraycopy(fourierPeaks, 0, sortedPeaks, 0,
                         fourierPeaks.length);
        sort(sortedPeaks);
        for (int i = 1; i <= numPeak; i++) {
            double peak = sortedPeaks[sortedPeaks.length - i];
            int index = ArraySupport.doubleSearch(fourierPeaks, peak) + 1;
            double frequency = Fourier.fourierFrequency(wavefile, index,
                                                        fourierData.length);
            System.out.println(
                    "The " + i + " highest peak is: " + peak + " at frequency: " + frequency + "Hz");
        }
    }

    /**
     * Perform Fourier Transform on given wave file using multi thread
     *
     * @param wavefile the wavefile in memory
     * @throws InterruptedException
     */
    public static void fourierTransformMulti(WAVReader wavefile) throws InterruptedException {
        while (true) {
            try {
                System.out.println("Please enter number of peaks needed: ");
                Scanner in = new Scanner(System.in);
                int numPeak = in.nextInt();
                short[] shortBuffer = wavefile.generateShortArray();
                byte[] byteBuffer = wavefile.getByteBuffer();
                Complex[] complexBuffer;
                int bitsize = wavefile.getAudioFormat().getSampleSizeInBits();
                switch (bitsize) {
                    case 8:
                        byte[] byteArray = ArraySupport.pad_0(byteBuffer);
                        complexBuffer = ArraySupport.generateComplexArray(
                                byteArray);
                        break;
                    case 16:
                        short[] shortArray = ArraySupport.pad_0(shortBuffer);
                        complexBuffer = ArraySupport.generateComplexArray(
                                shortArray);
                        break;
                    default:
                        return;
                }
                long startTime = System.nanoTime();
                Complex[] fourierData = Fourier.parallelFFT(complexBuffer);
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);
                System.out.println("Multi Thread runtime: " + duration);
                displayPeaks(fourierData, numPeak, wavefile);
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.");
            }
        }
    }

    /**
     * A method that handles a user's option to perform FFT using single thread
     * or multi thread
     *
     * @param wavefile the wavefile in memory
     * @throws FileNotFoundException
     * @throws InterruptedException
     */
    public static void fourierTransformMultiOptions(WAVReader wavefile) throws FileNotFoundException, InterruptedException {
        while (true) {
            System.out.println(
                    "Please choose to perform fourier transform using single thread or multi thread");
            System.out.println("Single thread type 1");
            System.out.println("Multi thread type 2");
            System.out.println("Back to main menu type 0");
            try {
                Scanner in = new Scanner(System.in);
                int choice = in.nextInt();
                switch (choice) {
                    case 1:
                        fourierTransform(wavefile);
                        break;
                    case 2:
                        fourierTransformMulti(wavefile);
                        break;
                    case 0:
                        return;
                    default:
                        break;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer.");
            }
        }
    }

    /**
     * A input/generated wave to a .wav file
     *
     * @param wavefile the wavefile in memory
     * @throws IOException
     */
    public static void saveWAVFile(WAVReader wavefile) throws IOException {
        while (true) {
            System.out.println(
                    "Please enter a output file name ending with .wav");
            try {
                Scanner in = new Scanner(System.in);
                String fileName = in.next();
                if (fileName.endsWith(".wav")) {
                    wavefile.saveToWAV(fileName);
                    break;
                } else {
                    System.out.println(
                            "Please enter a file name ending with .wav");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid file name");
            }
        }
    }
}
