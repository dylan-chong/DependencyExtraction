package topViewDE.blocks;

import java.awt.Graphics2D;

public interface Blocks<V>{
  Drawable get(V view, int coord);
  void set(V view, Drawable elem, int coord);
  int maxX(V view);
  int maxY(V view);
  int maxZ(V view);
  int coordDs(V view, int x, int y, int z);
  int coordPs(V view, int x, int y, int z);
  Graphics2D getGraphics(V view);
  int pixelX(V view, int coord);
  int pixelY(V view, int coord);
  default void drawCell(V view,int x,int y,int z) {
    get(view,coordDs(view,x,y,z)).draw(this,view,x,y,z);
  }
}
