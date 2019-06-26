package topViewDE.game0;

import javax.swing.SwingUtilities;

import topViewDE.model0.ModelMap;

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