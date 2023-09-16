import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Main extends Canvas implements Runnable{
  
  private static final long serialVersionUID = 1L;

  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;
  public static final String TITLE = "DangerGame 3D";

  private Thread thread;
  private Screen screen;
  private BufferedImage img;
  private BufferedImage img2;
  private Game game;
  private Render render;
  private boolean isRunning = false;
  private int[] pixels;
  private String fps;

  //KeyHandler kh = new KeyHandler();
  InputHandler input; 


  public Main(){
    screen = new Screen(WIDTH, HEIGHT);
    img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    img2 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    input = new InputHandler();
    game = new Game(input);
    pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();

    addKeyListener(input);
    addMouseListener(input);
    addMouseMotionListener(input);
  }

  private void start(){
    if(isRunning) return;
    isRunning = true;
    thread = new Thread(this);
    thread.start();

    System.out.println("Working!");
  }

  private void stop(){
    if(!isRunning) return;
    isRunning = false;
    try{
      thread.join();
    } catch(Exception e){
      e.printStackTrace();
      System.exit(0);
    }
  }

  public void run(){
    int timer = 0;
    long lastTime = System.nanoTime();
    double amountOfTicks = 60.0;
    double ns = 1000000000 / amountOfTicks;
    double delta = 0;
    int tic = 0;
    while(isRunning) {
      long now = System.nanoTime();

      delta += (now - lastTime) / ns;
      timer += (now - lastTime);

      lastTime = now;

      while(delta >= 1) {
        tick();
        render();
        tic++;
        delta--;
        if(timer % 1000000000 < 1000){
        //String.format("%c[%d;%df",0x1B, 1, 1) + 
          System.out.println("FPS: " + tic + " xpos: " + game.x + " ypos: " + game.y + " rot " + game.rot);
          fps = "FPS: " + tic;
          tic = 0;
        }
      } 
    }
  }
  private void tick(){
    game.tick();
  }
  private void render(){
    BufferStrategy bs = this.getBufferStrategy();
    if(bs == null){
      createBufferStrategy(3);
      return;
    }

    screen.render(game);

    for(int i = 0; i < WIDTH * HEIGHT; i++){
      pixels[i] = screen.pixels[i];
    }
    Graphics g = bs.getDrawGraphics();
    g.drawImage(img, 0, 0, WIDTH, HEIGHT, null);
    //g.drawImage(img2, 350, 250, 100, 100, null);
    g.setColor(Color.YELLOW);
    //g.drawString(fps, 20, 40);
    g.dispose();
    bs.show();
  }

  public static void main(String[] args) {
    BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
    Main game = new Main();
    JFrame frame = new JFrame();
    frame.add(game);
    frame.pack();
    frame.getContentPane().setCursor(blank);
    frame.setTitle(TITLE);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(WIDTH, HEIGHT);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
    System.out.println("Running...");

    game.start();
  }
  
}

class InputHandler implements KeyListener, MouseListener, MouseMotionListener, FocusListener{
  
  public boolean upPressed;
  public boolean downPressed;
  public boolean leftPressed;
  public boolean rightPressed;
  public boolean leftTurnPressed;
  public boolean rightTurnPressed;

  public int mouseX;
  public int mouseY;

  @Override
//KeyListener
  public void keyTyped(KeyEvent e){}
  public void keyPressed(KeyEvent e){
    int code = e.getKeyCode();

    if(code == KeyEvent.VK_W){
      upPressed = true;
    }
    if(code == KeyEvent.VK_S){
      downPressed = true;
    }
    if(code == KeyEvent.VK_A){
      leftPressed = true;
    }
    if(code == KeyEvent.VK_D){
      rightPressed = true;
    }
    if(code == KeyEvent.VK_Q){
      leftTurnPressed = true;
    }
    if(code == KeyEvent.VK_E){
      rightTurnPressed = true;
    }
  }
  public void keyReleased(KeyEvent e){
    System.out.println("Key Released");
    int code = e.getKeyCode();

    if(code == KeyEvent.VK_W){
      upPressed = false;
    }
    if(code == KeyEvent.VK_S){
      downPressed = false;
    }
    if(code == KeyEvent.VK_A){
      leftPressed = false;
    }
    if(code == KeyEvent.VK_D){
      rightPressed = false;
    }
    if(code == KeyEvent.VK_Q){
      leftTurnPressed = false;
    }
    if(code == KeyEvent.VK_E){
      rightTurnPressed = false;
    }
  }

//MouseListener
  public void mouseExited(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
  public void mousePressed(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}

//MouseMotionListener
  public void mouseDragged(MouseEvent e){}
  public void mouseMoved(MouseEvent e){
    mouseX = e.getX();
  }
  
//FocusListener
  public void focusGained(FocusEvent e){}
  public void focusLost(FocusEvent e){}
}
/*
class KeyHandler implements KeyListener{
  public boolean upPressed;
  public boolean downPressed;
  public boolean leftPressed;
  public boolean rightPressed;
  public boolean leftTurnPressed;
  public boolean rightTurnPressed;
      
  @Override
  public void keyTyped(KeyEvent e){}
  public void keyPressed(KeyEvent e){
    int code = e.getKeyCode();

    if(code == KeyEvent.VK_W){
      upPressed = true;
    }
    if(code == KeyEvent.VK_S){
      downPressed = true;
    }
    if(code == KeyEvent.VK_A){
      leftPressed = true;
    }
    if(code == KeyEvent.VK_D){
      rightPressed = true;
    }
    if(code == KeyEvent.VK_Q){
      leftTurnPressed = true;
    }
    if(code == KeyEvent.VK_E){
      rightTurnPressed = true;
    }
  }
  public void keyReleased(KeyEvent e){
    System.out.println("Key Released");
    int code = e.getKeyCode();

    if(code == KeyEvent.VK_W){
      upPressed = false;
    }
    if(code == KeyEvent.VK_S){
      downPressed = false;
    }
    if(code == KeyEvent.VK_A){
      leftPressed = false;
    }
    if(code == KeyEvent.VK_D){
      rightPressed = false;
    }
    if(code == KeyEvent.VK_Q){
      leftTurnPressed = false;
    }
    if(code == KeyEvent.VK_E){
      rightTurnPressed = false;
    }
  }
}
*/
