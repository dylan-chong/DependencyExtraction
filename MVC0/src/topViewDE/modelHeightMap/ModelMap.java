package topViewDE.modelHeightMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class ModelMap{
  double centerX=32;
  double centerY=32;
  public double centerX() {return centerX;}
  public double centerY() {return centerY;}
  int side=64;
  int[][]map=new int[side][side];{initMap();}
  int[][]riverMap;
  void initMap(){
    var r=new Random(8);
    int source=addMontains(r);
    addShores(r);
    lowerShores();
    slides();
    slides();
    makeRiver(source,r);
  }
  private void makeRiver(int source,Random r) {
    riverMap=runWater(source,r);
    for(int x=1;x<side-1;x++)for(int y=1;y<side-1;y++){
      if(riverMap[x][y]>0) continue;
      int[]xs= {x-1,x,x+1,x-1,  x+1,x-1,x,x+1};
      int[]ys= {y-1,y-1,y-1,y,  y,y+1,y+1,y+1};     
      int max=Integer.MIN_VALUE;
      for(int i=0;i<8;i++){
        if(!(riverMap[xs[i]][ys[i]]>0))continue;
        int candidate=map[xs[i]][ys[i]]+riverMap[xs[i]][ys[i]];
        if(candidate>max)max=candidate;
      }
      if(max==Integer.MIN_VALUE) {continue;}
      //map[x][y]=max;
      if(map[x][y]<=max) map[x][y]=max;
      else map[x][y]=(map[x][y]+max)/2;
    }
  }
  //also mutate the map
  private int[][] runWater(int source, Random r) {
    int[][]riverMap=new int[side][side];
    int x=source%side+r.nextInt(5)-2;
    int y=source/side+r.nextInt(5)-2;
    List<Integer>rxs=new ArrayList<>();
    List<Integer>rys=new ArrayList<>();
    while(true){
      //do river
      riverMap[x][y]=1;
      map[x][y]-=1;
      rxs.add(x);
      rys.add(y);
      //compute next point
      int[]xs= {  x, x-1, x+1,   x};
      int[]ys= {y-1,   y,   y, y+1};     
      int min=Integer.MAX_VALUE;
      int minP=-1;
      for(int i=0;i<4;i++){
        if(riverMap[xs[i]][ys[i]]>0)continue;
        int candidate=map[xs[i]][ys[i]];
        if(candidate<min) {min=candidate;minP=i;}
      }
      if(minP==-1)break;
      x=xs[minP];
      y=ys[minP];
      if(min<waterLevel-2)break;
    }
    if(rxs.size()<2)return riverMap;
    int size=rxs.size();
    int lastH=map[rxs.get(size-1)][rys.get(size-1)];
    for(int i=size-2;i>=0;i--){
      int currentH=map[rxs.get(i)][rys.get(i)];
      if(lastH>currentH)riverMap[rxs.get(i)][rys.get(i)]=1+lastH-currentH;
      if(lastH+1<currentH){//should make more water on waterfalls
        int delta=currentH-(lastH+1);
        riverMap[rxs.get(i)][rys.get(i)]=delta+1;
        map[rxs.get(i)][rys.get(i)]-=delta;
      }
      lastH=currentH;
    }
    return riverMap;
  }
  private int addMontains(Random r) {
    var c=new CellularExpansion<Integer>(side,side,r){
      public Integer combine(Integer seed,Integer oldCell){
        assert seed!=null;
        if(oldCell==null) {return seed;}
        if(oldCell+1==seed) {return seed;}
        if(oldCell+6<seed) {return (oldCell+seed)/2;}
        return null;
      }};
    int[]lastSize={side*side};
    //IntStream.range(0, 4).forEach(i->c.addSeed(c.rCoord(), 0));
    int p1=c.rCoord(8,54,8,54);
    int p2=c.rCoord();
    int p3=c.coord((c.x(p1)+c.x(p1)+c.x(p2))/3,(c.y(p1)+c.y(p1)+c.y(p2))/3);
    int p4=c.coord((c.x(p1)+c.x(p2)+c.x(p2))/3,(c.y(p1)+c.y(p2)+c.y(p2))/3);
    c.addSeed(p1,0); c.addSeed(p2,0); c.addSeed(p3,0); c.addSeed(p4,0);
    IntStream.range(1, 75).forEach(h->{
      //if(c.seedsSize()==0)return;
      //for(int attempts=0; attempts<10 &&c.seedsSize()<lastSize[0]*0.90d;attempts++)
      double limit=(1d-(h*1d)/300d);
      for(int attempts=0; attempts<200 &&c.seedsSize()<lastSize[0]*limit;attempts++)
        c.grow(0.1d);
      lastSize[0]=c.seedsSize();
      c.clearSeeds();
      c.addSeed(p1,h); c.addSeed(p2,h); c.addSeed(p3,h); c.addSeed(p4,h);
      });
    c.read((i,h)->map[c.x(i)][c.y(i)]=h==null?0:h);
    return p3;
  }
  private void addShores(Random r) {
    var c=new CellularExpansion<Boolean>(side,side,r);
    for(int x=0;x<side;x++){c.addSeed(c.coord(x,0),true);c.addSeed(c.coord(x,side-1),true);}
    for(int y=0;y<side;y++){c.addSeed(c.coord(0,y),true);c.addSeed(c.coord(side-1,y),true);}
    for(int x=0;x<side/3;x++)for(int y=0;y<side/3;y++) {c.addSeed(c.coord(x,y),true);}
    int seeds=c.seedsSize();
    for(int attempts=0; attempts<200 && c.seedsSize()<seeds*3;attempts++)
        c.grow(0.1d);
    c.read((i,b)->{if(b!=null)map[c.x(i)][c.y(i)]=0;});//needed since values are true/null
  }
  private void lowerShores() {
    int min=Integer.MAX_VALUE;
    for(int x=0;x<side;x++)for(int y=0;y<side;y++) {
      if (map[x][y]==0)continue;
      if (map[x][y]<min)min=map[x][y];
    }
    for(int x=0;x<side;x++)for(int y=0;y<side;y++) {
      if (map[x][y]==0)continue;
      map[x][y]-=min;
    }
  }
  private void slides() {
    int[][] oldMap=Arrays.stream(map).map(r -> r.clone()).toArray(int[][]::new);
    for(int x=1;x<side-1;x++)for(int y=1;y<side-1;y++) {
      double average=
        (oldMap[x-1][y-1]+
        oldMap[x][y-1]+
        oldMap[x+1][y-1]+
        oldMap[x-1][y]+
        oldMap[x+1][y]+
        oldMap[x-1][y+1]+
        oldMap[x][y+1]+
        oldMap[x+1][y+1])/8d;
      if(Math.abs(average-oldMap[x][y])>10d)map[x][y]=(int)((oldMap[x][y]+average)/2d);
      else map[x][y]=(int)((oldMap[x][y]*5d+average)/6d);
    }
  }
  public static int waterLevel=10;
  public static int treeLevel=13;
  public Item get(int x, int y, int z) {
    try{
      if(map[x][y]<=z && z<waterLevel)return Item.water;
      if(map[x][y]>z)return Item.ground;
      if(map[x][y]+riverMap[x][y]>z && riverMap[x][y]>0)return Item.water;
      if(range(map[x][y],z,2) && z>treeLevel && !(riverMap[x][y]>0))return Item.trunk;
      if(map[x][y]==z-2 && z>treeLevel && !(riverMap[x][y]>0))return Item.forest;
      }
    catch(ArrayIndexOutOfBoundsException e) {}
    return Item.nope;
  }
  private boolean range(int a,int b,int c) {return a<b && b-a<c;}
  
}