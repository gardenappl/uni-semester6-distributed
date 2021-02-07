package ua.yuriih.task1;

import javax.swing.*;
import java.awt.*;

public class Task1 {
    private static class TaskRunnable implements Runnable {
        private final int target;
        private final JSlider slider;

        TaskRunnable(int target, JSlider slider) {
            this.target = target;
            this.slider = slider;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                int value = slider.getValue();
                if (value < target)
                    slider.setValue(value + 1);
                else if (value > target)
                    slider.setValue(value - 1);

//                //simulate work
//                for (int i = 0; i < Integer.MAX_VALUE; i++) {
//                    slider.getValue();
//                }
                //sleep
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static final int WIDTH = 400;
    private static final int HEIGHT = 250;
    private static final int ELEMENT_HEIGHT = 20;

    private static Thread thread1;
    private static Thread thread2;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Task 1A");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);


        JButton startButton = new JButton("Start");
        startButton.setBounds(WIDTH / 4, 0, WIDTH / 2, ELEMENT_HEIGHT);

        frame.add(startButton);


        JSlider slider = new JSlider(0, 100, 50);
        slider.setBounds(0, ELEMENT_HEIGHT * 2, WIDTH, ELEMENT_HEIGHT * 2);
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(10);
        slider.setMajorTickSpacing(20);
        slider.setPaintLabels(true);

        frame.add(slider);


        startButton.addActionListener(actionEvent -> {
            if (thread1 != null && thread1.isAlive())
                return;
            thread1 = new Thread(new TaskRunnable(10, slider));
            thread1.setDaemon(true);
            thread1.setPriority(Thread.MIN_PRIORITY);

            thread2 = new Thread(new TaskRunnable(90, slider));
            thread2.setDaemon(true);
            thread2.setPriority(Thread.MAX_PRIORITY);

            thread1.start();
            thread2.start();
        });


        frame.setLayout(null);
        frame.setVisible(true);
    }
}
