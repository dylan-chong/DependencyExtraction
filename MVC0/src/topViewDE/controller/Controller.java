package topViewDE.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public interface Controller<M>{
  M getModel();
  Map<Character,java.util.function.Consumer<Controller<M>>> getMap();
  void goNorht(M m);
  void goSouth(M m);
  void goEast(M m);
  void goWest(M m);
  void cameraUp();
  void cameraDown();
  default void handleKeyEvent(Character c){
    var mapped=getMap().get(c);
    if(mapped!=null)mapped.accept(this);
    }
  public static <M,V>Map<Character,Consumer<Controller<M>>> makeMap(){
    var res=new HashMap<Character,Consumer<Controller<M>>>();
    res.put('8',c->c.goNorht(c.getModel()));
    res.put('2',c->c.goSouth(c.getModel()));
    res.put('6',c->c.goEast(c.getModel()));
    res.put('4',c->c.goWest(c.getModel()));
    res.put('5',c->c.cameraUp());
    res.put('0',c->c.cameraDown());
    return res;
    }
  }