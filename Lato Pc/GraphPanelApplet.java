import java.applet.*;
import java.security.*;
import java.awt.*;
import java.net.*;

public class GraphPanelApplet extends Applet implements ImageReader
{
String args[];
GraphPanel gp;
public void init()
  {
  int i;
  for (i=0;getParameter("graph"+i)!=null;i++)
    ;
  args=new String[i];
  for (i=0;i<args.length;i++)
    args[i]=getParameter("graph"+i);
  gp=new GraphPanel();
  gp.setImageReader(this);
  add(gp);
  }
public Image getImage(String s)
  {
  try
    {
    URL u=new URL(getCodeBase(),s);
    URLConnection uc=u.openConnection();
    uc.connect();
    return super.getImage(getCodeBase(),s);
    }
   catch(Exception ex)
    {
    return null;
    }
  }
}
