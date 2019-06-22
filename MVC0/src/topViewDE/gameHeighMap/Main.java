package topViewDE.gameHeighMap;

import java.awt.Graphics2D;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import topViewDE.blocks.Blocks;
import topViewDE.blocks.Drawable;
import topViewDE.blocks.DrawableConsts;
import topViewDE.controller.Controller;
import topViewDE.modelHeightMap.Item;
import topViewDE.modelHeightMap.Model;
import topViewDE.modelHeightMap.ModelMap;
import topViewDE.view.View;
import topViewDE.view.Viewport;

class Game extends JFrame implements 
Model,
View<ModelMap,Drawable>,
Controller,
Blocks<Viewport<ModelMap,Drawable>>
{
private static final long serialVersionUID = 1L;
double cameraZ=30;
private Map<Character,Runnable> actionMap=makeMap();
ModelMap m;
public Game(ModelMap m) {this.m=m;}
@Override public JFrame getFrame() {return this;}
@Override public Drawable get(Viewport<ModelMap,Drawable> view, int coord) {return view.get(coord);}
@Override public void set(Viewport<ModelMap,Drawable> view, Drawable elem, int coord) {view.set(elem,coord);}
@Override public int coordDs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordDs(x,y,z);}
@Override public int coordPs(Viewport<ModelMap,Drawable> view, int x, int y, int z) {return view.coordPs(x,y,z);}
@Override public int pixelX(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelX(coord);}
@Override public int pixelY(Viewport<ModelMap,Drawable> view, int coord) {return view.pixelY(coord);}
@Override public double centerX(ModelMap m) {return m.centerX();}
@Override public double centerY(ModelMap m) {return m.centerY();}
@Override public Graphics2D getGraphics(Viewport<ModelMap,Drawable> view) {return view.getGraphics();}
@Override public void cameraUp() {cameraZ+=0.2d;}
@Override public void cameraDown() {cameraZ-=0.2d;}
@Override public int maxX(Viewport<ModelMap, Drawable> view) {return view.maxX;}
@Override public int maxY(Viewport<ModelMap, Drawable> view) {return view.maxY;}
@Override public int maxZ(Viewport<ModelMap, Drawable> view) {return view.maxZ;}
@Override public Map<Character, Runnable> actions() {return this.actionMap;}
@Override public ModelMap getMap() {return m;}
@Override public Drawable get(ModelMap m, int x, int y, int z){
return itemToDrawable(m.get(x,y,z));
}
//only interesting method
protected Drawable itemToDrawable(Item item){
if(item==Item.nope)return DrawableConsts.air;
if(item==Item.forest)return DrawableConsts.treeL;
if(item==Item.trunk)return DrawableConsts.treeT;
if(item==Item.water)return DrawableConsts.water;
return DrawableConsts.rock;
}
//should not be needed but Java is confused
@Override public void handleKeyEvent(Character c) {Controller.super.handleKeyEvent(c);}
//@Override public void drawCell(Viewport<ModelMap, Drawable> view, int x, int y, int z){Blocks.super.drawCell(view, x, y, z);}
@Override public void goNorht() {Model.super.goNorth();}
@Override public void goSouth() {Model.super.goSouth();}
@Override public void goEast() {Model.super.goEast();}
@Override public void goWest() {Model.super.goWest();}
@Override public double getCameraZ() {return cameraZ;}
}
public class Main {
  public static void main(String[] args) {
    ModelMap m=new ModelMap();
    SwingUtilities.invokeLater(()->{
      Game g=new Game(m);
      g.initializeApp();
      g.setVisible(true);
    });
  }
}