package topViewDE.modelHeightMap;

import java.util.Random;

import randomMap.RandomMap;

import static general.General.*;

public class ModelMap{
  double centerX;
  double centerY;
  public double centerX() {return centerX;}
  public double centerY() {return centerY;}
  public RandomMap<Item> map;
  public ModelMap(int side,Random r){
    this.centerX=side/2;
    this.centerY=side/2;
    this.map=new RandomMap<Item>(side,r,10,13,65,75){
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
    } 
}