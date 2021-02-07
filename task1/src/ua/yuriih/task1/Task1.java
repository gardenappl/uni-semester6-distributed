package ua.yuriih.task1;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Task1 {
    private static class TaskRunnable implements Runnable {
        private final int target;
        private final JSlider slider;
        private final AtomicInteger semaphore;
        private final JButton otherStopButton;

        TaskRunnable(int target, JSlider slider, AtomicInteger semaphore, JButton otherStopButton) {
            this.target = target;
            this.slider = slider;
            this.semaphore = semaphore;
            this.otherStopButton = otherStopButton;
        }

        @Override
        public void run() {
            while (!semaphore.compareAndSet(UNLOCKED, LOCKED)) {
                Thread.yield();
            }
            //entered critical region

            otherStopButton.setEnabled(false);

            while (!Thread.interrupted()) {
                int value = slider.getValue();
                if (value < target)
                    slider.setValue(value + 1);
                else if (value > target)
                    slider.setValue(value - 1);

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            otherStopButton.setEnabled(true);

            //leaving critical region
            semaphore.set(UNLOCKED);
        }
    }

    private static final int WIDTH = 400;
    private static final int HEIGHT = 250;
    private static final int ELEMENT_HEIGHT = 20;

    private static Thread thread1;
    private static Thread thread2;

    private static final int UNLOCKED = 0;
    private static final int LOCKED = 1;
    private static final AtomicInteger semaphore = new AtomicInteger();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Task 1B");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);


        JButton startButton1 = new JButton("Start 1");
        startButton1.setBounds(0, 0, WIDTH / 2, ELEMENT_HEIGHT);

        frame.add(startButton1);

        JButton startButton2 = new JButton("Start 2");
        startButton2.setBounds(WIDTH / 2, 0, WIDTH / 2, ELEMENT_HEIGHT);

        frame.add(startButton2);

        JButton stopButton1 = new JButton("Stop 1");
        stopButton1.setBounds(0, ELEMENT_HEIGHT, WIDTH / 2, ELEMENT_HEIGHT);

        frame.add(stopButton1);

        JButton stopButton2 = new JButton("Stop 2");
        stopButton2.setBounds(WIDTH / 2, ELEMENT_HEIGHT, WIDTH / 2, ELEMENT_HEIGHT);

        frame.add(stopButton2);


        JSlider slider = new JSlider(0, 100, 50);
        slider.setBounds(0, ELEMENT_HEIGHT * 3, WIDTH, ELEMENT_HEIGHT * 2);
        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(10);
        slider.setMajorTickSpacing(20);
        slider.setPaintLabels(true);

        frame.add(slider);


        startButton1.addActionListener(actionEvent -> {
            if (thread1 != null && thread1.isAlive())
                return;
            thread1 = new Thread(new TaskRunnable(10, slider, semaphore, stopButton2));
            thread1.setDaemon(true);
            thread1.setPriority(Thread.MIN_PRIORITY);

            thread1.start();
        });

        startButton2.addActionListener(actionEvent -> {
            if (thread2 != null && thread2.isAlive())
                return;
            thread2 = new Thread(new TaskRunnable(90, slider, semaphore, stopButton1));
            thread2.setDaemon(true);
            thread2.setPriority(Thread.MAX_PRIORITY);

            thread2.start();
        });

        stopButton1.addActionListener(actionEvent -> {
            if (thread1 == null || !thread1.isAlive())
                return;

            thread1.interrupt();
        });

        stopButton2.addActionListener(actionEvent -> {
            if (thread2 == null || !thread2.isAlive())
                return;

            thread2.interrupt();
        });


        frame.setLayout(null);
        frame.setVisible(true);
    }
}
