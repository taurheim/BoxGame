/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JFrame;
import graphics.level;
import java.util.HashMap;

public class loadgfx extends JFrame {
    level level = new level();
    public void loadscreen(){
    }
    public HashMap<String, Rectangle[]> loadPlatforms(int lv){
        //Get the character
        

        HashMap<String, Rectangle[]> map = new HashMap<String, Rectangle[]>();
        map.put("solid", level.getPlatforms(lv, "solid"));
        map.put("trans", level.getPlatforms(lv, "trans"));
        map.put("portal",level.getPlatforms(lv,"portal"));
        //Return the array of rectangles
        return map;
    }

    public Rectangle[] loadStars(Integer l) {
        Rectangle[] stars = level.getStars(l);
        return stars;
    }
    public Integer[] loadSpawn(Integer l){
        Integer[] spawn = level.getSpawn(l);
        return spawn;
    }
    public Integer getLevelCount(){
        return level.count();
    }

    public String getLevelName(int l) {
        return level.levelname(l);
    }
}
