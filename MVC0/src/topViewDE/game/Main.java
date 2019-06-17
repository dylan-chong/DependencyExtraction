package topViewDE.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import topViewDE.model.ModelMap;


public class Main {
  private static void createAndShowGUI() {
    Game g=new Game(new ModelMap());
    JFrame frame = new JFrame("MAP");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new GameMap(g));
    frame.pack();
    frame.setVisible(true);
    frame.addKeyListener(new KeyListener() {
    @Override public void keyTyped(KeyEvent e) {
      g.handleKeyEvent(e.getKeyChar());
      frame.repaint();
      }
    @Override public void keyReleased(KeyEvent arg0) {}
    @Override public void keyPressed(KeyEvent arg0) {}
    });
  }
  public static void main(String[] args) {
    SwingUtilities.invokeLater(()->createAndShowGUI());
    }
}