package topViewDE.model;

public interface Model{
 void updateView();
 ModelMap getMap();
 
 default void goWest() {getMap().centerX-=0.1d;if(getMap().centerX<0d)getMap().centerX=0d;}
 default void goEast() {getMap().centerX+=0.1d;}
 default void goSouth() {getMap().centerY+=0.1d;}
 default void goNorth() {getMap().centerY-=0.1d;if(getMap().centerY<0d)getMap().centerY=0d;}
 }

