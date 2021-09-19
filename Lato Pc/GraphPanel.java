import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.security.*;

/*****
 * GraphPanel e' un Panel su cui e' visualizzato un grafo 
 * 
 * 1/3/2011
 *      corretto rappresentazione degli anelli
 *      corretto collocazione dei nodi di una matrice
 * 19/12/2012
 *      reso GraphPanel un ImageReader
 *      aggiunto la possibilità di dare i colori in altre basi
 *      sistemato il flickering
 * 20/12/2012
 *      salva il nome del background
 * 29/12/2012
 *      gestione del colore dei singoli archi (non ancora visualizzato autonomamente)
 * 1/3/2013
 *      aggiunto costruttore che fornisce la collocazione dei nodi
 * 4/3/2013
 *      aggiunto metodi che ritornano il nodo dato l'indice e l'arco dati i nodi
 * 3/12/2013
 *		  aggiunto metodi la rappresentazione di un nodo con diverse forme e con riempimento
 * 6/5/2014
 *      aggiunto metodi per abilitare e disabilitare le modifiche, lo scivolamento e zoom interattivi
 *      sistemato la lettura di immagini da applet
 * DA FARE
 *      selezionare più nodi e modificarli assieme (muoverli e cambiarli)
 *      scegliare l'attivazione di un metodo tramite puntamento
 *      visualizzare in colori diversi gli archi sovrapposti
 * @version 2.1
 *****/
