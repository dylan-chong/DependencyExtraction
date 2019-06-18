package topViewDE.game;

import javax.swing.SwingUtilities;

import topViewDE.model.ModelMap;

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