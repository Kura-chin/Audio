package SoundGUI.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.Scrollable;

public class WaveFormComponent extends JComponent implements Scrollable {
    private int samplesPerPixel;
    private short[] shortArray;
    private final int FRAME_WIDTH = 600;
    private int height;
    private double scale;

    public WaveFormComponent(short[] shortArray, int height, int bits) {
        this.shortArray = shortArray;
        this.samplesPerPixel = Math.round(shortArray.length / (FRAME_WIDTH - 10));
        this.height = height;
        if (bits == 8) {
            this.scale = 127 / (height / 2) * 1.2;
        } else {
            this.scale = 32767 / (height / 2) * 1.2;
        }
    }

    public void zoom(int change) {

//        if (samplesPerPixel < 10) {
//            change /= 5;
//        }
        change = (int) (change * (Math.max(Math.log(Math.abs(
                                  samplesPerPixel)),
                                           1)));
        this.samplesPerPixel += change;
//        if (this.samplesPerPixel == 0) {
//            this.samplesPerPixel += change;
//        }
        this.samplesPerPixel = Math.min(this.samplesPerPixel, Math.round(
                                        shortArray.length / (FRAME_WIDTH)));
        this.setPreferredSize(new Dimension(
                this.shortArray.length / Math.abs(
                        samplesPerPixel),
                height));
    }

//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics panel2D = (Graphics) g;
//        panel2D.setColor(new Color(40, 40, 40));
//        panel2D.fillRect(0, 0, getWidth(), getHeight());
//        panel2D.setColor(Color.WHITE);
//        int frame = 0;
//        int oldX = 0;
//        int oldY = this.height / 2;
//        if (samplesPerPixel > 0) {
//            for (int i = 0; i < shortArray.length - samplesPerPixel; i += samplesPerPixel) {
//                short[] array = Arrays.copyOfRange(shortArray, i,
//                                                   i + samplesPerPixel);
//                short maxValue = this.findMax(array);
//                if (samplesPerPixel <= 4) {
//
//                    int y = (int) (getHeight() / 2 - maxValue / scale);
//                    panel2D.drawLine(oldX, oldY, frame, y);
//                    oldX = frame;
//                    oldY = y;
//                    frame++;
//                } else {
//
//                    int posY = (int) (getHeight() / 2 - maxValue / scale);
//                    int negY = (int) (getHeight() / 2 + maxValue / scale);
//                    panel2D.drawLine(frame, posY, frame, negY);
//                    frame++;
//                }
//            }
//        } else {
//            for (int i = 0; i < shortArray.length; i++) {
//                int y = (int) (getHeight() / 2 - shortArray[i] / scale);
//                panel2D.drawLine(oldX, oldY, frame, y);
//                int radius = samplesPerPixel / -10;
//                panel2D.fillOval(oldX - radius, oldY - radius, radius * 2,
//                                 radius * 2);
//                oldX = frame;
//                oldY = y;
//                frame -= samplesPerPixel;
//
//            }
//        }
//    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.BLACK);
//        g2D.fillRect(0, 0, getWidth(), getHeight());
        g2D.setColor(new Color(100, 100, 255));
        int curX = 0;
        int nextX = 0;
        int curY = getHeight() / 2;
        int nextY;
        if (samplesPerPixel < 0) {
            for (int i = 0; i < shortArray.length; i++) {
                nextY = (int) (getHeight() / 2 - shortArray[i] / scale);
                g2D.drawLine(curX, curY, nextX, nextY);
                int radius = samplesPerPixel / -10;
                g2D.fillRect(curX - radius, curY - radius, radius * 2,
                             radius * 2);
                curX = nextX;
                curY = nextY;
                nextX -= samplesPerPixel;

            }
        } else if (samplesPerPixel > 0) {
            for (int i = 0; i < shortArray.length - samplesPerPixel; i += samplesPerPixel) {
                short[] array = Arrays.copyOfRange(shortArray, i,
                                                   i + samplesPerPixel);
                short maxValue = this.findMax(array);
                if (samplesPerPixel <= 4) {
                    nextY = (int) (getHeight() / 2 - maxValue / scale);
                    g2D.drawLine(curX, curY, nextX, nextY);
                    curX = nextX;
                    curY = nextY;
                    nextX++;
                } else {
                    curY = (int) (getHeight() / 2 - maxValue / scale);
                    nextY = (int) (getHeight() / 2 + maxValue / scale);
                    g2D.drawLine(curX, curY, nextX, nextY);
                    curX++;
                    nextX++;
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (samplesPerPixel > 0) {
            return (new Dimension(this.shortArray.length / Math.abs(
                    samplesPerPixel),
                                  height));
        } else {
            return (new Dimension(this.shortArray.length * Math.abs(
                    samplesPerPixel),
                                  height));
        }

    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(getWidth(), getHeight());
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle rctngl, int i, int i1) {
        return 10;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle rctngl, int i, int i1) {
        return 10;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    private short findMax(short[] array) {
        short result = Short.MIN_VALUE;
        for (short i : array) {
            if (i > result) {
                result = i;
            }
        }
        return result;
    }
}
