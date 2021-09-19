public interface GraphListener
{
public void arcSelected(ArcSign a);
public void nodeSelected(NodeSign n);
public void zoomed(int zoom);
public void moved(int x, int y);
}