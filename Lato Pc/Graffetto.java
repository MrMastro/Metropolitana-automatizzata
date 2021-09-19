import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Graffetto extends JPanel
{
GraphJPanel gr;

public Graffetto()
  {
  super();
  setPreferredSize(new Dimension(700,450));
  gr=new GraphJPanel();
  setLayout(new BorderLayout());
  add("Center",gr);
  pulisciGrafo(true);
  }

String metrogiu="5\n"+
                "2 4 1 scambio 3 \n"+
                "0 0 0 1 0 \n"+
                "0 0 0 0 0 \n"+
                "1 0 0 0 0 \n"+
                "0 1 0 0 1 \n"+
                "0 0 0 1 0 \n"+
                "0 0 0 1.0 0 \n"+
                "0 0 0 0 0 \n"+
                "1.0 0 0 0 0 \n"+
                "0 1.0 0 0 1.0 \n"+
                "0 0 0 1.0 0 \n"+
                "0x0,333,149,10,0,0 0x0,68,295,10,0,0 0x0,81,83,10,0,0 0x0,491,295,10,0,0 0x0,623,295,10,0,0 \n"+
                "0 0 0 0x0 0 \n"+
                "0 0 0 0 0 \n"+
                "0x0 0 0 0 0 \n"+
                "0 0x0 0 0 0x0 \n"+
                "0 0 0 0x0 0 ";

String metrosu= "5\n"+
                "2 4 1 scambio 3 \n"+
                "0 0 1 0 0 \n"+
                "0 0 0 1 0 \n"+
                "0 0 0 0 0 \n"+
                "1 0 0 0 1 \n"+
                "0 0 0 1 0 \n"+
                "0 0 1.0 0 0 \n"+
                "0 0 0 1.0 0 \n"+
                "0 0 0 0 0 \n"+
                "1.0 0 0 0 1.0 \n"+
                "0 0 0 1.0 0 \n"+
                "0x0,333,149,10,0,0 0x0,68,295,10,0,0 0x0,81,83,10,0,0 0x0,491,295,10,0,0 0x0,623,295,10,0,0 \n"+
                "0 0 0x0 0 0 \n"+
                "0 0 0 0x0 0 \n"+
                "0 0 0 0 0 \n"+
                "0x0 0 0 0 0x0 \n"+
                "0 0 0 0x0 0 \n";
void pulisciGrafo(boolean verso)
    {
	String grafo;
	if(verso)
	  grafo=metrogiu;
	 else
	  grafo=metrosu;
	  gr.clear(); //cancella il grafo;
  gr.load(new StringReader(grafo));
	gr.getNode("scambio").setShape(NodeSign.SQUARE);
    gr.setZoom(0);
  gr.setEdit(false);
  gr.setSliding(false);
  gr.setShowWeights(false);
	gr.setNodeSize(30);
	}
	
void coloraPercorso(String n,Boolean verso)
    {
	pulisciGrafo(verso);
	if (verso)
     {
	 switch (n)
	  {
	    case "1":
        gr.getArc(gr.getNode(n),gr.getNode("2")).setColor(Color.red);
	    break;
	    case "2":
        gr.getArc(gr.getNode(n),gr.getNode("scambio")).setColor(Color.red);
	            gr.getArc(gr.getNode("scambio"),gr.getNode("3")).setColor(Color.red);
				gr.getArc(gr.getNode("3"),gr.getNode("scambio")).setColor(Color.red); //aggiunta
		break;
      case "3":
        gr.getArc(gr.getNode(n),gr.getNode("scambio")).setColor(Color.red);
	            gr.getArc(gr.getNode("scambio"),gr.getNode("4")).setColor(Color.red);
		break;
	  }
	 }
	if (!verso)
	 {
	 switch (n)
	  {
	    case "4":
        gr.getArc(gr.getNode(n),gr.getNode("scambio")).setColor(Color.red);
	            gr.getArc(gr.getNode("scambio"),gr.getNode("3")).setColor(Color.red);
			    gr.getArc(gr.getNode("3"),gr.getNode("scambio")).setColor(Color.red); //aggiunta
		break;
      case "3":
        gr.getArc(gr.getNode(n),gr.getNode("scambio")).setColor(Color.red);
	          gr.getArc(gr.getNode("scambio"),gr.getNode("2")).setColor(Color.red);
		break;
	  case "2": gr.getArc(gr.getNode(n),gr.getNode("1")).setColor(Color.red);
	    break;
	  }
	 }
    }	

	
void coloraFermata(String nome,boolean verso)
    {
	pulisciGrafo(verso);
    gr.getNode(nome).setColor(Color.red); //coloro il nodo 
  gr.getNode(nome).setFilled(true); //coloro il nodo 
    }
	
public static void main(String args[])
  {
  JFrame f=new JFrame("prova");
  f.setVisible(true);
  f.setSize(200,400);
  f.getContentPane().add(new Graffetto());
  f.pack();
  }
}