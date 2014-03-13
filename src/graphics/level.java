/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package graphics;

import java.awt.List;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

public class level {
    /*
     * 2d Array for platforms on a level
     * 
     * >Type< >xpos< >ypos< >width< >height< >Number of stars<
     * solid - Cannot move through
     * trans - Can move through
     * portal - Teleporter to next level
     */
    public String[][][] levels = {
        { //Level1
            {"0","-50","Level 1"}, //Spawn
            {"solid", "10","10","10","10","0"},
            {"solid", "50","50","10","10","0"},
            {"solid", "0","200","840","10","2"},
            {"solid","20","100","40","190","0"},
            {"solid","900","400","300","200","1"},
            {"solid","1280","300","1000","20","3"},
            {"portal","2200","260"}
        },
        { //Level2
            {"0","0","Watch your head!"},
            {"solid", "0","350","800","600","3"},
            {"solid","800","450","100","600","0"},
            {"solid","1000","450","700","400","1"},
            {"solid","1700","480","200","400","0"},
            {"solid","2000","400","100","500","1"},
            {"solid","2100","250","100","500","0"},
            {"solid","1800","100","200","150","2"},
            {"solid","2300","500","200","200","0"},
            {"portal","2350","460"}
        },
        { //Level3
            {"0","0","Things are not as they seem..."},
            {"solid","0","200","600","400","1"},
            {"solid","600","100","100","600","1"},
            {"solid","700","100","50","150","0"},
            {"trans","700","250","50","400","0"},
            {"solid","600","500","2000","30","2"},
            {"solid","750","300","1300","30","2"},
            {"solid","1750","280","300","20","0"},
            {"solid","2050","30","400","300","0"},
            {"solid","2550","400","30","30","0"},
            {"portal","2450","180"}
        },
        { //Level4
            {"0","0","Have fun."},
            {"solid","0","300","30","600","0"},
            {"solid","100","80","40","600","1"},
            {"solid","200","400","20","600","0"},
            {"solid","300","250","30","600","0"},
            {"solid","400","200","40","600","1"},
            {"solid","500","200","20","600","0"},
            {"solid","600","300","30","600","1"},
            {"solid","700","80","40","600","0"},
            {"solid","800","10","40","600","0"},
            {"solid","1000","550","300","20","1"},
            {"portal","1100","510"}
        },
        { //Level5
            {"300","50","Leap of Faith"},
            {"solid","200","150","300","25","1"},
            {"solid","500","3000","500","100","1"},
            {"portal","550","2900"}
        },
        { //Level6
            {"0","0","Easy"},
            {"solid","0","100","100","50","1"},
            {"portal","150","0"}
        },
        { //Level7
            {"300","1000","Climb, You Fool!"},
            {"solid","225","1100","175","25","1"}, 
            {"solid","175","300","50","825","0"}, //Left pole
            {"solid","400","300","50","825","0"}, //Right pole
            {"solid","225","1000","50","25","1"},
            {"solid","350","900","50","25","1"},
            {"solid","225","800","50","25","1"},
            {"solid","225","625","25","10","1"},
            {"solid","225","450","25","10","0"},
            {"portal","300","0"}
        }
    };
    public Rectangle[] getStars(Integer l){
        
        ArrayList<Rectangle> temprects = new ArrayList<>();
        
        for(int i=1;i<levels[l].length-1;i++){
            if(levels[l][i][0].equals("solid") || levels[l][i][0].equals("trans")){
                // (xpos + (width * rand)
                Integer numstars = Integer.parseInt(levels[l][i][5]);
                for(int n=numstars;n>0;n--){
                double xpos = Double.parseDouble(levels[l][i][1]) + (Double.parseDouble(levels[l][i][3]) * Math.random());
                // (ypos + (40*rand)
                double ypos = Double.parseDouble(levels[l][i][2])- (Math.random()*50)-100;
                temprects.add(new Rectangle((int)xpos,(int)ypos-5,5,5));
                }
            }
        }
        //Turn the ArrayList into an Array
        Rectangle[] stars = new Rectangle[ temprects.size() ];
        temprects.toArray( stars );
        
        return stars;
    }
    public Rectangle[] getPlatforms(Integer l, String type){
        /*
         * Get all platforms for a given level , return them in a Rectangle array.
         */
        ArrayList<Rectangle> temprects = new ArrayList<>();
        
        //For each String for the given level, make a rectangle
        for(int i=1;i<levels[l].length;i++){
            if(levels[l][i][0].equals(type)){
                int xpos = Integer.parseInt(levels[l][i][1]);
                int ypos = Integer.parseInt(levels[l][i][2]);
                int width =0;
                int height=0;
                if(type.equals("solid")||type.equals("trans")){
                    width = Integer.parseInt(levels[l][i][3]);
                    height = Integer.parseInt(levels[l][i][4]);
                } else if(type.equals("portal")){
                    width = 40;
                    height = 40;
                } else if(type.equals("tp")){
                    width=20;
                    height=20;
                }
                temprects.add(new Rectangle(xpos,ypos,width,height));
            }
        }
        //Turn the ArrayList into an Array
        Rectangle[] levelplatforms = new Rectangle[ temprects.size() ];
        temprects.toArray( levelplatforms );
        
        return levelplatforms;
    }
    public Integer[] getSpawn(Integer l){
        Integer[] spawnloc = {Integer.parseInt(levels[l][0][0]),Integer.parseInt(levels[l][0][1])};
        return spawnloc;
    }

    Integer count() {
        return levels.length;
    }

    String levelname(int l) {
        return levels[l][0][2];
    }
}
