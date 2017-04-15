package SoundGUI;

import SoundGUI.controller.SoundController;
import SoundGUI.model.PureWave;
import SoundGUI.model.SoundModel;
import SoundGUI.view.SoundView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.sound.sampled.*;
import javax.swing.UIManager;

public class SoundMain {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SoundView.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SoundView.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SoundView.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SoundView.class.getName()).log(
                    java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SoundModel theModel = new SoundModel();
                SoundView theView = new SoundView();
                SoundController theController = new SoundController(
                        theModel, theView);
                theView.setVisible(true);
            }
        });
    }

    /**
     * Switch to different methods given the user input
     *
     * @param choice An integer that represents user's choice
     * @throws LineUnavailableException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.InterruptedException
     */
    public static void handleChoice(MainMenuChoices choice) throws LineUnavailableException, IOException, UnsupportedAudioFileException, FileNotFoundException, InterruptedException {
//        switch (choice) {
//            case EXIT:
//                System.out.println("Goodbye");
//                System.exit(0);
//            case OPENFILE:
////                WAVReader wavefile = openFile();
//                WAVReadinClient configMenu = new WAVReadinClient(wavefile);
//                configMenu.printMenu();
//                break;
//            case GENERATE_TONE:
//                PureWave pureWave = generateWave();
//                wavefile = new WAVReader(pureWave);
//                configMenu = new WAVReadinClient(wavefile);
//                configMenu.printMenu();
//                break;
//            default:
//                break;
//    }
    }

    /**
     * Hard coded constants for the pure wave generated with 44100 Hz sample
     * rate and 2 seconds length
     */
    private static final double SAMPLE_RATE = 44100.0;
    private static final double LENGTH_IN_TIME = 5;

    /**
     * Generate pure wave given the user input
     *
     * @return PureWave pureWave
     */
    public static PureWave generateWave() {
        String waveType = "";
        double frequency;
        double amplitude;
        while (true) {
            System.out.println(
                    "Which type of wave to be generated (sine, square, sawtooth): ");
            Scanner in = new Scanner(System.in);
            waveType = in.next();
            if (waveType.equalsIgnoreCase("sine") || waveType.equalsIgnoreCase(
                    "square") || waveType.equalsIgnoreCase("sawtooth")) {
                break;
            } else {
                System.out.println(
                        "Please enter only sine, square or sawtooth. Try again");
            }
        }

        while (true) {
            try {
                System.out.println(
                        "Please enter a frequency in Hz.");
                System.out.println(
                        "Usually from 20Hz to 20,000Hz");
                Scanner inFreq = new Scanner(System.in);
                frequency = inFreq.nextDouble();
                if (frequency < 0) {
                    System.out.println(
                            "Please enter a positive frequency. Try again.\n ");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a number.\n");
            }
        }

        while (true) {
            try {
                System.out.println(
                        "Please enter an amptitude from 0 to 1 (not including 0).");
                Scanner inAmp = new Scanner(System.in);
                amplitude = inAmp.nextDouble();
                if (amplitude <= 0 || amplitude > 1) {
                    System.out.println(
                            "Please enter a frequncy in range from 0 to 1. Try again.\n");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter numbers only\n");
            }
        }
        PureWave pureWave = new PureWave(waveType, amplitude,
                                         frequency,
                                         LENGTH_IN_TIME, SAMPLE_RATE);
        return pureWave;

    }

    /**
     * Enumerate main menu choices for easy future use and management
     */
    enum MainMenuChoices {
        EXIT("Exit"), OPENFILE("Open a WAV file"), GENERATE_TONE(
                "Generate a pure tone");

        /**
         * A string that represents the meaning of each option/choice
         */
        private final String label;

        // Contruct choices with string information
        MainMenuChoices(String s) {
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
}
