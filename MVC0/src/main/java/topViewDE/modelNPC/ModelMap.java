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
  public ModelMap(Random r){
    assert side%4==0;
    RandomMap<Item> map=new RandomMap<Item>(side/4,r,10,13,65,maxZ){
      @Override public Item water() {return Item.water;}
      @Override public Item grass() {return Item.grass;}
      @Override public Item rock() {return Item.rock;}
      @Override public Item ground() {return Item.ground;}
      @Override public Item treeTrunk() {return Item.treeTrunk;}
      @Override public Item treeTop() {return Item.treeTop;}
      @Override public Item air() {return Item.air;}
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
      for(int x0 :range(4))for(int y0:range(4)) set(x*4+x0,y*4+y0,z,i);
    }
    //smoothing
    for(int z :range(1,maxZ-1))for(int x:range(1,side-1))for(int y:range(1,side-1)){
      int ox=x/4;
      int oy=y/4;
      //if ground if level 10, then 9-10 is last ground
      if(map.heightAt(ox,oy)!=z+1)continue;
      Item i0=get(x,y,z);
      Item i1=get(x,y,z+1);
      if(i1==Item.water)continue;
      if(!(i0 instanceof Item.Full))continue;//can be water at the edges
      int near=nearHeight(map,x,y);
      int h=map.heightAt(ox,oy);
      if(h>near) {
      if(h>near+3 && i0==Item.rock && r.nextInt(4)!=0) {
        set(x,y,near+1,Item.grass);
        for(int i :range(near+2,h))set(x,y,i,Item.air);
        }
        else set(x,y,z,i1);
      }
      if(h<near)set(x,y,z+1,i0); 
      }
    //random
    for(int z :range(1,maxZ-1))for(int x:range(1,side-1))for(int y:range(1,side-1)){
      Item i0=get(x,y,z);
      Item i1=get(x,y,z+1);
      if(i1==Item.water)continue;
      if(!(i0 instanceof Item.Full))continue;
      if(i1 instanceof Item.Full)continue;
      int upDown=r.nextInt(10);
      if(upDown>8)set(x,y,z+1,i0);
      //if(upDown<2)set(x,y,z,i1);
      }
    //trees died/grow
    for(int z :range(1,maxZ-1))for(int x:range(1,side-1))for(int y:range(1,side-1)){
      Item i0=get(x,y,z);
      Item i1=get(x,y,z+1);
      if(!(i0 instanceof Item.Full))continue;
      if(!(i1 instanceof Item.Tree))continue;
      int d10=r.nextInt(10)+1;
      if(d10>=7)continue;
      if(d10>=4)//kill tree
        for(int i=z+1;get(x,y,i) instanceof Item.Tree;i+=1)
          set(x,y,i,Item.air);
      else {//grow tree
        for(int i=z+1;i<z+3+d10;i+=1)set(x,y,i,Item.treeTrunk);
        set(x,y,z+3+d10,Item.treeTop);
        }
      }
    }
  private int nearHeight(RandomMap<Item> map,int x, int y) {
    if(x%4 == 0 && y%4==0)return Integer.max(Integer.max(//topleft
      map.heightAt(x/4-1, y/4-1),map.heightAt(x/4, y/4-1)),map.heightAt(x/4-1, y/4));
    if(x%4 == 3 && y%4==0)return Integer.max(Integer.max(//topright
      map.heightAt(x/4+1, y/4-1),map.heightAt(x/4, y/4-1)),map.heightAt(x/4+1, y/4));
    if(x%4 == 0 && y%4==3)return Integer.max(Integer.max(//bottomleft
      map.heightAt(x/4-1, y/4+1),map.heightAt(x/4, y/4+1)),map.heightAt(x/4-1, y/4));
    if(x%4 == 3 && y%4==3)return Integer.max(Integer.max(//bottomright
      map.heightAt(x/4+1, y/4+1),map.heightAt(x/4, y/4+1)),map.heightAt(x/4+1, y/4));
    if(x%4 == 0)return map.heightAt(x/4-1, y/4);//left
    if(x%4 == 3)return map.heightAt(x/4+1, y/4);//right
    if(y%4==0)return map.heightAt(x/4, y/4-1);//top
    if(y%4==3)return map.heightAt(x/4, y/4+1);//bottom
    return map.heightAt(x/4, y/4);
  }
  List<Item>[] items;
  public Item get(int x, int y, int z) {
    var lz=items[x+y*side];
    if(lz.size()<=z)return Item.air;
    return lz.get(z);
  } 
  public void set(int x, int y, int z,Item item) {
    var lz=items[x+y*side];
    while(lz.size()<=z)lz.add(Item.air);
    lz.set(z,item);
  } 

}