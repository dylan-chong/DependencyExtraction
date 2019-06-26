package randomMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class CellularExpansion<T> {
  final int maxX;
  final int maxY;
  final T[] cells;
  final Random r;
  List<Integer>seeds=new ArrayList<>();
  final int maxSeeds;
  public CellularExpansion(int maxX,int maxY,Random r,int maxSeeds){
    this.maxX=maxX;
    this.maxY=maxY;
    this.r=r;
    this.maxSeeds=maxSeeds;
    @SuppressWarnings("unchecked")
    T[] cells=(T[])new Object[maxX*maxY];
    this.cells=cells;
  }
  @Override public String toString() {
    StringBuilder b=new StringBuilder();
    for(int y=0;y<maxY;y++) {
      b.append("\n");
      for(int x=0;x<maxX;x++){
        T e=cells[coord(x,y)];
        if (e==null) b.append("@");
        else b.append(e.toString().substring(0,1));
      }
    }
    return b.toString();
  }
  public int coord(int x,int y){
    assert x>=0;assert x<maxX;
    assert y>=0;assert y<maxY;
    return x+maxX*y;
    }
  public int x(int coord) {return coord%maxX;}
  public int y(int coord) {return coord/maxX;}
  public void addSeed(int coord,T seed) {
    assert coord>=0; assert coord<cells.length;
    this.seeds.add(coord);
    cells[coord]=seed;
    }
  private static final int[] xDeltas=new int[]{-1, 0, 1,-1, 1,-1, 0, 1};
  private static final int[] yDeltas=new int[]{-1,-1,-1, 0, 0, 1, 1, 1};
  private List<Integer> order=new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7));
  public void grow(double chance) { 
    Collections.shuffle(order, r);
    int limit=seeds.size()-maxSeeds;
    if(maxSeeds>0 && limit>0)seeds=new ArrayList<>(seeds.subList(limit,limit+maxSeeds));
    Collections.shuffle(seeds,r);
    int size=seeds.size();//will change down below
    for(int i=0;i<size;i++) {
      int c=seeds.get(i);
      int x=x(c);
      int y=y(c);
      T seed=cells[c];
      assert seed!=null;
      //int countNearEq=0;
      for(var o:order) {
        int xd=x+xDeltas[o];
        int yd=y+yDeltas[o];
        if(xd<0 || xd>=maxX ||yd<0 || yd>=maxY)continue;
        int nearC=coord(xd,yd);
        if(r.nextDouble()>chance)continue;
        //if(cells[nearC]==seed){countNearEq+=1;}
        T newSeed=combine(x,y,seed,cells[nearC]);
        if(newSeed==null) continue;
        cells[nearC]=newSeed;
        cells[c]=newSeed;
        seeds.add(nearC);
      }
      //if(countNearEq==8)killSeed(seed,c);
    }
  }
  //public void killSeed(T seed, int c) {seeds.remove((Object)c);}
  public int rCoord() {
    return r.nextInt(cells.length);
  }
  public int rCoord(int xMin,int xMax,int yMin,int yMax) {
    int xD=xMax-xMin;
    int yD=yMax-yMin;
    int rx=r.nextInt(xD);
    int ry=r.nextInt(yD);
    return coord(xMin+rx,yMin+ry);
  }
  public void clearSeeds() {seeds.clear();}
  public T combine(int x, int y, T seed,T oldCell){
    if(oldCell!=null) {return null;}
    return seed;
  }
  public void read(BiConsumer<Integer, T> op) {
    for(int i=0;i<cells.length;i++)
      op.accept(i, cells[i]);
  }
  //if return null, delete seed
  //if return something, update seed
  public void transformSeeds(BiFunction<Integer, T,T> op){
    List<Integer>newSeeds=new ArrayList<>();
    for(var c:seeds) {
      T newS=op.apply(c,cells[c]);
      if(newS==null)continue;
      newSeeds.add(c);
      cells[c]=newS;
      }
    seeds.clear();
    seeds.addAll(newSeeds);
    }
  public int seedsSize() {return seeds.size();}
}
