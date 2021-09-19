import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import java.util.Enumeration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ControlArduino implements Runnable
  {
  SerialPort serialPort; //dichiaro un oggetto SerialPort
  BufferedReader input; //dichiaro il mio input, userò questo oggetto per leggere le Stringhe che ricevo dal mio arduino
  OutputStream output; //dichiaro il mio output,userò questo oggetto per inviare Stringhe al mio arduino
  private static final int TIME_OUT = 2000; //dichiaro un tempo di attesa
  private static final int DATA_RATE = 9600; //dichiaro quanti bits per secondo possono passare dalla porta seriale COM
  Finestra grafica;
  boolean verso=true;
  
public ControlArduino()
	{
  grafica= new Finestra("Arduino - waiting for port list",this);
  buildPort();
	  PrintStream out= new PrintStream(output);
	  Thread ricevitore=new Thread(this);
	  ricevitore.start();
	  }
	
public void run()
  {
  ricevo();
  }	

  
public void buildPort() 
	{
  Vector <String>ports=new Vector<String>();
  Vector <CommPortIdentifier>ids=new Vector<CommPortIdentifier>();
  System.out.println("adding ports");
	Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
  for (;portEnum.hasMoreElements();)
		{
    CommPortIdentifier cpi=(CommPortIdentifier)portEnum.nextElement();
    ids.add(cpi);
    ports.add(cpi.getName());
    System.out.println("adding "+cpi.getName());
    }
  final JDialog quest=new JDialog(grafica,true);
  quest.setLocation(200,300);
  JComboBox jcb=new JComboBox(ports);
  quest.add(jcb,BorderLayout.CENTER);
  JButton ok=new JButton("ok");
  ok.addActionListener(new ActionListener()
                               {
                               public void actionPerformed(ActionEvent ae)
                                 {
                                 quest.setVisible(false);
                                 }
                               });
  quest.add(ok,BorderLayout.SOUTH);
  quest.pack();
  System.out.println("setting visible");
  quest.setVisible(true);
  System.out.println("dopo visible");
//	String portName= ;
  CommPortIdentifier portId = null; // un oggetto utile per il metodo getName, che mi ritorna la vera posizione della porta arduino
  System.out.println("setting port");
  String portName=(String)jcb.getSelectedItem();
  quest.setVisible(false);
  for (int i=0;i<ids.size();i++)
    {
//		System.out.println("tentativo di connessione alla porta n' = " + i);
    CommPortIdentifier currPortId = ids.elementAt(i);
		if (currPortId.getName().equals(portName)) 
			{
      System.out.println("Connessione a "+currPortId.getName());
			portId = currPortId;
			break;
			}
    }
		if (portId == null) 
			{
    System.out.println("Impossibile trovare un dispositivo collegato ad una porta");
			return;
			}
	try 
	 {
	 // open serial port, and use class name for the appName.
    serialPort = (SerialPort) portId.open(this.getClass().getName(),TIME_OUT);
	 // set port parameters
	 serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

	 // open the streams
	 input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

	 // add event listeners
	 //serialPort.addEventListener(this);
	 //serialPort.notifyOnDataAvailable(true);
    grafica.enable(portId.getName());
	 } 
	catch (Exception e) 
		{
		System.err.println(e.toString());
		}
	}
	
public synchronized void close() 
	{
	serialPort.close();
	}

public synchronized void ricevo() 
	{
	String fermataAttuale="1";
    for(;;)
		{
		   try 
			{
			if(input.ready())
				{
				String inputLine=input.readLine();
				switch (inputLine)
				 {
          case "1":
            grafica.areaDiTesto.append("\nIl treno è arrivato alla fermata numero 1");
                           grafica.g.coloraFermata(inputLine,grafica.getVerso());
						   fermataAttuale=inputLine;
				  break;
          case "2":
            grafica.areaDiTesto.append("\nIl treno è arrivato alla fermata numero 2");
				           grafica.g.coloraFermata(inputLine,grafica.getVerso());
						   fermataAttuale=inputLine;
				  break;
          case "3":
            grafica.areaDiTesto.append("\nIl treno è arrivato alla fermata numero 3");
				           grafica.g.coloraFermata(inputLine,grafica.getVerso());
						   fermataAttuale=inputLine;
				  break;
          case "4":
            grafica.areaDiTesto.append("\nIl treno è arrivato alla fermata numero 4");
				           grafica.g.coloraFermata(inputLine,grafica.getVerso());
						   fermataAttuale=inputLine;
				  break;
          case "verso true":
            grafica.setVerso(true);
				  break;
          case "verso false":
            grafica.setVerso(false);
				  break;
          case "lasciata fermata":
            grafica.areaDiTesto.append("\n Il treno ha lasciato la fermata "+ fermataAttuale);
				                          grafica.g.coloraPercorso(fermataAttuale,grafica.getVerso());
				  break;
          default:
            grafica.areaDiTesto.append("\nArduino dice: "+inputLine);
				  break;
				 }
				}
			} 
		   catch (Exception e) 
			 {
			 System.err.println(e.toString());
			 }
		}
	}
	
void invio(char comando)
	{
	try
	  { 
	  output.write(comando);
	  }
	 catch(IOException io) 
	  {
	  System.out.println("errore input");
	  }
	}
	
public static void main(String arg[])
    {
	ControlArduino test=new ControlArduino();
	}
  }