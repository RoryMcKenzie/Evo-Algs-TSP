import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuGui {
    private JButton startButton;
    private JPanel panel1;
    private JCheckBox GUICheckBox;
    private JComboBox comboBoxMutation;
    private JComboBox comboBoxCrossover;

    public StartMenuGui() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //use .getSelectedItem() for combobox to set crossover/mutation operators

                for(int i = 0; i < 20; i++) {
                    Gui gui = new Gui(i);
                    EA ea = new EA();
                    //islands.add(ea);
                    ea.addObserver(gui);
                    Thread t = new Thread(ea);
                    t.start();
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
