package topViewDE.view;

import java.awt.Graphics2D;
public interface View<M,D>{
  double centerX(M m);//always positive
  double centerY(M m);//always positive
  void handleKeyEvent(Character c);
  D get(M m,int x,int y, int z);
  void drawCell(Viewport<M,D> view,int x,int y,int z);
  
  static int max=51;
  static int half=26;
  static double scaleZ=0.5d;
  default void renderViewPort(Graphics2D g, M m,double elevation) {
    assert centerX(m)>=0d;
    assert centerY(m)>=0d;
    int offX=(int)centerX(m);
    int offY=(int)centerY(m);
    double extraX=centerX(m)-offX;//only ok if centerX>=0
    double extraY=centerY(m)-offY;
    var map=new Viewport<M,D>(g,max+1,max+1,max+1);
    for(int z=0;z<max+1;z++) for(int x=0;x<max+1;x++)for(int y=0;y<max+1;y++)
      map.cachePoint(x, y, z, elevation, x+1d-extraX, y+1d-extraY,z*scaleZ);
    for(int z=0;z<max;z++) for(int x=0;x<max;x++)for(int y=0;y<max;y++) {
      D d=get(m,x+offX-half,y+offY-half,z);
      map.set(d,map.coordDs(x,y,z));
    }
    visitQuadrants(map,0,0,max,max,max);
  }
  default void visitQuadrants(Viewport<M,D> map,int minX,int minY,int maxX,int maxY,int maxZ) {
    maxX-=minX;
    maxY-=minY;
    assert maxX%2!=0;
    assert maxY%2!=0;
    maxX=maxX/2;
    maxY=maxY/2;
    for(int z = 0; z < maxZ; z+=1){
      for(int y = 0; y < maxY; y+=1)
        for (int x = 0; x < maxX; x+=1)
          drawCell(map,minX+x,minY+y,z);
      for(int y = 0; y < maxY; y+=1)
       for (int x = maxX-1; x >= 0; x-=1)
          drawCell(map,minX+maxX+1+x,minY+y,z); 
      for(int y = maxY-1; y >= 0; y-=1)
        for (int x = maxX-1; x >= 0; x-=1)
          drawCell(map,minX+maxX+1+x,minY+maxY+1+y,z);
      for(int y = maxY-1; y >= 0; y-=1)
        for (int x = 0; x < maxX; x+=1)
          drawCell(map,minX+x,minY+maxY+1+y,z);
      for (int x = 0; x < maxX; x+=1)
        drawCell(map,minX+x,minY+maxY,z);
      for (int x = maxX-1; x >= 0; x-=1)
        drawCell(map,minX+maxX+1+x,minY+maxY,z);
      for (int y = 0; y < maxY; y+=1)
        drawCell(map,minX+maxX,minY+y,z);
      for (int y = maxY-1; y >= 0; y-=1)
        drawCell(map,minX+maxX,minY+maxY+1+y,z);
      drawCell(map,minX+maxX,minY+maxY,z);
      }
  }
}