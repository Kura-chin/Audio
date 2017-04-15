package SoundGUI.controller;

import SoundGUI.model.*;
import SoundGUI.utility.*;
import SoundGUI.view.SoundView;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SoundController implements ActionListener {
    /**
     * The model of the program.
     */
    private SoundModel theModel;

    /**
     * The view of the program.
     */
    private SoundView theView;

    private Player player;

    /**
     * Instantiates a new Kelvin convert controller.
     *
     * @param theModel the model
     * @param theView the view
     */
    public SoundController(SoundModel theModel, SoundView theView) {
        this.theModel = theModel;
        this.theView = theView;

        // Register bottons/menus
        theView.getMenuExit().addActionListener(this);
        theView.getBtnPause().addActionListener(this);
        theView.getBtnPlay().addActionListener(this);
        theView.getBtnStop().addActionListener(this);
        theView.getMenuOpenFile().addActionListener(this);
        theView.getMenuWave().addActionListener(this);
    }

    /**
     * Update view from model.
     */
    private void updateViewFromModel() {
        drawSoundWave();
    }

    /**
     * Ask for a WAV file, open it and load it to the constructor of WAVReader
     *
     * @return WAVReader wavefile
     */
    public WAVReader openFile() {
        while (true) {
            try {
                JFileChooser fileChooser = new JFileChooser(".");
                int value = fileChooser.showOpenDialog(null);
                if (value == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String fileName = file.getAbsolutePath();
                    WAVReader wavefile = new WAVReader(fileName);
                    theModel.loadWaveFile(wavefile);
                    updateViewFromModel();
                    return wavefile;
                }
            } catch (UnsupportedAudioFileException e) {
                System.out.println("UnsupportedAudioFileException");
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == theView.getMenuOpenFile()) {
            openFile();
        }
        if (e.getSource() == theView.getMenuExit()) {
            exit();
        }

        if (e.getSource() == theView.getMenuWave()) {
            try {
                generateWave();
            } catch (LineUnavailableException | IOException ex) {
                Logger.getLogger(SoundController.class.getName()).log(
                        Level.SEVERE,
                        null, ex);
            }
        }

        if (e.getSource() == theView.getBtnPlay()) {
            WAVReader wavefile = theModel.getWavefile();
            try {
                player = new Player(wavefile);
                player.start();
            } catch (LineUnavailableException | IOException ex) {
                Logger.getLogger(SoundController.class.getName()).log(
                        Level.SEVERE,
                        null, ex);
            }
        }
        if (e.getSource() == theView.getBtnPause()) {
            player.pause();
        }
        if (e.getSource() == theView.getBtnStop()) {
            player.stop();
        }
    }

    private void exit() {
        JOptionPane.showMessageDialog(theView,
                                      "Thanks for using sound player.",
                                      "GoodBye!",
                                      JOptionPane.PLAIN_MESSAGE);
        theView.dispose();
        System.exit(0);
    }
    /**
     * Hard coded constants for the pure wave generated with 44100 Hz sample
     * rate and 2 seconds length
     */
    private static final double SAMPLE_RATE = 16000.0;
    private static final double FREQUENCY = 440.0;
    private static final double LENGTH_IN_TIME = 1;

    /**
     * @return @throws javax.sound.sampled.LineUnavailableException
     * @throws java.io.IOException
     * @see
     * http://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog
     */
    public void generateWave() throws LineUnavailableException, IOException {
        JTextField fField = new JTextField(String.format("%.1f", FREQUENCY), 5);
        JTextField aField = new JTextField(5);
        JTextField rateField = new JTextField(String.format("%.1f", SAMPLE_RATE),
                                              5);
        JTextField lengthField = new JTextField(String.format("%.1f",
                                                              LENGTH_IN_TIME),
                                                5);

        JPanel myPanel = new JPanel(new GridLayout(5, 2));
        myPanel.add(new JLabel("Type:"));
        String[] waveTypes = {"Sine", "Square", "Sawtooth"};
        JComboBox waveType = new JComboBox(waveTypes);
        myPanel.add(waveType);
        myPanel.add(new JLabel("Frequency(Hz):"));
        myPanel.add(fField);
        myPanel.add(new JLabel("Sample rate(Hz):"));
        myPanel.add(rateField);

//        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Amplitude:"));
        myPanel.add(aField);

        myPanel.add(new JLabel("Length(s):"));
        myPanel.add(lengthField);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                                                   "Please Enter wave paramenters",
                                                   JOptionPane.OK_CANCEL_OPTION);
        PureWave pureWave = new PureWave(waveType.getSelectedItem().toString(),
                                         Double.parseDouble(aField.getText()),
                                         Double.parseDouble(fField.getText()),
                                         Double.parseDouble(
                                                 lengthField.getText()),
                                         Double.parseDouble(rateField.getText()));
        WAVReader wavefile = new WAVReader(pureWave);
        theModel.loadWaveFile(wavefile);
        updateViewFromModel();

        if (result == JOptionPane.OK_OPTION) {
            System.out.println("f value: " + fField.getText());
            System.out.println("a value: " + aField.getText());
        }

    }

    /**
     * Draw the sound wave to the View. The screen will be divided by two if the
     * input is two channels.
     *
     *
     */
    public void drawSoundWave() {
        //clear the current panel
        if (theModel.getWavefile() != null) {
            theView.getWaveFormPanel().removeAll();
            theView.revalidate();
            theView.repaint();
            //get the short array
            short[] shortArray = theModel.getWavefile().generateShortArray();
            //if 1 channel
            theView.getWaveFormPanel().setLayout(new GridLayout(1, 1));
            if (theModel.getWavefile().getAudioFormat().getChannels() == 1) {
                JPanel soundPane = GUIutility.createWavePane(
                        shortArray, theView.getWaveFormPanel().getHeight(),
                        theModel.getWavefile().getAudioFormat().getSampleSizeInBits());
                theView.getWaveFormPanel().add(soundPane);
                theView.revalidate();
                theView.repaint();
            } //2 channels
            else if (theModel.getWavefile().getAudioFormat().getChannels() == 2) {
                //get the two channels to short arrays
                short[] firstChannel = new short[shortArray.length / 2];
                short[] secondChannel = new short[shortArray.length / 2];
                for (int i = 0; i < firstChannel.length; i++) {
                    firstChannel[i] = shortArray[i * 2];
                    secondChannel[i] = shortArray[i * 2 + 1];
                }
                //make the wave panel a grid array for easier processing
                theView.getWaveFormPanel().setLayout(new GridLayout(2, 1));
//                draw the first channel
                JPanel firstPane = GUIutility.createWavePane(
                        firstChannel, theView.getWaveFormPanel().getHeight() / 2,
                        theModel.getWavefile().getAudioFormat().getSampleSizeInBits());
                theView.getWaveFormPanel().add(firstPane);
//                draw the second channel
                JPanel secondPane = GUIutility.createWavePane(
                        secondChannel,
                        theView.getWaveFormPanel().getHeight() / 2,
                        theModel.getWavefile().getAudioFormat().getSampleSizeInBits());
                theView.getWaveFormPanel().add(secondPane);
                theView.revalidate();
                theView.repaint();

            }
        }
    }
}
