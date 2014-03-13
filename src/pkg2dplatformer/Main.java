
package pkg2dplatformer;

import java.awt.Color;
import javax.swing.*;

/**
 *
 * @author Niko
 * 
 * Code based off: https://jasonjavaadventure.wikispaces.com/Java+Game+1+-+Space+Duck
 */
public class Main {
    public static frame f;
    public static int width = 800;
    public static int height = 600;
    
    public static void main(String[] args) {
        f = new frame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setSize(width,height);
        f.setTitle("Niko's 2d platformer");
        f.setLocationRelativeTo(null);
    }
}
