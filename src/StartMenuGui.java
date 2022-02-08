import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenuGui {
    private JButton startButton;
    private JPanel panel1;
    private JCheckBox GUICheckBox;
    private JComboBox comboBox1;
    private JComboBox comboBox2;

    public StartMenuGui() {
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
    }

    public static void main (String[] args){

        JFrame frame = new JFrame("StartMenuGui");
        frame.setContentPane(new StartMenuGui().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        /*JFrame f=new JFrame();//creating instance of JFrame
        //startButton.setSize(400,500);
        JButton b=new JButton("click");//creating instance of JButton

        f.add(b);//adding button in JFrame

        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible */

    }
}
