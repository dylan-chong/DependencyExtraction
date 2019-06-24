package topViewDE.view;

import java.awt.Graphics2D;

public final class Viewport<M,D>{
  public D get(int coord){return ds[coord];}
  public void set(D elem, int coord){ds[coord]=elem;}
  public Graphics2D getGraphics(){return g;}  
  final int[] points;
  final D[] ds;
  Graphics2D g;
  public final int maxX;
  public final int maxY;
  public final int maxZ;
  public Viewport(Graphics2D g,int maxX,int maxY, int maxZ) {
    this.maxX=maxX;
    this.maxY=maxY;
    this.maxZ=maxZ;
    this.g=g;
    points=new int[(maxX+1)*(maxY+1)*(maxZ+1)*2];
    @SuppressWarnings("unchecked")
    D[] ds=(D[])new Object[maxX*maxY*maxZ];
    this.ds=ds;
    }
  public int pixelX(int coord) {return points[2*coord];}
  public int pixelY(int coord) {return points[1+2*coord];}
  public void setPixelX(int coord,int that) {points[2*coord]=that;}
  public void setPixelY(int coord,int that) {points[1+2*coord]=that;}
  static double scale=450d;
  static int half=400;
  public void cachePoint(int x,int y,int z,double cameraElevation,double xi, double yi,double zi){
    double zd=cameraElevation-(zi/2d);
    double xd=xi-(maxX-1d)/2d;
    double yd=yi-(maxY-1d)/2d;
    double d=Math.sqrt(zd*zd+xd*xd+yd*yd);
    double f=scale/d;
    setPixelX(coordPs(x,y,z),(int)(scale*xd/d-f/2d)+half);
    setPixelY(coordPs(x,y,z),(int)(scale*yd/d-f/2d)+half);
    }
  /*    void cachePoint(int x,int y,int z,double cameraElevation,double xi, double yi,double zi){
    double zd=cameraElevation-(zi/2d);
    double xd=xi-maxX/2d;
    double yd=yi-maxY/2d;
    double d=Math.sqrt(zd*zd+xd*xd+yd*yd);
    double f=scale/d;
    setPixelX(coord(x,y,z),(int)(scale*xd/d-f/2d)+half);
    setPixelY(coord(x,y,z),(int)(scale*yd/d-f/2d)+half);*/
  public int coordPs(int x,int y,int z) {
    assert x>=0: x;
    assert y>=0: y;
    assert z>=0: z;
    assert x<maxX+1;
    assert y<maxY+1;
    assert z<maxZ+1;
    return x+y*(maxX+1)+z*(maxX+1)*(maxY+1);
    }
  public int coordDs(int x,int y,int z) {
    assert x>=0;
    assert y>=0;
    assert z>=0;
    assert x<maxX;
    assert y<maxY;
    assert z<maxZ:" "+z+" "+maxZ;
    return x+y*maxX+z*maxX*maxY;
    }
}