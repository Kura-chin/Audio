/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SoundGUI.utility;

import SoundGUI.view.WaveComponent;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GUIutility {
    private static final int ROTATE_SENSITIVITY = 5;

    public static JPanel createWavePane(short[] shortArray, int height, int bits) {
        WaveComponent waveComponent = new WaveComponent(shortArray,
                                                        (int) (height - 5),
                                                        bits);
        JScrollPane wavePanel = new JScrollPane(waveComponent);
        wavePanel.setViewportView(waveComponent);
        wavePanel.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        wavePanel.setOpaque(false);
        wavePanel.setWheelScrollingEnabled(false);
        wavePanel.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        waveComponent.addMouseWheelListener((MouseWheelEvent mouseWheel) -> {
            int wheelsRotation = mouseWheel.getWheelRotation();
            double mousePosToChan = mouseWheel.getPoint().getX() / waveComponent.getPreferredSize().getWidth();
            double distanceToViewOrigin = mouseWheel.getPoint().getX() - wavePanel.getViewport().getViewPosition().getX();
            waveComponent.zoom(wheelsRotation * ROTATE_SENSITIVITY);
            waveComponent.repaint();
            int newX = (int) (waveComponent.getPreferredSize().getWidth() * mousePosToChan - distanceToViewOrigin);
            wavePanel.getViewport().setViewPosition(new Point((int) (newX), 0));
            wavePanel.getViewport().revalidate();
            wavePanel.getViewport().repaint();
            wavePanel.getViewport().setViewPosition(new Point((int) (newX), 0));
        });
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(wavePanel);
        return panel;
    }
}
