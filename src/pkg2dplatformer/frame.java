package pkg2dplatformer;

import java.awt.GridLayout;
import javax.swing.*;
 
public class frame extends JFrame {
    public rungame p;
 
    public frame() {
        p = new rungame(this);
        setLayout(new GridLayout(1, 1, 0, 0));
        add(p);
    }
 
}