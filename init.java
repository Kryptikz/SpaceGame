import java.awt.*;
import javax.swing.*;
public class init {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Game Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920,1080);
        frame.setResizable(false);
        frame.setVisible(true);
        Display d = new Display();
        frame.add(d);
        d.setVisible(true);
        KeyboardThread kt = new KeyboardThread(d);
        frame.addKeyListener(kt);
        frame.getContentPane().setBackground(Color.BLACK);
        
    }
}