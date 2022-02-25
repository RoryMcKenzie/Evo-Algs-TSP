import javax.swing.*;
import java.io.File;

public class StartMenuGui {
    private JButton startButton;
    private JPanel panel1;
    private JCheckBox GUICheckBox;
    private JComboBox comboBoxMutation;
    private JComboBox comboBoxCrossover;
    private JSpinner spinnerIterations;
    private JComboBox comboBoxInstance;

    public StartMenuGui() {
        startButton.addActionListener(e -> {
            String writename = comboBoxCrossover.getSelectedItem() + "_" + comboBoxMutation.getSelectedItem() + "_" + comboBoxInstance.getSelectedItem().toString() + "_" + spinnerIterations.getValue() + ".csv";

            File myObj = new File("results/" + writename);
            if (myObj.delete()) {
                System.out.println("Deleted the file: " + myObj.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
            //should probably have an error if spinnerIterations.getValue() < 1, but still works
            for (int j = 0; j < (Integer)spinnerIterations.getValue(); j++) {
                    EA ea = new EA(comboBoxCrossover.getSelectedItem().toString(), comboBoxMutation.getSelectedItem().toString(), comboBoxInstance.getSelectedItem().toString(), (Integer) spinnerIterations.getValue());
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

        comboBoxInstance.addItem("berlin52");
        comboBoxInstance.addItem("burma14");
        comboBoxInstance.addItem("dj38");
        comboBoxInstance.addItem("dsj1000");
        comboBoxInstance.addItem("gr666");
        comboBoxInstance.addItem("rat99");

        GUICheckBox.setSelected(false);
        spinnerIterations.setValue(10);
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