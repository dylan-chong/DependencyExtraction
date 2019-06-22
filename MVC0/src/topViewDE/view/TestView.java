package topViewDE.view;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TestView {
  @Test
  void testQuadrants() {
    var b=new StringBuilder();
    var l=List.of(
        List.of( 1, 2, 21, 6, 5),
        List.of( 3, 4,22, 8, 7),
        List.of(17,18,25,20, 19),
        List.of(15,16,24,12,11),
        List.of(13,14,23,10, 9)
        );
    new Visit<Object,Object>(){
      public void drawCell(Viewport<Object,Object> vp,int x, int y, int z) {
        b.append(l.get(y).get(x)+" ");
      }}.visitQuadrants(null, 5, 5, 1);
    assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 ",
        b.toString());    
  }
}
