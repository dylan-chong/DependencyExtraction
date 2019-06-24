package topViewDE.modelNPC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import randomMap.RandomMap;

import static general.General.*;

public class ModelMap{
  int side=256;
  double centerX=side/2;
  double centerY=side/2;
  int maxZ=75;
  public double centerX() {return centerX;}
  public double centerY() {return centerY;}
  public RandomMap<Item> map;
  public ModelMap(Random r){
    assert side%4==0;
    RandomMap<Item> map=new RandomMap<Item>(side/4,r,10,13,65,maxZ){
      @Override public Item water() {return Item.water;}
      @Override public Item grass() {return Item.grass;}
      @Override public Item rock() {return Item.rock;}
      @Override public Item ground() {return Item.ground;}
      @Override public Item treeTrunk() {return Item.trunk;}
      @Override public Item treeTop() {return Item.forest;}
      @Override public Item air() {return Item.nope;}
      };
    map.addMontains();
    map.addShores();
    map.lowerShores();
    map.slides();
    map.slides();
    map.makeRivers();
    map.makeGrass();
    map.makeTrees();
    map.markRocks();
    @SuppressWarnings("unchecked")
    List<Item>[] items=Stream.generate(()->new ArrayList<Item>())
        .limit(side*side).toArray(List[]::new);
    this.items=items;
    for(int z :range(maxZ))for(int x:range(side/4))for(int y:range(side/4)){
      Item i=map.get(x, y, z);
      for(int x0 :range(4))for(int y0:range(4)) {
        int xi=x*4+x0;
        int yi=y*4+y0;
        boolean isTree=i==Item.forest || i==Item.trunk;
        if(isTree) {//all trees are onto air?
          if(z==0 /*||get(xi,yi,z-1)==Item.nope*/)i=Item.nope;
          //else if(r.nextBoolean())i=Item.nope;
        }
        set(xi,yi,z,i);
      //need to insert noise here, remove trees/grass, up/down terrain
      }
    }
    }
  List<Item>[] items;
  public Item get(int x, int y, int z) {
    var lz=items[x+y*side];
    if(lz.size()<=z)return Item.nope;
    return lz.get(z);
  } 
  public void set(int x, int y, int z,Item item) {
    var lz=items[x+y*side];
    while(lz.size()<=z)lz.add(Item.nope);
    lz.set(z,item);
  } 

}