public class GraphPanel extends Panel implements MouseListener, MouseMotionListener, ActionListener, ImageReader
{
static final long serialVersionUID=1;
NodeSign n[];
ArcSign a[][];
Graph g;
Color defaultColor=Color.black;
double defaultWeight=1.0;
String defaultName="n";
int defaultSize=10;
int defaultShape=0;
boolean defaultFill=false;
String defaultImage=null;
int zoomRate=0;
boolean showAxis=false;
boolean showNames=true;
boolean showWeights=true;
final static int MINZOOM=-2,MAXZOOM=7;
final static int MFILE=0,MEDIT=1,MSERACH=2,MCOL=3,MZOOM=4;
final static int WEIGHT=1,REN1=2,NAME=3,ZOOM=4,COLOR=5,SIZE=6,IMAGE=7,SHAPE=8,FILL=9,/*senza scritta */MINL1=10,MINP1=11,REN=12,MINL=13,MINP=14;
final static int VDELN=1,VDELA=2,VMINP=3,VMINL=4,VNAME=5,VCOL=6,VWEI=7,VZOOM=8,VSIZE=9,VIMAGE=10,VSHAPE=11,VFILL=12;
String voice[]={"",
                "Del node",
                "Del arc",
                "Min price -> other node",
                "Min length -> other node",
                "Change name",
                "Change color",
                "Change weight",
                "Change zoom",
                "Change size",
                "Change image",
                "Change shape",
                "Change filling",
               };
String prompt[]={"",
                 "weight of arcs:",
                 "new name for ",
                 "node name:",
                 "zoom (-2..7):",
                 "choose a color",
                 "new size for ",
                 "new image for ",
                 "new shape for ",
                 "set filling of ",
                };
Image bgImage=null;
String bgName;
ImageReader ir=this;
boolean modified=false;
final static int prefSize=400;
int size=prefSize;
int operation=0;
boolean usePopUp=true, editing=true, sliding=true;
PopupMenu mnPointPU;
MenuItem mnRemPU,mnUnlinkPU,mnMinLPU,mnMinPPU,mnRenPU,mnColSetPU,mnWeightPU,mnZoomPU,mnSizePU,mnImagePU,mnShapePU,mnFillPU;
MouseEvent lastEvent;
String primo;

/******
 * Crea un GraphPanel di un grafo vuoto
 *******/
public GraphPanel()
  {
  this(new Graph());
  }
/******
 * Crea un GraphPanel di un grafo vuoto con una immagine di sfondo
 * @param bg l'immagine di sfondo
 *******/
public GraphPanel(Image bg, String bgn)
  {
  this(new Graph(),bg,bgn);
  }
/******
 * Crea un GraphPanel di un grafo dato
 * @param gr il grafo
 *******/
public GraphPanel(Graph gr)
  {
  this(gr,null,null);
  }
/******
 * Crea un GraphPanel di un grafo dato con una immagine di sfondo
 * @param gr il grafo
 * @param bg l'immagine di sfondo
 * @param bgn il nome dell'immagine di sfondo
 *******/
public GraphPanel(Graph gr, Image bg, String bgn)
  {
  g=gr;
  bgImage=bg;
  bgName=bgn;
  setGraph(g);
  setListeners();
  }
/******
 * Crea un GraphPanel prelevando dal Reader r la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * In seguito, se c'e' un'altra riga, contiene una terna  di valori separati da virgola per ogni nodo
 * contenente il  valore del codice del colore in decimale e le coordinate x e y del nodo nel grafo.
 * Infine dopo puo' esserci una matrice contenente i valori dei colori degli archi.
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non puo' essere un numero intero
 * @param r il Reader da cui viene prelevato il grafo
 *******/
public GraphPanel (Reader r)
  {
  load(r);
  setListeners();
  }
/******
 * Crea un GraphPanel contente un grafo di cui viene fornita la matrice di adiacenza 
 * e la matrice contente i nomi dei nodi
 * @param name nomi dei nodi del grafo
 * @param matAd matrice di adiacenza del grafo
 ******/
public GraphPanel(String name[], int matAd[][])
  {
  this(name,matAd,null);
  }
/******
 * Crea un GraphPanel contenente un grafo di cui viene fornita la matrice di adiacenza,
 * la matrice contente i nomi dei nodi e
 * la matrice dei prezzi di ogni arco.
 * @param name nomi dei nodi del grafo
 * @param matAd matrice di adiacenza del grafo
 * @param p matrice dei prezzi di ogni arco
 *******/
public GraphPanel(String name[], int matAd[][], double p[][][])
  {
  this(name,null,matAd,p);
  }
/******
 * Crea un GraphPanel contenente un grafo di cui viene fornita la matrice di adiacenza,
 * la matrice contente i nomi dei nodi e
 * la matrice dei prezzi di ogni arco.
 * @param name nomi dei nodi del grafo
 * @param pos le coordinate dei nodi (primo indice orizzontale, secondo indice verticale)
 * @param matAd matrice di adiacenza del grafo
 * @param p matrice dei prezzi di ogni arco
 *******/
public GraphPanel(String name[], int pos[][], int matAd[][], double p[][][])
  {
  this(name,pos,matAd,p,Color.black);
  }
/******
 * Crea un GraphPanel contenente un grafo di cui viene fornita la matrice di adiacenza,
 * la matrice contente i nomi dei nodi e la matrice dei prezzi di ogni arco.
 * @param name nomi dei nodi del grafo
 * @param pos le coordinate dei nodi (primo indice orizzontale, secondo indice verticale)
 * @param matAd matrice di adiacenza del grafo
 * @param col il colore degli archi e dei nodi
 * @param p matrice dei prezzi di ogni arco
 *******/
public GraphPanel(String name[], int pos[][], int matAd[][], double p[][][], Color col)
  {
  setGraph(new Graph(name,matAd,p),pos,col);
  setListeners();
  }
/******
 * Crea un GraphPanel contenente un grafo di cui viene fornita la lista di coppie di nodi con i relativi costi,
 * @param couples le coppie di nomi dei nodi del grafo collegati da un arco
 * @param p matrice dei prezzi di ogni arco
 *******/
public GraphPanel(String couples[][], double p[])
  {
  this(couples,p,null,Color.black);
  }
/******
 * Crea un GraphPanel contenente un grafo di cui viene fornita la lista di coppie di nodi con i relativi costi,
 * @param couples le coppie di nomi dei nodi del grafo collegati da un arco
 * @param p matrice dei prezzi di ogni arco
 * @param col il colore degli archi e dei nodi
 *******/
public GraphPanel(String couples[][], double p[], Color col)
  {
  this(couples,p,null,col);
  }
/******
 * Crea un GraphPanel contenente un grafo di cui viene fornita la lista di coppie di nodi con i relativi costi,
 * @param couples le coppie di nomi dei nodi del grafo collegati da un arco
 * @param p matrice dei prezzi di ogni arco
 * @param pos le coordinate dei nodi (primo indice orizzontale, secondo indice verticale)
 * @param col il colore degli archi e dei nodi
 *******/
public GraphPanel(String couples[][], double p[], int pos[][], Color col)
  {
  setGraph(new Graph(couples,p),pos,col);
  setListeners();
  }
/******
 * Predispone i listeners necessari
 ******/
void setListeners()
  {
  mnPointPU=new PopupMenu("Point");
  mnRemPU=new MenuItem(voice[VDELN]);
  mnRemPU.addActionListener(this);
  mnPointPU.add(mnRemPU);
  mnUnlinkPU=new MenuItem(voice[VDELA]);
  mnUnlinkPU.addActionListener(this);
  mnPointPU.add(mnUnlinkPU);
  mnMinPPU=new MenuItem(voice[VMINP]);
  mnMinPPU.addActionListener(this);
  mnPointPU.add(mnMinPPU);
  mnMinLPU=new MenuItem(voice[VMINL]);
  mnMinLPU.addActionListener(this);
  mnPointPU.add(mnMinLPU);
  mnRenPU=new MenuItem(voice[VNAME]);
  mnRenPU.addActionListener(this);
  mnPointPU.add(mnRenPU);
  mnSizePU=new MenuItem(voice[VSIZE]);
  mnSizePU.addActionListener(this);
  mnPointPU.add(mnSizePU);
  mnShapePU=new MenuItem(voice[VSHAPE]);
  mnShapePU.addActionListener(this);
  mnPointPU.add(mnShapePU);
  mnFillPU=new MenuItem(voice[VFILL]);
  mnFillPU.addActionListener(this);
  mnPointPU.add(mnFillPU);
  mnImagePU=new MenuItem(voice[VIMAGE]);
  mnImagePU.addActionListener(this);
  mnPointPU.add(mnImagePU);
  mnColSetPU=new MenuItem(voice[VCOL]);
  mnColSetPU.addActionListener(this);
  mnPointPU.add(mnColSetPU);
  mnWeightPU=new MenuItem(voice[VWEI]);
  mnWeightPU.addActionListener(this);
  mnPointPU.add(mnWeightPU);
  mnZoomPU=new MenuItem(voice[VZOOM]);
  mnZoomPU.addActionListener(this);
  mnPointPU.add(mnZoomPU);
  add(mnPointPU);
  addMouseListener(this);
  addMouseMotionListener(this);
  }
/******
 * Sostituisce i testi nei menu e nelle finestre
 * @param v le voci del menu
 * @param p le frasi di prompt
 ******/
public void setLanguage(String v[],String p[])
  {
//  System.out.println("Language "+v.length+" "+voice.length);
  if (v!=null && v.length>=voice.length)
    {
    for (int i=0;i<voice.length;i++)
      voice[i]=v[i];
    mnRemPU.setLabel(voice[VDELN]);
    mnUnlinkPU.setLabel(voice[VDELA]);
    mnMinPPU.setLabel(voice[VMINP]);
    mnMinLPU.setLabel(voice[VMINL]);
    mnRenPU.setLabel(voice[VNAME]);
    mnColSetPU.setLabel(voice[VCOL]);
    mnWeightPU.setLabel(voice[VWEI]);
    mnZoomPU.setLabel(voice[VZOOM]);
    mnSizePU.setLabel(voice[VSIZE]);
    mnShapePU.setLabel(voice[VSHAPE]);
    mnFillPU.setLabel(voice[VFILL]);
    mnImagePU.setLabel(voice[VIMAGE]);
    }
  if (p!=null && p.length>=prompt.length)
    for (int i=0;i<prompt.length;i++)
      prompt[i]=p[i];
  }
/******
 * Abilita e disabilita scivolamento sul GraphPanel
 * @param pu <b>true</b> se si vuole abilitare lo scivolamento interattivo e <b>false</b> altrimenti
 ******/
public void setSliding(boolean pu)
  {
  sliding=pu;
  }
/******
 * Controlla se sono abilitate lo scivolamento sul GraphPanel
 * @return <b>true</b> se e' abilitato lo scivolamento interattivo, <b>false</b> altrimenti
 ******/
public boolean isSliding()
  {
  return sliding;
  }
/******
 * Abilita e disabilita le modifiche sul GraphPanel
 * @param pu <b>true</b> se si vuole abilitare le modifiche interattive e <b>false</b> altrimenti
 ******/
public void setEdit(boolean pu)
  {
  editing=pu;
  }
/******
 * Controlla se sono abilitate le modifiche sul GraphPanel
 * @return <b>true</b> se sono abilitate le modifiche interattive, <b>false</b> altrimenti
 ******/
public boolean isEdit()
  {
  return editing;
  }
/******
 * Abilita e disabilita il popup sul GraphPanel
 * @param pu <b>true</b> se si vuole abilitare il popup e <b>false</b> altrimenti
 ******/
public void setPopUp(boolean pu)
  {
  usePopUp=pu;
  }
/******
 * Controlla se e' abilitato il popup sul GraphPanel
 * @return <b>true</b> se il popup e' abilitato, <b>false</b> altrimenti
 ******/
public boolean isPopUp()
  {
  return usePopUp;
  }
/******
 * Segna come modificato il GraphPanel
 * @param mo <b>true</b> se si vuole segnare come modificato il GraphPanel  e <b>false</b> altrimenti
 ******/
public void setModified(boolean mo)
  {
  modified=mo;
  }
/******
 * Controlla se il GraphPanel e' stato modificato
 * @return <b>true</b> se il popup e' modificato, <b>false</b> altrimenti
 ******/
public boolean isModified()
  {
  return modified;
  }
/******
 * Ritorna la matrice di adiacenza dei collegamenti tra i nodi in un passo
 * @return la matrice dei collegamenti in un passo
 *******/
public double[][] getAdiacence()
  {
  return g.adiacence().getMatrix();
  }
/******
 * Ritorna la matrice dei collegamenti tra i nodi in un dato numero di passi
 * @param n il numero di passi
 * @return la matrice dei collegamenti in n passi
 *******/
public double[][] getConnection(int n)
  {
  return g.adiacence().pow(n).getMatrix();
  }
/******
 * Ritorna la matrice delle connessioni tra i nodi in un numero qualsiasi di  passi
 * @return la matrice dei collegamenti in un numero qualsiasi di passi inferiori al numero dei nodi
 *******/
public double[][] getConnections()
  {
  return g.connections().getMatrix();
  }
/******
 * Carica un grafo nel GraphPanel prelevando dal Reader r la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * In seguito, se c'e' un'altra riga, contiene una terna  di valori separati da virgola per ogni nodo
 *  contenente il  valore del codice del colore in decimale e le coordinate x e y del nodo nel grafo.
 * Infine dopo puo' esserci una matrice contenente i valori dei colori degli archi.
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param r il Reader da cui viene prelevato il grafo
 *******/
public void load(Reader r)
  {
  load(r,false);
  }
/******
 * Carica un grafo nel GraphPanel prelevando dal Reader r la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * In seguito, se c'e' un'altra riga, contiene una terna  di valori separati da virgola per ogni nodo
 *  contenente il  valore del codice del colore in decimale e le coordinate x e y del nodo nel grafo.
 * Infine dopo puo' esserci una matrice contenente i valori dei colori degli archi.
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param r il Reader da cui viene prelevato il grafo
 * @param merge <b>true</b> se il grafo va sovrapposto a quello esistente, <b>false</b> altrimenti
 *******/
public void load(Reader r, boolean merge)
  {
  BufferedReader br=new BufferedReader(r);
  NodeSign on[]=null;
  ArcSign oa[][]=null;
  if (merge)
    {
    g=new Graph(br,g);
    on=n;
    oa=a;
    }
   else
    g=new Graph(br);
  setGraph(g);
  String line=null;
  try
    {
    line=br.readLine();
    }
   catch (IOException ioe)
    {
    }
  if (line!=null)
    {
    StringTokenizer st=new StringTokenizer(line);
    for (int i=0;i<n.length && st.hasMoreTokens();i++)
      {
      if (merge)
        {
        for (;i<on.length;i++)
          {
          n[i]=on[i];
          }
        }
      try
        {
        StringTokenizer lineT=new StringTokenizer(st.nextToken(),",");
        Color col=new Color(parseInt(lineT.nextToken()));
        int x=Integer.parseInt(lineT.nextToken());
        int y=Integer.parseInt(lineT.nextToken());
        int dim=defaultSize;
        int shape=defaultShape;
        boolean fill=defaultFill;
        Image im=null;
        String imn=null;
        if (lineT.hasMoreTokens())
          {
          dim=parseInt(lineT.nextToken());
          }
        if (lineT.hasMoreTokens())
          {
          String last=lineT.nextToken();
          if (lineT.hasMoreTokens())
            {
            shape=parseInt(last);
            fill=(parseInt(lineT.nextToken()))==0?false:true;
            if (lineT.hasMoreTokens())
              last=lineT.nextToken();
            }
          if (ir!=null)
            {
            imn=last;
            im=ir.getImage(imn);
            }
          }
        n[i].setX(x);
        n[i].setY(y);
        n[i].setColor(col);
        n[i].setSize(dim);
        n[i].setShape(shape);
        n[i].setFilled(fill);
        n[i].setImage(imn,im);
        }
       catch (NumberFormatException nfe)
        {
        }
      }
    for (int i=0;i<a.length;i++)
      {
      try
        {
        line=br.readLine();
        }
       catch (IOException ioe)
        {
        break;
        }
      if (line!=null)
        {
        st=new StringTokenizer(line);
        if (merge)
          {
          for (;i<on.length;i++)
            {
            int j;
            for (j=0;j<on.length;j++)
              a[i][j]=oa[i][j];
            for (;j<n.length;j++)
              a[i][j]=null;
            }
          }
        for (int j=0;st.hasMoreTokens() && j<a[i].length;j++)
          {
          if (merge)
            {
            for (;j<on.length;j++)
              {
              a[i][j]=null;
              }
            }
          Arc ap=g.getArc(i,j);
          ArcSign last=null;
          a[i][j]=null;
          StringTokenizer vals=new StringTokenizer(st.nextToken(),"|");
          Color col=Color.black;
          for (;ap!=null;ap=ap.next)
            {
            if (vals.hasMoreTokens())
              col=new Color(parseInt(vals.nextToken()));
            if (last==null)
              {
              a[i][j]=new ArcSign(n[i],ap,n[j],col,null);
//            a[i][j]=new ArcSign(n[i],a[i][j].a,n[j],col,null);
              last=a[i][j];
              }
             else
              {
              last.next=new ArcSign(n[i],ap,n[j],col,null);
              last=last.next;
              }
            }
          }
        }
       else
        break;
      }
    try
      {
      line=null;
      line=br.readLine();
      }
     catch (IOException ioe)
      {
      }
    if (line!=null)
      {
      if (!merge || bgImage==null)
        setBackImage(line);
      }
     else
      setBackImage((Image)null);
    }
  if (merge)
    setModified(true);
   else
    setModified(false);
  }
/******
 * converte una stringa contenente un valore intero nelle diverse basi
 *
 * @param val la stringa contenente il valore intero
 * @return il valore convertito in intero
 ******/
private int parseInt(String val)
  {
  try
    {
    int ris;
  //  System.out.print("<"+val+">");
    if (val.startsWith("0x"))
      ris=Integer.parseInt(val.substring(2),0x10);
     else if (val.startsWith("0b"))
      ris=Integer.parseInt(val.substring(2),0b10);
     else if (val.startsWith("0"))
      ris=Integer.parseInt(val,010);
     else
      ris=Integer.parseInt(val);
    return ris;
    }
   catch (NumberFormatException nfe)
    {
    return 0;
    }
  }
/******
 *Salva  la matrice di adiacenza del grafo nel GraphPanel su un Writer  in un formato che
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente c'e' la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * In seguito c'e' una riga che contiene una terna  di valori separati da virgola per ogni nodo
 * contenente il  valore del codice del colore in decimale e le coordinate x e y del nodo nel grafo.
 * Infine dopo c'e' una matrice contenente i valori dei colori degli archi.
 * @param w il Writer su cui viene salvato il grafo
 *******/
public void save(Writer w)
  {
  g.save(w);
  PrintWriter pw=new PrintWriter(w);
  for (int i=0;i<a.length;i++)
    {
    if (n[i].getImageName()==null)
      pw.format("0x%x,%d,%d,%d,%d,%d ",n[i].getColor().getRGB()&0xffffff,n[i].getX(),n[i].getY(),n[i].getSize(),n[i].getShape(),(n[i].isFilled())?1:0);
//    pw.format("0x%x,%d,%d,%d ",n[i].getColor().getRGB()&0xffffff,n[i].getX(),n[i].getY(),n[i].getSize());
//    pw.print((n[i].getColor().getRGB()&0xffffff)+","+n[i].getX()+","+n[i].getY()+","+n[i].getSize()+" ");
     else
      pw.format("0x%x,%d,%d,%d,%d,%d,%s ",n[i].getColor().getRGB()&0xffffff,n[i].getX(),n[i].getY(),n[i].getSize(),n[i].getShape(),(n[i].isFilled())?1:0,n[i].getImageName());
//      pw.format("0x%x,%d,%d,%d,%s ",n[i].getColor().getRGB()&0xffffff,n[i].getX(),n[i].getY(),n[i].getSize(),n[i].getImageName());
//    pw.print((n[i].getColor().getRGB()&0xffffff)+","+n[i].getX()+","+n[i].getY()+","+n[i].getSize()+","+n[i].getImageName()+" ");
    }
  pw.println();
  for (int i=0;i<a.length;i++)
    {
    for (int j=0;j<a[i].length;j++)
      {
      if (a[i][j]!=null)
        {
        pw.format("0x%x",a[i][j].getColor().getRGB()&0xffffff);
//        pw.print(""+a[i][j].getColor().getRGB()&0xffffff);
        for (ArcSign asp=a[i][j].next;asp!=null;asp=asp.next)
          pw.format("|0x%x",asp.getColor().getRGB()&0xffffff);
//        pw.print("|"+(asp.getColor().getRGB()&0xffffff));
        pw.print(" ");
        }
       else
        pw.print("0 ");
      }
    pw.println();
    }
  if (bgName!=null)
    pw.println(bgName);
  pw.flush();
  setModified(false);
  }
/********
 * Cancella il grafo nel GraphPanel
 ********/
public void clear()
  {
  clear(true);
  }
/********
 * Cancella il grafo nel GraphPanel
 * @param erase <b>true></b> se deve essere cancellato anche lo sfondo, <b>false</b> altrimenti
 ********/
public void clear(boolean erase)
  {
  if (erase)
    {
    bgImage=null;
    bgName=null;
    }
  g=new Graph();
  setGraph(g);
  setModified(false);
  }
/******
 * Inizializza il GraphPanel con un grafo
 * @param name nomi dei nodi del grafo
 * @param matAd matrice di adiacenza del grafo
 *******/
public void setGraph(String name[], int matAd[][])
  {
  setGraph(name, matAd, null);
  }
/******
 * Inizializza il GraphPanel con un grafo pesato
 * @param name nomi dei nodi del grafo
 * @param matAd matrice di adiacenza del grafo
 * @param prices matrice dei prezzi di ogni arco
 *******/
public void setGraph(String name[], int matAd[][], double prices[][][])
  {
  g=new Graph(name,matAd,prices);
  setGraph(g);
  }
/*******
 * Visualizza un grafo sul GraphPanel
 * @param g il grafo da visualizzare
 *******/
public void setGraph(Graph g)
  {
  setGraph(g,null);
  }
/*******
 * Visualizza un grafo sul GraphPanel
 * @param g il grafo da visualizzare
 * @param c le coordinate dei nodi (primo indice orizzontale, secondo indice verticale)
 *******/
public void setGraph(Graph g, int c[][])
  {
  setGraph(g,c,Color.black);
  }
/*******
 * Visualizza un grafo sul GraphPanel
 * @param g il grafo da visualizzare
 * @param c le coordinate dei nodi (primo indice orizzontale, secondo indice verticale)
 * @param col il colore dei nodi e degli archi
 *******/
public void setGraph(Graph g, int c[][], Color col)
  {
  this.g=g;
  n=new NodeSign[g.degree()];
  if (c==null || c.length<n.length)
    {
    c=new int[g.degree()][2];
    double deg=0;
    if (n.length!=0)
      deg=2*Math.PI/n.length;
  //  size=(getSize().width>getSize().height)?getSize().width:getSize().height;
    int r=size/2-defaultSize;
    for (int i=0;i<n.length;i++)
      {
      int x0=(int)(defaultSize+r+r*Math.cos(i*deg));
      int y0=(int)(defaultSize+r+r*Math.sin(i*deg));
      c[i][0]=x0;
      c[i][1]=y0;
      }
    }
  for (int i=0;i<n.length;i++)
    {
    n[i]=new NodeSign(g.getNode(i),c[i][0],c[i][1],col,defaultSize);
    }
  a=new ArcSign[n.length][n.length];
  for (int i=0;i<n.length;i++)
    for (int j=0;j<n.length;j++)
      {
      Arc ap=g.getArc(i,j);
      ArcSign last=null;
      a[i][j]=null;
      for (;ap!=null;ap=ap.next)
        if (last==null)
          {
          a[i][j]=new ArcSign(n[i],ap,n[j],col,null);
          last=a[i][j];
          }
         else
          {
          last.next=new ArcSign(n[i],ap,n[j],col,null);
          last=last.next;
          }
      }
  repaint();
  }

/***********
 * Controlla se i due indici di nodi sono validi per il grafo
 * @param i la riga dell'arco
 * @param j la colonna dell'arco
 * @return <b>true</b> se le coordinate dell'arco sono valide, <b>false</b> altrimenti
 ************/
public boolean coordOk(int i, int j)
  {
  return i>=0 && j>=0 && i<n.length && j<n.length;
  }

/***********
 * Ritorna la dimensione preferita del GraphPanel
 * @return la dimensione preferita del GraphPanel
 ************/
public Dimension getPreferredSize()
  {
  return new Dimension(prefSize+defaultSize,prefSize+defaultSize);
  }

/***********
 * Modifica la dimensione del GraphPanel
 ************/
public void setSize(int w, int h)
  {
  super.setSize(w,h);
//  System.out.println("resizing1 "+getSize().width+" "+getSize().height);
  size=(getSize().width>getSize().height)?getSize().width:getSize().height;
  }
 
/***********
 * Modifica la dimensione del GraphPanel
 ************/
public void setSize(Dimension d)
  {
  super.setSize(d);
//  System.out.println("resizing2 "+getSize().width+" "+getSize().height);
  size=(getSize().width>getSize().height)?getSize().width:getSize().height;
  }

/********
 * Ritorna l'indice del nodo dato il nome
 * @param name nome del nodo
 * @return l'indice del nodo nel grafo, -1 se non esiste nodo con il nome dato
 ********/
public int findNode(String name)
  {
  return g.findNode(name);
  }

/********
 * Disegna un cammino nel colore indicato
 * @param c il colore del percorso
 * @param steps l'elenco dei nodi che costituiscono il percorso
 ********/
public void drawPath(Color c, String steps[])
  {
  if (steps==null) return;
  int stp[]=new int[steps.length];
  for (int i=0;i<steps.length;i++)
    {
    stp[i]=findNode(steps[i]);
    }
  drawPath(c,stp);
  }

/********
 * Disegna un cammino nel colore indicato
 * @param c il colore del percorso
 * @param steps l'elenco degli indici dei nodi che costituiscono il percorso
 ********/
public void drawPath(Color c, int steps[])
  {
  if (steps==null) return;
  for (int i=0;i<steps.length-1;i++)
    {
    int im=steps[i];
    int it=steps[i+1];
    if (coordOk(im,it))
      if (a[im][it]!=null)
        for (ArcSign asp=a[im][it];asp!=null;asp=asp.next)
          asp.setColor(c);
       else
        a[im][it]=new ArcSign(n[im],g.addArc(n[im].nn.name,n[it].nn.name),n[it],c,null);
     else
      break;
    }
  repaint();
  setModified(true);
  }

/********
 * Modifica la dimensione di un nodo nel grafo
 * @param name il vecchio nome del nodo
 * @param newSize la nuova dimensione del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ********/
public boolean resizeNode(String name,int newSize)
  {
  int res;
  defaultSize=newSize;
  if ((res=g.findNode(name))>=0)
    {
    if (n[res].getSize()!=newSize)
      {
      n[res].setSize(newSize);
      setModified(true);
      repaint();
      return true;
      }
    }
  return false;
  }

/********
 * Modifica la forma di un nodo nel grafo
 * @param name il vecchio nome del nodo
 * @param newShape la nuova forma del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ********/
public boolean reshapeNode(String name,int newShape)
  {
  int res;
  defaultShape=newShape;
  if ((res=g.findNode(name))>=0)
    {
    if (n[res].getShape()!=newShape)
      {
      n[res].setShape(newShape);
      setModified(true);
      repaint();
      return true;
      }
    }
  return false;
  }

/********
 * Modifica il riempimento di un nodo nel grafo
 * @param name il vecchio nome del nodo
 * @param newFill la nuova dimensione del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ********/
public boolean changeFillNode(String name,boolean newFill)
  {
  int res;
  defaultFill=newFill;
  if ((res=g.findNode(name))>=0)
    {
    if (n[res].isFilled()!=newFill)
      {
      n[res].setFilled(newFill);
      setModified(true);
      repaint();
      return true;
      }
    }
  return false;
  }

/********
 * Modifica l'immagine di un nodo nel grafo
 * @param name il nome del nodo
 * @param image la nuova immagine del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ********/
public boolean changeNodeImage(String name,String image)
  {
  boolean res=false;
  int ind=-1;
  defaultImage=image;
  if ((ind=findNode(name))>=0)
    {
    if (image==null && n[ind].getImageName()!=null)
      {
      n[ind].setImage(null,null);
      setModified(true);
      repaint();
      res=true;
      }
     else if(image!=null && !image.equals(n[ind].getImageName()))
      {
      n[ind].setImage(image,getImage(image));
      setModified(true);
      repaint();
      res=true;
      }
    }
  return res;
  }

/********
 * Rinomina un nodo nel grafo
 * @param name il vecchio nome del nodo
 * @param newName il nuovo nome del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ********/
public boolean renameNode(String name,String newName)
  {
  boolean res;
  if (res=g.renameNode(name,newName))
    {
    setModified(true);
    repaint();
    }
  return res;
  }

/*****************
 * Rimuove il nodo selezionato dal grafo 
 * @param x l'ascissa del punto di selezione
 * @param y l'ordinata del punto di selezione
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ******************/
public boolean removeNode(int x,int y)
  {
  String n=getNodeName(x,y);
  if (n!=null)
    {
    removeNode(n);
    return true;
    }
   else
    return false;
  }

/********
 * Rimuove un nodo col dato nome dal grafo
 * @param name nome del nodo da eliminare
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ********/
public boolean removeNode(String name)
  {
//  System.out.println("removing "+name);
  int pos=-1;
  pos=findNode(name);
  if (g.removeNode(name))
    {
    int i,k;
    NodeSign nn[]=new NodeSign[n.length-1];
    ArcSign aa[][]=new ArcSign[n.length-1][n.length-1];
    for (i=0,k=0;i<n.length;i++)
      {
      if (i==pos)
        continue;
      if (k==nn.length) break;
      nn[k]=n[i];
      int j,h;
      for (j=0,h=0;j<n.length;j++)
        {
        if (j==pos)
          continue;
        if (h==nn.length) break;
        aa[k][h]=a[i][j];
        h++;
        }
      k++;
      }
    repaint();
    n=nn;
    a=aa;
    setModified(true);
//    System.out.println("removed "+name);
    return true;
    }
   else
    return false;
  }

/*********
 * Aggiunge un nodo col dato nome al grafo
 * @param name nome del nodo da aggiungere
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name)
  {
  return addNode(name,defaultColor);
  }

/*********
 * Aggiunge un nodo col dato nome al grafo di un certo colore
 * @param name nome del nodo da aggiungere
 * @param c colore del nodo
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name,Color c)
  {
  int xn=size/2,yn=size/2;
  for (int i=0;i<n.length;i++)
    {
    if (Math.abs(n[i].x-xn)<defaultSize && Math.abs(n[i].y-yn)<defaultSize)
      {
      if (xn!=n[i].x)
        xn+=(xn-n[i].x)*2*defaultSize/Math.abs(xn-n[i].x);
       else
        xn+=2*defaultSize;
      if (yn!=n[i].y)
        yn-=(yn-n[i].y)*2*defaultSize/Math.abs(yn-n[i].y);
       else
        yn-=2*defaultSize;
      if (xn<0) xn=0;
      if (yn<0) yn=0;
      if (xn>=size) xn=size;
      if (yn>=size) yn=size;
      }
    }
  return addNode(name,xn,yn,c);
  }

/*********
 * Aggiunge un nodo col dato nome al grafo in una data posizione
 * @param name nome del nodo da aggiungere
 * @param x la coordinata x del nodo
 * @param y la coordinata y del nodo
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name, int x, int y)
  {
  return addNode(name,x,y,Color.black);
  }

/*********
 * Aggiunge un nodo col dato nome al grafo di un certo colore in una data posizione
 * @param name nome del nodo da aggiungere
 * @param x la coordinata x del nodo
 * @param y la coordinata y del nodo
 * @param c colore del nodo
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name, int x, int y, Color c)
  {
  return addNode(name,x,y,c,defaultSize);
  }

/*********
 * Aggiunge un nodo col dato nome al grafo di un certo colore in una data posizione
 * @param name nome del nodo da aggiungere
 * @param x la coordinata x del nodo
 * @param y la coordinata y del nodo
 * @param c colore del nodo
 * @param dim dimensione del nodo
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name, int x, int y, Color c,int dim)
  {
  return addNode(name,x,y,c,dim,null);
  }

/*********
 * Aggiunge un nodo col dato nome al grafo di un certo colore in una data posizione
 * @param name nome del nodo da aggiungere
 * @param x la coordinata x del nodo
 * @param y la coordinata y del nodo
 * @param c colore del nodo
 * @param dim dimensione del nodo
 * @param image immagine di sfondo del nodo
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name,int x,int y,Color c,int dim,String image)
  {
  return addNode(name,x,y,c,dim,image,defaultShape,defaultFill);
  }
/*********
 * Aggiunge un nodo col dato nome al grafo di un certo colore in una data posizione
 * @param name nome del nodo da aggiungere
 * @param x la coordinata x del nodo
 * @param y la coordinata y del nodo
 * @param c colore del nodo
 * @param dim dimensione del nodo
 * @param shape forma del nodo
 * @param filled <b>true</b> se il nodo è pieno, <b<false</b> se il nodo è vuoto
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name,int x,int y,Color c,int dim,int shape,boolean filled)
  {
  return addNode(name,x,y,c,dim,null,shape,filled);
  }
/*********
 * Aggiunge un nodo col dato nome al grafo di un certo colore in una data posizione
 * @param name nome del nodo da aggiungere
 * @param x la coordinata x del nodo
 * @param y la coordinata y del nodo
 * @param c colore del nodo
 * @param dim dimensione del nodo
 * @param image immagine di sfondo del nodo, <b>null</b> se il nodo è una forma geometrica
 * @param shape forma del nodo
 * @param filled <b>true</b> se il nodo è pieno, <b<false</b> se il nodo è vuoto
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean addNode(String name,int x,int y,Color c,int dim,String image,int shape,boolean filled)
  {
  name=name.replace(' ','_');
  if (!name.equals("") && findNode(name)==-1 && g.addNode(name))
    {
    int i;
    NodeSign nn[]=new NodeSign[n.length+1];
    ArcSign aa[][]=new ArcSign[n.length+1][n.length+1];
    for (i=0;i<n.length;i++)
      {
      nn[i]=n[i];
      for (int j=0;j<n.length;j++)
        {
        aa[i][j]=a[i][j];
        }
      aa[i][n.length]=null;
      aa[n.length][i]=null;
      }
    aa[n.length][n.length]=null;
    if (ir!=null && image!=null)
      nn[n.length]=new NodeSign(g.getNode(name),x,y,c,dim,ir.getImage(image),image);
     else
      nn[n.length]=new NodeSign(g.getNode(name),x,y,c,dim,shape,filled);
    n=nn;
    a=aa;
    setModified(true);
    repaint();
    return true;
    }
   else
    return false;
  }

/*********
 * Aggiunge un nodo col dato nome al grafo di un certo colore in una data posizione
 * @param name nome del nodo di cui sostituire la rappresentazione
 * @param ns la nuova rappresentazione del nodo
 * @return true se il grafo e' cambiato, false altrimenti
 *********/
public boolean changeNode(String name,NodeSign ns)
  {
  name=name.replace(' ','_');
  int nn=findNode(name);
  if (nn!=-1)
    {
    n[nn]=ns;
    repaint();
    return true;
    }
   else
    return false;
  }

/*****************
 * Rimuove un arco dal grafo tra il nodo source e il nodo dest
 * @param source la sorgente dell'arco
 * @param dest la destinazione dell'arco
 * @return <b<true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ******************/
public boolean removeArc(String source, String dest)
  {
  int i=findNode(source),j=findNode(dest);
  if (i!=-1 && j!=-1 && g.removeArc(source,dest))
    {
    a[i][j]=null;
    setModified(true);
    repaint();
    return true;
    }
   else
    return false;
  }

/*****************
 * Rimuove l'arco selezionato dal grafo 
 * @param x l'ascissa del punto di selezione
 * @param y l'ordinata del punto di selezione
 * @return <b<true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ******************/
public boolean removeArc(int x,int y)
  {
  ArcSign as=getArc(x,y);
  if (as!=null)
    {
    int i=g.findNode(as.a.f),j=g.findNode(as.a.t);
    if (!g.removeArc(as.a))
      a[i][j]=null;
     else
      {
      ArcSign last=null;
      for (ArcSign asp=a[i][j];asp!=null;last=asp,asp=asp.next)
        if (asp.a==as.a)
          if (last==null)
            a[i][j]=asp.next;
           else
            last.next=asp.next;
      }
    setModified(true);
    repaint();
    return true;
    }
   else
    return false;
  }

/*****************
 * Aggiunge un arco al grafo tra il nodo sorg e il nodo dest
 * @param source la sorgente dell'arco
 * @param dest la destinazione dell'arco
 * @return <b<true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ******************/
public boolean addArc(String source, String dest)
  {
  return addArc(source,dest,defaultColor,defaultWeight);
  }

/*****************
 * Aggiunge un arco al grafo tra il nodo sorg e il nodo dest del colore indicato
 * @param source la sorgente dell'arco
 * @param dest la destinazione dell'arco
 * @param c il colore dell'arco
 * @return <b<true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ******************/
public boolean addArc(String source, String dest, Color c)
  {
  return addArc(source,dest,c,defaultWeight);
  }

/*****************
 * Aggiunge un arco al grafo tra il nodo sorg e il nodo dest con il costo indicato
 * @param source la sorgente dell'arco
 * @param dest la destinazione dell'arco
 * @param price il costo dell'arco
 * @return <b<true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 ******************/
public boolean addArc(String source, String dest, double price)
  {
  return addArc(source,dest,defaultColor,price);
  }

/*****************
 * Aggiunge un arco al grafo tra il nodo sorg e il nodo dest con il costo indicato del colore indicato
 * @param source la sorgente dell'arco
 * @param dest la destinazione dell'arco
 * @param c il colore dell'arco
 * @param price il costo dell'arco
 * @return true se il grafo e' cambiato, false altrimenti
 ******************/
public boolean addArc(String source, String dest, Color c, double price)
  {
  addNode(source);
  addNode(dest);
  int da=findNode(source),ad=findNode(dest);
  Arc newArc=g.addArc(source,dest,price);
  if (newArc!=null)
    {
    a[da][ad]=new ArcSign(n[da],newArc,n[ad],c,a[da][ad]);
    setModified(true);
    repaint();
    return true;
    }
   else
    return false;
  }

int dx=0,dy=0;
int oldx=0,oldy=0;
int posx=0,posy=0;

/*****************
 * Rimuove un nodo in seguito alla scelta da popup
 ******************/
void remNodePU()
  {
  String nn=getNodeName(lastEvent.getX(),lastEvent.getY());
  if (nn!=null)
    {
    removeNode(nn);
    setModified(true);
    }
  }

/*****************
 * Modifica lo zoom da popup
 ******************/
private void setZoomPU()
  {
  operation=ZOOM;
  doOperation(lastEvent);
  }

/*****************
 * Rinomina un nodo in seguito alla scelta da popup
 ******************/
private void renNodePU()
  {
  operation=REN;
  doOperation(lastEvent);
  }

/*****************
 * Rinomina un nodo in seguito alla scelta da popup
 ******************/
private void setSizePU()
  {
  operation=SIZE;
  doOperation(lastEvent);
  }

/*****************
 * Rinomina un nodo in seguito alla scelta da popup
 ******************/
private void setShapePU()
  {
  operation=SHAPE;
  doOperation(lastEvent);
  }

/*****************
 * Rinomina un nodo in seguito alla scelta da popup
 ******************/
private void setFillPU()
  {
  operation=FILL;
  doOperation(lastEvent);
  }

/*****************
 * Rinomina un nodo in seguito alla scelta da popup
 ******************/
private void setImagePU()
  {
  operation=IMAGE;
  doOperation(lastEvent);
  }

/*****************
 * Cancella un arco in seguito alla scelta da popup
 ******************/
void unlinkPU()
  {
  if (removeArc(lastEvent.getX(),lastEvent.getY()))
    setModified(true);
  }

/*****************
 * Evidenzia un percorso in seguito alla scelta da popup
 * @param length se e' <b>true</b> viene scelta la lunghezza altrimenti il peso
 ******************/
void searchPU(boolean length)
  {
  if (length)
    operation=MINL;
   else
    operation=MINP;
  doOperation(lastEvent);
  }

/*****************
 * Modifica il peso di un arco in seguito alla scelta da popup
 ******************/
void setWeightPU()
  {
  operation=WEIGHT;
  doOperation(lastEvent);
  }

/*****************
 * Modifica il colore di un arco o di un nodo in seguito alla scelta da popup
 ******************/
void setNodeArcColorPU()
  {
  operation=COLOR;
  doOperation(lastEvent);
  }

/*****************
 * Permette di selezionare un colore tramite un ColorPickPanel
 ******************/
private Color inputColor()
  {
  Component par=this.getParent();
  for (;!(par instanceof Frame);)
    {
//    System.out.println(par);
    par=par.getParent();
    }
  final Dialog d=new Dialog((Frame)par,prompt[COLOR],true);
  d.addWindowListener(new WindowAdapter()
    {
    public void windowClosing(WindowEvent we)
      {
      d.setVisible(false);
      }
    });
  ColorPickPanel cpp=new ColorPickPanel(defaultColor);
  d.add(cpp);
  d.setBounds(getBounds().x+getWidth()/2-100,getBounds().y+getHeight()/2,100,30);
  d.pack();
  d.setVisible(true);
  return cpp.getColor();
  }

/*****************
 * Permette l'input di una stringa tramite un dialog
 * @param t titolo del dialog
 ******************/
private String inputText(String t)
  {
  return inputText(t,"");
  }
/*****************
 * Permette l'input di una stringa tramite un dialog
 * @param t titolo del dialog
 * @param v valore iniziale del dialog
 * @return la stringa immessa, <b>null</b> se la stringa e' vuota
 ******************/
private String inputText(String t,String v)
  {
  return inputText(t,v,true);
  }
/*****************
 * Permette l'input di una stringa tramite un dialog
 * @param t titolo del dialog
 * @param v valore iniziale del dialog
 * @param selected <b>true</b> se il valore iniziale deve essere selezionato, <b>false</b> altrimenti
 * @return la stringa immessa, <b>null</b> se la stringa e' vuota
 ******************/
private String inputText(String t,String v,boolean selected)
  {
  Component par=this;
  for (;!(par instanceof Frame);)
    {
//    System.out.println(par);
    par=par.getParent();
    }
  final Dialog d=new Dialog((Frame)par,t,true);
  TextField text=new TextField(t.length()+5);
  text.setText(v);
  if (selected)
    {
    text.setSelectionStart(0);
    text.setSelectionEnd(text.getText().length());
    }
  text.addActionListener(new ActionListener()
    {
    public void actionPerformed(ActionEvent ae)
      {
      d.setVisible(false);
      }
    });
  d.addWindowListener(new WindowAdapter()
    {
    public void windowClosing(WindowEvent we)
      {
      d.setVisible(false);
      }
    });
  d.add(text);
  d.setBounds(getBounds().x+getWidth()/2-100,getBounds().y+getHeight()/2,100,30);
  d.pack();
  d.setVisible(true);
//  System.out.println("Passato dopo inputtext");
  String res=text.getText().trim();
  if ("".equals(res))
    return null;
   else
    return res;
  }

/*****************
 * Gestisce le azioni relative ai menu
 * @param ae l'evento da gestire
 ******************/
public void actionPerformed(ActionEvent ae)
  {
  if (ae.getSource()==mnMinLPU||ae.getSource()==mnMinPPU)
    searchPU(ae.getSource()==mnMinLPU);
   else if (ae.getSource()==mnRenPU)
    renNodePU();
   else if (ae.getSource()==mnUnlinkPU)
    unlinkPU();
   else if (ae.getSource()==mnColSetPU)
    setNodeArcColorPU();
   else if (ae.getSource()==mnRemPU)
    remNodePU();
   else if (ae.getSource()==mnWeightPU)
    setWeightPU();
   else if (ae.getSource()==mnZoomPU)
    setZoomPU();
   else if (ae.getSource()==mnSizePU)
    setSizePU();
   else if (ae.getSource()==mnShapePU)
    setShapePU();
   else if (ae.getSource()==mnFillPU)
    setFillPU();
   else if (ae.getSource()==mnImagePU)
    setImagePU();
  setColor(defaultColor);
  setWeight(defaultWeight);
  lastEvent=null;
  repaint();
  }

/*************
 * Gestisce l'evento mouse click se l'evento non e' stato gia' consumato
 * @param e l'evento del mouse
 *************/
public void mouseClicked(MouseEvent e)
  {
//  System.out.println("mouseclicked dx="+dx+" dy="+dy+" oldx="+oldx+" oldy="+oldy);
  if (e.isConsumed())
    return;
//  System.out.println("Click in GraphPanel");
  if (operation==0)
    {
    if ((e.getModifiers() & 
        (InputEvent.BUTTON3_MASK|InputEvent.BUTTON1_MASK)) == InputEvent.BUTTON3_MASK)
//    if (e.isPopupTrigger())  Non funziona in Windows perché viene rilevato solo al mouseReleased
      {  // click col tasto destro
      if (usePopUp && editing)
        {
        lastEvent=e;
        boolean selNode,selArc;
        selNode=getNode(e.getX(),e.getY())!=null;
        selArc=getArc(e.getX(),e.getY())!=null;
        if (selNode)
          {
          mnRemPU.setEnabled(true);
          mnRenPU.setEnabled(true);
          mnMinLPU.setEnabled(true);
          mnMinPPU.setEnabled(true);
//          mnSizePU.setEnabled(true); always enabled
//          mnImagePU.setEnabled(true); always enabled
          }
         else
          {
          mnRemPU.setEnabled(false);
          mnRenPU.setEnabled(false);
          mnMinLPU.setEnabled(false);
          mnMinPPU.setEnabled(false);
//          mnSizePU.setEnabled(false); always enabled
//          mnImagePU.setEnabled(false); always enabled
          }
        if (selArc)
          {
          mnUnlinkPU.setEnabled(true);
          mnWeightPU.setEnabled(true);
          }
         else
          {
          mnUnlinkPU.setEnabled(false);
          mnWeightPU.setEnabled(false);
          }
        if (selNode || selArc)
          mnPointPU.show(this,e.getX(),e.getY());
         else
          {
          mnWeightPU.setEnabled(true);
          mnRenPU.setEnabled(true);
          mnPointPU.show(this,e.getX(),e.getY());
          }
        }
       else if (editing)
        {
        removeNode(e.getX(),e.getY());
        removeArc(e.getX(),e.getY());
        }
      }
     else if (editing)
      {
      if (getNodeIndex(defaultName)<0)
        addNode(defaultName,dezoom(e.getX())-dx,dezoom(e.getY())-dy,defaultColor,defaultSize,defaultImage);
       else
        for (int nodeNum=0;true;nodeNum++)
          {
          if (getNodeIndex(defaultName+nodeNum)<0)
            {
            addNode(defaultName+nodeNum,dezoom(e.getX())-dx,dezoom(e.getY())-dy,defaultColor,defaultSize,defaultImage);
            break;
            }
          }
      }
    }
   else
    {
    doOperation(e);
    }
//  System.out.println("finito mouseClicked "+defaultColor);
  repaint();
  }

/*****************
 * Esegue le diverse operazioni in seguito al click del mouse o al completamento di un comando popup
 * @param e l'evento da gestire
 *****************/
void doOperation(MouseEvent e)
  {
  String p[]=null;
  String nome=null;
  switch (operation)
    {
    case 0:
      break;
    case SIZE:
      nome=getNodeName(e.getX(),e.getY());
      if (nome!=null)
        {
        NodeSign ns=getNode(e.getX(),e.getY());
        String nn=inputText(prompt[SIZE]+nome,ns.getSize()+"",true);
        if (nn!=null)
          {
          defaultSize=parseInt(nn);
          resizeNode(nome,defaultSize);
          }
        }
       else
        {
        String nn=inputText(prompt[SIZE],defaultSize+"",true);
        if (nn!=null)
          {
          defaultSize=parseInt(nn);
          }
        }
      e.consume();
      break;
    case SHAPE:
      nome=getNodeName(e.getX(),e.getY());
      if (nome!=null)
        {
        NodeSign ns=getNode(e.getX(),e.getY());
        String nn=inputText(prompt[SHAPE]+nome,ns.getShape()+"",true);
        if (nn!=null)
          {
          defaultShape=parseInt(nn);
          reshapeNode(nome,defaultShape);
          }
        }
       else
        {
        String type="circle";
        if (defaultShape==NodeSign.CIRCLE)
          type="circle";
         else if (defaultShape==NodeSign.SQUARE)
          type="square";
        String nn=inputText(prompt[SHAPE],type+"",true);
        if (nn!=null)
          {
          if (nn.equalsIgnoreCase("circle"))
            defaultShape=NodeSign.CIRCLE;
           else if (nn.equalsIgnoreCase("square"))
            defaultShape=NodeSign.SQUARE;
           else defaultShape=parseInt(nn);
          }
        }
      e.consume();
      break;
    case FILL:
      nome=getNodeName(e.getX(),e.getY());
      if (nome!=null)
        {
        NodeSign ns=getNode(e.getX(),e.getY());
        String nn=inputText(prompt[FILL]+nome,ns.isFilled()+"",true);
        if (nn!=null)
          {
          if (nn.equalsIgnoreCase("false") || nn.equalsIgnoreCase("no"))
            defaultFill=false;
           else if (nn.equalsIgnoreCase("true") || nn.equalsIgnoreCase("yes"))
            defaultFill=true;
           else defaultFill=(parseInt(nn)==0)?false:true;
          changeFillNode(nome,defaultFill);
          }
        }
       else
        {
        String nn=inputText(prompt[FILL],defaultFill+"",true);
        if (nn!=null)
          {
          defaultFill=(parseInt(nn)==0)?false:true;
          }
        }
      e.consume();
      break;
    case IMAGE:
      nome=getNodeName(e.getX(),e.getY());
      if (nome!=null)
        {
        NodeSign ns=getNode(e.getX(),e.getY());
        String nn=inputText(prompt[IMAGE]+nome,(ns.getImageName()!=null)?ns.getImageName():"",true);
        if (nn!=null)
          {
          if (nn.equals(""))
            nn=null;
          defaultImage=nn;
          changeNodeImage(nome,defaultImage);
          }
        }
       else
        {
        String nn=inputText(prompt[IMAGE],defaultImage,true);
        defaultImage=nn;
        }
      e.consume();
      break;
    case REN:
      nome=getNodeName(e.getX(),e.getY());
      if (nome!=null)
        {
        String nn=inputText(prompt[REN1]+nome,nome,true);
        if (nn!=null)
          {
          defaultName=nn;
          renameNode(nome,nn);
          }
        }
       else
        {
        String nn=inputText(prompt[NAME],defaultName,true);
        if (nn!=null)
          {
          defaultName=nn;
          }
        }
      e.consume();
      break;
    case MINL: case MINP:
      primo=getNodeName(e.getX(),e.getY());
      if (primo!=null)
        {
        switch(operation)
          {
          case MINL:
            operation=MINL1;
            break;
          case MINP:
            operation=MINP1;
            break;
          }
        }
      e.consume();
      break;
    case MINL1: case MINP1:
      String secondo=getNodeName(e.getX(),e.getY());
//      System.out.println("Primo nodo="+primo+", secondo="+secondo);
      p=null;
      switch (operation)
        {
        case MINP1:
          p=g.minPricePath(primo,secondo);
          break;
        case MINL1:
          p=g.minLengthPath(primo,secondo);
          break;
        }
//      for (int i=0;i<p.length;i++)
//        System.out.println(","+p[i]);
      if (p!=null)
        drawPath(defaultColor,p);
      operation=0;
      setModified(true);
      e.consume();
      break;
    case COLOR:
      NodeSign ns=getNode(e.getX(),e.getY());
      defaultColor=inputColor();
      if (ns!=null)
        {
        ns.setColor(defaultColor);
        setModified(true);
        }
      ArcSign as=getArc(e.getX(),e.getY());
      if (as!=null)
        {
        as.setColor(defaultColor);
        setModified(true);
        }
//      System.out.println("finito cambio colore "+defaultColor);
      e.consume();
      break;
    case WEIGHT:
      ArcSign as1=getArc(e.getX(),e.getY());
      if (as1!=null)
        {
        try
          {
          String nn=inputText(prompt[WEIGHT],""+as1.a.p,true);
          if (nn!=null)
            {
            defaultWeight=Double.parseDouble(nn.trim());
            as1.a.p=defaultWeight;
            }
          setModified(true);
          }
         catch (NumberFormatException nfe)
          {
          }
        }
       else
        {
        try
          {
          String nn=inputText(prompt[WEIGHT],""+defaultWeight,true);
          if (nn!=null)
            {
            defaultWeight=Double.parseDouble(nn.trim());
            }
          setModified(true);
          }
         catch (NumberFormatException nfe)
          {
          }
        }
      e.consume();
      break;
    case ZOOM:
      try
        {
        String nn=inputText(prompt[ZOOM],""+getZoom(),true);
        if (nn!=null)
          {
          setZoom(Integer.parseInt(nn.trim()));
          }
        }
       catch (NumberFormatException nfe)
        {
        }
      e.consume();
      break;
    }
  if (operation!=MINL1 && operation!=MINP1)
    {
    operation=0;
    }
  }

/*************
 * Gestisce l'evento mouse entered
 * @param e l'evento del mouse
 *************/
public void mouseEntered(MouseEvent e)
  {
//  System.out.println("mouseentered dx="+dx+" dy="+dy+" oldx="+oldx+" oldy="+oldy);
  }

/*************
 * Gestisce l'evento mouse exited
 * @param e l'evento del mouse
 *************/
public void mouseExited(MouseEvent e)
  {
//  System.out.println("mouseexited dx="+dx+" dy="+dy+" oldx="+oldx+" oldy="+oldy);
  }

/*************
 * Indice del nodo selezionato
 *************/
int selectedNode=-1;
/*************
 * <b>true</b> se viene evidenziato l'elastico col punto precedentemente clickato, <b>false</b>
 *************/
boolean lineDraw=false;
/*************
 * ascissa del punto di partenza dell'elastico
 *************/
int linePosX;
/*************
 * ordinata del punto di partenza dell'elastico
 *************/
int linePosY;

/*************
 * Gestisce l'evento mouse pressed se l'evento non e' stato gia' consumato
 * @param e l'evento del mouse
 *************/
public void mousePressed(MouseEvent e)
  {
  if (sliding)
    {
    oldx=dezoom(e.getX())-dx;
    oldy=dezoom(e.getY())-dy;
    posx=e.getX();
    posy=e.getY();
    }
//  System.out.println("mousepressed dx="+dx+" dy="+dy+" oldx="+oldx+" oldy="+oldy);
  if (!editing || e.isConsumed())
    return;
//  System.out.println("Mouse pressed in GraphPanel ");
  int i=getNodeIndex(e.getX(),e.getY());
  selectedNode=i;
  if (i>=0)
    {
    n[i].selected=true;
    if ((e.getModifiers() &
        (InputEvent.BUTTON3_MASK|InputEvent.BUTTON1_MASK)) == (InputEvent.BUTTON3_MASK))
      { // schiacciato col tasto destro
      linePosX=e.getX();
      linePosY=e.getY();
      }
    }
  repaint();
  }

/*************
 * Gestisce l'evento mouse released se l'evento non e' stato gia' consumato
 * @param e l'evento del mouse
 *************/
public void mouseReleased(MouseEvent e)
  {
//  System.out.println("mousereleased dx="+dx+" dy="+dy+" oldx="+oldx+" oldy="+oldy);
  if (!editing || e.isConsumed())
    return;
//  System.out.println("Mouse released in GraphPanel ");
  if (getNodeName(e.getX(),e.getY())!=null && lineDraw)
    addArc(n[selectedNode].nn.name,getNodeName(e.getX(),e.getY()));
  lineDraw=false;
  if (selectedNode!=-1)
    n[selectedNode].selected=false;
  selectedNode=-1;
  repaint();
  }

/*************
 * Gestisce l'evento mouse dragged se l'evento non e' stato gia' consumato
 * @param e l'evento del mouse
 *************/
public void mouseDragged(MouseEvent e)
  {
//  System.out.println("mousedragged dx="+dx+" dy="+dy+" oldx="+oldx+" oldy="+oldy);
  if (!sliding || e.isConsumed())
    return;
  if (selectedNode!=-1)
    {
    if ((e.getModifiers() &
        (InputEvent.BUTTON3_MASK|InputEvent.BUTTON1_MASK)) == (InputEvent.BUTTON3_MASK))
      { // col tasto destro viene segnato un nuovo arco
      lineDraw=true;
      linePosX=e.getX();
      linePosY=e.getY();
      }
     else
      {
      n[selectedNode].x=dezoom(e.getX())-dx;
      n[selectedNode].y=dezoom(e.getY())-dy;
      }
    }
   else
    {
    if ((e.getModifiers() &
        (InputEvent.BUTTON3_MASK|InputEvent.BUTTON1_MASK)) == (InputEvent.BUTTON3_MASK))
      {
      setZoom((posy-e.getY())/5);
//      System.out.print("x="+posx+","+e.getX()+" y="+posy+","+e.getY());
//      System.out.println(" zoom="+getZoom()+" diffx="+(posx-e.getX())+" diffy="+(posy-e.getY()));
      }
     else
      {
      int newx=dezoom(e.getX()),newy=dezoom(e.getY());
      dx=(newx-oldx);
      dy=(newy-oldy);
//      System.out.println("mousedragged oldx="+oldx+",oldy="+oldy+",newx="+newx+",newy="+newy+"dx="+dx+",dy="+dy);
      }
    }
  repaint();
  }

/*************
 * Gestisce l'evento mouse moved
 * @param e l'evento del mouse
 *************/
public void mouseMoved(MouseEvent e)
  {
//  System.out.println("mousemoved dx="+dx+" dy="+dy+" oldx="+oldx+" oldy="+oldy);
  }

/*************
 * Ritorna il grafo rappresentato nel GraphPanel
 * @return il grafo
 *************/
public Graph getGraph()
  {
  return g;
  }

/*************
 * Ritorna il nome di un nodo indicato con le sue coordinate sul grafo
 * @param x l'ascissa sul grafo
 * @param y l'ordinata sul grafo
 * @return il nome del nodo
 *************/
public String getNodeName(int x, int y)
  {
  int i=getNodeIndex(x,y);
  if (i<0)
    return null;
   else
    return n[i].nn.name;
  }

/*************
 * Ritorna un nodo indicato con il suo indice
 * @param i l'indice del nodo
 * @return l'indice del nodo, null se il nodo non esiste
 *************/
public NodeSign getNode(int i)
  {
  return (i<0 || i>=n.length)?null:n[i];
  }

/*************
 * Ritorna un nodo indicato con il suo nome
 * @param s il nome
 * @return l'indice del nodo, null se il nodo non esiste
 *************/
public NodeSign getNode(String s)
  {
  int i=getNodeIndex(s);
  return (i<0)?null:n[i];
  }

/*************
 * Ritorna un nodo indicato con le sue coordinate sul grafo
 * @param x l'ascissa sul grafo
 * @param y l'ordinata sul grafo
 * @return il nodo alla posizione indicata, null se non esiste alcun nodo in tale posizione
 *************/
public NodeSign getNode(int x, int y)
  {
  int i=getNodeIndex(x,y);
  if (i<0)
    return null;
   else
    return n[i];
  }

/*************
 * Ritorna l'indice di un nodo indicato con il suo nome
 * @param s il nome
 * @return l'indice del nodo, -1 se il nodo non esiste
 *************/
public int getNodeIndex(String s)
  {
  for (int i=0;i<n.length;i++)
    if (n[i].nn.name.equals(s))
      {
      return i;
      }
  return -1;
  }
  
/*************
 * Ritorna l'indice di un nodo indicato con le sue coordinate sul grafo
 * @param x l'ascissa sul grafo
 * @param y l'ordinata sul grafo
 * @return l'indice del nodo, -1 se il nodo non esiste
 *************/
public int getNodeIndex(int x, int y)
  {
  for (int i=0;i<n.length;i++)
    if (Math.abs(dezoom(x)-dx-n[i].x)<n[i].dim &&
        Math.abs(dezoom(y)-dy-n[i].y)<n[i].dim)
      {
      return i;
      }
  return -1;
  }  

/*************
 * Ritorna la matrice dei nodi presenti nel GraphPanel
 * @return la matrice dei nodi
 *************/
public NodeSign[] getNodes()
  {
  return n;
  }

/*************
 * Ritorna l'arco tra due nodi, se presente
 * @param n1 il nodo di partenza
 * @param n2 il nodo di arrivo
 * @return l'arco tra i due nodi, null se non esiste alcun arco
 *************/
public ArcSign getArc(NodeSign n1, NodeSign n2)
  {
  int ni1=getNodeIndex(n1.getName());
  int ni2=getNodeIndex(n2.getName());
  return a[ni1][ni2];
  }

/*************
 * Ritorna l'arco indicato con le sue coordinate sul grafo
 * @param x l'ascissa sul grafo
 * @param y l'ordinata sul grafo
 * @return l'arco alla posizione indicata, null se non esiste alcun arco in tale posizione
 *************/
public ArcSign getArc(int x,int y)
  {
  for (int i=0;i<a.length;i++)
    for (int j=0;j<a[i].length;j++)
      {
      if (a[i][j]!=null)
        if (i!=j)
          {
          NodeSign f=a[i][j].from;
          NodeSign t=a[i][j].to;
          double x0=f.x,y0=f.y,
              x1=t.x,y1=t.y,
              xp=dezoom(x)-dx,yp=dezoom(y)-dy;
          if ((((x1-2*xp+x0)*(x1-2*xp+x0)/4)+((y1-2*yp+y0)*(y1-2*yp+y0)/4))-
              (((x1-x0)/2)*((x1-x0)/2)+((y1-y0)/2)*((y1-y0)/2))<0 &&
              (Math.abs((y1-y0)*(xp-x0)-(x1-x0)*(yp-y0))<10*f.dim*t.dim))
            return a[i][j];
          }
         else
          {
          NodeSign f=a[i][j].from;
          int xp=dezoom(x)-dx,yp=dezoom(y)-dy;
          if (Math.abs(xp-f.x-f.dim)<f.dim &&
              Math.abs(yp-f.y-f.dim)<f.dim)
            return a[i][j];
          }
      }
  return null;
  }

/*************
 * Ritorna la matrice degli archi presenti nel GraphPanel
 * @return la matrice degli archi
 *************/
public ArcSign[][] getArcs()
  {
  return a;
  }

/*************
 * Stabilisce se visualizzare gli assi nel grafo
 * @param show true se gli assi vanno visualizzati, false altrimenti
 *************/
public void setShowAxis(boolean show)
  {
  showAxis=show;
  repaint();
  }
  
/*************
 * Ritorna la visualizzazione degli assi del grafo
 * @return true se gli assi vanno visualizzati, false altrimenti
 *************/
public boolean getShowAxis()
  {
  return showAxis;
  }
  
/*************
 * Stabilisce se visualizzare i nomi dei nodi nel grafo
 * @param show true se le scritte vanno visualizzate, false altrimenti
 *************/
public void setShowNames(boolean show)
  {
  showNames=show;
  repaint();
  }
  
/*************
 * Ritorna la visualizzazione dei nomi dei nodi del grafo
 * @return true se le scritte vanno visualizzate, false altrimenti
 *************/
public boolean getShowNames()
  {
  return showNames;
  }
  
/*************
 * Stabilisce se visualizzare i pesi degli archi nel grafo
 * @param show true se le scritte vanno visualizzate, false altrimenti
 *************/
public void setShowWeights(boolean show)
  {
  showWeights=show;
  repaint();
  }
  
/*************
 * Ritorna la visualizzazione dei pesi degli archi del grafo
 * @return true se le scritte vanno visualizzate, false altrimenti
 *************/
public boolean getShowWeights()
  {
  return showWeights;
  }
  
/*************
 * Stabilisce se visualizzare le scritte nel grafo
 * @param show true se le scritte vanno visualizzate, false altrimenti
 *************/
public void setShowText(boolean show)
  {
  showNames=show;
  showWeights=show;
  repaint();
  }
  
/*************
 * Stabilisce la stringa di default
 * @param s la stringa di default
 + @deprecated substituted by setName
 *************/
@Deprecated
public void setString(String s)
  {
  setName(s);
  }
  
/*************
 * Stabilisce il nome di default
 * @param s la stringa di default
 *************/
public void setName(String s)
  {
  defaultName=s;
  }
  
/*************
 * Ritorna la stringa di default
 * @return la stringa di default
 * @deprecated substituted by getName
 *************/
@Deprecated
public String getString()
  {
  return getName();
  }

/*************
 * Ritorna il nome di default
 * @return il nome di default
 *************/
public String getName()
  {
  return defaultName;
  }
  
/*************
 * Stabilisce il peso di default
 * @param w il peso di default
 *************/
public void setWeight(double w)
  {
  defaultWeight=w;
  }
  
/*************
 * Ritorna il peso di default
 * @return il peso di default
 *************/
public double getWeight()
  {
  return defaultWeight;
  }
  
/*************
 * Stabilisce il colore di default
 * @param c il colore di default
 *************/
public void setColor(Color c)
  {
  defaultColor=c;
  }
  
/*************
 * Ritorna il colore di default
 * @return il colore di default
 *************/
public Color getColor()
  {
  return defaultColor;
  }
  
/*************
 * Stabilisce la dimensione dei nodi di default
 * @param dim la dimensione dei nodi di default
 *************/
public void setNodeSize(int dim)
  {
  defaultSize=dim;
  }
  
/*************
 * Stabilisce la forma dei nodi di default
 * @param sh la forma dei nodi di default
 *************/
public void setNodeShape(int sh)
  {
  defaultShape=sh;
  }
  
/*************
 * Stabilisce il rempimento di dafault dei nodi
 * @param fill il riempimento di default dei nodi
 *************/
public void setNodeFill(boolean fill)
  {
  defaultFill=fill;
  }
  
/*************
 * Ritorna la dimensione dei nodi di default
 * @return la dimensione dei nodi default
 *************/
public int getNodeSize()
  {
  return defaultSize;
  }
  
/*************
 * Ritorna la forma dei nodi di default
 * @return la forma dei nodi default
 *************/
public int getNodeShape()
  {
  return defaultShape;
  }
  
/*************
 * Ritorna il riempimento di default dei nodi
 * @return la riempimento di default dei nodi
 *************/
public boolean getNodeFill()
  {
  return defaultFill;
  }
  
/*************
 * Stabilisce l'immagine dei nodi di default
 * @param im l'immagine dei nodi di default
 *************/
public void setImageName(String im)
  {
  defaultImage=im;
  }
  
/*************
 * Ritorna l'immagine dei nodi di default
 * @return l'immagine dei nodi di default
 *************/
public String getImageName()
  {
  return defaultImage;
  }
  
/*************
 * Ingrandisce il grafo
 *************/
public void inZoom()
  {
  dx=zoom(dx);
  dy=zoom(dy);
  if (zoomRate<MAXZOOM)
    zoomRate++;
  defaultSize=20+5*zoomRate;
  dx=dezoom(dx);
  dy=dezoom(dy);
  repaint();
  }

/*************
 * Rimpicciolisce il grafo
 *************/
public void outZoom()
  {
  dx=zoom(dx);
  dy=zoom(dy);
  if (zoomRate>MINZOOM)
    zoomRate--;
  defaultSize=20+5*zoomRate;
  dx=dezoom(dx);
  dy=dezoom(dy);
  repaint();
  }

/*************
 * Stabilisce il grado di ingrandimento del grafo
 * @param z il grado di ingrandimento del grafo
 *************/
public void setZoom(int z)
  {
  zoomRate=(z<MINZOOM?MINZOOM:z>MAXZOOM?MAXZOOM:z);
  repaint();
  }

/*************
 * Ritorna il grado di ingrandimento del grafo
 * @return il grado di ingrandimento del grafo
 *************/
public int getZoom()
  {
  return zoomRate;
  }

/*************
 * Rimuove la zoommata dalla dimensione
 * @param v il valore da dezoommare
 * @return la dimensione alle coordinate assolute
 *************/
public int dezoom(int v)
  {
  return (int)(4.0*v/(4.0+zoomRate));
  }

/*************
 * Applica la zoommata alla dimensione
 * @param v il valore da zoommare
 * @return la dimensione all'ingrandimento attuale
 *************/
public int zoom(int v)
  {
  return (int)(v*(1+zoomRate/4.0));
  }

/*************
 * Ripristina la zoommata e l'origine
 *************/
public void reset()
  {
  dx=0;
  dy=0;
  setZoom(0);
  repaint();
  }

/*************
 * Stabilisce l'ImageReader da utilizzare per la lettura delle immagini
 * @param i l'ImageReader da utilizzare
 *************/
public void setImageReader(ImageReader i)
  {
  ir=i;
  }

/*************
 * Ritorna il nome dello sfondo 
 * @return il nome del file contenente l'immagine di sfondo
 *************/
public String getBackImage()
  {
  return bgName;
  }

/*************
 * Modifica lo sfondo 
 * @param i il nome del file contenente l'immagine di sfondo
 *************/
public void setBackImage(String i)
  {
  if (i!=null && !i.equals(getBackImage()))
    {
    bgImage=ir.getImage(i);
    setModified(true);
    }
   else if (i==null && getBackImage()!=null)
    setModified(true);
  bgName=i;
  if (bgName==null)
    bgImage=null;
  repaint();
  }

/*************
 * Modifica lo sfondo 
 * @param i l'immagine di sfondo
 * @deprecated versione 2.0 utilizzare la forma che conserva il nome dell'immagine per poterlo salvare
 *************/
@Deprecated
public void setBackImage(Image i)
  {
  bgImage=i;
  bgName=null;
  setModified(true);
  repaint();
  }

/*************
 * Ritorna una immagine. Implementa ImageReader
 * @param ims nome dell'immagine da ritornare
 * @return l'immagine letta
 *************/
public Image getImage(String ims)
  {
  Image im=null;
  try
    {
    if (new File(ims).exists())
      {
      im=Toolkit.getDefaultToolkit().getImage(ims);
      }
    }
   catch (AccessControlException ace)
    {
//    ace.printStackTrace();
    }
  if (im==null)
    {
    URL imUrl=getClass().getResource(ims);
//    System.out.println("Image "+ims+" in JAR is "+imUrl);
    if (imUrl!=null)
      im=Toolkit.getDefaultToolkit().getImage(imUrl);
    }
  if (im!=null)
    {
    MediaTracker mt=new MediaTracker(this);
    mt.addImage(im,0);
    try
      {
      mt.waitForAll();
      }
     catch(InterruptedException ie)
      {}
    }
  return im;
  }

/*************
 * Disegna il GraphPanel 
 * @param g lo spazio grafico di tracciamento
 *************/
public void update(Graphics g)
  {
  BufferedImage buffer=null;
  Graphics gbuf=null;
  buffer = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
  gbuf = buffer.getGraphics();
  paint(gbuf);
  g.drawImage(buffer,0,0,this);
  }

/*************
 * Disegna il GraphPanel 
 * @param g lo spazio grafico di tracciamento
 *************/
public void paint(Graphics g)
  {
  g.setFont(new Font(null,Font.PLAIN,10+zoomRate));
  g.setColor(getBackground());
  if (bgImage!=null)
    {
    if (zoom(dx)>0 || zoom(dy)>0 ||
        zoom(dx+bgImage.getWidth(this))<getSize().width ||
        zoom(dy+bgImage.getHeight(this))<getSize().height)
      {
      g.fillRect(0,0,getSize().width,getSize().height);
      }
    g.drawImage(bgImage,zoom(dx),zoom(dy),
                        zoom(bgImage.getWidth(this)),zoom(bgImage.getHeight(this)),this);
    }
   else
    g.fillRect(0,0,getSize().width,getSize().height);
  /* draws axis */
  if (showAxis)
	  {
	  g.setColor(Color.black);
	  g.drawLine(zoom(dx),0,zoom(dx),getSize().height);
	  g.drawLine(0,zoom(dy),getSize().width,zoom(dy));
	  }
  for (int i=0;i<n.length;i++)
    {
    n[i].paint(g,dx,dy,1+zoomRate/4.0,showNames);
    for (int j=0;j<n.length;j++)
      {
//      for (ArcSign asp=a[i][j];asp!=null;asp=asp.next)
//        System.out.println(asp);
      if (a[i][j]!=null)
        {
        a[i][j].paint(g,dx,dy,1+zoomRate/4.0,showWeights);
        }
      }
    }
  if (lineDraw)
    {
    g.setColor(Color.black);
    g.drawLine(zoom(dx)+zoom(n[selectedNode].x),zoom(dy)+zoom(n[selectedNode].y),linePosX,linePosY);
    }
  }
/************
 * Ritorna la stringa che descrive il grafo
 * @return la descrizione del grafo
 ************/
public String toString()
  {
  if (g!=null)
    return g.toString();
   else
    return null;
  }

/*************
 * Il frame che contiene il GraphPanel di prova
 *************/
static Frame f;

/*************
 * Il programma di prova
 * @param s i nomi dei file contenenti le descrizioni dei grafi. Se non ne sono indicati viene utilizzato un grafo di default
 *************/
public static void main(String s[])
  {
  GraphPanel gp=null;
  if (s.length==0)
    {
    String n[]={"A","B","C","D","E"};
    int v[][]={{1,3,0,1,0}
              ,{0,0,1,1,0}
              ,{0,1,0,0,1}
              ,{0,0,1,0,0}
              ,{2,0,0,0,0}
              };
    double p[][][]={{{12.37},{3.2,7.4,6.9},{0},{106.1},{0}}
                 ,{{0},{0},{7.1},{2.3},{0}}
                 ,{{0},{0},{0},{0},{3}}
                 ,{{0},{0},{4.211},{0},{0}}
                 ,{{0.73,10.3},{0},{0},{0},{0}}
                 };
    f=new Frame("default graph");
    f.addWindowListener(new WindowAdapter()
         {
         public void windowClosing(WindowEvent we)
           {
           f.setVisible(false);
           System.exit(0);
           }
         });
    f.add(gp=new GraphPanel(n,v,p),BorderLayout.CENTER);
    f.setSize(200,200);
    Button go=new Button("Adding node F");
    f.add(go,BorderLayout.SOUTH);
    ButtonChanger bc=new ButtonChanger(go,gp);
    go.addActionListener(bc);
    f.pack();
    f.setVisible(true);
    }
   else
    {
    for (int i=0;i<s.length;i++)
      {
      f=new Frame(s[i]+" graph");
      f.addWindowListener(new WindowAdapter()
           {
           public void windowClosing(WindowEvent we)
             {
             f.setVisible(false);
             System.exit(0);
             }
           });
      try
        {
        f.add(gp=new GraphPanel(new FileReader(s[i])),BorderLayout.CENTER);
        System.out.println(gp);
        System.out.println(gp.g);
        System.out.println(gp.g.connections());
        }
       catch(FileNotFoundException fnfe)
        {
        f.setTitle("Missing file "+s[i]);
        }
      f.setSize(200,200);
      Button go=new Button("Va avanti");
      f.add(go,BorderLayout.SOUTH);
      go.addActionListener(new ButtonChanger(go,gp));
      f.pack();
      f.setVisible(true);
      }
    }
  }
}

