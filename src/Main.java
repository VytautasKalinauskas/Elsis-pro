

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame jFrame = new MainFrame("Elsis Pro");

                jFrame.setSize(500,250);
                jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                jFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        MainFrame.isFinished = true;
                        try {
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.exit(0);

                    }
                });
                jFrame.setVisible(true);
            }
        });


    }


}
