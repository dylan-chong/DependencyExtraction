package topViewDE.modelHeightMap;

import java.util.Random;
import java.util.stream.IntStream;

public class ModelMap{
  double centerX=10;
  double centerY=10;
  public double centerX() {return centerX;}
  public double centerY() {return centerY;}
  int side=64;
  int[][]map=new int[side][side];{initMap();}
  void initMap(){
    var r=new Random(3);
    var c=new CellularExpansion<Integer>(side,side,r){
      public Integer combine(Integer seed,Integer oldCell){
        assert seed!=null;
        if(oldCell==null) {return seed;}
        if(oldCell+1==seed) {return seed;}
        if(oldCell+4<seed) {return (oldCell+seed)/2;}
        if(oldCell+7<seed) {return (oldCell+seed-8)/2;}
        return null;
      }};
    int[]lastSize={side*side};
    IntStream.range(0, 4).forEach(i->c.addSeed(c.rCoord(), 0));
    IntStream.range(0, 50).forEach(h->{
      if(c.seedsSize()==0)return;
      for(int attempts=0; attempts<10 &&c.seedsSize()<lastSize[0]*0.90d;attempts++)
        c.grow(0.5d);
      lastSize[0]=c.seedsSize();
      c.transformSeeds((ccc,sh)->{
        if(r.nextDouble()>0.08d)return null;
        return sh+1;
      });
      });
    c.read((i,h)->{
      assert i!=null;
      map[c.x(i)][c.y(i)]=h==null?0:h;
    });
  }
  public Item get(int x, int y, int z) {
    try{
      if(map[x][y]>z)return Item.ground;
      if(map[x][y]==z)return Item.trunk;
      if(map[x][y]==z-1)return Item.trunk;
      if(map[x][y]==z-2)return Item.forest;

      }
    catch(ArrayIndexOutOfBoundsException e) {}
    return Item.nope;
  }
  
}