/*************
 * Il listener che  controlla il bottone del GraphPanel
 *************/
class ButtonChanger implements ActionListener
{
/*************
 * Il GraphPanel su cui agisce il bottone 
 *************/
GraphPanel gp;
/*************
 * Il bottone da gestire 
 *************/
Button f;
/*************
 * lo stato in cui si trova il GraphPanel di prova 
 *************/
int stato=0;
/*************
 * Crea un ButtonChanger
 * @param f il bottone da controllare
 * @param gp il GraphPanel associato
 *************/
public ButtonChanger(Button f,GraphPanel gp)
  {
  this.gp=gp;
  this.f=f;
  }

/*************
 * Il gestore dell'evento
 * @param ae l'evento da gestire 
 *************/
public void actionPerformed(ActionEvent ae)
  {
  String voiceEn[]={"",
                    "Del node",
                    "Del arc",
                    "Min price -> other node",
                    "Min length -> other node",
                    "Change name",
                    "Change color",
                    "Change weight",
                    "Change zoom",
                    "Change size",
                    "Change image",
                    "Change shape",
                    "Change filling",
                  };
  String voiceIt[]={"",
                    "Canc nodo",
                    "Canc arco",
                    "Min prezzo",
                    "Min lung",
                    "Cambia nome",
                    "Cambia colore",
                    "Cambia peso",
                    "Cambia zoom",
                    "Cambia dimensione",
                    "Cambia immagine",
                    "Cambia forma",
                    "Cambia riempimento",
                   };
  String promptEn[]={"",
                     "weight of arcs:",
                     "new name for ",
                     "node name:",
                     "zoom (-2..7):",
                     "choose a color",
                     "new size for ",
                     "new image for ",
                     "new shape for ",
                     "set filling of ",
                  };
  String promptIt[]={"",
                     "peso dell'arco:",
                     "nuovo nome per ",
                     "nome nodo:",
                     "zoom (-2..7):",
                     "scegli colore",
                     "nuova dimensione per ",
                     "nuova immagine per ",
                     "nuova forma per ",
                     "scegli riempimento di ",
                    };
  switch(stato)
    {
    case 0:
      System.out.println("Adding node F");
      f.setLabel("Adding arc A-F");
      gp.addNode("F");
      System.out.println(gp);
      System.out.println(gp.g.connections());
      stato++;
      break;
    case 1:
      System.out.println("Adding arc A-F");
      f.setLabel("Drawing path F-C-D PU false");
      gp.setPopUp(false);
      gp.addArc("A","F");
      System.out.println(gp);
      stato++;
      break;
    case 2:
      System.out.println("Drawing path F-C-D");
      f.setLabel("Finding min path A-E");
      gp.drawPath(Color.red,new String[]{"F","C","D"});
      gp.setPopUp(true);
      stato++;
      break;
    case 3:
      System.out.println("Finding min path A-E");
      f.setLabel("Finding short path A-E");
      String s[]=gp.g.minPricePath("A","E");
      System.out.println(gp);
      if (s!=null)
        {
        System.out.print("minPricePath ");
        for (int i=0;i<s.length;i++)
          System.out.print("\""+s[i]+"\":");
        System.out.println();
        gp.drawPath(Color.blue,s);
        }
       else
        System.out.println("minPricePath not connected");
      stato++;
      break;
    case 4:
      System.out.println("Finding short path A-E");
      f.setLabel("Adding arc F-E 61.5");
      String s1[]=gp.g.minLengthPath("A","E");
      if (s1!=null)
        {
        System.out.print("minLengthPath ");
        for (int i=0;i<s1.length;i++)
          System.out.print("\""+s1[i]+"\":");
        System.out.println();
        gp.drawPath(Color.green,s1);
        }
       else
        System.out.println("minLengthPath not connected");
      stato++;
      break;
    case 5:
      System.out.println("Adding arc F-E 61.5");
      f.setLabel("Finding min path A-E");
      gp.addArc("F","E",61.5);
      System.out.println(gp);
      stato++;
      break;
    case 6:
      System.out.println("Finding min path A-E");
      f.setLabel("Finding short path A-E - italiano");
      String s2[]=gp.g.minPricePath("A","E");
      if (s2!=null)
        {
        System.out.print("minPricePath ");
        for (int i=0;i<s2.length;i++)
          System.out.print("\""+s2[i]+"\":");
        System.out.println();
        gp.drawPath(Color.pink,s2);
        }
       else
        System.out.println("minPricePath not connected");
      stato++;
      break;
    case 7:
      System.out.println("Finding short path A-E - italiano");
      gp.setLanguage(voiceIt,promptIt);
      f.setLabel("Adding arc F E -1.5");
      String s3[]=gp.g.minLengthPath("A","E");
      if (s3!=null)
        {
        System.out.print("minLengthPath ");
        for (int i=0;i<s3.length;i++)
          System.out.print("\""+s3[i]+"\":");
        System.out.println();
        gp.drawPath(Color.cyan,s3);
        }
       else
        System.out.println("minLengthPath not connected");
      stato++;
      break;
    case 8:
      System.out.println("Adding arc F E -1.5");
      f.setLabel("Finding min path A-E");
      gp.addArc("F","E",-1.5);
      stato++;
      break;
    case 9:
      System.out.println("Finding min path A-E");
      f.setLabel("Remove arc A-B - english");
      String s4[]=gp.g.minPricePath("A","E");
      if (s4!=null)
        {
        System.out.print("minPricePath ");
        for (int i=0;i<s4.length;i++)
          System.out.print("\""+s4[i]+"\":");
        System.out.println();
        gp.drawPath(Color.yellow,s4);
        }
       else
        System.out.println("minLengthPath not connected");
      stato++;
      break;
    case 10:
      System.out.println("Remove arc A-B - english");
      gp.setLanguage(voiceEn,promptEn);
      f.setLabel("Removing node C");
      gp.removeArc("A","B");
      System.out.println(gp);
      stato++;
      break;
    case 11:
      System.out.println("Removing node C");
      f.setLabel("Adding node C with image");
      gp.removeNode("C");
      System.out.println(gp);
      stato++;
      break;
    case 12:
      System.out.println("Adding node C with image");
      f.setLabel("Adding node M with filled square");
      gp.addNode("C",120,100,Color.pink,30,"Nodo.GIF");
      System.out.println(gp);
      stato++;
      break;
    case 13:
      System.out.println("Adding node M with filled square");
      f.setLabel("Adding node N with filled circle");
      gp.addNode("M",20,40,new Color(0xff00fe),30,1,true);
      System.out.println(gp);
      stato++;
      break;
    case 14:
      System.out.println("Adding node N with filled circle");
      f.setLabel("Adding path M N A");
      gp.addNode("N",70,80,new Color(0xfcf0fe),20,0,true);
      System.out.println(gp);
      stato++;
      break;
    case 15:
      System.out.println("Adding path M N A");
      f.setLabel("Adding background image");
      gp.drawPath(new Color(0x0F000F),new String[]{"M","N","A"});
      System.out.println(gp);
      stato++;
      break;
    case 16:
      System.out.println("Adding background image");
      f.setLabel("Bye");
      gp.setBackImage("Italia.gif");
      System.out.println(gp);
      stato++;
      f.setEnabled(false);
      break;
    }
  }
}