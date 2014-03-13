/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2dplatformer;

import graphics.loadgfx;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import physics.loadphysics;


public class rungame extends JPanel implements Runnable {
    // This is the game thread that runs the entire game
    public Thread gamethread;
    
    //Number of levels
    public int levelcount = 0;
    //Keybinds
    public int keyJump = KeyEvent.VK_SPACE;
    public int keyLeft = KeyEvent.VK_A;
    public int keyRight = KeyEvent.VK_D;
    public int keyPause = KeyEvent.VK_P;
    
    //For scrolling
    public int xs=0;
    public int ys=0;
    public double starswing=0;
    
    //Level information
    public String levelname;
    public Rectangle[] solidobjects;
    public Rectangle[] transobjects;
    public Rectangle[] stars;
    public Rectangle portal;
    public Integer[] spawnloc = {0,0}; //x,y
    
    //Character
    public Rectangle charbox;
    
    //Progress
    public Integer starcount = 0;
    public Integer level = 0;
    public Integer player = 1;
    public Boolean portalactive = false;
    public Integer deathcount=0;
    public Boolean win = false;
    
    public Boolean running = true;
    public Boolean ingame = false;
    
    //Left,Right,Air
    public Boolean[] movement = {false,false,true};
    
    //vvelocity, airtime
    public Integer[] jumpinfo = {0,0};

    loadgfx loadgfx = new loadgfx();
    loadphysics loadphysics = new loadphysics();
    public rungame(frame f){
        /*
         * This will run once:
         * 1. Load the solid and transparent blocks
         * 2. Load the character
         * 3. Start the thread for the game
         * 4. Add keybinds
         * 5. Count number of levels
         * 
         */
        load();
        
        setBackground(Color.BLACK);
        gamethread = new Thread(this);
        gamethread.start();
        
        
        levelcount = loadgfx.getLevelCount();
        
        
        //Add the keybinds
        f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == keyLeft) {
                    movement[0] = true;
                }
 
                if(e.getKeyCode() == keyRight) {
                    movement[1] = true;
                }
 
                if(e.getKeyCode() == keyJump && !movement[2]) {
                    movement[2] = true;
                    jumpinfo[0] = 5;
                    jumpinfo[1] =0;
                }
                if(e.getKeyCode() == keyPause) {
                }
                if(e.getKeyCode() == KeyEvent.VK_S){
                    if(win){
                        level = 0;
                        starcount = 0;
                        deathcount = 0;
                        win = false;
                        load();
                    }
                    spawn();
                }
                if(e.getKeyCode() == KeyEvent.VK_1){
                    nextLevel();
                }
                if(e.getKeyCode() == KeyEvent.VK_2){
                    level-=2;
                    nextLevel();
                }
            }
 
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == keyLeft) {
                    movement[0] = false;
                }
 
                if(e.getKeyCode() == keyRight) {
                    movement[1] = false;
                }
            }
        });
    }
    public void spawn(){
        if(ingame){
                        charbox.x=spawnloc[0];
                        charbox.y=spawnloc[1];
                        xs=charbox.x-400;
                        System.out.println(charbox.x+"-"+spawnloc[0]);
                        ys=spawnloc[1]-200;
                        jumpinfo[0] = 0;
                        jumpinfo[1] = 0;
                        deathcount++;
                    }
        ingame = true;
    }
    public void load(){
        //Get level name
        levelname = loadgfx.getLevelName(level);
        
        //Load platforms
        
        solidobjects = loadgfx.loadPlatforms(level).get("solid");
        transobjects = loadgfx.loadPlatforms(level).get("trans");
        
        //Load final portal
        portal = loadgfx.loadPlatforms(level).get("portal")[0];
        
        //Load teleporters
        
        
        //Get the spawn location
        spawnloc = loadgfx.loadSpawn(level);
        
        //Populate star list
        
        charbox = new Rectangle(spawnloc[0],spawnloc[1],30,30);
        ys=spawnloc[1]-100;
        xs=charbox.x-400;
        stars = loadgfx.loadStars(level);
        
        
    }
    public void move(){
        //Store the y value before moving. This will help us determine how to scroll the screen
        int oldy = charbox.y;
        
        //Checks for collisions, if no collisions then it makes the character fall
        if(!loadphysics.checkcollisions(movement, solidobjects, charbox, jumpinfo)){
            charbox = loadphysics.fall(movement,charbox,jumpinfo);
        }
        
        //Change the scroll variable, move charbox left/right
        xs=loadphysics.walk(movement,solidobjects,charbox,jumpinfo, xs);
        
        //Add a unit to airtime
        if(movement[2]){
            jumpinfo[1]++;
        }
        
        //Check for star intersection
        starcount = loadphysics.checkStars(stars, charbox, starcount);
        
        //Check if all stars have been collected
        if(starcount == stars.length){
            portalactive = true;
        }
        
        //If collision with the portal and portal is active, then go to next level
        if(portalactive && charbox.intersects(portal)){
            nextLevel();
        }
        
        //If player leaves bounds
        if(charbox.y>5000){
            spawn();
        }
        
        //Scroll
        if(charbox.y<(ys+100) || charbox.y>(ys+350)){ //Out of bounds
            ys+=(charbox.y-oldy);
        }
        
    }
    /*
     * Advances character to the next level, resetting all important variables.
     */
    public void nextLevel(){
        if((level+1)==levelcount){
            win = true;
            ingame = false;
        } else{
        starcount = 0;
        level++;
        load();
        charbox.x=spawnloc[0];
        charbox.y=spawnloc[1];
        xs=charbox.x-400;
        ys=spawnloc[1]-200;
        jumpinfo[0] = 0;
        jumpinfo[1] = 0;
        }
    }
    public Boolean inbounds(Rectangle r){
        if((r.x>(xs-200) && r.x<(xs+1000))|| ((r.x+r.width)>(xs-200)) && ((r.x+r.width)<(xs+1000)) || ((charbox.x>r.x) && (charbox.x<(r.x+r.width)))){
            return true;
        } else{
            return false;
        }
    }
