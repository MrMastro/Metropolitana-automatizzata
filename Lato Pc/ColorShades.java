import java.awt.*;
import java.util.*;
import java.awt.event.*;

/*********
 * Il pannello con tutte le sfumature dei colori all'interno del ColorPickPanel
 *********/
public class ColorShades extends Panel implements MouseListener
{
/*********
 * Le componenti HSB del colore rappresentato
 *********/
float f[];
/*********
 * Il colore rappresentato
 *********/
Color c;
/*********
 * I listeners che gestiscono i cambiamenti di colore nel Panel
 *********/
Vector<ColorChangeListener> listeners=new Vector<ColorChangeListener>();
/*********
 * Crea un Panel ColorShades con selezionato il colore indicato
 * @param c il colore selezionato
 *********/
public ColorShades(Color c)
  {
  addMouseListener(this);
  setColor(c);
  }
/*********
 * Ritorna il colore selezionato
 * @return il colore selezionato
 *********/
public Color getColor()
  {
  return c;
  }
/*********
 * Stabilisce il colore selezionato
 * @param c il colore selezioanto
 *********/
public void setColor(Color c)
  {
  this.c=c;
  f=Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),null);
  repaint();
  }
/**********
 * Rimuove un ColorChangeListener dal ColoreShades
 * @param ccl il ColoreCHangeListener da rimuovere
 **********/
public void removeColorChangeListener(ColorChangeListener ccl)
  {
  listeners.remove(ccl);
  }
/**********
 * Aggiunge un ColorChangeListener al ColoreShades
 * @param ccl il ColoreCHangeListener da aggiungere
 **********/
public void addColorChangeListener(ColorChangeListener ccl)
  {
  listeners.add(ccl);
  }
/**********
 * Aggiorna l'immagine del ColorShades
 * @param g lo spazio grafico su cui visualizzare il ColorShades
 **********/
public void update(Graphics g)
  {
  paint(g);
  }
/**********
 * Viene chiamato quando viene clickato un punto del ColorShades aggiorando il colore selezionato
 * richiamando tutti i listeners.
 * @param e il MaouseEvent che rappresenta l'evento della pressione del tasto sul mouse
 **********/
public void mouseClicked(MouseEvent e)
  {
  if (e.getX()<(getWidth()-10))
    if (f[2]==0.0)
      c=Color.getHSBColor(e.getY()*1.0f/getHeight(),e.getX()*1.0f/(getWidth()-10),0.5f);
     else
      c=Color.getHSBColor(e.getY()*1.0f/getHeight(),e.getX()*1.0f/(getWidth()-10),f[2]);
   else
    c=Color.getHSBColor(f[0],f[1],e.getY()*1.0f/getHeight());
  setColor(c);
  for(int l=0;l<listeners.size();l++)
    {
    listeners.elementAt(l).colorChanged(new Event(this,0,c));
    }
  }
public void mouseEntered(MouseEvent e) 
  {
  }
public void mouseExited(MouseEvent e) 
  {
  }
public void mousePressed(MouseEvent e) 
  {
  }
public void mouseReleased(MouseEvent e) 
  {
  }
/*********
 * Disegna il ColorShades sul Graphics indicato
 * @param g la superficie su cui disegnare il ColorShades
 *********/
public void paint(Graphics g)
  {
  int i,j;
  float s,h,b;
  for (s=0,i=0;s<=1.0;i++,s+=1.0f/(getWidth()-10))
    for (h=0,j=0;h<=1.0;j++,h+=1.0f/getHeight())
      {
      g.setColor(Color.getHSBColor(h,s,0.5f+s/2));
      g.drawLine(i,j,i,j);
      }
  float f[]=Color.RGBtoHSB(c.getRed(),c.getGreen(),c.getBlue(),null);
  for (b=0,j=0;b<=1.0;j++,b+=1.0f/getHeight())
    {
    g.setColor(Color.getHSBColor(f[0],f[1],b));
    g.drawLine(getWidth()-10,j,getWidth(),j);
    }
  g.setColor(Color.black);
  i=(int)(f[1]*(getWidth()-10));
  j=(int)(f[0]*getHeight());
  g.drawLine(i-5,j,i+5,j);
  g.drawLine(i,j-5,i,j+5);
  j=(int)(f[2]*getHeight()-1);
  if (f[2]<0.5)
    g.setColor(Color.white);
  g.drawLine(getWidth()-10,j,getWidth(),j);
  }
}

