import java.util.*;
import java.io.*;

/*****
 * Graph e' la rappresentazine di un grafo pesato
 * @version 2.1
 * 20/12/12
 *     eliminato spazi dai nomi dei nodi
 *     eliminato eventuali duplicazione di nomi dei nodi
 * 21/12/13
 *     aggiunta la creazione e tramite coppie di nodi
 *****/
public class Graph
{
/***************
 * L'elenco dei nodi
 ***************/
Node n[];
/***************
 * La matrice degli archi
 ***************/
Arc a[][];
/***************
 * La matrice delle connessioni
 ***************/
Matrix conn;
/***************
 * La matrice di adiacenza
 ***************/
Matrix adiac;
/********
 * crea un Graph vuoto
 *********/
public Graph()
  {
  this(new String[0],new int[0][0]);
  }
/********
 * crea un Graph fornendo la matrice di adiacenza del grafo e la matrice contente i nomi dei nodi
 * @param name la matrice dei nomi dei nodi
 * @param matAd la matrice di adiacenza del grafo in cui i le righe e le colonne hanno lo stesso ordine dei nomi
 *********/
public Graph(String name[], int matAd[][])
  {
  this(name,matAd,null);
  }
/********
 * crea un Graph fornendo la matrice di adiacenza del grafo pesato, la matrice dei pesi degli archi e la matrice contente i nomi dei nodi
 * @param name la matrice dei nomi dei nodi
 * @param matAd la matrice di adiacenza del grafo in cui le righe e le colonne hanno lo stesso ordine dei nomi
 * @param p la matrice dei pesi del grafo in cui i le righe e le colonne hanno lo stesso ordine dei nomi
 *********/
public Graph(String name[], int matAd[][], double p[][][])
  {
  setGraph(name,matAd,p);
  }
/********
 * crea un Graph partendo dall’elenco delle coppie di nodi
 * unite da un arco.
 * @param couples una matrice dove ogni riga è una coppia di nomi di nodi collegati tramite un arco
 ********/
public Graph(String couples[][])
  {
  this(couples,null);
  }
/********
 * crea un Graph partendo dall’elenco delle coppie di nodi
 * unite da un arco.
 * @param couples una matrice dove ogni riga è una coppia di nomi di nodi collegati tramite un arco
 * @param prices una matrice dove ogni elemento è il costo del corrispondente arco
 ********/
public Graph(String couples[][], double prices[])
  {
  setGraph(couples,prices);
  }
/******
 * Crea un Graph prelevando da un Reader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param r il Reader da cui viene prelevato il grafo
 *******/
public Graph (Reader r)
  {
  this(r,null);
  }
/******
 * Crea un Graph prelevando da un Reader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param r il Reader da cui viene prelevato il grafo
 * @param og il grafo da includere, <b>null</b> se un grafo nuovo
 *******/
public Graph (Reader r, Graph og)
  {
  BufferedReader br=new BufferedReader(r);
  load(br,og);
  }
/******
 * Crea un Graph prelevando da un BufferedReader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param br il BufferedReader da cui viene prelevato il grafo
 *******/
public Graph (BufferedReader br)
  {
  this(br,null);
  }
/******
 * Crea un Graph prelevando da un BufferedReader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param br il BufferedReader da cui viene prelevato il grafo
 * @param og il grafo da includere, <b>null</b> se un grafo nuovo
 *******/
public Graph (BufferedReader br, Graph og)
  {
  load(br,og);
  }
/******
 * Carica un Graph prelevando da un Reader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param r il Reader da cui viene prelevato il grafo
 *******/
public void load(Reader r)
  {
  load(r,null);
  }
/******
 * Carica un Graph prelevando da un Reader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param r il Reader da cui viene prelevato il grafo
 * @param og il grafo da includere, <b>null</b> se un grafo nuovo
 *******/
public void load(Reader r, Graph og)
  {
  BufferedReader br=new BufferedReader(r);
  load(br,og);
  }
/******
 * Carica un Graph prelevando da un BufferedReader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param br il BufferedReader da cui viene prelevato il grafo
 *******/
public void load(BufferedReader br)
  {
  load(br,null);
  }
/******
 * Carica un Graph prelevando da un BufferedReader la descrizione di un grafo in due formati. Nel primo il formato
 * prevede che la prima riga letta contenga il numero di nodi e la successiva riga contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente puo' esserci la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
 * Nel secondo formato invece sono elencati su ogni riga la coppia di nodi connessi da un arco ed eventualmente il costo dell'arco.
 * Il nome del primo nodo non può essere un numero intero
 * @param br il BufferedReader da cui viene prelevato il grafo
 * @param og il grafo da includere, <b>null</b> se un grafo nuovo
 *******/
public void load(BufferedReader br, Graph og)
  {
//  System.out.println((og==null?"not":"")+" merging");
  String nm[]=null;
  int am[][]=null;
  double pr[][][]=null;
  int i=0;
  try
    {
    String linea=br.readLine();
    StringTokenizer st=new StringTokenizer(linea);
    int ns=-1;
    String firstToken=st.nextToken();
    try
      {
      ns=Integer.parseInt(firstToken);
      }
     catch (NumberFormatException nfe)
      {  // first token not a number means list of couples of tokens
      String names[][]=new String[0][2];
      double prices[]=new double[0];
      do
        {
        String secondToken;
        if (st.hasMoreTokens())
          {
          String newNames[][]=new String[names.length+1][];
          double newPrices[]=new double[prices.length+1];
          for (int in=0;in<names.length;in++)
            {
            newNames[in]=names[in];
            newPrices[in]=prices[in];
            }
          newNames[names.length]=new String[2];
          newNames[names.length][0]=firstToken;
          newNames[names.length][1]=st.nextToken();
          if (st.hasMoreTokens())
            {
            try
              {
              newPrices[names.length]=Double.parseDouble(st.nextToken());
              }
             catch (NumberFormatException nfe1)
              {
              newPrices[names.length]=1;
              }
            }
           else
            newPrices[names.length]=1;
          names=newNames;
          prices=newPrices;
          }
        linea=br.readLine();
        if (linea!=null)
          {
          st=new StringTokenizer(linea);
          if (st.hasMoreTokens())
            firstToken=st.nextToken();
           else
            linea=null;
          }
        } while(linea!=null && st.hasMoreTokens());
      for (int iv=0;iv<names.length;iv++)
        System.out.println("coppia "+names[iv][0]+" "+names[iv][1]+" "+prices[iv]);
      setGraph(names,prices);
      return;
      }
    linea=br.readLine();
    st=new StringTokenizer(linea);
    if (ns!=st.countTokens())
      System.err.println("Error in the number of nodes");
    ns=st.countTokens();
/*merge*/
    if (og!=null)
      ns+=og.n.length;
/**/
    nm=new String[ns];
    am=new int[ns][ns];
    pr=new double[ns][ns][];
    int j;
    i=0;
/*merge*/
    if (og!=null)
      {
      for (;i<og.n.length;i++)
        nm[i]=og.n[i].name;
      }
/**/
    for (;i<nm.length && st.hasMoreTokens();i++)
      {
      String temp=st.nextToken();
      for (int k=0;k<i;k++)
        if (temp.equals(nm[k]))
          {
          temp+="_";
          k=0;
          }
      nm[i]=temp;
      }
    i=0;
/*merge*/
    if (og!=null)
      {
      for (;i<og.n.length;i++)
        {
        for (j=0;j<og.n.length;j++)
          {
          int k=0;
          Arc p=og.a[i][j];
          for (;p!=null;k++)
            p=p.next;
          am[i][j]=k;
          pr[i][j]=new double[k];
          }
        for (;j<nm.length;j++)
          {
          am[i][j]=0;
          pr[i][j]=new double[0];
          }
        }
      }
/**/
    for (;i<nm.length;i++)
      {
      j=0;
/*merge*/
      if (og!=null)
        {
        for (;j<og.n.length;j++)
          am[i][j]=0;
        }
/**/
      linea=br.readLine();
      for (st=new StringTokenizer(linea);j<nm.length;j++)
        {
        String val=st.nextToken().trim();
        if (val.equals("0"))
          am[i][j]=0;
         else
          {
          try
            {
            am[i][j]=Integer.parseInt(val);
            }
           catch (NumberFormatException nfe)
            {
            am[i][j]=1;
            }
          }
        }
      }
    i=0;
/*merge*/
    if (og!=null)
      {
      for (;i<og.n.length;i++)
        {
        for (j=0;j<og.n.length;j++)
          {
          Arc ap;
          int k;
          for (ap=og.a[i][j],k=0;ap!=null;ap=ap.next)
            k++;
          pr[i][j]=new double[k];
          for (ap=og.a[i][j],k=0;k<pr[i][j].length;k++)
            {
            pr[i][j][k]=ap.p;
//            System.out.println("arc "+i+","+j+","+k+"="+pr[i][j][k]);
            ap=ap.next;
            }
          }
        for (;j<nm.length;j++)
          {
          pr[i][j]=new double[1];
          pr[i][j][0]=0.0;
          }
        }
      }
/**/
    for (;i<nm.length;i++)
      {
      linea=br.readLine();
//      System.out.println("Linea "+i+"="+linea);
      for (j=0;j<nm.length;j++)
        {
        pr[i][j]=new double[am[i][j]];
        for (int k=0;k<pr[i][j].length;k++)
          pr[i][j][k]=1.0;
        }
      if (linea!=null)
        {
/*
        for (int ix=0;ix<am.length;ix++)
          {
          for (int jx=0;jx<am[i].length;jx++)
            System.out.print(" "+am[ix][jx]);
          System.out.println();
          }
*/
        j=0;
        st=new StringTokenizer(linea);
//        int nTok=st.countTokens();
//        System.out.println(st.countTokens()+"&&"+nm.length+" j="+j);
        for (;st.hasMoreTokens() && j<nm.length;j++)
          {
//          System.out.println(st.countTokens()+"&&"+nm.length);
          String pesi=st.nextToken();
//          System.out.println(am[i][j]+"->pesi("+i+","+j+")<=<"+pesi+">");
          if (am[i][j]!=0)
            {
            StringTokenizer lineSt=new StringTokenizer(pesi,"|");
            if (lineSt.countTokens()!=am[i][j])
              {
              am[i][j]=lineSt.countTokens();
              pr[i][j]=new double[lineSt.countTokens()];
              System.err.println("incongruent number of arcs in "+i+" "+j+" ("+nm[i]+","+nm[j]+"): assuming "+lineSt.countTokens());
              }
            for (int k=pr[i][j].length-1;k>=0;k--)
              try
                {
                String val=lineSt.nextToken();
//                System.out.println("i="+i+" j="+j+" k="+k+" "+val);
                pr[i][j][k]=Double.parseDouble(val);
                }
               catch (NumberFormatException nfe)
                {
                pr[i][j][k]=1;
                }
            }
           else
            {
            pr[i][j]=new double[0];
            }
          }
        for (;j<nm.length;j++) // nel caso ci siano meno elementi nella matrice
          pr[i][j]=new double[0];
        }
      }
    }
   catch (IOException ioe)
    {
    if (i!=0)
      {
      ioe.printStackTrace();
      System.out.println("Missing lines");
      }
    }
   catch (NullPointerException npe)
    {
    npe.printStackTrace();
    System.out.println("Missing values in line "+i);
    }
  setGraph(nm,am,pr);
  }
/******************+
 * Salva  la matrice di adiacenza del grafo nel GraphPanel su un Writer  in un formato che
 * prevede che la prima riga letta contenga il numero di nodi e la successiva � una riga vuota
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente c'� la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
* @param w il Writer su cui viene salvato il grafo
  ********************/
public void save(Writer w)
  {
  save(w,false);
  }
/******************+
 * Salva  la matrice di adiacenza del grafo su un Writer  in un formato che
 * prevede che eventualmente la prima riga scritta contenga il numero di nodi e la successiva 
 * contiene l'elenco dei nomi dei nodi
 * e nelle righe successive sono conservati i valori della matrice di adiacenza
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * Successivamente c'e' la matrice dei pesi
 * dove le colonne e le righe conservano lo stesso ordine dei nomi dei nodi
 * e gli elementi sono i valori dei singoli archi separati da '|'
* @param w il Writer su cui viene salvato il grafo
* @param labels se <b>true</b> indica anche il numero di nodi e le etichette delle righe della matrice di adiacenza, altrimenti no.
********************/
public void save(Writer w,boolean labels)
  {
  PrintWriter pw=new PrintWriter(w);
  pw.print(n.length);
  if (labels)
    pw.println(" "+n.length);
   else
    pw.println();
  for (int i=0;i<n.length;i++)
    pw.print(n[i]+" ");
  pw.println();
  for (int i=0;i<n.length;i++)
    {
    if (labels)
      pw.print(n[i]+" ");
    for (int j=0;j<n.length;j++)
      if (a[i][j]!=null)
        {
        int count=0;
        for (Arc p=a[i][j];p!=null;p=p.next)
          count++;
        pw.print(count+" ");
        }
       else
        pw.print("0 ");
    pw.println();
    }
  if (!labels)
    {
    for (int i=0;i<n.length;i++)
      {
      for (int j=0;j<n.length;j++)
        if (a[i][j]!=null)
          {
          String prices="";
          for (Arc ai=a[i][j];ai!=null;ai=ai.next)
            {
            prices+=ai.p;
            if (ai.next!=null)
              prices+="|";
            }
          pw.print(prices+" ");
          }
         else
          pw.print("0 ");
      pw.println();
      }
    }
  }
/******************
 * Salva  la successione delle potenze della matrice
 * d'adiacenza del grafo e delle matrici delle connessioni
 * @param w il Writer su cui viene salvato il grafo
 ********************/
public void saveMatrix(Writer w)
  {
  PrintWriter pw=new PrintWriter(w);
  pw.println("Adiacence Matrix");
  pw.println(this);
  Matrix m=adiacence();
  Matrix sum=Matrix.zero(degree());
  for (int i=1;i<=degree();i++)
    {
    pw.println("Step "+i);
    pw.println(m.pow(i));
    sum=sum.sum(m.pow(i));
    pw.println(sum);
    }
  pw.println("Connections");
  pw.println(connections());
  }
/**************
 * Sostituisce il grafo rappresentato sostituendo la matrice dei nomi e la matrice di adiacenza
 * @param name la matrice dei nuovi nomi dei nodi
 * @param matAd la nuova matrice di adiacenza del grafo
 ***************/
void setGraph(String name[], int matAd[][])
  {
  setGraph(name, matAd, null);
  }
/**************
 * Sostituisce il grafo rappresentato sostituendo la matrice dei nomi e le matrici di adiacenza e dei prezzi
 * @param name la matrice dei nuovi nomi dei nodi
 * @param matAd la nuova matrice di adiacenza del grafo
 * @param prices la nuova matrice dei pesi del grafo
 ***************/
void setGraph(String name[], int matAd[][], double prices[][][])
  {
  reset();
  n=new Node[name.length];
  for (int i=0;i<n.length;i++)
    n[i]=new Node(name[i]);
  a=new Arc[n.length][n.length];
  for (int i=0;i<n.length;i++)
    for (int j=0;j<n.length;j++)
      {
      a[i][j]=null;
      if (matAd[i][j]!=0)
        {
        for (int k=0;k<matAd[i][j];k++)
          if (prices!=null)
            a[i][j]=new Arc(n[i],n[j],(k<prices[i][j].length)?prices[i][j][k]:1.0,a[i][j]);
           else
            a[i][j]=new Arc(n[i],n[j],1.0,a[i][j]);
        }
      }
  }
/********
 * sostituisce il grafo rappresentato partendo dall’elenco delle coppie di nodi uniti da un arco.
 * @param couples una matrice dove ogni riga è una coppia di nomi di nodi collegati tramite un arco
 * @param prices una matrice dove ogni elemento è il costo del corrispondente arco
 ********/
void setGraph(String couples[][], double prices[])
  {
  String names[]=new String[0];
  for (int i=0;i<couples.length && couples[i].length>1;i++)
    {
//    System.out.println(couples[i][0]+"->"+couples[i][1]);
    boolean presentFrom=false, presentTo=false;
    for (int j=0;j<names.length;j++)
      {
      if (names[j].equals(couples[i][0]))
        presentFrom=true;
      if (names[j].equals(couples[i][1]))
        presentTo=true;
      }
    if (!presentFrom)
      {
      String newNames[]=new String[names.length+1];
      for (int n=0;n<names.length;n++)
        newNames[n]=names[n];
      newNames[names.length]=couples[i][0];
      names=newNames;
      }
    if (!presentTo)
      {
      String newNames[]=new String[names.length+1];
      for (int n=0;n<names.length;n++)
        newNames[n]=names[n];
      newNames[names.length]=couples[i][1];
      names=newNames;
      }
    }
  n=new Node[names.length];
  for (int ni=0; ni<names.length; ni++)
    {
    n[ni]=new Node(names[ni]);
    }
  conn=null;
  adiac=null;
  a=new Arc[n.length][n.length];
  for (int i=0;i<a.length;i++)
    for (int j=0;j<a[i].length;j++)
      a[i][j]=null;
  for (int c=0;c<couples.length && c<prices.length;c++)
    {
    for (int np=0;np<n.length;np++)
      if (n[np].name.equals(couples[c][0]))
        {
        for (int nd=0;nd<n.length;nd++)
          if (n[nd].name.equals(couples[c][1]))
            {
            if (prices!=null)
              a[np][nd]=new Arc(n[np],n[nd],prices[c],a[np][nd]);
             else
              a[np][nd]=new Arc(n[np],n[nd],1,a[np][nd]);
            break;
            }
        break;
        }
    }
  }
/***********
 * Controlla se i due indici dei nodi sono validi per il grafo
 * @param i la riga dell'arco
 * @param j la colonna dell'arco
 * @return <b>true</b> se gli indici dei nodi dell'arco sono validi, <b>false</b> altrimenti
 ************/
public boolean coordOk(int i, int j)
  {
  return i>=0 && j>=0 && i<n.length && j<n.length;
  }
/**************
 * Ritorna l'arco compreso da due nodi
 * @param from il nome del nodi di partenza
 * @param to il nome del nodo di arrivo
 * @return l'arco
 ***************/
public Arc getArc(String from, String to)
  {
  int i=findNode(from),j=findNode(to);
  return getArc(i,j);
  }
/**************
 * Ritorna l'arco compreso da due nodi
 * @param i l'indice del nodi di partenza
 * @param j l'indice del nodo di arrivo
 * @return l'arco
 ***************/
public Arc getArc(int i, int j)
  {
  if (coordOk(i,j))
    return a[i][j];
   else
    return null;
  }
/*************
 * Cancella un arco dal grafo
 * @param ar l'arco da cancellare
 * @return <b>true</b> se ci sono altri archi tra gli stressi nodi, <b>false</b> altrimenti
 *************/
public boolean removeArc(Arc ar)
  {
  for (int i=0;i<a.length;i++)
    for (int j=0;j<a[i].length;j++)
      {
      if (a[i][j]!=null)
        {
        Arc ai=a[i][j],pre=null;
        for (;ai!=null && ai!=ar;)
          {
          pre=ai;
          ai=ai.next;
          }
        if (ai!=null)
          {
          if (pre!=null)
            pre.next=ai.next;
           else
            a[i][j]=ai.next;
          reset();
          return a[i][j]!=null;
          }
        }
      }
  return true;
  }
/*************
 * Cancella gli archi che collegano due nodi del grafo
 * @param from in nome del nodo di partenza
 * @param to in nome del nodo di arrivo
 * @return <b>true</b> se ci sono altri archi tra gli stressi nodi, <b>false</b> altrimenti
 *************/
public boolean removeArc(String from, String to)
  {
  int i=findNode(from),j=findNode(to);
  if (i!=-1 && j!=-1)
    {
    a[i][j]=null;
    reset();
    return true;
    }
   else
    return false;
  }
/*****
 * Aggiunge un arco al grafo
 * @param arc l'arco da aggiungere
 * @return l'arco
 *****/
public Arc addArc(Arc arc)
  {
  int i=findNode(arc.f),j=findNode(arc.t);
  if (i!=-1 && j!=-1)
    {
    arc.next=a[i][j];
    a[i][j]=arc;
    reset();
    return arc;
    }
   else
    return null;
  }
/*****
 * Aggiunge un arco al grafo
 * @param sorg il nodo di partenza
 * @param dest il nodo di destinazione
 * @return l'arco
 *****/
public Arc addArc(Node sorg, Node dest)
  {
  return addArc(sorg,dest,1.0);
  }
/*****
 * Aggiunge un arco al grafo con un certo prezzo
 * @param sorg il nodo di partenza
 * @param dest il nodo di destinazione
 * @param price il prezzo dell'arco
 * @return l'arco
 *****/
public Arc addArc(Node sorg, Node dest, double price)
  {
  int da=findNode(sorg),ad=findNode(dest);
  if (da!=-1 && ad!=-1)
    {
    a[da][ad]=new Arc(sorg,dest,price,a[da][ad]);
    reset();
    return a[da][ad];
    }
   else
    return null;
  }
/*****
 * Aggiunge un arco al grafo
 * @param sorg il nome del nodo di partenza
 * @param dest il nome del nodo di destinazione
 * @return l'arco
 *****/
public Arc addArc(String sorg, String dest)
  {
  return addArc(sorg,dest,1.0);
  }
/*****
 * Aggiunge un arco al grafo. Se i nodi collegati non esistono vengon creati
 * @param sorg il nome del nodo di partenza
 * @param dest il nome del nodo di destinazione
 * @param price il prezzo dell'arco
 * @return l'arco
 *****/
public Arc addArc(String sorg, String dest, double price)
  {
  addNode(sorg);
  addNode(dest);
  int da=findNode(sorg),ad=findNode(dest);
  return addArc(n[da],n[ad],price);
  }
/**********
 * Ritorna il nodo dato l'indice
 * @param i l'indice del nodo
 * @return il nodo
 **********/
public Node getNode(int i)
  {
  if (i>=0 && i<n.length)
    return n[i];
   else
    return null;
  }
/**********
 * Ritorna il nodo dato il nome
 * @param name il nome del nodo
 * @return il nodo
 **********/
public Node getNode(String name)
  {
  int i=findNode(name);
  return getNode(i);
  }
/**********
 * Cambia il nome di un nodo
 * @param name il vecchio nome del nodo
 * @param newName il nuovo nome del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
**********/
public boolean renameNode(String name, String newName)
  {
  newName=newName.replace(' ','_');
  int i=findNode(name),ni=findNode(newName);
  if (i>=0 && ni<0 && !newName.equals(""))
    {
    n[i].name=newName;
    reset();
    return true;
    }
   else
    return false;
  }
/****************
 * Rimuove un nodo dal grafo
 * @param nome il nome del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 *****************/
public boolean removeNode(String nome)
  {
  int pos=-1;
  if ((pos=findNode(nome))>=0)
    {
    int i,k;
    Node nn[]=new Node[n.length-1];
    Arc aa[][]=new Arc[n.length-1][n.length-1];
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
    n=nn;
    a=aa;
    reset();
    return true;
    }
   else
    return false;
  }
/*****
 * Aggiunge un nodo al grafo
 * @param name il nome del nodo
 * @return <b>true</b> se il grafo e' cambiato, <b>false</b> altrimenti
 *****/
public boolean addNode(String name)
  {
  name=name.replace(' ','_');
  if (findNode(name)==-1)
    {
    int i;
    Node nn[]=new Node[n.length+1];
    Arc aa[][]=new Arc[n.length+1][n.length+1];
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
    nn[n.length]=new Node(name);
    n=nn;
    a=aa;
    reset();
    return true;
    }
   else
    return false;
  }
/**********
 * Ritorna l'indice di un nodo dato il nome
 * @param nn il nome del nodo
 * @return l'indice del nodo
 **********/
 public int findNode(String nn)
  {
  String name=nn.replace(" ","_");
  int ris=-1;
  for (int i=0;i<n.length && ris<0;i++)
    {
    if (n[i].name.equals(name))
      ris=i;
    }
  return ris;
  }
/**********
 * Ritorna l'indice di un nodo
 * @param node il nodo
 * @return l'indice del nodo
 **********/
public int findNode(Node node)
  {
  int ris=-1;
  for (int i=0;i<n.length && ris<0;i++)
    {
    if (n[i]==node)
      ris=i;
    }
  return ris;
  }
/**********
 * Ritorna la matrice dei nodi
 * @return la matrice dei nomi dei nodi
 **********/
public String[] getNodes()
  {
  String r[]=new String[n.length];
  for (int i=0;i<n.length;i++)
    r[i]=new String(n[i].name);
  return r;
  }
/**********
 * Azzera le matrici di adiacenza e delle connessioni
 **********/
public void reset()
  {
  adiac=null;
  conn=null;
  }
/**********
 * Ritorna l'ordine del grafo
 * @return l'ordine del grafo
 **********/
public int degree()
  {
  return n.length;
  }
/**********
 * Dice se due nodi sono connessi
 * @param from il nodo di partenza
 * @param to il nodo di arrivo
 * @return <b>true</b> se i due nodi sono connessi, <b>false</b> altrimenti
 **********/
public boolean isConnected(String from, String to)
  {
  int f=findNode(from),t=findNode(to);
  if (f==-1 || t==-1)
    return false;
  conn=connections();
  return conn.getMatrix()[f][t]!=0;
  }
/*********
 * Ritorna la matrice di adiacenza del grafo
 * @return la matrice di adiacenza del grafo
 *********/
public Matrix adiacence()
  {
  if (adiac==null)
    {
    double ma[][]=new double[n.length][n.length];
    for (int i=0;i<a.length;i++)
      for (int j=0;j<a[i].length;j++)
        {
        int count=0;
        for (Arc p=a[i][j];p!=null;p=p.next)
          count++;
        ma[i][j]=count;
        }
    adiac=new Matrix(ma);
    }
  return adiac;
  }

/*********
 * Ritorna la matrice dei collegamenti del grafo 
 * @return la matrice dei collegamenti del grafo
 *********/
public Matrix connections()
  {
  if (conn==null)
    {
    Matrix mat=adiacence();
    conn = Matrix.one(mat.order());
    for (int ni=1;ni<=n.length;ni++)
      conn=conn.sum(mat.pow(ni));
    }
  return conn;
  }

/****************
 * Calcola il costo di un percorso costituito dalla sequenza di nodi indicati, se esiste
 * @param path il percorso da verificare
 * @return il costo del percorso, se esiste la connessione lungo il percorso, un valore infinito altrimenti
 *****************/
public double costs(String path[])
  {
  double tot=0.0;
  for (int i=0;i<path.length-1;i++)
    {
    Arc ar=a[findNode(path[i])][findNode(path[i+1])];
    if (ar==null)  // non c'è connessione
      return Double.POSITIVE_INFINITY;
     else
      tot+=ar.p;
    }
  return tot;
  }

/****************
 * Dice se il percorso costituito dalla sequenza di nodi indicati esiste
 * @param path il percorso da verificare
 * @return <b>treu</b> se esiste la connessione lungo il percorso, <b>false</b> altrimenti
 *****************/
public boolean connected(String path[])
  {
  for (int i=0;i<path.length-1;i++)
    {
    Arc ar=a[findNode(path[i])][findNode(path[i+1])];
    if (ar==null)
      return false;
    }
  return true;
  }

final int PRICE=0, LEN=1;
/****************
 * Ritorna la sequenze dei nomi dei nodi che sostituiscono il cammino di lunghezza minima tra due nodi
 * @param from il nodo di partenza
 * @param to il nodo di arrivo
 * @return la sequenze dei nomi dei nodi che sostituiscono il cammino di lunghezza minima tra due nodi
 *****************/
public String[] minLengthPath(String from, String to)
  {
  return minPath(from,to,LEN);
  }
/****************
 * Ritorna la sequenze dei nomi dei nodi che sostituiscono il cammino di prezzo minimo tra due nodi
 * @param from il nodo di partenza
 * @param to il nodo di arrivo
 * @return la sequenze dei nomi dei nodi che sostituiscono il cammino di prezzo minimo tra due nodi
 *****************/
public String[] minPricePath(String from, String to)
  {
  return minPath(from,to,PRICE);
  }
/****************
 * Ritorna la sequenze dei nomi dei nodi che sostituiscono il cammino minimo tra due nodi
 * @param from il nodo di partenza
 * @param to il nodo di arrivo
 * @param type se il valore e' PRICE viene effettuata la ricerca sul percorso di prezzo minimo, se il valore e' LEN viene effettuata la ricerca sul percorso pi� breve.
 * @return la sequenze dei nomi dei nodi che sostituiscono il cammino minimo tra due nodi
 *****************/
public String[] minPath(String from, String to, int type)
  {
  if (!isConnected(from,to))
    return null;
  double cost[]=new double[n.length];
  int previous[]=new int[n.length];
  boolean visited[]=new boolean[n.length];
  boolean frontier[]=new boolean[n.length];
  for (int i=0;i<n.length;i++)
    {
    visited[i]=false;
    frontier[i]=false;
    }
  cost[findNode(from)]=0;
  frontier[findNode(from)]=true;
  boolean more=true;
  for (;more;)
    {
    more=false;
    double min=Double.POSITIVE_INFINITY;
    int minI=-1;
    for (int i=0;i<n.length;i++)
      if (frontier[i] && (minI<0 || (minI!=-1 && min>cost[i])))
        {
        more=true;
        min=cost[i];
        minI=i;
//        System.out.println("minimo in "+i+"="+cost[i]);
        }
    if (more)
      {
      for (int i=0;i<n.length;i++)
        {
        if (a[minI][i]!=null)
          {
          double val=(type==PRICE)?a[minI][i].p:1;
          for (Arc ap=a[minI][i].next;type==PRICE && ap!=null;ap=ap.next)
            if (ap.p<val)  // cerca l'arco di prezzo minimo
              val=ap.p;
//          System.out.println("Controllo "+i+" preceduto da "+minI+" con valore="+cost[minI]+"+"+val+" rispetto a "+cost[i]);
          if (!visited[i] || (frontier[i] && cost[i]>cost[minI]+val))
            {
            cost[i]=cost[minI]+val;
            visited[i]=true;
            previous[i]=minI;
            frontier[i]=true;
//            System.out.println("Aggiungo "+i+" preceduto da "+minI+" con valore="+cost[i]);
            }
           else
            if (visited[i] && !frontier[i] && cost[i]>cost[minI]+val)
              {
//              System.out.println("Cycling");
              more=false;
              break;
              }
          }
        }
      frontier[minI]=false;
      }
    }
  if (!visited[findNode(to)])
    return null;
   else
    {
    String path[];
    int count=0;
    for (int i=findNode(to);i!=findNode(from);i=previous[i],count++)
      if (count>degree())
        {
//        System.out.println("Cycling");
        return null;
        }
    path=new String[count+1];
    for (int i=findNode(to);i!=findNode(from);i=previous[i],count--)
      path[count]=n[i].name;
    path[count]=from;
    return path;
    }
  }
  
/****************
 * Ritorna una stringa rappresentante il grafo come sua matrice di adiacenza.
 * @return la stringa rappresentante il grafo
 *****************/ 
public String toString()
  {
  String ris="\t\t";
  double ma[][]=adiacence().getMatrix();
  for (int i=0;i<ma.length;i++)
    ris+=n[i]+"\t";
  ris+="\n";
  for (int i=0;i<ma.length;i++)
    {
    ris+=n[i]+"\t|\t";
    for (int j=0;j<ma[i].length;j++)
      {
      ris+=((int)ma[i][j])+"(";
      int num=((int)ma[i][j]);
      Arc ap=a[i][j];
      for (int k=0;k<num;k++,ap=ap.next)
        ris+=ap.p+",";
      ris+=")\t";
      }
    ris+="\n";
    }
  return ris;
  }
/****************
 * Applicazione di prova.
 * @param ar viene ignorato
 *****************/ 
public static void main(String ar[])
  {
  String names[]={"primo","secondo","terzo","quarto"};
  String perc[]={"secondo","primo","quarto"};
  String perc1[]={"secondo","primo","quarto","terzo"};
  double p[][][]={{{3},{2.5,1.0},{-1},{2}}
               ,{{1},{3},{1,3.7,2.0},{0}}
               ,{{0},{1},{2.3},{-1}}
               ,{{1},{0},{-2},{0.5}}
               };
  int a[][]={{1,2,0,1}
               ,{1,0,3,0}
               ,{0,1,0,0}
               ,{1,0,0,0}
               };
  Graph gr=new Graph(names,a,p);
  System.out.println(gr);
  System.out.println(gr.adiacence());
  System.out.println(gr.connections());
  System.out.println(gr.connected(perc));
  System.out.println(gr.connected(perc1));
  gr.addArc("quarto","terzo");
  gr.addArc("primo","quinto");
  gr.addNode("sesto");
  System.out.println(gr);
  System.out.println(gr.adiacence());
  System.out.println(gr.connections());
  System.out.println(gr.connected(perc));
  System.out.println(gr.connected(perc1));
  String couples[][]={{"primo","secondo"},
                      {"quarto","terzo"},
                      {"secondo","terzo"},
                      {"terzo","quarto"},
                      {"terzo","secondo"},
                      {"secondo","primo"},
                      {"quarto","primo"},
                      {"primo","quarto"},
                     };
  double prices[]={1,1,1,1,1,1,1,1};
  gr=new Graph(couples,prices);
  System.out.println(gr);
  System.out.println(gr.adiacence());
  System.out.println(gr.connections());
  System.out.println(gr.connected(perc));
  System.out.println(gr.connected(perc1));
  }
}


