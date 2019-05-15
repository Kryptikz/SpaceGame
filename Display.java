import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
public class Display extends JComponent{
    private ArrayList<int[]> stars;
    private double rotation;
    private Color groundColor;
    private final int FOV = 90;
    private final int ASPECT = 1;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    private ArrayList<ZObject> screenobjects;
    private boolean a;
    private boolean b;
    private boolean c;
    private boolean d;
    public Display(){
        screenobjects = new ArrayList<ZObject>();
        a=b=c=d=false;
        (new Thread(new FrameThread(this,60))).start();
    }
    public void redraw(){
        super.repaint();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(ZObject z : onscreen) {
            if (z.getType().equals("Polygon")) {
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
    }
    public ArrayList<ZObject> MoveCamera(ArrayList<ZObject> objects, char dir, double dis) {
        ArrayList<ZObject> tempzobj = new ArrayList<ZObject>();
        double xdist = 0;
        double ydist = 0;
        double zdist = 0;
        if (dir == 'x') {
            xdist = dis;
        }
        if (dir == 'y') {
            ydist = dis;
        }
        if (dir == 'z') {
            zdist = dis;
        }
        for (ZObject zo : objects) {
            if (zo.getType().equals("Quad")) {
                double[] oreturned = Calculate.translateDouble(zo.getOneList(), xdist, ydist, zdist);            
                double[] twreturned = Calculate.translateDouble(zo.getTwoList(), xdist, ydist, zdist);
                double[] trreturned = Calculate.translateDouble(zo.getThreeList(), xdist, ydist, zdist);   
                double[] freturned = Calculate.translateDouble(zo.getFourList(), xdist, ydist, zdist);
                OtherPoint dpone = new OtherPoint(oreturned[0],oreturned[1],oreturned[2]);
                OtherPoint dptwo = new OtherPoint(twreturned[0],twreturned[1],twreturned[2]);
                OtherPoint dpthree = new OtherPoint(trreturned[0],trreturned[1],trreturned[2]);
                OtherPoint dpfour = new OtherPoint(freturned[0],freturned[1],freturned[2]);
                tempzobj.add(new ZObject(dpone,dptwo,dpthree,dpfour,zo.getColor(),zo.isTouched()));
            } else if (zo.getType().equals("Polygon")) {
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
    
}