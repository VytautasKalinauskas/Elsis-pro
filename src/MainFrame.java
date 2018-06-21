import sun.awt.WindowClosingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {

    public static boolean isFinished = false;
    String resultFile = "rezultatai.txt";
    JProgressBar progressBar;
    JLabel resultFileLabel;
    JLabel currentNumberLabel;


    public MainFrame(String title) {
        super(title);
        setContentPane(createContent());
    }


    // Kuriama grafinė sąsaja
    private Container createContent()
    {
        JPanel result = new JPanel();
        result.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );


        GroupLayout layout = new GroupLayout( result );
        result.setLayout( layout );
        layout.setAutoCreateGaps( true );

        JLabel firstNumberLabel = new JLabel( "Pradinis skaičius" );
        JTextField firstNumberTextField = new JTextField( 10 );
        JLabel secondNumberLabel = new JLabel( "Galutinis skaičius" );
        JTextField secondNumberTextField = new JTextField( 10 );
        JLabel additionalNumberLabel = new JLabel( "Kas kiek didinti" );
        JTextField additionalNumberTextField = new JTextField( 10 );

        resultFileLabel = new JLabel("");

        currentNumberLabel = new JLabel("");

        JButton startButton = new JButton("Pradėti");
        JButton finishButton = new JButton("Baigti");

        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);


        // Funkcija, paspaudus mygtuką pradėti
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (Integer.parseInt(firstNumberTextField.getText().toString()) > Integer.parseInt(secondNumberTextField.getText().toString())) {
                    JOptionPane.showMessageDialog(result, "Neteisingi skaičiai");
                    return;
                }

                resultFileLabel.setText("");
                isFinished = false;

                double addPercent = (Integer.parseInt(secondNumberTextField.getText().toString()) - Integer.parseInt(firstNumberTextField.getText().toString()))
                            / Integer.parseInt(additionalNumberTextField.getText().toString()) + 1;


                progressBar.setValue(0);

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(resultFile, "UTF-8");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                final Date[] date = {new Date()};
                final Timestamp[] data = {new Timestamp(date[0].getTime())};

                writer.println(data[0] + " Skaičiavimo pradžia. Naudojami skaičiai: " + firstNumberTextField.getText().toString()
                + " " + secondNumberTextField.getText().toString() + " " + additionalNumberTextField.getText().toString() );


                PrintWriter finalWriter = writer;
                Runnable r = new Runnable() {
                    @Override
                    public void run() {

                        int number = Integer.parseInt(firstNumberTextField.getText().toString());
                        while (number <= Integer.parseInt(secondNumberTextField.getText().toString())) {
                            currentNumberLabel.setText("Skaidomas skaičius: " + number);
                            date[0] = new Date();
                            data[0] = new Timestamp(date[0].getTime());
                            long currentMili = System.currentTimeMillis();
                            finalWriter.print(data[0] + " ");
                            divide(number, finalWriter);
                            finalWriter.println("\n");

                            progressBar.setValue(progressBar.getValue()+100/(int)addPercent);

                            if(isFinished){

                                finalWriter.print(data[0] + " Skaičiavimas nutrauktas");
                                finalWriter.close();
                                return;
                            }

                            number += Integer.parseInt(additionalNumberTextField.getText().toString());
                            if(currentMili+500 > System.currentTimeMillis()) {
                                try {
                                    TimeUnit.MILLISECONDS.sleep(System.currentTimeMillis()-currentMili+500);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                        date[0] = new Date();
                        data[0] = new Timestamp(date[0].getTime());
                        finalWriter.print(data[0] + " Skaičiavimo pabaiga");
                        finalWriter.close();
                        progressBar.setValue(100);
                        resultFileLabel.setText("Skaidymas baigtas. Rezultatai faile " + resultFile);
                    }
                };

                Thread thread = new Thread(r);
                thread.start();

            }
        });


        // Funkcija, paspaudus mygtuką baigti
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isFinished = true;
            }
        });


        // Lygiavimas horizantaliai pagal kairę kraštinę

        layout.setHorizontalGroup( layout.createSequentialGroup()
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( firstNumberLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE  )
                        .addComponent( secondNumberLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE  )
                        .addComponent( additionalNumberLabel, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE  )
                        .addComponent( startButton )
                        .addComponent( progressBar, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE )
                        .addComponent( currentNumberLabel, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE  ))
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING )
                        .addComponent( firstNumberTextField )
                        .addComponent( secondNumberTextField )
                        .addComponent( additionalNumberTextField )
                        .addComponent( resultFileLabel )
                        .addComponent(finishButton))

        );

        // Lygiavimas vertikaliai
        layout.setVerticalGroup( layout.createSequentialGroup()
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( firstNumberLabel )
                        .addComponent( firstNumberTextField ) )
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( secondNumberLabel )
                        .addComponent( secondNumberTextField ) )
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( additionalNumberLabel )
                        .addComponent( additionalNumberTextField ) )
                        .addGap(20)
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE)
                        .addComponent(progressBar)
                        .addComponent( resultFileLabel ) )
                        .addGap(20)
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( currentNumberLabel ))
                        .addGap(20)
                .addGroup( layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                        .addComponent( startButton )
                        .addComponent( finishButton ) )
        );

        return result;
    }


    /*
    divide - funkcija skaičių skaidymui
    @param number - Skaidomas skaičius
    @param writer - PrintWriter'is rašymui į failą
     */
    private void divide(int number, PrintWriter writer) {

        boolean first = true;
        writer.print(number + " =");
        int divide = 2;
        while (number != 1) {

            if(number % divide == 0) {
                number = number/divide;
                if (first) {
                    writer.print(" " + divide);
                    first = false;
                }

                else {
                    writer.print(" * " + divide);
                }
                divide = 2;
            }
            else {
                divide++;
            }
        }
    }


}
