import java.awt.*;
public class Laser implements Runnable {
    double[] loc;
    double[] direction;
    boolean paused;
    int ops;
    boolean dead;
    public Laser(double startx, double starty, double startz, double magx, double magy, double magz, double maxspeed) {
        paused=false;
        dead=false;
        ops=0;
        loc = new double[]{startx,starty,startz};
        double largest = magx;
        double offset = 1;
        if (Math.abs(magy)>Math.abs(magx)) {
            largest=magy;
        }
        if (Math.abs(magz)>Math.abs(largest)) {
            largest=magz;
        }
        if (largest!=0) {
            offset = 100*Math.abs(maxspeed/largest);
        } else {
            offset=0;
        }
        direction = new double[]{magx*offset,magy*offset,magz*offset}; 
        direction[0]=(Math.abs(direction[0])/direction[0])*Math.log(Math.abs(direction[0])+1)/Math.log(2);
        direction[1]=(Math.abs(direction[1])/direction[1])*Math.log(Math.abs(direction[1])+1)/Math.log(2);
        direction[2]=(Math.abs(direction[2])/direction[2])*Math.log(Math.abs(direction[2])+1)/Math.log(2);
        for(int i=0;i<direction.length;i++) {
            if (Double.isNaN(direction[i])) {
                direction[i]=0;
            }
        }
    }
    public void run() {
        /*while(true) {
            //loc //DONT FORGET TO CHANGE DIRECTION ON THE LOGARITHMIC EQUATION!!!
            if (!paused) {
                System.out.println("laser location: " + loc[0] + "," + loc[1] + "," + loc[2]);
                loc[0]-=direction[0];
                loc[1]-=direction[1];
                loc[2]-=direction[2];
                
                try {
                    Thread.sleep(1000/2); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
    }
    public void update() {
        ops++;
        loc[0]-=direction[0];
        loc[1]-=direction[1];
        loc[2]-=direction[2];
        if (ops>=1800) {
            dead=true;
        }
    }
    public double[] getLocation() {
        return loc;
    }
    public ZObject getZObject() {
        OtherPoint p1 = new OtherPoint(loc[0],loc[1],loc[2]);
        OtherPoint p2 = new OtherPoint(loc[0]+direction[0],loc[1]+direction[1],loc[2]+direction[2]);
        ZObject z = new ZObject(p1,p2,Color.RED);
        return z;
    }
    public ZObject getDirectionAsZObject() {
        OtherPoint p1 = new OtherPoint(direction[0],direction[1],direction[2]);
        ZObject z = new ZObject(p1);
        return z;
    }
    public void pause() {
        paused=true;
    }
    public void unpause() {
        paused=false;
    }
    public void setLocation(OtherPoint point) {
        loc[0]=point.getX();
        loc[1]=point.getY();
        loc[2]=point.getZ();
    }
    public void setDirection(OtherPoint point) {
        direction[0]=point.getX();
        direction[1]=point.getY();
        direction[2]=point.getZ();
    }
    public boolean isDead() {
        return dead;
    }
}