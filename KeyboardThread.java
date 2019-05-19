import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
public class KeyboardThread extends KeyAdapter { 
    Display dis;
    boolean jumping;
    boolean a;
    boolean d;
    boolean w;
    boolean s;
    public KeyboardThread(Display d) {
        dis=d;
    }
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            dis.aPress();
        }
        if (key == KeyEvent.VK_D) {
            dis.dPress();
        }
        if (key == KeyEvent.VK_W) {
            dis.wPress();
        }
        if (key == KeyEvent.VK_S) {
            dis.sPress();
        }
        if (key == KeyEvent.VK_UP) {
            dis.upPress();
        }
        if (key == KeyEvent.VK_DOWN) {
            dis.downPress();
        }
        if (key == KeyEvent.VK_RIGHT) {
            dis.rightPress();
        }
        if (key == KeyEvent.VK_LEFT) {
            dis.leftPress();
        }
        if (key == KeyEvent.VK_SPACE) {
            dis.spacePress();
        }
        if (key == KeyEvent.VK_E) {
            dis.ePress();
        }
        if (key == KeyEvent.VK_Q) {
            dis.qPress();
        }
        if (key == KeyEvent.VK_F) {
            dis.shootLaser();
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            dis.aRelease();
        }
        if (key == KeyEvent.VK_D) {
            dis.dRelease();
        }
        if (key == KeyEvent.VK_W) {
            dis.wRelease();
        }
        if (key == KeyEvent.VK_S) {
            dis.sRelease();
        }
        if (key == KeyEvent.VK_UP) {
            dis.upRelease();
        }
        if (key == KeyEvent.VK_DOWN) {
            dis.downRelease();
        }
        if (key == KeyEvent.VK_RIGHT) {
            dis.rightRelease();
        }
        if (key == KeyEvent.VK_LEFT) {
            dis.leftRelease();
        }
        if (key == KeyEvent.VK_SPACE) {
            dis.spaceRelease();
        }
        if (key == KeyEvent.VK_E) {
            dis.eRelease();
        }
        if (key == KeyEvent.VK_Q) {
            dis.qRelease();
        }
    }
}       