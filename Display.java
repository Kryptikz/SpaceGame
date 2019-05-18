import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
public class Display extends JComponent{
    private final int FOV = 90;
    private final int ASPECT = 1;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1920;
    private final int ACTUALHEIGHT = 1080;
    private ArrayList<ZObject> screenobjects;
    private ArrayList<ZObject> stars;
    private boolean w,a,s,d,up,down,right,left,space,e,q;
    private double momenx;
    private double momeny;
    private double momenz;
    public Display(){
        screenobjects = new ArrayList<ZObject>();
        stars = new ArrayList<ZObject>();
        w=a=s=d=up=down=right=left=space=e=q=false;
        for(int i=0;i<10000;i++) {
            Color[] starcolors = new Color[]{Color.WHITE,new Color(255,167,0),new Color(0,204,255),new Color(255,0,204)};
            Color scol = starcolors[(int)(Math.random()*starcolors.length)];
            stars.add(new ZObject(new OtherPoint((Math.random()*40000)-20000,(Math.random()*40000)-20000,(Math.random()*40000)-20000),scol));
        }
        momenx=momeny=momenz=.01;
        (new Thread(new FrameThread(this,60))).start();
    }
    public void update() {
        double mov = .05;
        double rot = .01;
        
        if (e) {
            stars = this.look('z',-rot,stars);
        } else if (q) {
            stars = this.look('z',rot,stars);
        }
        
        if (w) {
            //stars = this.move('z',-mov,stars);
            momenz-=mov;
        }
        if (a) {
            //stars = this.move('x',mov,stars);
            momenx+=mov;
        }
        if (s) {
            //stars = this.move('z',mov,stars);
            momenz+=mov;
        }
        if (d) {
            //stars = this.move('x',-mov,stars);
            momenx-=mov;
        }
        double movx = (Math.abs(momenx)/momenx)*Math.log(Math.abs(momenx)+1)/Math.log(2);
        double movy = (Math.abs(momeny)/momeny)*Math.log(Math.abs(momeny)+1)/Math.log(2);
        double movz = (Math.abs(momenz)/momenz)*Math.log(Math.abs(momenz)+1)/Math.log(2); 
        
        if (Double.isNaN(movx)) {
            movx=0;
        }
        if (Double.isNaN(movy)) {
            movy=0;
        }
        if (Double.isNaN(movz)) {
            movz=0;
        }

        
        //System.out.println(movx);
        
        
        
        if(Math.abs(momenx)>12) {
            momenx=12*(Math.abs(momenx)/momenx);
        }
        if(Math.abs(momeny)>12) {
            momeny=12*(Math.abs(momeny)/momeny);
        }
        if(Math.abs(momenz)>12) {
            momenz=12*(Math.abs(momenz)/momenz);
        }
        
        
        //System.out.println(momenx + " " + movx);
        if (up) {
            stars = this.look('x',rot,stars);
        }
        if (down) {
            stars = this.look('x',-rot,stars);
        }
        if (right) {
            stars = this.look('y',-rot,stars);
        } 
        if (left) {
            stars = this.look('y',rot,stars);
        }
        
        if (space) {
            if (momenx!=0) {
                momenx=(Math.abs(momenx)/momenx)*(Math.abs(momenx)-.05);
                if (Math.abs(momenx)<.05) {
                    momenx=0;
                }
            }
            if (momeny!=0) {
                momeny=(Math.abs(momeny)/momeny)*(Math.abs(momeny)-.05);
                if (Math.abs(momeny)<.05) {
                    momeny=0;
                }
            }
            if (momenz!=0) {
                momenz=(Math.abs(momenz)/momenz)*(Math.abs(momenz)-.05);
                if (Math.abs(momenz)<.05) {
                    momenz=0;
                }
            }
        }
        
        stars = move(movx,movy,movz,stars);
    }
    public void redraw(){
        super.repaint();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(ZObject z : screenobjects) {
            if (z.getType().equals("Point")) {
                double[] oneproj = Calculate.project2Ddouble(new double[]{z.getOne().getX(),z.getOne().getY(),z.getOne().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                int x = (int)(oneproj[0]*WIDTH);
                int y = (int)(oneproj[1]*HEIGHT);
                g.setColor(z.getColor());
                g.fillRect(x,y-840,1,1);
            } else if (z.getType().equals("Polygon")) {
                double[] oneproj = Calculate.project2Ddouble(new double[]{z.getPolygon().getOne().getX(),z.getPolygon().getOne().getY(),z.getPolygon().getOne().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] twoproj = Calculate.project2Ddouble(new double[]{z.getPolygon().getTwo().getX(),z.getPolygon().getTwo().getY(),z.getPolygon().getTwo().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] threeproj = Calculate.project2Ddouble(new double[]{z.getPolygon().getThree().getX(),z.getPolygon().getThree().getY(),z.getPolygon().getThree().getSpecialZ(),1},FOV,ASPECT,5.0,100.0);
                int[] xp = new int[]{(int)(WIDTH*oneproj[0]),(int)(WIDTH*twoproj[0]),(int)(WIDTH*threeproj[0])};
                int[] yp = new int[]{(int)(HEIGHT*oneproj[1])-840,(int)(HEIGHT*twoproj[1])-840,(int)(HEIGHT*threeproj[1])-840};
                g.setColor(z.getPolygon().getColor());
                g.fillPolygon(xp,yp,3);
                g.setColor(Color.BLACK);
                g.drawPolygon(xp,yp,3);
            } else if (z.getType().equals("Quad")) {
                double[] oneproj = Calculate.project2Ddouble(new double[]{z.getQuad().getOne().getX(),z.getQuad().getOne().getY(),z.getQuad().getOne().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] twoproj = Calculate.project2Ddouble(new double[]{z.getQuad().getTwo().getX(),z.getQuad().getTwo().getY(),z.getQuad().getTwo().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] threeproj = Calculate.project2Ddouble(new double[]{z.getQuad().getThree().getX(),z.getQuad().getThree().getY(),z.getQuad().getThree().getSpecialZ(),1},FOV,ASPECT,5.0,100.0);     
                double[] fourproj = Calculate.project2Ddouble(new double[]{z.getQuad().getFour().getX(),z.getQuad().getFour().getY(),z.getQuad().getFour().getSpecialZ(),1},FOV,ASPECT,5.0,100.0);
                int[] xp = new int[]{(int)(WIDTH*oneproj[0]),(int)(WIDTH*twoproj[0]),(int)(WIDTH*threeproj[0]),(int)(WIDTH*fourproj[0])};
                int[] yp = new int[]{(int)(HEIGHT*oneproj[1])-840,(int)(HEIGHT*twoproj[1])-840,(int)(HEIGHT*threeproj[1])-840,(int)(HEIGHT*fourproj[1])-840};
                g.setColor(z.getQuad().getColor());
                g.fillPolygon(xp,yp,4);
                g.setColor(Color.BLACK);
                g.drawPolygon(xp,yp,4);
            }
        }
        for(ZObject z : stars) {
            if (z.getType().equals("Point")) {
                double[] oneproj = Calculate.project2Ddouble(new double[]{z.getOne().getX(),z.getOne().getY(),z.getOne().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                int x = (int)(oneproj[0]*WIDTH);
                int y = (int)(oneproj[1]*HEIGHT);
                g.setColor(z.getColor());
                g.fillRect(x,y-607,1,1);
            } else if (z.getType().equals("Polygon")) {
                double[] oneproj = Calculate.project2Ddouble(new double[]{z.getPolygon().getOne().getX(),z.getPolygon().getOne().getY(),z.getPolygon().getOne().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] twoproj = Calculate.project2Ddouble(new double[]{z.getPolygon().getTwo().getX(),z.getPolygon().getTwo().getY(),z.getPolygon().getTwo().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] threeproj = Calculate.project2Ddouble(new double[]{z.getPolygon().getThree().getX(),z.getPolygon().getThree().getY(),z.getPolygon().getThree().getSpecialZ(),1},FOV,ASPECT,5.0,100.0);
                int[] xp = new int[]{(int)(WIDTH*oneproj[0]),(int)(WIDTH*twoproj[0]),(int)(WIDTH*threeproj[0])};
                int[] yp = new int[]{(int)(HEIGHT*oneproj[1]),(int)(HEIGHT*twoproj[1]),(int)(HEIGHT*threeproj[1])};
                g.setColor(z.getPolygon().getColor());
                g.fillPolygon(xp,yp,3);
                g.setColor(Color.BLACK);
                g.drawPolygon(xp,yp,3);
            } else if (z.getType().equals("Quad")) {
                double[] oneproj = Calculate.project2Ddouble(new double[]{z.getQuad().getOne().getX(),z.getQuad().getOne().getY(),z.getQuad().getOne().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] twoproj = Calculate.project2Ddouble(new double[]{z.getQuad().getTwo().getX(),z.getQuad().getTwo().getY(),z.getQuad().getTwo().getSpecialZ(),1},FOV,ASPECT,0.0,100.0);
                double[] threeproj = Calculate.project2Ddouble(new double[]{z.getQuad().getThree().getX(),z.getQuad().getThree().getY(),z.getQuad().getThree().getSpecialZ(),1},FOV,ASPECT,5.0,100.0);     
                double[] fourproj = Calculate.project2Ddouble(new double[]{z.getQuad().getFour().getX(),z.getQuad().getFour().getY(),z.getQuad().getFour().getSpecialZ(),1},FOV,ASPECT,5.0,100.0);
                int[] xp = new int[]{(int)(WIDTH*oneproj[0]),(int)(WIDTH*twoproj[0]),(int)(WIDTH*threeproj[0]),(int)(WIDTH*fourproj[0])};
                int[] yp = new int[]{(int)(HEIGHT*oneproj[1]),(int)(HEIGHT*twoproj[1]),(int)(HEIGHT*threeproj[1]),(int)(HEIGHT*fourproj[1])};
                g.setColor(z.getQuad().getColor());
                g.fillPolygon(xp,yp,4);
                g.setColor(Color.BLACK);
                g.drawPolygon(xp,yp,4);
            }
        }
        
        g.setColor(new Color(255,0,0,100));
        g.fillOval(WIDTH/2-3, ACTUALHEIGHT/2-3, 6, 6);
        
        g.setColor(Color.BLUE);
        int startx=30;
        int starty=ACTUALHEIGHT-100;
        for(int i=0;i<12;i++) {
            g.fillRect(startx,starty,30,10);
            starty-=15;
        }
        if (momenz>0) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.GREEN);
        }
        starty=ACTUALHEIGHT-100;
        for(int i=0;i<(int)Math.abs(momenz);i++) {
            g.fillRect(startx,starty,30,10);
            starty-=15;
        }
        
    }
    public void aPress() {
        a=true;
    }
    public void aRelease() {
        a=false;
    }
    public void dPress() {
        d=true;
    }
    public void dRelease() {
        d=false;
    }
    public void wPress() {
        w=true;
    }
    public void wRelease() {
        w=false;
    }
    public void sPress() {
        s=true;
    }
    public void sRelease() {
        s=false;
    }
    
    public void upPress() {
        up=true;
    }
    public void upRelease() {
        up=false;
    }
    public void downPress() {
        down=true;
    }
    public void downRelease() {
        down=false;
    }
    public void rightPress() {
        right=true;
    }
    public void rightRelease() {
        right=false;
    }
    public void leftPress() {
        left=true;
    }
    public void leftRelease() {
        left=false;
    }
    public void spacePress() {
        space=true;
    }
    public void spaceRelease() {
        space=false;
    }
    public void ePress() {
        e=true;
    }
    public void eRelease() {
        e=false;
    }
    public void qPress() {
        q=true;
    }
    public void qRelease() {
        q=false;
    }
    
    public ArrayList<ZObject> move(double xdist, double ydist, double zdist, ArrayList<ZObject> pzobjects) {
        long moveStartTime = System.nanoTime();
        ArrayList<ZObject> tempzobj = new ArrayList<ZObject>();
        //double xdist = 0;
        //double ydist = 0;
        //double zdist = 0;
        /*if (dir == 'x') {
            xdist = dis;
        }
        if (dir == 'y') {
            ydist = dis;
        }
        if (dir == 'z') {
            zdist = dis;
        }*/
        for (ZObject zo : pzobjects) {
            if (zo.getType().equals("Point")) {
                double[] oreturned = Calculate.translateDouble(zo.getOneList(), xdist, ydist, zdist);
                OtherPoint dpone = new OtherPoint(oreturned[0],oreturned[1],oreturned[2]);
                tempzobj.add(new ZObject(dpone,zo.getColor()));
            }
            if (zo.getType().equals("Vector")) {
                double[] oreturned = Calculate.translateDouble(zo.getOneList(), xdist, ydist, zdist);
                OtherPoint dpone = new OtherPoint(oreturned[0],oreturned[1],oreturned[2]);
                double[] treturned = Calculate.translateDouble(zo.getTwoList(), xdist, ydist, zdist);
                OtherPoint dptwo = new OtherPoint(treturned[0],treturned[1],treturned[2]);            
                tempzobj.add(new ZObject(dpone,dptwo));               
            }
            if (zo.getType().equals("Polygon")) {
                double[] oreturned = Calculate.translateDouble(zo.getOneList(), xdist, ydist, zdist);            
                double[] twreturned = Calculate.translateDouble(zo.getTwoList(), xdist, ydist, zdist);
                double[] trreturned = Calculate.translateDouble(zo.getThreeList(), xdist, ydist, zdist);
                OtherPoint dpone = new OtherPoint(oreturned[0],oreturned[1],oreturned[2]);
                OtherPoint dptwo = new OtherPoint(twreturned[0],twreturned[1],twreturned[2]);
                OtherPoint dpthree = new OtherPoint(trreturned[0],trreturned[1],trreturned[2]);
                tempzobj.add(new ZObject(dpone,dptwo,dpthree,zo.getColor()));                
            }
        }
        return tempzobj;
        //pzobjects = tempzobj;
        //System.out.println("TRANSLATION MOVEMENT TOOK: " + (System.nanoTime()-moveStartTime)/1000000000.0 + " seconds");     
        //redraw();
    }
    public ArrayList<ZObject> look(char ax, double angle, ArrayList<ZObject> spzobjects) {
        long lookStartTime = System.nanoTime();
        ArrayList<ZObject> tempzobj = new ArrayList<ZObject>();
        for (ZObject zo : spzobjects) {
            if (zo.getType().equals("Point")) {
                double[] oreturned = Calculate.rotateDouble(zo.getOneList(), ax, angle);
                OtherPoint dpone = new OtherPoint(oreturned[0],oreturned[1],oreturned[2]);
                tempzobj.add(new ZObject(dpone,zo.getColor()));
            }
            if (zo.getType().equals("Vector")) {
                double[] oreturned = Calculate.rotateDouble(zo.getOneList(), ax, angle);
                OtherPoint dpone = new OtherPoint(oreturned[0],oreturned[1],oreturned[2]);
                double[] treturned = Calculate.rotateDouble(zo.getTwoList(), ax, angle);
                OtherPoint dptwo = new OtherPoint(treturned[0],treturned[1],treturned[2]);            
                tempzobj.add(new ZObject(dpone,dptwo));               
            }
            if (zo.getType().equals("Polygon")) {
                double[] oreturned = Calculate.rotateDouble(zo.getOneList(), ax, angle);          
                double[] twreturned = Calculate.rotateDouble(zo.getTwoList(), ax, angle);
                double[] trreturned = Calculate.rotateDouble(zo.getThreeList(), ax, angle);
                OtherPoint dpone = new OtherPoint(oreturned[0],oreturned[1],oreturned[2]);
                OtherPoint dptwo = new OtherPoint(twreturned[0],twreturned[1],twreturned[2]);
                OtherPoint dpthree = new OtherPoint(trreturned[0],trreturned[1],trreturned[2]);
                tempzobj.add(new ZObject(dpone,dptwo,dpthree,zo.getColor()));                
            }
        }
        return tempzobj;
        //System.out.println("TRANSFORMED OBJECT COUNT: " + tempzobj.size());
        //System.out.println("ROTATION MOVEMENT TOOK: " + (System.nanoTime()-lookStartTime)/1000000000.0 + " seconds");          
        //redraw();
    }
}