package topViewDE.model;

public interface Model{
 void repaint();
 ModelMap getMap();
 default double boundPos(double pos) {return Math.max(0,pos);}
 default void goWest() {
   getMap().centerX=boundPos(getMap().centerX-0.1d);
   repaint();
   }
 default void goEast() {
   getMap().centerX=boundPos(getMap().centerX+0.1d);
   repaint();
   }
 default void goSouth() {
   getMap().centerY=boundPos(getMap().centerY+0.1d);
   repaint();
   }
 default void goNorth() {
   getMap().centerY=boundPos(getMap().centerY-0.1d);
   repaint();
   }
 }

