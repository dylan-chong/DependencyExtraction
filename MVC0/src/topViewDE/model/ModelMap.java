package topViewDE.model;

import java.util.Random;

public class ModelMap{
  double centerX=10;
  double centerY=10;
  public double centerX() {return centerX;}
  public double centerY() {return centerY;}
  
  /*public Item get(int x, int y, int z) {
    if(x==4 && z==15)return Item.ground;
    if(y==4 && z==8)return Item.ground;
    if(y==3 && z==6)return Item.ground;
    return Item.nope;
  }*/
  
  int[][]map=new int[100][100];
  {for(int x=0;x<100;x+=1)for(int y=0;y<100;y+=1)map[x][y]=25;
  Random r=new Random();
  for(int i=0;i<100000;i+=1) {
    int x=r.nextInt(100);
    int y=r.nextInt(100);
    map[x][y]=Math.max(0,map[x][y]-1);
  }
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