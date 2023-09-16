import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Game{
  public InputHandler input;
  public int time;
  public double rot;
  public double x;
  public double y;
  public double speed;
  private int lastMouseX;

  public Game(InputHandler input){
    this.input = input;
    time = 0;
    rot = 0;
    x = 0;
    y = 0;
    speed = 0.6;
  }
  
  public void tick(){
    time++;

    if(input.mouseX > lastMouseX){
      rot += Math.PI / (500.0) * (input.mouseX - lastMouseX);
      lastMouseX = input.mouseX;
    }
    else if(input.mouseX < lastMouseX){
      rot -= Math.PI / (500.0) * (lastMouseX - input.mouseX);
      lastMouseX = input.mouseX;
    }

    if(input.upPressed){
      x += speed * Math.sin(rot);
      y += speed * Math.cos(rot);
    }
    if(input.downPressed){
      x -= speed * Math.sin(rot);
      y -= speed * Math.cos(rot);
    }
    if(input.leftPressed){
      x -= speed * Math.sin(rot + Math.PI/2);
      y -= speed * Math.cos(rot + Math.PI/2);
    }
    if(input.rightPressed){
      x += speed * Math.sin(rot + Math.PI/2);
      y += speed * Math.cos(rot + Math.PI/2);
    }
    if(input.leftTurnPressed){
      rot -= Math.PI / 30.0;
    }
    if(input.rightTurnPressed){
      rot += Math.PI / 30.0;
    }
  }
}
