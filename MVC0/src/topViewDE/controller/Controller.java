package topViewDE.controller;

import java.util.HashMap;
import java.util.Map;

public interface Controller{
  Map<Character,Runnable> actions();
  void goNorht();
  void goSouth();
  void goEast();
  void goWest();
  void cameraUp();
  void cameraDown();
  
  default void handleKeyEvent(Character c){
    var mapped=actions().get(c);
    if(mapped!=null)mapped.run();
    }
  default Map<Character,Runnable> makeMap(){
    var res=new HashMap<Character,Runnable>();
    res.put('8',()->goNorht());
    res.put('2',()->goSouth());
    res.put('6',()->goEast());
    res.put('4',()->goWest());
    res.put('5',()->cameraUp());
    res.put('0',()->cameraDown());
    return res;
    }
  }