/*
 * This will load the physics necessary for all of the types of platforms that exist on the level.
 */
package physics;

import java.awt.Rectangle;

public class loadphysics {

    public Boolean checkcollisions(Boolean[] movement, Rectangle[] solidobjects, Rectangle character, Integer[] jumpinfo) {
        Boolean left = movement[0];
        Boolean right = movement[1];
        Boolean air = movement[2];
        
        Integer airtime = jumpinfo[1];
        Integer vvelocity = jumpinfo[0];
        
        Rectangle newbox = new Rectangle(character.x,character.y,character.width,character.height);
        
        
        if(air){
            //Air
            //d= vit + 1/2at^2
            Integer d = (int) Math.round(vvelocity - 0.001*airtime*airtime);
            newbox.y -= d;
            for(Rectangle collide:solidobjects){
                //Hits floor
                if(collide.intersects(newbox) && (character.y<newbox.y) ){
                    movement[2]=false;
                    character.y = collide.y-character.height;
                    return true;
                }
                
                //Hits roof
                if(collide.intersects(newbox) && (character.y>newbox.y)){
                    movement[2]=true;
                    character.y = collide.y + collide.height;
                    jumpinfo[1] = 0;
                    jumpinfo[0] = 0;
                    return true;
                }
            }
        }
        return false;
    }
    public Rectangle fall(Boolean[] movement, Rectangle charbox, Integer[] jumpinfo){
        Boolean left = movement[0];
        Boolean right = movement[1];
        Boolean air = movement[2];
        
        Integer airtime = jumpinfo[1];
        Integer vvelocity = jumpinfo[0];
        Integer terminalvelocity = -10;
        
        if(air){
            Integer d = (int) Math.round(vvelocity - 0.001*airtime*airtime);
            if(d<terminalvelocity){d=terminalvelocity;}
            charbox.y -= d;
        }
        return charbox;
    }

    public Integer walk(Boolean[] movement, Rectangle[] solidobjects, Rectangle charbox, Integer[] jumpinfo, Integer xs) {
        Boolean left = movement[0];
        Boolean right = movement[1];
        int oldx = charbox.x;
        int olds = xs;
        if(left){
            charbox.x--;
            if(charbox.x<(150+xs)){
            xs--;
            }
        }
        if(right){
            charbox.x++;
            if(charbox.x>(400+xs)){
            xs++;
            }
        }
        for(Rectangle collide:solidobjects){
                if(collide.intersects(charbox)){
                    charbox.x=oldx;
                    xs= olds;
                }
            }
        
        //Test for falling
        if(!movement[2]){
        Rectangle colbox = new Rectangle(charbox.x,charbox.y + 1,charbox.width,charbox.height);
        Boolean colliding = false;
        
        for(Rectangle collide: solidobjects){
            if(collide.intersects(colbox)){
                colliding = true;
                break;
            }
        }
        if(!colliding){
            movement[2]=true;
            jumpinfo[1] = 0;
            jumpinfo[0] = 0;
        }
        }
        return xs;
    }


    public Integer checkStars(Rectangle[] stars, Rectangle charbox, Integer starcount) {
        for(int i=0;i<stars.length;i++){
                if(stars[i].intersects(charbox)){
                    stars[i].width = 0;
                    starcount++;
                }
            }
        return starcount;
    }
}
