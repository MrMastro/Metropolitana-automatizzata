import java.awt.*;
import java.awt.event.*;

/**********
 * Il Panel per la selezione del colore
 **********/
public class ColorPickPanel extends Panel implements AdjustmentListener,ActionListener,TextListener,ColorChangeListener
{
/*******
 * Il Panel contenente il colore selezionato
 *******/
Panel color;
/*******
 * Il Panel contenente gli sliders delle tre componenti del colore
 *******/
Panel componentsPanel;
/*******
 * Il Panel contenente tutte le sfumature di colore alle diverse intensità
 * selezionabile via mouse
 *******/
ColorShades shades;
/*******
 * Le scrollaBars per le tre componenti di colore
 *******/
Scrollbar rgb[];
/*******
 * Il TextField contenente il valore della componente rossa (0-255)
 *******/
TextField redTf;
/*******
 * Il TextField contenente il valore della componente verde (0-255)
 *******/
TextField greenTf;
/*******
 * Il TextField contenente il valore della componente blu (0-255)
 *******/
TextField blueTf;
/*******
 * I valori delle componenti rossa, verde e blu
 *******/
int r=0,g=0,b=0;

/*******
 * Crea un COlorePickPanel con selezionato il colore nero
 *******/
public ColorPickPanel()
  {
  this(Color.black);
  }
  
/*******
 * Crea un ColorPickPanel con selezionato il colore dato
 * @param old il colore selezionato all'inizio
 *******/
public ColorPickPanel(Color old)
  {
  setLayout(new BorderLayout());
//  setLayout(new GridLayout(1,3));
  color = new Panel();
  shades = new ColorShades(old);
  shades.addColorChangeListener(this);
  componentsPanel = new Panel();
  redTf = new TextField();
  greenTf = new TextField();
  blueTf = new TextField();
  redTf.setColumns(2);
  greenTf.setColumns(2);
  blueTf.setColumns(2);
  redTf.setBackground(Color.red);
  greenTf.setBackground(Color.green);
  blueTf.setBackground(Color.blue);
  redTf.addActionListener(this);
  greenTf.addActionListener(this);
  blueTf.addActionListener(this);
  redTf.addTextListener(this);
  greenTf.addTextListener(this);
  blueTf.addTextListener(this);
  componentsPanel.setLayout(new GridLayout(3,1));
  componentsPanel.add(redTf);
  componentsPanel.add(greenTf);
  componentsPanel.add(blueTf);
  Panel sliders = new Panel();
  sliders.setLayout(new GridLayout(1,3));
  rgb = new Scrollbar[3];
  for(int i=0;i<3;i++)
    {
    rgb[i] = new Scrollbar(Scrollbar.VERTICAL);
    rgb[i].setMaximum(255);
    rgb[i].setMinimum(0);
    rgb[i].addAdjustmentListener(this);
    sliders.add(rgb[i]);
    }
  setColor(old);
  setSlider();
  add(sliders,BorderLayout.WEST);
  add(color,BorderLayout.SOUTH);
  add(componentsPanel,BorderLayout.EAST);
  add(shades,BorderLayout.CENTER);
  }
  
/******
 * Ritorna la dimensione preferita del Panel
 ******/
public Dimension getPreferredSize()
  {
  return new Dimension(200,100);
  }
  
/*******
 * Ritorna il colore selezionato
 *******/
public Color getColor()
  {
  return color.getBackground();
  }
  
/*******
 * Viene richiamato dal ColorSHades quando viene modificato il colore col mouse
 *******/
public void colorChanged(Event e)
  {
  setColor((Color)(e.arg));
  }
  
/*******
 * Viene richiamato quando vengono modificate le posizioni degli sliders
 *******/
public void adjustmentValueChanged(java.awt.event.AdjustmentEvent e)
  {
  Color c = new Color(255-rgb[0].getValue(),255-rgb[1].getValue(),255- rgb[2].getValue());
  setColor(c);
  }
  
/*******
 * Posiziona tutte le informazioni per rappresentare un dato colore
 * @param c il colore da rappresentare
 *******/
public void setColor(Color c)
  {
  r=c.getRed();
  g=c.getGreen();
  b=c.getBlue();
  redTf.setText(""+r);
  greenTf.setText(""+g);
  blueTf.setText(""+b);
  color.setBackground(c);
  shades.setColor(c);
  }
  
/*******
 * Posiziona gli sliders in corrispondenza del colore selezionato
 *******/
public void setSlider()
  {
  try
    {
    r=Integer.parseInt(redTf.getText());
    g=Integer.parseInt(greenTf.getText());
    b=Integer.parseInt(blueTf.getText());
    }
   catch (NumberFormatException nfe)
    {
    }
  if (r<0 || r>255)
    r=r%256;
  if (g<0 || g>255)
    g=g%256;
  if (b<0 || b>255)
    b=b%256;
  Color c=new Color(r,g,b);
  rgb[0].setBackground(Color.red);
  rgb[0].setValue(255-c.getRed());
  rgb[1].setBackground(Color.green);
  rgb[1].setValue(255-c.getGreen());
  rgb[2].setBackground(Color.blue);
  rgb[2].setValue(255-c.getBlue());
  color.setBackground(c);
  shades.setColor(c);
  }
  
/********
 * Viene richiamato quando viene modificato il testo allegato agli sliders
 * @param te l'evento sul testo che viene gestito
 ********/
public void textValueChanged(TextEvent te)
  {
  setSlider();
  }

/********
 * Viene richiamato quando viene modificato il testo allegato agli sliders
 * @param ae l'evento sul testo che viene gestito
 ********/
public void actionPerformed(ActionEvent ae)
  {
  setSlider();
  if (ae.getSource()==redTf)
    {
    redTf.setText(""+r);
    greenTf.selectAll();
    greenTf.requestFocus();
    }
  if (ae.getSource()==greenTf)
    {
    greenTf.setText(""+g);
    blueTf.selectAll();
    blueTf.requestFocus();
    }
  if (ae.getSource()==blueTf)
    {
    blueTf.setText(""+b);
    redTf.selectAll();
    redTf.requestFocus();
    }
  }

public static void main(String args[])
  {
  Frame f = new Frame();
  f.setSize(100,250);
  f.addWindowListener(new WindowAdapter()
    {
    public void windowClosing(WindowEvent we)
      {
      System.exit(0);
      }
    });
  ColorPickPanel cp = new ColorPickPanel();
  f.add(cp);
  f.pack();
  f.setVisible(true);
  }
}
