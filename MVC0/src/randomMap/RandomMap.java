package randomMap;

import static general.General.range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public abstract class RandomMap<I>{
  final private int side;
  final private Random r;
  final private int waterLevel;
  final private int treeLevel;
  final private int rockLevel;
  final private int maxZ;
final private int[][]map;
final private int[][]riverMap;
final private boolean[][]grassMap;
final private boolean[][]treeMap;  
final private boolean[][]rocksMap;
private int[] montainsTop;  
public RandomMap(int side,Random r,
  int waterLevel,int treeLevel,int rockLevel,int maxZ){
  this.side=side;
  this.r=r;
  this.waterLevel=waterLevel;
  this.treeLevel=treeLevel;
  this.rockLevel=rockLevel;
  this.maxZ=maxZ;
  map=new int[side][side];
  riverMap=new int[side][side];
  grassMap=new boolean[side][side];
  treeMap=new boolean[side][side];  
  rocksMap=new boolean[side][side];
  }
public void markRocks() {
  for(int x=1;x<side-1;x++)for(int y=1;y<side-1;y++){
    if(map[x][y]>=rockLevel) {rocksMap[x][y]=true;continue;}  
    int[]xs= {x-1,x,x+1,x-1,  x+1,x-1,x,x+1};
    int[]ys= {y-1,y-1,y-1,y,  y,y+1,y+1,y+1};     
    int min=Integer.MAX_VALUE;
    for(int i=0;i<8;i++){
      int candidate=map[xs[i]][ys[i]];
      if(candidate<min)min=candidate;
    }
    if(min<map[x][y]-6) {
      rocksMap[x][y]=true;
      grassMap[x][y]=false;
      treeMap[x][y]=false;
    }
  }
}
public void makeGrass() {
  var c=new CellularExpansion<Boolean>(side,side,r,500){
    @Override public Boolean 
    combine(int x, int y, Boolean seed,Boolean oldCell){
      if(map[x][y]<treeLevel) {return null;}
      if(map[x][y]>rockLevel) {return null;}
      return seed;}};
  for(int i:range(50)) c.addSeed(c.rCoord(), true);
  for(int i:range(50)) c.grow(0.2d);
  c.read((i,b)->{
    assert grassMap!=null;
    if(b!=null)
      grassMap[c.x(i)][c.y(i)]=true;
    });
}
public void makeTrees() {
  var c=new CellularExpansion<Boolean>(side,side,r,500){
    @Override public Boolean 
    combine(int x, int y, Boolean seed,Boolean oldCell){
      if(!grassMap[x][y]) {return null;}
      return seed;}};
  for(int i:range(50)) c.addSeed(c.rCoord(), true);
  for(int i:range(50)) c.grow(0.2d);
  c.read((i,b)->{if(b!=null)treeMap[c.x(i)][c.y(i)]=true;});
}
public void makeRivers() {
  runWater(montainsTop[2]);
  runWater(montainsTop[3]);
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
    if(map[x][y]<waterLevel-1) {continue;}
    if(map[x][y]<=max) map[x][y]=max;
    else map[x][y]=(map[x][y]+max)/2;
  }
}
//also mutate the map
public void runWater(int source) {
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
    //if(min<waterLevel-2)break;
    if(map[x][y]<waterLevel-2)break;
    x=xs[minP];
    y=ys[minP];
  }
  if(rxs.size()<2)return;
  int size=rxs.size();
  int lastH=map[rxs.get(size-1)][rys.get(size-1)];
  for(int i=size-2;i>=0;i--){
    int currentH=map[rxs.get(i)][rys.get(i)];
    if(lastH>currentH){
      riverMap[rxs.get(i)][rys.get(i)]+=lastH-currentH;
      currentH=map[rxs.get(i)][rys.get(i)]+riverMap[rxs.get(i)][rys.get(i)]-1;
    }
    if(lastH+1<currentH){//should make more water on waterfalls
      int delta=currentH-(lastH+1);
      riverMap[rxs.get(i)][rys.get(i)]+=delta;
      map[rxs.get(i)][rys.get(i)]-=delta;
    }
    lastH=currentH;
  }
}
public void addMontains() {
  var c=new CellularExpansion<Integer>(side,side,r,-1){
    @Override public Integer
    combine(int x, int y, Integer seed,Integer oldCell){
      assert seed!=null;
      if(oldCell==null) {return seed;}
      if(oldCell+1==seed) {return seed;}
      if(oldCell+6<seed) {return (oldCell+seed)/2;}
      return null;
    }};
  int[]lastSize={side*side};
  //IntStream.range(0, 4).forEach(i->c.addSeed(c.rCoord(), 0));
  int p1=c.rCoord(8,32,8,32);
  int p2=c.rCoord(32,54,32,54);
  int p3=c.coord((c.x(p1)+c.x(p1)+c.x(p2))/3,(c.y(p1)+c.y(p1)+c.y(p2))/3);
  int p4=c.coord((c.x(p1)+c.x(p2)+c.x(p2))/3,(c.y(p1)+c.y(p2)+c.y(p2))/3);
  int p5=c.rCoord();
  int p6=c.rCoord();
  int p7=c.rCoord();
  montainsTop=new int[]{p1,p2,p3,p4,p5,p6,p7};
  c.addSeed(p1,0); c.addSeed(p2,0); c.addSeed(p3,0); c.addSeed(p4,0);
  c.addSeed(p5,0);c.addSeed(p6,0);c.addSeed(p7,0);
  IntStream.range(1, maxZ).forEach(h->{
    //if(c.seedsSize()==0)return;
    //for(int attempts=0; attempts<10 &&c.seedsSize()<lastSize[0]*0.90d;attempts++)
    double limit=(1d-(h*1d)/300d);
    for(int attempts=0; attempts<200 &&c.seedsSize()<lastSize[0]*limit;attempts++)
      c.grow(0.1d);
    lastSize[0]=c.seedsSize();
    c.clearSeeds();
    c.addSeed(p3,h); c.addSeed(p4,h);
    if(h<maxZ*0.75) {c.addSeed(p1,h);c.addSeed(p2,h);} 
    if(h<maxZ*0.5) {c.addSeed(p5,h);c.addSeed(p6,h);c.addSeed(p7,h);}
    });
  c.read((i,h)->map[c.x(i)][c.y(i)]=h==null?0:h);
}
public void addShores() {
  var c=new CellularExpansion<Boolean>(side,side,r,-1);
  for(int x=0;x<side;x++){c.addSeed(c.coord(x,0),true);c.addSeed(c.coord(x,side-1),true);}
  for(int y=0;y<side;y++){c.addSeed(c.coord(0,y),true);c.addSeed(c.coord(side-1,y),true);}
  for(int x=0;x<side/3;x++)for(int y=0;y<side/3;y++) {c.addSeed(c.coord(x,y),true);}
  int seeds=c.seedsSize();
  for(int attempts=0; attempts<200 && c.seedsSize()<seeds*3;attempts++)
      c.grow(0.1d);
  c.read((i,b)->{if(b!=null)map[c.x(i)][c.y(i)]=0;});
}
public void lowerShores() {
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
public void slides() {
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
public abstract I water();
public abstract I grass();
public abstract I rock();
public abstract I air();
public abstract I ground();
public abstract I treeTrunk();
public abstract I treeTop();
public I get(int x, int y, int z) {
    if(x<0 || x>=side || y<0 || y>=side)return air();
    if(map[x][y]<=z && z<waterLevel)return water();
    if(map[x][y]>z && grassMap[x][y])return grass();
    if(map[x][y]>z && !grassMap[x][y] && !rocksMap[x][y])return ground();
    if(map[x][y]>z && rocksMap[x][y])return rock();
    if(map[x][y]+riverMap[x][y]>z && riverMap[x][y]>0)return water();
    if(treeMap[x][y] && map[x][y]>treeLevel && riverMap[x][y]==0){
      if(map[x][y]==z)return treeTrunk();
      if(map[x][y]==z-1)return treeTrunk();
      if(map[x][y]==z-2)return treeTop();
      }
    return air();
  }
  public int heightAt(int x, int y) {
    return Integer.max(waterLevel,map[x][y]+riverMap[x][y]);}
}