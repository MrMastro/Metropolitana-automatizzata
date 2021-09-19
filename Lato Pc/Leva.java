import javax.swing.*;
import java.awt.event.*;
import java.awt.*;



public class Leva extends JPanel implements MouseListener, MouseMotionListener
{
boolean mode;
int mX=20;
int mY=19;
int valore=1;

public  Leva(Component c, boolean mode)
  {
  this();
  setSize(c.getWidth(),c.getHeight());
  this.mode= mode;
  }
public Leva()
  {
  super();
  addMouseListener(this);
  addMouseMotionListener(this);
  }
  
int valore()
  {
  return valore;
  }
public void paint(Graphics g)
  {
  g.setColor(Color.white);
  g.fillRect(0,0,getWidth(),getHeight());
  g.setColor(Color.black);
  g.drawLine(0,19,255,19);
  g.drawLine(mX,16,mX,25);
  g.drawString("velocita':          "+valore, 4,10);
//g.drawString(""+valore, 120,10);
  }
public void	mouseClicked(MouseEvent e) 
{
//active=true;
// System.out.println("click on  x= " +e.getX() + " y= "+e.getY());
if( e.getX()>-1&& e.getX()<255)
  {
  mX=e.getX();
  valore=e.getX();
  }
repaint();
}
public void	mouseEntered(MouseEvent e) 
{}
public void	mouseExited(MouseEvent e) 
{}
public void	mousePressed(MouseEvent e) 
{}
public void	mouseReleased(MouseEvent e) 
{}


public void	mouseDragged(MouseEvent e) 
{

// System.out.println("x= " +e.getX() + " y= "+e.getY());
mX=e.getX();
mY=e.getY();
if( e.getX()>-1&& e.getX()<255)
  {
  valore=e.getX();
  }
if(e.getX()<0)
  {
  valore=1;
  mX=1;
  }
if(e.getX()>255)
  {
  valore=254;
  mX=254;
  }

repaint();
}
public void	mouseMoved(MouseEvent e) 
{
  // if( e.getX()>-1&& e.getX()<255)
  // {
  // valore=e.getX();
  // }
}

public static void main(String args[])
{
JFrame j=new JFrame();
j.setSize(200,80);
j.setVisible(true);
JPanel lev=new Leva();
j.getContentPane().add(lev);
}
  }
  