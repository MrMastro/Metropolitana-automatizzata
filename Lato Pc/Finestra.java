import sun.audio.*;
import java.io.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Finestra extends JFrame implements ActionListener,MouseListener
{
boolean verso=true;
int numeroVelocita;
private Leva l;
private Container c;
private JPanel labContenitore;
private JPanel labBott;
JTextArea areaDiTesto;
private JTextField input;
private JButton bott[];
private JPanel leva;
Graffetto g=new Graffetto();
String message;
//PrintStream output;
String utente;
ControlArduino control;
int xMouse;
int yMouse;

public Finestra()
  {
  this("Arduino",null);
  }

public Finestra(String nome,ControlArduino applicazione)
  {
  super(nome);
  control=applicazione;
  utente=nome;
  c = getContentPane();
  setResizable(true);
  labContenitore = new JPanel();
  labBott = new JPanel();
  areaDiTesto=new JTextArea();
  input=new JTextField();
  JScrollPane sp = new JScrollPane(areaDiTesto);
  areaDiTesto.append("Conversazione: (clicca per pulire l'area)");
  areaDiTesto.setEditable(false);
  l=new Leva();
  l.setSize(200,60);
  //areaDiTesto.setSize(500,200);
  labBott.setSize(300,200);
  labBott.setLayout(new GridLayout(2,4));
  String nomi[]={
                "Controllo Remoto (a)",
                "Controllo Automatismo (b)",
                "Motore Avanti (c)",
                "Motore Indietro (d)",
                "Stop/Riprendi (e)",
                "Cambio Scambio Curvo (f)",
                "Cambio scambio Dritto (g)",
                "Invia Velocita' (h)",
                "ESCI"
                };
  bott=new JButton[nomi.length];
  for (int i=0;i<bott.length;i++)
    {
    bott[i]= new JButton (nomi[i]);
    bott[i].setSize(500,60);
    labBott.add(bott[i]);
    bott[i].addActionListener(this);
    }
  for(int i=0;i<8;i++)
  bott[i].setEnabled(false);
  labBott.add(l);
  //c.setLayout(new BorderLayout());
  labContenitore.setLayout(new GridLayout(1,2));
  labContenitore.add(sp);
  labContenitore.add(g);
  c.add("Center",labContenitore);
  c.add("South",labBott);
  //labContenitore.add("South",input);
  input.addActionListener(this);
  areaDiTesto.addMouseListener(this);
  pack();
  setVisible(true);
  setSize(1300,500);
  setLocation(50,50);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  g.gr.outZoom();
  g.gr.setShowWeights(false);
  g.pulisciGrafo(verso); //Cancello e rimetto il grafo in modo da avere una buona zoommata
 // g.reshapeNode("scambio",1);
  g.gr.setModified(false);
  g.gr.setPopUp(false);
  }

void enable (String com)
  {
  for(int i=0;i<2;i++)
    bott[i].setEnabled(true);
  setTitle("Arduino - on port "+com);
  }
void setVerso(boolean sv)
  {
  verso=sv;
  }
  
boolean getVerso()
  {
  return verso;
  }
public void actionPerformed(ActionEvent ae)
  {
  if (ae.getSource()==input)
    {
    message=input.getText();
    input.setText("");
    areaDiTesto.append("\n inviato il numero: "+message);
    // int max=message.length();
    // if(max>9)
    // {
    // areaDiTesto.append("\n numero troppo grande per essere inviato");
    // }
    // else
    // {
    // System.out.println(max);
    // String limite= String.valueOf(max);
    // control.invio(limite.charAt(0));
    // for(int i=0;i<max;i++)
    // {
    // control.invio(message.charAt(i));
    // }
    // }
    }
  if (ae.getSource()==bott[0])
    {
    control.invio("a".charAt(0));
    for(int i=2;i<8;i++)
    bott[i].setEnabled(true);
    }
  if (ae.getSource()==bott[1])
	  {
    control.invio("b".charAt(0));
	  for(int i=2;i<8;i++)
    bott[i].setEnabled(false);
	  }
  if (ae.getSource()==bott[2])
    {
    control.invio("c".charAt(0));
    }
  if (ae.getSource()==bott[3])
    {
    control.invio("d".charAt(0));
    }
	if (ae.getSource()==bott[4])
    {
    control.invio("e".charAt(0));
    }
	if (ae.getSource()==bott[5])
    {
    control.invio("f".charAt(0));
    }
	if (ae.getSource()==bott[6])
    {
    control.invio("g".charAt(0));
    }
	if (ae.getSource()==bott[7])
    {
    control.invio("h".charAt(0));
    numeroVelocita=l.valore();
    //if(numeroVelocita==0)
      //{
      //numeroVelocita=1;
      //areaDiTesto.append("\nAttenzione: non e' possibile avere una velocita a 0, e' stata impostata ad 1");
      //}
    String numero = String.valueOf(numeroVelocita);
    int max=numero.length();
    System.out.println("numero di cifre " + max);
    String limite= String.valueOf(max);
    control.invio(limite.charAt(0));
    for(int i=0;i<max;i++)
      {
      control.invio(numero.charAt(i));
      }
	 }
  if(ae.getSource()==bott[8])
	  {
    System.exit(0);
	  }
    }

  public void	mouseClicked(MouseEvent e)
    {
    areaDiTesto.setText("Conversazione: ");
    System.out.println("schermo pulito");
   // System.out.println(g.gr.getSize());
	/* 	System.out.println(g.gr.getZoom()); // debug (da cancellare)
	if(verso)
	  verso=false;
	 else
	  verso=true;
	g.pulisciGrafo(verso); */
    }
  public void	mouseEntered(MouseEvent e)  {}
  public  void	mouseExited(MouseEvent e) {}
  public  void	mousePressed(MouseEvent e){}
  public void	mouseReleased(MouseEvent e) {}

public static void main(String args[]) // main di prova per visualizzare la finestra grafica
  {
  Finestra f=new Finestra();
  }
}