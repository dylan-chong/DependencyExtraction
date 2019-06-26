package topViewDE.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

public interface View<M,D> extends Visit<M,D>{
  double centerX(M m);//always positive
  double centerY(M m);//always positive
  void handleKeyEvent(Character c);
  D get(M m,int x,int y, int z);
  void drawCell(Viewport<M,D> view,int x,int y,int z);
  M getMap();
  double getCameraZ();
  JFrame getFrame();
  static int max=51;
  static int maxZ=300;
  static int half=26;
  static double scaleZ=0.5d;
  default void renderViewPort(Graphics2D g, M m,double elevation) {
    assert centerX(m)>=0d;
    assert centerY(m)>=0d;
    int offX=(int)centerX(m);
    int offY=(int)centerY(m);
    double extraX=centerX(m)-offX;//only ok if centerX>=0
    double extraY=centerY(m)-offY;
    var map=new Viewport<M,D>(g,max+1,max+1,maxZ+1);
    for(int z=0;z<maxZ+1;z++) for(int x=0;x<max+1;x++)for(int y=0;y<max+1;y++)
      map.cachePoint(x, y, z, elevation, x+1d-extraX, y+1d-extraY,z*scaleZ);
    for(int z=0;z<maxZ;z++) for(int x=0;x<max;x++)for(int y=0;y<max;y++) {
      D d=get(m,x+offX-half,y+offY-half,z);
      map.set(d,map.coordDs(x,y,z));
    }
    visitQuadrants(map,max,max,maxZ);
  }
  default void initializeApp() {
      JFrame f=getFrame();
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.getContentPane().add(new GameMap<M,D>(this));
      f.pack();
      f.addKeyListener(new KeyListener() {
      @Override public void keyTyped(KeyEvent e) {
        View.this.handleKeyEvent(e.getKeyChar());
        }
      @Override public void keyReleased(KeyEvent arg0) {}
      @Override public void keyPressed(KeyEvent arg0) {}
      });
    }
}
class GameMap<M,D> extends JComponent{
  private static final long serialVersionUID = 1L;
  @Override public Dimension getPreferredSize() {return new Dimension(800,800);}
  View<M,D> game;
  GameMap(View<M,D> game){this.game=game;}
  @Override public void paintComponent(Graphics g) {
    game.renderViewPort((Graphics2D)g,game.getMap(),game.getCameraZ());
  }
}
interface Visit<M,D>{
  void drawCell(Viewport<M,D> map,int x,int y,int z);
  default void visitQuadrants(Viewport<M,D> map,int maxX,int maxY,int maxZ) {
    assert maxX%2!=0;
    assert maxY%2!=0;
    maxX=maxX/2;
    maxY=maxY/2;
    for(int z = 0; z < maxZ; z+=1){
      for(int y = 0; y < maxY; y+=1)
        for (int x = 0; x < maxX; x+=1)
          drawCell(map,x,y,z);
      for(int y = 0; y < maxY; y+=1)
        for (int x = maxX; x > 0; x-=1)
          drawCell(map,maxX+x,y,z); 
      for(int y = maxY; y > 0; y-=1)
        for (int x = maxX; x > 0; x-=1)
          drawCell(map,maxX+x,maxY+y,z);
      for(int y = maxY; y > 0; y-=1)
        for (int x = 0; x < maxX; x+=1)
          drawCell(map,x,maxY+y,z);
      for (int x = 0; x < maxX; x+=1)
        drawCell(map,x,maxY,z);
      for (int x = maxX; x > 0; x-=1)
        drawCell(map,maxX+x,maxY,z);
      for (int y = 0; y < maxY; y+=1)
        drawCell(map,maxX,y,z);
      for (int y = maxY; y > 0; y-=1)
        drawCell(map,maxX,maxY+y,z);
      drawCell(map,maxX,maxY,z);
    }
  }
}