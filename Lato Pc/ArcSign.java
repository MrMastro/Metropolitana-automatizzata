import java.awt.*;

/*************
 * Il segno che rappresenta un arco 
 *
 * 19/12/2012
 *        aggiunta la visibilità dell'arco
 * @version 2.0
 *************/
public class ArcSign
{
/*************
 * L'arco rappresentato
 *************/
Arc a;
/*************
 * il segno del nodo di partenza
 *************/
NodeSign from;
/*************
 * il segno del nodo di arrivo
 *************/
NodeSign to;
/*************
 * Il colore dell'arco
 *************/
Color c;
/*************
 * La visibilità dell'arco
 *************/
boolean visible=true;
/*************
 * il successivo arco nella lista degli archi tra i due nodi
 *************/
ArcSign next=null;
/*************
 * Crea un nuovo arco 
 * @param from il nodo di partenza dell'arco da creare
 * @param ar l'arco da creare
 * @param to il nodo di arrivo dell'arco da creare
 * @param c il colore del l'arco
 *************/
public ArcSign(NodeSign from,Arc ar,NodeSign to,Color c)
  {
  this(from,ar,to,c,null);
  }
/*************
 * Crea un nuovo arco 
 * @param from il nodo di partenza dell'arco da creare
 * @param ar l'arco da creare
 * @param to il nodo di arrivo dell'arco da creare
 * @param c il colore del l'arco
 * @param next il successivo arco sovrapposto tra gli stessi nodi
 *************/
public ArcSign(NodeSign from,Arc ar,NodeSign to,Color c,ArcSign next)
  {
  a=ar;
  this.from=from;
  this.to=to;
  this.c=c;
  this.next=next;
  }
/*************
 * Cambia il colore dell'arco
 * @param col il nuovo colore dell'arco
 *************/
public void setColor(Color col)
  {
  c=col;
  }
/*************
 * Ritorna il colore dell'arco
 * @return il nuovo colore dell'arco
 *************/
public Color getColor()
  {
  return c;
  }
/*************
 * Modifica la visibilita' dell'arco
 * @param vis <b>true</b> se l'arco è visibile, <b>false</b> altrimenti
 *************/
public void setVisible(boolean vis)
  {
  visible=vis;
  }
/*************
 * Ritorna la visibilita' del nodo
 * @return <b>true</b> se il nodo è visibile, <b>false</b> altrimenti
 *************/
public boolean isVisible()
  {
  return visible;
  }
/*************
* Disegna l'ArcSign 
* @param g lo spazio grafico di tracciamento
* @param dx l'offset dell'immagine
* @param dy l'offset dell'immagine
* @param z lo zoom dell'immagine
*************/
public void paint(Graphics g,int dx,int dy,double z)
  {
  paint(g,dx,dy,z,true);
  }
/*************
 * Disegna l'ArcSign 
 * @param g lo spazio grafico di tracciamento
 * @param dx l'offset dell'immagine
 * @param dy l'offset dell'immagine
 * @param z lo zoom dell'immagine
 * @param showtext true se va visualizzato i prezzi dell'arco
 *************/
public void paint(Graphics g,int dx,int dy,double z,boolean showtext)
  {
//  System.out.println(this);
  if (!visible) return;
  g.setColor(c);
  int x0=(int)((dx+from.x)*z);
  int y0=(int)((dy+from.y)*z);
  int x1=(int)((dx+to.x)*z);
  int y1=(int)((dy+to.y)*z);
  int xd0,yd0;
  int xd1,yd1;
  if (x0!=x1)
    {
    if (from.shape==NodeSign.CIRCLE)
      xd0=(int)((from.dim)*z*((x1-x0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
     else if (from.shape==NodeSign.SQUARE)
//      xd0=(int)((from.dim)*z*((x1-x0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
      if (y0!=y1)
        xd0=(int)((from.dim)*z*(x1-x0)/Math.abs(y1-y0));
       else
        xd0=(int)((from.dim)*z*(x1-x0)/Math.abs(x1-x0));
     else
      xd0=0;
    if (to.shape==NodeSign.CIRCLE)
      xd1=(int)((to.dim)*z*((x1-x0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
     else if (to.shape==NodeSign.SQUARE)
//      xd1=(int)((to.dim)*z*((x1-x0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
      if (y0!=y1)
        xd1=(int)((to.dim)*z*(x1-x0)/Math.abs(y1-y0));
       else
        xd1=(int)((to.dim)*z*(x1-x0)/Math.abs(x1-x0));
     else
      xd1=0;
    }
   else
    {
    xd0=0;
    xd1=0;
    }
  if (y0!=y1)
    {
    if (from.shape==NodeSign.CIRCLE)
      yd0=(int)((from.dim)*z*((y1-y0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
     else if (from.shape==NodeSign.SQUARE)
      {
      if (x0!=x1)
        {
  //      yd0=(int)((from.dim)*z*((y1-y0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
        yd0=(int)((from.dim)*z*(y1-y0)/Math.abs(x1-x0));
        if (Math.abs(xd0)>Math.abs(yd0))
          xd0=(int)((from.dim)*z*(x1-x0)/Math.abs(x1-x0));
         else
          yd0=(int)((from.dim)*z*(y1-y0)/Math.abs(y1-y0));
        }
       else
        {
        yd0=(int)((from.dim)*z*(y1-y0)/Math.abs(y1-y0));
        }
      }
     else
      yd0=0;
    if (to.shape==NodeSign.CIRCLE)
      yd1=(int)((to.dim)*z*((y1-y0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
     else if (to.shape==NodeSign.SQUARE)
      {
      if (x0!=x1)
        {
//      yd1=(int)((to.dim)*z*((y1-y0)/Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0))));
        yd1=(int)((to.dim)*z*(y1-y0)/Math.abs(x1-x0));
        if (Math.abs(xd1)>Math.abs(yd1))
          xd1=(int)((to.dim)*z*(x1-x0)/Math.abs(x1-x0));
         else
          yd1=(int)((to.dim)*z*(y1-y0)/Math.abs(y1-y0));
        }
       else
        {
        yd1=(int)((from.dim)*z*(y1-y0)/Math.abs(y1-y0));
        }
      }
     else
      yd1=0;
    }
   else
    {
    yd0=0;
    yd1=0;
    }
  String prices="";
  for (Arc ai=a;ai!=null;ai=ai.next)
    {
    prices+=ai.p;
    if (ai.next!=null)
      prices+="|";
    }
  if (xd0!=0 || yd0!=0)
    {
    g.drawLine(x0+xd0,y0+yd0,x1-xd1,y1-yd1);
    if (showtext)
      g.drawString(prices,(2*x0+3*x1)/5,(2*y0+3*y1)/5);
    }
   else
    { // anello
    xd0=(int)(-to.dim*z);
    yd0=0;
    xd1=(int)(-to.dim*z);
    g.drawArc(x0,y0,(int)(z*2*-xd0/z),(int)(z*2*-xd0/z),90,-270);
    if (showtext)
      g.drawString(prices,x0-xd0,y0-xd0);
    }
  int m=(z<1)?1:(z<2)?2:3;
//  System.out.println(a+" "+x1+" "+y1+" "+xd1+" "+yd1+" "+m+" "+(x1-xd1)+" "+(x1-xd1-xd1/m-yd1/(m+1))+" "+(x1-xd1-xd1/m+yd1/(m+1))+" "+
//                (y1-yd1)+" "+(y1-yd1-yd1/m+xd1/(m+1))+" "+(y1-yd1-yd1/m-xd1/(m+1)));
  g.fillPolygon(new int[]{x1-xd1,x1-xd1-xd1/m-yd1/(m+1),x1-xd1-xd1/m+yd1/(m+1)},
                new int[]{y1-yd1,y1-yd1-yd1/m+xd1/(m+1),y1-yd1-yd1/m-xd1/(m+1)},3);
  }
/*************
 * Ritorna la stringa che rappresenta l'arco 
 * @return la stringa che rappresenta l'arco
 *************/
public String toString()
  {
  return "ArcSign["+from+"("+a+")"+to+","+c+"]";
  }
}
