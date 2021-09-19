import java.awt.*;

/*************
 * Il segno che rappresenta un nodo 
 * 19/12/2012
 *       aggiunta la visibilità del nodo
 * @version 2
 *************/
public class NodeSign
{
/*************
 * Il nodo rappresentato
 *************/
Node nn;
/*************
 * Il colore del nodo
 *************/
Color c;
/*************
 * Il tipo di forma usata per rappresentare il nodo 
 *************/
static final int CIRCLE=0, SQUARE=1;
/*************
 * Il tipo di forma usata per rappresentare il nodo 
 *************/
int shape;
/*************
 * Se il nodo e' rappresentato da una forma piena e' true, false altrimenti 
 *************/
boolean filled;
/*************
 * Le coordinate del nodo nel piano 
 *************/
int x,y;
/*************
 * La dimensione del segno 
 *************/
int dim;
/*************
 * L'immagine che rappresenta il segno 
 *************/
Image im;
/*************
 * Il nome dell'immagine che rappresenta il segno 
 *************/
String imn;
/*************
 * Se il nodo e' selezionato e' true, false altrimenti 
 *************/
boolean selected;
/*************
 * Se il nodo e' visibile e' true, false altrimenti 
 *************/
boolean visible=true;
/*************
 * La dimensione default dello spazio di visualizzazione
 *************/
//static int size=200;
/*************
 * La dimensione default dei segni 
 *************/
static int nodeSize=10;
public static void setDefault(int s, int ns)
  {
//  size=s;
  nodeSize=ns;
  }
/*************
 * Crea un nuovo nodo 
 * @param name il nome del nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 *************/
public NodeSign(String name, int x, int y)
  {
  this(new Node(name),x,y,Color.black);
  }
/*************
 * Crea un nuovo nodo 
 * @param n il nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 *************/
public NodeSign(Node n, int x, int y)
  {
  this(n,x,y,Color.black);
  }
/*************
 * Crea un nuovo nodo 
 * @param name il nome del nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 * @param c il colore del  nodo
 *************/
public NodeSign(String name, int x, int y, Color c)
  {
  this(new Node(name),x,y,c);
  }
/*************
 * Crea un nuovo nodo 
 * @param n il nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 * @param c il colore del  nodo
 *************/
public NodeSign(Node n, int x, int y, Color c)
  {
  this(n,x,y,(c!=null)?c:Color.black,nodeSize);
  }
/*************
 * Crea un nuovo nodo 
 * @param name il nome del nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 * @param c il colore del  nodo
 * @param dim la dimensione del segno
 *************/
public NodeSign(String name, int x, int y, Color c, int dim)
  {
  this(new Node(name),x,y,c,dim);
  }
/*************
 * Crea un nuovo nodo 
 * @param n il nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 * @param c il colore del  nodo
 * @param dim la dimensione del segno
 *************/
public NodeSign(Node n, int x, int y, Color c, int dim)
  {
  this(n,x,y,c,dim,null,null);
  }
/*************
 * Crea un nuovo nodo 
 * @param n il nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 * @param c il colore del  nodo
 * @param dim la dimensione del segno
 * @param im l'immagine che rappresenta il segno
 * @param imn il nome dell'immagine che rappresenta il segno
 *************/
public NodeSign(Node n, int x, int y, Color c, int dim,Image im, String imn)
  {
  this(n,x,y,c,dim,im,imn,0 /* CIRCLE */,false);
  }
/*************
 * Crea un nuovo nodo 
 * @param n il nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 * @param c il colore del  nodo
 * @param dim la dimensione del segno
 * @param shape la forma che rappresenta il nodo (CIRCLE=0, SQUARE=1)
 * @param filled <b>true</b> se la forma che rappresenta il nodo è piena, <b>false</b> altrimenti 
 *************/
public NodeSign(Node n, int x, int y, Color c, int dim, int shape, boolean filled)
  {
  this(n,x,y,c,dim,null,null,shape,filled);
  }
/*************
 * Crea un nuovo nodo 
 * @param n il nodo rappresentato
 * @param x l'ascissa del nodo sul piano
 * @param y l'ordinata del nodo sul piano
 * @param c il colore del  nodo
 * @param dim la dimensione del segno
 * @param im l'immagine che rappresenta il segno
 * @param imn il nome dell'immagine che rappresenta il segno
 * @param shape la forma che rappresenta il nodo (CIRCLE=0, SQUARE=1)
 * @param filled <b>true</b> se la forma che rappresenta il nodo è piena, <b>false</b> altrimenti 
 *************/
public NodeSign(Node n, int x, int y, Color c, int dim,Image im, String imn, int shape, boolean filled)
  {
  nn=n;
/*
  if (x<0 || y<0)
    {
    int xn=size/2,yn=size/2;
    this.x=xn;
    this.y=yn;
    }
   else
*/
    {
    this.x=x;
    this.y=y;
    }
  this.c=c;
  this.dim=dim;
  this.im=im;
  this.imn=imn;
  this.shape=shape;
  this.filled=filled;
  selected=false;
  }
/*************
 * Cambia il nodo rappresentato
 * @param n il nuovo nodo rappresentato
 */
public void setNode(Node n)
  {
  nn=n;
  }
/*************
 * Ritorna il nodo rappresentato
 * @return il nodo rappresentato
 */
public Node getNode()
  {
  return nn;
  }
/*************
 * Ritorna il nome del nodo rappresentato
 * @return il nome del nodo rappresentato
 */
public String getName()
  {
  return getNode().getName();
  }
/*************
 * Cambia l'ascissa del nodo
 * @param nx la nuova ascissa del nodo
 */
public void setX(int nx)
  {
  x=nx;
  }
/*************
 * Ritorna l'ascissa del nodo
 * @return l'ascissa del nodo
 */
public int getX()
  {
  return x;
  }
/*************
 * Cambia l'ordinata del nodo
 * @param ny la nuova ordinata del nodo
 */
public void setY(int ny)
  {
  y=ny;
  }
/*************
 * Ritorna l'ordinata del nodo
 * @return l'ordinata del nodo
 */
public int getY()
  {
  return y;
  }
/*************
 * Cambia il nuovo colore del nodo
 * @param col il nuovo colore del nodo
 */
public void setColor(Color col)
  {
  c=col;
  }
/*************
 * Ritorna il colore del nodo
 * @return il colore del nodo
 */
public Color getColor()
  {
  return c;
  }
/*************
 * Cambia la nuova dimensione del nodo
 * @param size la nuova dimensione del nodo
 */
public void setSize(int size)
  {
  dim=size;
  }
/*************
 * Ritorna la dimensione del nodo
 * @return la dimensione del nodo
 */
public int getSize()
  {
  return dim;
  }
/*************
 * Cambia la nuova forma del nodo
 * @param s la nuova forma del nodo, 0 cerchio, 1 quadrato
 */
public void setShape(int s)
  {
  shape=s;
  }
/*************
 * Ritorna la dimensione del nodo
 * @return la dimensione del nodo
 */
public int getShape()
  {
  return shape;
  }
/*************
 * Modifica la campitura del nodo
 * @param f <b>true</b> se il nodo è pieno, <b>false</b> altrimenti
 */
public void setFilled(boolean f)
  {
  filled=f;
  }
/*************
 * Ritorna la visibilita' del nodo
 * @return <b>true</b> se il nodo è visibile, <b>false</b> altrimenti
 */
public boolean isFilled()
  {
  return filled;
  }
/*************
 * Cambia la nuova immagine del nodo
 * @param img la nuova immagine del nodo
 */
public void setImage(String imgn, Image img)
  {
  imn=imgn;
  im=img;
  if (imn==null || im==null)
    {
    im=null;
    imn=null;
    }
  }
/*************
 * Ritorna il nome della immagine del nodo
 * @return il nome della immagine del nodo
 */
public String getImageName()
  {
  return imn;
  }
/*************
 * Ritorna l'immagine del nodo
 * @return l'immagine del nodo
 */
public Image getImage()
  {
  return im;
  }
/*************
 * Modifica la visibilita' del nodo
 * @param vis <b>true</b> se il nodo è visibile, <b>false</b> altrimenti
 */
public void setVisible(boolean vis)
  {
  visible=vis;
  }
/*************
 * Ritorna la visibilita' del nodo
 * @return <b>true</b> se il nodo è visibile, <b>false</b> altrimenti
 */
public boolean isVisible()
  {
  return visible;
  }
/*************
* Disegna il GraphPanel 
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
 * Disegna il GraphPanel 
 * @param g lo spazio grafico di tracciamento
 * @param dx l'offset dell'immagine
 * @param dy l'offset dell'immagine
 * @param z lo zoom dell'immagine
 * @param showtext true se va visualizzato il testo del nome
 *************/
public void paint(Graphics g,int dx,int dy,double z,boolean showtext)
  {
  if (!visible) return;
  int x0=(int)((dx+x)*z);
  int y0=(int)((dy+y)*z);
  g.setColor(c);
  int nodeSize=(int)(dim*z);
  if (im!=null)
    g.drawImage(im,x0-nodeSize,y0-nodeSize,2*nodeSize,2*nodeSize,null);
   else
    if (filled)
      {
      if (shape==CIRCLE)
        g.fillOval(x0-nodeSize,y0-nodeSize,2*nodeSize,2*nodeSize);
       else if (shape==SQUARE)
        g.fillRect(x0-nodeSize,y0-nodeSize,2*nodeSize,2*nodeSize);
      }
     else
      {
      if (shape==CIRCLE)
        g.drawOval(x0-nodeSize,y0-nodeSize,2*nodeSize,2*nodeSize);
       else if (shape==SQUARE)
        g.drawRect(x0-nodeSize,y0-nodeSize,2*nodeSize,2*nodeSize);
      }
  if (selected)
      if (shape==CIRCLE)
        g.drawOval(x0-nodeSize-1,y0-nodeSize-1,2*nodeSize+2,2*nodeSize+2);
       else if (shape==SQUARE)
        g.drawRect(x0-nodeSize-1,y0-nodeSize-1,2*nodeSize+2,2*nodeSize+2);
  if (showtext)
    {
    if (filled)
      {
      int ac=c.getRGB();
      int lum=((ac&0xff0000)>>16)|((ac&0x00ff00)>>8)|(ac&0x0000ff);
      if ((lum&0xc0)!=0xc0)
        g.setColor(Color.white);
       else
        g.setColor(Color.black);
      }
    g.drawString(nn.name,x0-g.getFontMetrics().stringWidth(nn.name)/2,y0);
    }
  }
/************
 * Ritorna la stringa che descrive il nodo
 * @return la descrizione del nodo
 ************/
public String toString()
  {
  return "NodeSign["+nn+",("+x+","+y+"),"+dim+","+c+","+shape+","+filled+"]";
  }
}
