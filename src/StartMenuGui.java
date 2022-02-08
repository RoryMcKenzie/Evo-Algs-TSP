import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuGui {
    private JButton startButton;
    private JPanel panel1;
    private JCheckBox GUICheckBox;
    private JComboBox comboBoxMutation;
    private JComboBox comboBoxCrossover;
    private JSpinner spinnerIterations;


    //Need to get it so each EA instance "returns" a value so the best EA out of 20 can be discovered as the best, so can be run multiple times
    //This doesn't seem quite possible with Runnable
    //Needs to be done so I can do something with the JSpinner
    public StartMenuGui() {
        startButton.addActionListener(e -> {
            for(int i = 0; i < 20; i++) {
                EA ea = new EA(comboBoxCrossover.getSelectedItem().toString(), comboBoxMutation.getSelectedItem().toString());
                //islands.add(ea);
                if (GUICheckBox.isSelected()) {
                    Gui gui = new Gui(i);
                    ea.addObserver(gui);
                }
                Thread t = new Thread(ea);
                t.start();
            }
        });
        comboBoxCrossover.addItem("PMX");
        comboBoxCrossover.addItem("Order");
        comboBoxCrossover.addItem("Cycle");

        comboBoxMutation.addItem("Swap");
        comboBoxMutation.addItem("Scramble");
        comboBoxMutation.addItem("Invert");
        comboBoxMutation.addItem("2-opt");
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
