import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;


public class RunDir extends Frame implements ActionListener, Runnable, FilenameFilter
{
private MenuBar menu;
private Menu file,play,info;
protected MenuItem esci,nuovo,salva,rileggi,about;
protected MenuItem games[];
String dir;
String dirList[];
Vector <String>toRun;
Vector <String>toAvoid;

public RunDir()
  {
  this (".");
  }
public RunDir(String d)
  {
  super("Run directory");
  this.dir=d;
  addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e)
        {
        if (proc!=null)
          proc.destroy();
        System.exit(0);
        }
      }
    );
  menu=new MenuBar();
  setMenuBar(menu);
  file=new Menu("File");
  play=new Menu("Play");
  info=new Menu("About...");
  nuovo=new MenuItem("Nuovo");
  nuovo.addActionListener(this);
  file.add(nuovo);
  salva=new MenuItem("Salva");
  salva.addActionListener(this);
  file.add(salva);
  rileggi=new MenuItem("Rileggi");
  rileggi.addActionListener(this);
  file.add(rileggi);
  esci=new MenuItem("Esci");
  esci.addActionListener(this);
  file.add(esci);
  rescan();
  about=new MenuItem("by Carlo Schenone");
  info.add(about);
  menu.add(file);
  menu.add(play);
  menu.add(info);
  pack();
  setSize(170,55);
  setVisible(true);
  }
public void rescan()
  {
  File dirFile=new File(dir);
  String listFile="RunDir.txt";
  toRun=new Vector<String>();
  toAvoid=new Vector<String>();
  try
    {
    BufferedReader br=null;
    if (new File(dirFile,listFile).exists())
      br=new BufferedReader(new FileReader(new File(dirFile,listFile)));
     else
      {
      InputStream is=getClass().getResourceAsStream(listFile);
      if (is!=null)
        br=new BufferedReader(new InputStreamReader(is));
      }
    String linea;
    if (br!=null)
      {
      for (;(linea=br.readLine())!=null;)
        {
        if (!linea.trim().equals(""))
          {
          if (linea.charAt(0)!='-')
            toRun.add(linea);
           else
            toAvoid.add(linea.substring(1));
          }
        }
      br.close();
      }
    }
   catch (FileNotFoundException fnfe)
    {
    }
   catch (IOException fnfe)
    {
    }
  dirList=dirFile.list(this);
  for (int i=0;i<dirList.length;i++)
    {
    String pName=dirList[i].substring(0,dirList[i].length()-5);
    if (toAvoid.indexOf(pName)==-1 && toRun.indexOf(pName)<0)
      toRun.add(pName);
    }
  if (games!=null)
    play.removeAll();
  games=new MenuItem[toRun.size()];
  for (int i=0;i<games.length;i++)
    {
    games[i]=new MenuItem((String)(toRun.elementAt(i)));
    games[i].addActionListener(this);
    play.add(games[i]);
    }
  }
public boolean accept(File dir, String name)
  {
  if (name.endsWith(".java"))
    return true;
   else
    return false;
  }
static Process proc=null;
public void actionPerformed(ActionEvent ae)
  {
  if (esci==ae.getSource())
    {
    System.exit(0);
    }
   else if (rileggi==ae.getSource())
    {
    rescan();
    }
   else
    {
    for (int i=0;i<games.length;i++)
      {
      if (games[i]==ae.getSource())
        {
        prog=ae.getActionCommand();
        System.out.println("<"+prog+">");
        StringTokenizer st=new StringTokenizer(prog);
        progName=st.nextToken();
        new Thread(this).start();
        }
      }
    }
  }
String prog=null;
String progName=null;
public void run()
  {
  try
    {
/*
    if (proc!=null)
      proc.destroy();
    prog="java BoardGame strategy="+progName+" "+prog.substring(prog.indexOf(progName)+progName.length())+" fields="+progName+".labs ";
    System.out.println("Executing "+prog);
    proc=Runtime.getRuntime().exec(prog);
    InputStream ies=proc.getErrorStream();
    BufferedReader ebr=new BufferedReader(new InputStreamReader(ies));
    String elinea;
    for (;(elinea=ebr.readLine())!=null;)
      System.out.println(elinea);
    InputStream is=proc.getInputStream();
    BufferedReader br=new BufferedReader(new InputStreamReader(is));
    String linea;
    for (;(linea=br.readLine())!=null;)
      System.out.println(linea);
*/
    Class progClass=Class.forName(progName);
    System.out.println("Executing "+progName);
    String argClass[]=new String[0];
    progClass.getDeclaredMethod("main",argClass.getClass()).invoke(progClass,(Object)argClass);
    }
   catch (Exception exc)
    {
    exc.printStackTrace();
    }
  }
public static void main(String a[])
  {
  new RunDir();
  }
}
