/*************
 * Un nodo di un grafo
 *************/
public class Node
{
/*************
 * Il nome del nodo
 *************/
String name;
/*************
 * Crea un nuovo nodo 
 * @param name il nome del nodo rappresentato
 *************/
public Node(String name)
  {
  this.name=name;
  }
/************
 * Ritorna il nome del nodo
 * @return il nome del nodo
 ************/
public String getName()
  {
  return name;
  }
/************
 * Ritorna la stringa che descrive il nodo
 * @return la descrizione del nodo
 ************/
public String toString()
  {
  return name;
  }
/************
 * Confronta il nodo con un altro oggetto
 * @param n l'oggetto da confrontare
 * @return <b>true</b> se <b>n</b> rappresentaa lo stesso nodo, <b>false</b> altrimenti
 ************/
public boolean equals(Object n)
  {
  if (n instanceof Node && name.equals(((Node)n).name))
    return true;
   else
    return false;
  }
}

