package topViewDE.blocks;

import java.awt.Color;

public class DrawableConsts{
  public static final Drawable grass=new Cube(new Color(10,200,5));
  public static final Drawable rock=new Cube(new Color(100,100,100));
  public static final Drawable ground=new Cube(new Color(233, 168, 81));
  public static final Drawable air=new Air();
  public static final Drawable treeT=new TreeTrunk();
  public static final Drawable treeL=new TreeLeaves();
  public static final Drawable water=new Water();//new Cube(new Color(5,20,250));
}