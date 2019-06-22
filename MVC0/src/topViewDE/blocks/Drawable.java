package topViewDE.blocks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.stream.IntStream;

public interface Drawable{
  <V>void draw(Blocks<V> b,V view, int x, int y, int z);
  }
interface Transparent extends Drawable{}
class Air implements Transparent{
  @Override public <V> void draw(Blocks<V> b, V view, int x, int y, int z) {}
  public static final Air instance=new Air();
}

interface Decoration extends Transparent{
  default<V> void draw(Blocks<V> b,V v,int x, int y, int z) {
    int c11=b.coordPs(v,x, y, z);
    int c21=b.coordPs(v,x+1, y, z);
    int c31=b.coordPs(v,x+1, y+1, z);
    int c41=b.coordPs(v,x, y+1, z);  
    int c12=b.coordPs(v,x, y, z+1);
    int c22=b.coordPs(v,x+1, y, z+1);
    int c32=b.coordPs(v,x+1, y+1, z+1);
    int c42=b.coordPs(v,x, y+1, z+1);
    points(b.getGraphics(v),
      b.pixelX(v,c11),b.pixelX(v,c21),
      b.pixelX(v,c31),b.pixelX(v,c41),
      b.pixelX(v,c12),b.pixelX(v,c22),
      b.pixelX(v,c32),b.pixelX(v,c42),
        
      b.pixelY(v,c11),b.pixelY(v,c21),
      b.pixelY(v,c31),b.pixelY(v,c41),
      b.pixelY(v,c12),b.pixelY(v,c22),
      b.pixelY(v,c32),b.pixelY(v,c42)      
      );   
  }
  void points(Graphics2D g,
    int x000, int x100,
    int x010, int x110,
    int x001, int x101,
    int x011, int x111,
        
    int y000, int y100,
    int y010, int y110,
    int y001, int y101,
    int y011, int y111
    );
}
class TreeTrunk implements Decoration{
  public void points(Graphics2D g,
    int x000, int x100,
    int x010, int x110,
    int x001, int x101,
    int x011, int x111,
          
    int y000, int y100,
    int y010, int y110,
    int y001, int y101,
    int y011, int y111
    ) {
    int xMid0=(x000+x100+x010+x110)/4;
    int yMid0=(y000+y100+y010+y110)/4;
    int xMid1=(x001+x101+x011+x111)/4;
    int yMid1=(y001+y101+y011+y111)/4;

    g.setColor(Color.black);
    g.setStroke(new BasicStroke(3));
    g.drawLine(xMid0, yMid0, xMid1, yMid1);
   }
}

class TreeLeaves implements Decoration{
  public void points(Graphics2D g,
    int x000, int x100,
    int x010, int x110,
    int x001, int x101,
    int x011, int x111,
          
    int y000, int y100,
    int y010, int y110,
    int y001, int y101,
    int y011, int y111
    ) {
    int xMid=(x000+x100+x010+x110+x001+x101+x011+x111)/8;
    int yMid=(y000+y100+y010+y110+y001+y101+y011+y111)/8;
    g.setColor(new Color(15,250,12,200));
    int r=IntStream.of(x101-x001,x111-x001,y101-y001,y111-y001).max().getAsInt()/2;
    int startX = xMid-(r/2);
    int startY = yMid-(r/2);
    g.fillOval(startX, startY,r,r);
  }
}
class Cube implements Drawable{
  Color mainColor;
  Cube(Color mainColor){this.mainColor=mainColor;}
  <V>boolean isTransparent(Blocks<V> b,V v,int x, int y, int z){
    if(x<0 || y<0 || z<0)return true;
    if(x>=b.maxX(v) ||y>=b.maxY(v) ||z>=b.maxZ(v))return true;
    return b.get(v,b.coordDs(v,x,y,z)) instanceof Transparent;
  }
  public<V> void draw(Blocks<V> b,V v,int x, int y, int z) {
    boolean up=y<b.maxY(v)/2;
    boolean down=y>b.maxY(v)/2;
    boolean left=x<b.maxX(v)/2;
      boolean right=x>b.maxX(v)/2;
      boolean paintTop=isTransparent(b,v,x,y,z+1);
      boolean paintUp= down && isTransparent(b,v,x,y-1,z);
      boolean paintLeft=right && isTransparent(b,v,x-1,y,z);
      boolean paintRight=left && isTransparent(b,v,x+1,y,z);
      boolean paintDown=up && isTransparent(b,v,x,y+1,z);
      if(paintLeft)fill4(b,v,left(colorOf(z)),
        b.coordPs(v,x,y,z), b.coordPs(v,x,y+1,z), b.coordPs(v,x,y+1,z+1), b.coordPs(v,x,y,z+1));
      if(paintUp)fill4(b,v,up(colorOf(z)),
        b.coordPs(v,x, y, z),b.coordPs(v,x+1, y, z),b.coordPs(v,x+1, y, z+1),b.coordPs(v,x, y, z+1));
      if(paintRight)fill4(b,v,left(colorOf(z)),
        b.coordPs(v,x+1,y,z), b.coordPs(v,x+1,y+1,z), b.coordPs(v,x+1,y+1,z+1), b.coordPs(v,x+1,y,z+1));
      if(paintDown)fill4(b,v,up(colorOf(z)),
        b.coordPs(v,x,y+1,z), b.coordPs(v,x+1,y+1,z), b.coordPs(v,x+1,y+1,z+1), b.coordPs(v,x,y+1,z+1));
      if(paintTop)fill4(b,v,colorOf(z),
        b.coordPs(v,x,y,z+1), b.coordPs(v,x+1,y,z+1), b.coordPs(v,x+1,y+1,z+1), b.coordPs(v,x,y+1,z+1));
      }
    private Color colorOf(int z) {
      z=Math.max(0,Math.min(250,z*7-10));
      return mix(mainColor,new Color(z,z,z));
      }
    private Color mix(Color c1,Color c2) {
      return new Color(
        (c1.getRed()+c2.getRed())/2,
        (c1.getGreen()+c2.getGreen())/2,
        (c1.getBlue()+c2.getBlue())/2
        );
    }
    private static final Color brown1=new Color(233, 168, 81);
    private Color left(Color c) {return mix(c,brown1);}
    private static final Color brown2=new Color(154, 87, 25);
    private Color up(Color c) {return mix(c,brown2);}
    
    static public <V>void fill4(Blocks<V> b,V v,Color c, int p1, int p2, int p3,int p4) {
      int[]x= {b.pixelX(v,p1),b.pixelX(v,p2),b.pixelX(v,p3),b.pixelX(v,p4)};
      int[]y= {b.pixelY(v,p1),b.pixelY(v,p2),b.pixelY(v,p3),b.pixelY(v,p4)};
      b.getGraphics(v).setColor(c);
      b.getGraphics(v).fillPolygon(x, y, 4);
    }
  }