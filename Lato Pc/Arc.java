/*************
 * Un arco di un grafo
 *************/
public class Arc
{
/*************
 * il nodo di partenza
 *************/
Node f;
/*************
 * il nodo di arrivo
 *************/
Node t;
/*************
 * il peso/prezzo dell'arco
 *************/
double p;
/*************
 * il successivo arco nella lista degli archi tra i due nodi
 *************/
Arc next;
/*************
 * Crea un nuovo arco 
 * @param from il nodo di partenza dell'arco da creare
 * @param to il nodo di arrivo dell'arco da creare
 *************/
public Arc(Node from, Node to)
  {
  this(from,to,1.0,null);
  }
/*************
 * Crea un nuovo arco 
 * @param from il nodo di partenza dell'arco da creare
 * @param to il nodo di arrivo dell'arco da creare
 * @param price il prezzo/peso dell'arco da creare
 *************/
public Arc(Node from, Node to, double price)
  {
  this(from,to,price,null);
  }
/*************
 * Crea un nuovo arco 
 * @param from il nodo di partenza dell'arco da creare
 * @param to il nodo di arrivo dell'arco da creare
 * @param price il prezzo/peso dell'arco da creare
 * @param next il successivo arco nella lista degli archi tra i due nodi
 *************/
public Arc(Node from, Node to, double price, Arc next)
  {
  f=from;
  t=to;
  p=price;
  this.next=next;
  }
/*************
 * Ritorna la stringa che rappresenta l'arco 
 * @return la stringa che rappresenta l'arco
 *************/
public String toString()
  {
  return "["+f+"("+p+")"+t+"]";
  }
}

