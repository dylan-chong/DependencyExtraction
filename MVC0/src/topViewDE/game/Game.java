package topViewDE.game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.Consumer;

import javax.swing.JComponent;

import topViewDE.controller.*;
import topViewDE.logic.*;
import topViewDE.rendering.*;
import topViewDE.blocks.*;

public class Game implements 
    Logic,
    Controller<Model>,
    Rendering<Model,Drawable>,
    Blocks<Viewport<Model,Drawable>>
    {
  double cameraZ=30;
  private java.util.Map<Character,Consumer<Controller<Model>>> map=Controller.makeMap();
  Model m;
  public Game(Model m) {this.m=m;}
  @Override public Drawable get(Viewport<Model,Drawable> view, int coord) {return view.get(coord);}
  @Override public void set(Viewport<Model,Drawable> view, Drawable elem, int coord) {view.set(elem,coord);}
  @Override public int coordDs(Viewport<Model,Drawable> view, int x, int y, int z) {return view.coordDs(x,y,z);}
  @Override public int coordPs(Viewport<Model,Drawable> view, int x, int y, int z) {return view.coordPs(x,y,z);}
  @Override public int pixelX(Viewport<Model,Drawable> view, int coord) {return view.pixelX(coord);}
  @Override public int pixelY(Viewport<Model,Drawable> view, int coord) {return view.pixelY(coord);}
  @Override public double centerX(Model m) {return m.centerX();}
  @Override public double centerY(Model m) {return m.centerY();}
  @Override public java.util.Map<Character, Consumer<Controller<Model>>> getMap(){return map;}
  @Override public void goNorht(Model m) {m.goNorth();}
  @Override public void goSouth(Model m) {m.goSouth();}
  @Override public void goEast(Model m) {m.goEast();}
  @Override public void goWest(Model m) {m.goWest();}
  @Override public Drawable get(Model m, int x, int y, int z){
    return itemToDrawable(m.get(x,y,z));
    }
  @Override public Graphics2D getGraphics(Viewport<Model,Drawable> view) {return view.getGraphics();}
  @Override public Model getModel() {return m;}
  @Override public void cameraUp() {cameraZ+=0.1d;}
  @Override public void cameraDown() {cameraZ-=0.1d;}
  @Override public int maxX(Viewport<Model, Drawable> view) {return view.maxX;}
  @Override public int maxY(Viewport<Model, Drawable> view) {return view.maxY;}
  @Override public int maxZ(Viewport<Model, Drawable> view) {return view.maxZ;}
  protected Drawable itemToDrawable(Item item){
    if(item==Item.nope)return DrawableConsts.air;
    if(item==Item.forest)return DrawableConsts.treeL;
    if(item==Item.trunk)return DrawableConsts.treeT;
    return DrawableConsts.rock;
    }
}

class GameMap extends JComponent{
  private static final long serialVersionUID = 1L;
  @Override public Dimension getPreferredSize() {return new Dimension(800,800);}
  Game game;
  GameMap(Game game){this.game=game;}
  @Override public void paintComponent(Graphics g) {
    game.renderViewPort((Graphics2D)g,game.getModel(),game.cameraZ);
  }
}