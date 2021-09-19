javac *.java 2> err.txt
jar cvfm RunDir.jar META-INF\MANIFESTRunDir.MF *.class *.java RunDir.txt docs 2>&1 1>> err.txt
jar cvfm GraphPanel.jar META-INF\MANIFESTPanel.MF *.class *.java Italia.gif Nodo.GIF docs 2>&1 1>> err.txt
jar cvfm GraphJPanel.jar META-INF\MANIFESTJPanel.MF *.class *.java Italia.gif Nodo.GIF docs 2>&1 1>> err.txt
jar cvfm GraphEditor.jar META-INF\MANIFESTEditor.MF *.class *.java docs 2>&1 1>> err.txt
jar cvfm GraphJEditor.jar META-INF\MANIFESTJEditor.MF *.class *.java docs Icons 2>&1 1>> err.txt
jar cvfm CircleGraphMaker.jar META-INF\MANIFESTCircleGraphMaker.MF *.class *.java docs 2>&1 1>> err.txt
jar cvfm Terremoto.jar META-INF\MANIFESTTerremoto.MF *.class *.java docs 2>&1 1>> err.txt
more err.txt
pause