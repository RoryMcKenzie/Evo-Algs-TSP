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

    public StartMenuGui() {
        ArrayList<EA> eas = new ArrayList<>();
        startButton.addActionListener(e -> {
            //should probably have an error if spinnerIterations.getValue() < 1, but still works
            for (int j = 0; j < (Integer)spinnerIterations.getValue(); j++) {
                    EA ea = new EA(comboBoxCrossover.getSelectedItem().toString(), comboBoxMutation.getSelectedItem().toString());
                    if (GUICheckBox.isSelected()) {
                        Gui gui = new Gui(j);
                        ea.addObserver(gui);
                    }
                        Thread t = new Thread(ea);
                        t.start();

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

        spinnerIterations.setValue(1);
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