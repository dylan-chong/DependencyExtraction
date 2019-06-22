package topViewDE.modelHeightMap;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

import org.junit.jupiter.api.Test;

public class CellularExpansionTest {

  @Test
  public void testCenter() {
    var cells=new CellularExpansion<String>(7, 7, new Random(3),-1);
    cells.addSeed(cells.coord(3,3),"H");
    assertEquals("\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@@H@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@",cells.toString());
  }
  @Test
  public void testTop() {
    var cells=new CellularExpansion<String>(7, 7, new Random(3),-1);
    cells.addSeed(cells.coord(3,0),"H");
    assertEquals("\n" + 
        "@@@H@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@",cells.toString());
  }
  @Test
  public void testCenterGrow() {
    var cells=new CellularExpansion<String>(7, 7, new Random(3),-1);
    cells.addSeed(cells.coord(3,3),"H");
    cells.grow(1.1d);
    assertEquals("\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@HHH@@\n" + 
        "@@HHH@@\n" + 
        "@@HHH@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@",cells.toString());
  }
  @Test
  public void testCenterGrow02() {
    var cells=new CellularExpansion<String>(7, 7, new Random(3),-1);
    cells.addSeed(cells.coord(3,3),"H");
    cells.grow(0.2d);
    assertEquals("\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@\n" + 
        "@@H@@@@\n" + 
        "@@@H@@@\n" + 
        "@@@H@@@\n" + 
        "@@@@@@@\n" + 
        "@@@@@@@",cells.toString());
  }
  @Test
  public void testCenterGrow02_5() {
    var cells=new CellularExpansion<String>(7, 7, new Random(3),-1);
    cells.addSeed(cells.coord(3,3),"H");
    cells.grow(0.2d);
    cells.grow(0.2d);
    cells.grow(0.2d);
    cells.grow(0.2d);
    cells.grow(0.2d);
    assertEquals("\n" + 
        "@@@@@H@\n" + 
        "@@H@H@@\n" + 
        "HHHHH@@\n" + 
        "HHHHHH@\n" + 
        "@HHHH@H\n" + 
        "@@HH@H@\n" + 
        "@@@@@@@",cells.toString());
  }
}
