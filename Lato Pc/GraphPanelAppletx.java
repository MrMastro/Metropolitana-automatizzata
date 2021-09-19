import java.applet.*;
import java.security.*;

public class GraphPanelAppletx extends Applet
{
String args[];
public void init()
  {
  int i;
  for (i=0;getParameter("graph"+i)!=null;i++)
    ;
  args=new String[i];
  for (i=0;i<args.length;i++)
    args[i]=getParameter("graph"+i);
  AccessController.doPrivileged(new PrivilegedAction()
    {
    public Object run()
      {
      GraphPanel.main(args);
      return new Object();
      }
    });
  }
}
