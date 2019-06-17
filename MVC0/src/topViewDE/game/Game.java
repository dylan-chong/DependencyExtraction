package topViewDE.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JComponent;

import topViewDE.controller.*;
import topViewDE.model.*;
import topViewDE.view.*;
import topViewDE.blocks.*;

public class Game implements 
    Model,
    View<ModelMap,Drawable>,
    Controller,
    Blocks<Viewport<ModelMap,Drawable>>
    {
  double cameraZ=30;
  private Map<Character,Runnable> actionMap=makeMap();
  ModelMap m;
  public Game(ModelMap m) {this.m=m;}
  @Override public Drawable get(Viewport<ModelMap,Drawable> view, int coord) {return view.get(coord);}
  @Override public void set(Viewport<ModelMap,Drawable> view, Drawable elem, int coord) {view.set(elem,coord);}
  @Override public int coordDs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordDs(x,y,z);}
  @Override public int coordPs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordPs(x,y,z);}
  @Override public int pixelX(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelX(coord);}
  @Override public int pixelY(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelY(coord);}
  @Override public double centerX(ModelMap m) {return m.centerX();}
  @Override public double centerY(ModelMap m) {return m.centerY();}
  @Override public Drawable get(ModelMap m, int x, int y, int z){
    return itemToDrawable(m.get(x,y,z));
    }
  @Override public Graphics2D getGraphics(Viewport<ModelMap,Drawable> view) {return view.getGraphics();}
  @Override public void cameraUp() {cameraZ+=0.1d;}
  @Override public void cameraDown() {cameraZ-=0.1d;}
  @Override public int maxX(Viewport<ModelMap, Drawable> view) {return view.maxX;}
  @Override public int maxY(Viewport<ModelMap, Drawable> view) {return view.maxY;}
  @Override public int maxZ(Viewport<ModelMap, Drawable> view) {return view.maxZ;}
  protected Drawable itemToDrawable(Item item){
    if(item==Item.nope)return DrawableConsts.air;
    if(item==Item.forest)return DrawableConsts.treeL;
    if(item==Item.trunk)return DrawableConsts.treeT;
    return DrawableConsts.rock;
    }
  @Override public Map<Character, Runnable> actions() {return this.actionMap;}
  @Override public void updateView() {}
  @Override public ModelMap getMap() {return m;}

  //should not be needed but Java is confused
  @Override public void handleKeyEvent(Character c) {Controller.super.handleKeyEvent(c);}
  //@Override public void drawCell(Viewport<ModelMap, Drawable> view, int x, int y, int z){Blocks.super.drawCell(view, x, y, z);}
  @Override public void goNorht() {Model.super.goNorth();}
  @Override public void goSouth() {Model.super.goSouth();}
  @Override public void goEast() {Model.super.goEast();}
  @Override public void goWest() {Model.super.goWest();}

}

class GameMap extends JComponent{
  private static final long serialVersionUID = 1L;
  @Override public Dimension getPreferredSize() {return new Dimension(800,800);}
  Game game;
  GameMap(Game game){this.game=game;}
  @Override public void paintComponent(Graphics g) {
    game.renderViewPort((Graphics2D)g,game.getMap(),game.cameraZ);
  }
}