import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

public class StartMenuGui {
    private JButton startButton;
    private JPanel panel1;
    private JCheckBox GUICheckBox;
    private JComboBox comboBoxMutation;
    private JComboBox comboBoxCrossover;
    private JSpinner spinnerIterations;

    private ArrayList<Double> fitnesses;

    //Need to get it so each EA instance "returns" a value so the best EA out of 20 can be discovered as the best, so can be run multiple times
    //Needs to be done so I can do something with the JSpinner
    public StartMenuGui() {
        //fitnesses = new ArrayList<Double>();
        ArrayList<EA> eas = new ArrayList<>();
        startButton.addActionListener(e -> {
            for (int j = 0; j < 3; j++) {
                fitnesses = new ArrayList<>();
//                for (int i = 0; i < 20; i++) {
                    EA ea = new EA(comboBoxCrossover.getSelectedItem().toString(), comboBoxMutation.getSelectedItem().toString());
                    //islands.add(ea);
                    if (GUICheckBox.isSelected()) {
                        Gui gui = new Gui(j);
                        //maybe? if i only want one gui then just put the below line above the for loop so it only appears once
                        // and then it can just refresh with a new EA
                        ea.addObserver(gui);
                        Thread t = new Thread(ea);
                        t.start();
                    }else{
                        ea.run();
                    }


                    //About 5 times slower if I do it this way,
                    //where each EA is being run to completion before others are created
                    //But can't figure out how else to get ea.best
                    //don't know how this class can be notified when the thread ends

                   /* while(t.isAlive()){
                        continue;
                    }
                    fitnesses.add(ea.best.fitness); */
//                }

                //Collections.sort(fitnesses);
                //System.out.println(fitnesses.get(0));
            }
        });
        comboBoxCrossover.addItem("PMX");
        comboBoxCrossover.addItem("Order");
        comboBoxCrossover.addItem("Cycle");

        comboBoxMutation.addItem("Swap");
        comboBoxMutation.addItem("Scramble");
        comboBoxMutation.addItem("Invert");
        comboBoxMutation.addItem("2-opt");

        GUICheckBox.setSelected(true);
    }

    public static void main (String[] args){
        JFrame frame = new JFrame("StartMenuGui");
        frame.setContentPane(new StartMenuGui().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,300);
    }
}