/*
 * Paint on to the canvas
 */
    public void paintComponent(Graphics g){
        //Swing stars along a sin curve
        for(int i=0;i<stars.length;i++){
            stars[i].y += (Math.sin(starswing+i)*5) + Math.random();
        }
        //Draw canvas
        super.paintComponent(g);
        if(ingame){
            //Paint the character
            g.setColor(Color.BLUE);
            g.fillRect(charbox.x-xs,charbox.y-ys,charbox.width,charbox.height);
            //Paint the solid objects
            g.setColor(Color.WHITE);
            for(int i=0;i<solidobjects.length;i++){
                if(inbounds(solidobjects[i])){
                g.fillRect(solidobjects[i].x-xs,solidobjects[i].y-ys,solidobjects[i].width,solidobjects[i].height);
                }
            }

            //Paint the transparent objects
            g.setColor(Color.WHITE);
            for(int i=0;i<transobjects.length;i++){
                g.fillRect(transobjects[i].x-xs,transobjects[i].y-ys,transobjects[i].width,transobjects[i].height);
            }
            //Paint the portal to end the level
            g.setColor(Color.PINK);
            g.fillRect(portal.x-xs,portal.y-ys,portal.width,portal.height);
            
            //Draw stars
            g.setColor(Color.YELLOW);
            for(int i=0;i<stars.length;i++){
                g.fillRect(stars[i].x -xs,stars[i].y-ys,stars[i].width,stars[i].height);
            }

            //Onscreen text
            g.setColor(Color.WHITE);
            /* For testing purposes
            g.drawString("X:"+charbox.x, 5, 10);
            g.drawString("Y:"+charbox.y, 5,20);
            g.drawString("Scroll:"+xs+","+ys,5,30);
            */
            g.drawString("Level:"+(level+1)+"/"+levelcount,250,10);
            g.drawString("Score:"+starcount+"/"+stars.length,350,10);
            g.drawString("Deaths:"+deathcount,450,10);
            g.setColor(Color.ORANGE);
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,60));
            g.drawString("\""+levelname+"\"",(spawnloc[0])-xs-200,50+spawnloc[1]-ys);
        } else if(win){
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.DIALOG,Font.BOLD,60));
            g.drawString("YOU WIN!", 300, 100);
            g.setFont(new Font(Font.DIALOG,Font.BOLD,20));
            g.drawString("Total deaths:"+deathcount, 100, 220);
            g.setColor(Color.RED);
            g.drawString("Thanks for playing!",100,240);
            g.drawString("-To play again, press \"S\"",120,260);
            g.setColor(Color.BLUE);
            g.drawString("Game created by: Niko Savas,2013",400,400);
        }else{
            //Main Menu
            g.setColor(Color.BLUE);
            g.fillRect(100,100,30,30);
            g.setColor(Color.YELLOW);
            g.fillRect(115,150,5,5);
            g.setColor(Color.PINK);
            g.fillRect(100, 200, 40, 40);
            g.setColor(Color.WHITE);
            g.drawString("This is you.",150,120);
            g.drawString("Collect these stars in each level",150,153);
            g.drawString("Get to the portal once you've collected all stars", 150, 230);
            g.drawString("Keys:", 150, 280);
            g.drawString("A,D   - Movement",150,305);
            g.drawString("Space - Jump", 150, 320);
            g.drawString("S     - Restart level",150, 335);
            g.setColor(Color.RED);
            g.drawString("PRESS \"S\" TO START", 250,450);
        }
    }
    /*
     * This handles things like framerate and ticks. 1 tick =/= 1 frame
     * Tick: Game refreshes positions
     * Frame: When drawing to the canvas takes place
     */
    public void run() {
        
        long lastTime = System.nanoTime(); //Current time in nano seconds
        double tickspersecond = (240D*1D);
        double nsPerTick = 1000000000D/tickspersecond; //Nanoseconds per tick: 1 second/60 frames
        
        int ticks = 0; // Current Updates
        int fps = 0; // Current fps
        
        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        
        load();
        while(running){
            //Update the current time
            long now = System.nanoTime();
            //Delta is the time in TICKS since last tick: (current ns - last ns / amount of ns in one tick)           
            delta += (now-lastTime)/nsPerTick;
            //Update lastTime to reflect this latest tick
            lastTime = now;
            
            //If shouldRender is true, draw a frame. If this starts as true it will never become false
            boolean shouldRender = false;
            
            //If it has been longer than nsPerTick milliseconds since the last tick, tick.
            while(delta>=1){
                ticks++;
                move();
                delta-=1; //Resets delta to essentially 0 without losing a bunch of decimal places
                shouldRender = true;
            }
            //Call a frame (draw on canvas)
            if(shouldRender && ((ticks%(tickspersecond/60))==0)){
            fps++;
            starswing+=.1;
            repaint();
            }
            //If a second has passed
            if((System.currentTimeMillis() - lastTimer) >= 1000){
                lastTimer += 1000;
                
                System.out.println(fps+","+ticks);
                fps = 0;
                ticks = 0;
            }
        }
    }
}
