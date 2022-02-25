import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class StartMenuGui {
    private JButton startButton;
    private JPanel panel1;
    private JCheckBox GUICheckBox;
    private JComboBox comboBoxMutation;
    private JComboBox comboBoxCrossover;
    private JSpinner spinnerIterations;
    private JComboBox comboBoxInstance;

    private ArrayList<Thread> threads;

    public StartMenuGui() {
        startButton.addActionListener(e -> {

            String crossover;
            String mutation;

            for (int i = 1; i <= 12; i++) {

                threads = new ArrayList<>();

                if (i<=4){
                    crossover = "PMX";
                } else if (i<=8){
                    crossover = "Order";
                } else {
                    crossover = "Cycle";
                }

                if (i % 4 == 1){
                    mutation = "Swap";
                } else if (i % 4 == 2){
                    mutation = "Scramble";
                } else if (i % 4 == 3){
                    mutation = "Invert";
                } else {
                    mutation = "2-opt";
                }


                //String writename = comboBoxCrossover.getSelectedItem() + "_" + comboBoxMutation.getSelectedItem() + "_" + comboBoxInstance.getSelectedItem().toString() + "_" + spinnerIterations.getValue() + ".csv";

                String writename = crossover + "_" + mutation + "_" + comboBoxInstance.getSelectedItem() + "_" + spinnerIterations.getValue() + ".csv";

                File myObj = new File("results/" + comboBoxInstance.getSelectedItem() + "/" + writename);
                if (myObj.delete()) {
                    System.out.println("Deleted the file: " + myObj.getName());
                } else {
                    System.out.println("Failed to delete " +myObj.getName());
                }
                //should probably have an error if spinnerIterations.getValue() < 1, but still works
                for (int j = 0; j < (Integer) spinnerIterations.getValue(); j++) {
                    EA ea = new EA(crossover, mutation, comboBoxInstance.getSelectedItem().toString(), (Integer) spinnerIterations.getValue());
                    if (GUICheckBox.isSelected()) {
                        Gui gui = new Gui(j);
                        ea.addObserver(gui);
                    }
                    Thread t = new Thread(ea);
                    t.start();
                    threads.add(t);
                }
                for (Thread thread : threads){
                    try {
                        thread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
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