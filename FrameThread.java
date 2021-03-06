public class FrameThread implements Runnable {
    Display dis;
    double fps = 60;
    public FrameThread(Display d) {
        dis=d;
    }
    public FrameThread(Display d, int framespersecond) {
        dis=d;
        fps=framespersecond;
    }
    public void run() {
        while(true) {
            dis.update();
            dis.redraw();
            try {
                Thread.sleep((int)(1000/fps));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 