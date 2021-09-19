import java.util.Scanner;
import java.io.*;

public class Matrix
{
/********
 * The values of the Matrix
 ********/
double m[][];

/********
 * Creates a matrix from a bidimensional array
 *
 * @param mat the bidimensional array
 ********/
public Matrix(double mat[][])
  {
  if (mat==null || mat.length==0)
    m=new double[0][0];
   else
    if (!isRect(mat))
      {
      m=null;
      throw new ArrayIndexOutOfBoundsException("Not a rectangular array");
      }
     else
      {
      m=new double[mat.length][mat[0].length];
      for (int r=0;r<mat.length;r++)
        for (int c=0;c<mat[r].length;c++)
          m[r][c]=mat[r][c];
      }
  }
/********
 * Creates a matrix from an InputStream in which the first integer value
 * is the number of rows of the Matrix and the second integer value is
 * the number of columns. The following rows*columnes double values are
 * the values of the Matrix 
 *
 * @param is the InputStream
 ********/
public Matrix(InputStream is)
  {
  load(is);
  }
/********
 * Loads a matrix from an InputStream in which the first integer value
 * is the number of rows of the Matrix and the second integer value is
 * the number of columns. The following rows*columnes double values are
 * the values of the Matrix 
 *
 * @param is the InputStream
 ********/
public void load(InputStream is)
  {
  load(new InputStreamReader(is));
  }
/********
 * Creates a matrix from a Reader in which the first integer value
 * is the number of rows of the Matrix and the second integer value is
 * the number of columns. The following rows*columnes double values are
 * the values of the Matrix 
 *
 * @param r the Reader
 ********/
public Matrix(Reader r)
  {
  load(r);
  }
/********
 * Loads a matrix from an InputStream in which the first integer value
 * is the number of rows of the Matrix and the second integer value is
 * the number of columns. The following rows*columnes double values are
 * the values of the Matrix 
 *
 * @param r the Reader
 ********/
public void load(Reader r)
  {
  Scanner input=new Scanner(r);
  int rows=0, cols=0;
  if (input.hasNextInt())
    rows=input.nextInt();
  if (input.hasNextInt())
    cols=input.nextInt();
  m=new double[rows][cols];
  for (int i=0;i<rows;i++)
    for (int j=0;j<cols;j++)
      if (input.hasNextDouble())
        m[i][j]=input.nextDouble();
  }
/********
 * Returns the Matrix that is the sum of this Matrix to another Matrix
 *
 * @param mat the Matrix to sum
 *
 * @return the resulting sum Matrix
 ********/
public Matrix sum(Matrix mat)
  {
  return new Matrix(sum(m,mat.getMatrix()));
  }
/********
 * Returns the sum of two bidimensional arrays.
 *
 * @param m1 the first bidimensional array
 * @param m2 the second bidimensional array
 *
 * @return the bidimensional array that is the sum of <b>m1</b> and <b>m2</b>
 ********/
public static double [][] sum(double m1[][], double m2[][])
  {
  if (!isRect(m1) || !isRect(m2) || m1.length!=m2.length || m1[0].length!=m2[0].length)
    return null;
  double r[][]=new double[m1.length][m1[0].length];
  for (int a=0;a<m1.length;a++)
    for (int b=0;b<m1[0].length;b++)
      r[a][b]=m1[a][b]+m2[a][b];
  return r;
  }
/********
 * Tells if a bidimensional array is a rectangle
 *
 * @param m the bidimensional array
 *
 * @return <b>true</b> if the array is a rectangle, <b>false</b> otherwise
 ********/
public static boolean isRect(double m[][])
  {
  if (m==null || m.length<1)
    return false;
  boolean rect=true;
  for (int r=0;r<m.length && rect;r++)
    if (m[r].length!=m[0].length) rect=false;
  return rect;
  }
/********
 * Returns the Matrix that is the cartesian product of this Matrix to another Matrix
 *
 * @param mat the Matrix to make the product with
 *
 * @return the resulting cartesian product Matrix
 ********/
public Matrix prod(Matrix mat)
  {
  return new Matrix(prod(m,mat.getMatrix()));
  }
/********
 * Returns the cartesian product of two bidimensional arrays.
 *
 * @param m1 the first bidimensional array
 * @param m2 the second bidimensional array
 *
 * @return the bidimensional array that is the cartesian product of <b>m1</b> and <b>m2</b>
 ********/
public static double [][] prod(double m1[][], double m2[][])
  {
  if (!isRect(m1) || !isRect(m2) || m1[0].length!=m2.length) return null;
  double r[][]=new double[m1.length][m2[0].length];
  for (int a=0;a<m1.length;a++)
    for (int b=0;b<m2[0].length;b++)
      {
      r[a][b]=0;
      for (int c=0;c<m2.length;c++)
        {
        r[a][b]+=m1[a][c]*m2[c][b];
        }
      }
  return r;
  }
/********
 * Returns the Matrix that n-th power of this Matrix to another Matrix
 *
 * @param n the esponent of the power
 *
 * @return the resulting power Matrix
 ********/
public Matrix pow(int n)
  {
  return new Matrix(pow(m,n));
  }
/********
 * Returns the n-th power of a bidimensional array.
 *
 * @param m the first bidimensional array
 * @param n the esponent of the power
 *
 * @return the bidimensional array that is the elevation of <b>m</b> to the <b>n</b>-th power
 ********/
public static double [][] pow(double m[][], int n)
  {
  if (n==0)
    return identity(m.length);
   else
    {
    return prod(m,pow(m,n-1));
    }
  }
/********
 * Returns the number of rows of this Matrix
 *
 * @return the number of rows
 ********/
public int getRowsNumber()
  {
  return m.length;
  }
/********
 * Returns the number of columns of this Matrix
 *
 * @return the number of columns
 ********/
public int getColumnsNumber()
  {
  if (m.length!=0)
    return m[0].length;
   else
    return -1;
  }
/********
 * Returns the order of this Matrix if it is a square matrix
 *
 * @return the order of this Matrix if it is a square matrix, -1 otherwise
 ********/
public int order()
  {
  if (m.length!=0 && m.length==m[0].length)
    return m.length;
   else
    return -1;
  }
/********
 * Gets the bidimensional array representing this Matrix
 *
 * @return the bidimensional array
 ********/
public double [][] getMatrix()
  {
  if (m.length==0)
    return new double[0][0];
   else
    {
    double mat[][]=new double[m.length][m[0].length];
    for (int r=0;r<mat.length;r++)
      for (int c=0;c<mat[r].length;c++)
        mat[r][c]=m[r][c];
    return mat;
    }
  }
/*******
 * Returns the identity Matrix
 *
 * @param n the order of the identity Matrix
 *
 * @return the identity Matrix
 *******/
public static Matrix one(int n)
  {
  return new Matrix(identity(n));
  }
/*******
 * Returns a bidimensional array containing an identity matrix
 *
 * @param n the order of the identity matrix
 *
 * @return the identity matrix bidimensional array
 *******/
public static double[][] identity(int n)
  {
  if (n<0) return null;
  double res[][]=new double[n][n];
  for (int r=0;r<n;r++)
    for (int c=0;c<n;c++)
      res[r][c]=(r==c)?1:0;
  return res;
  }
/*******
 * Returns the zero Matrix
 *
 * @param n the order of the zero Matrix
 *
 * @return the zero Matrix
 *******/
public static Matrix zero(int n)
  {
  return new Matrix(nullMatrix(n));
  }
/*******
 * Returns a bidimensional array containing an zero matrix
 *
 * @param n the order of the zero matrix
 *
 * @return the zero matrix bidimensional array
 *******/
public static double[][] nullMatrix(int n)
  {
  if (n<0) return null;
  double res[][]=new double[n][n];
  for (int r=0;r<n;r++)
    for (int c=0;c<n;c++)
      res[r][c]=0;
  return res;
  }
/*******
 * Returns a String representing this Matrix
 *
 * @return the String to represent this Matrix
 *******/
public String toString()
  {
  String ris="";
  for (int i=0;i<m.length;i++)
    {
    for (int j=0;j<m[i].length;j++)
      {
      ris+=(m[i][j]+"\t");
      }
    ris+="\n";
    }
  return ris;
  }
/********
 * Tells if an Object is equal to this Matrix
 *
 * @param mi the Object to compare
 *
 * @return <b>true</b> if mi is a Matrix with the same elements, <b>false</b> otherwise
 ********/
public boolean equals(Object mi)
  {
  if (!(mi instanceof Matrix))
    return false;
   else
    {
    Matrix mat = (Matrix) mi;
    if (mat.getRowsNumber()!=this.getRowsNumber() ||
        mat.getColumnsNumber()!=this.getColumnsNumber())
      return false;
    boolean ris= true;
    for (int i=0;i<m.length;i++)
      for (int j=0;j<m[i].length;j++)
        if (mat.m[i][j]!=m[i][j])
          return false;
    return ris;
    }
  }
public static void main(String ar[])
  {
  System.out.println("scrivere prima il numero di righe e colonne della matrice quadrata e poi gli elementi");
  Matrix prova=new Matrix(System.in);
  System.out.println(prova);
  prova=prova.pow(prova.order());
  System.out.println(prova);
  
  double a[][]={{3,2,-1,2}
               ,{-1,3,1,0}
               ,{0,1,2,-1}
               ,{1,0,-2,0}
               };
  double d[][]={{0,0,0,1}
               ,{1,0,1,0}
               ,{0,1,0,0}
               ,{1,0,0,0}
               };
  Matrix am=new Matrix(a);
  System.out.println(am);
  for (int c=0;c<a.length;c++)
    {
    for (int b=0;b<a.length;b++)
      System.out.print(a[c][b]+"\t");
    System.out.println();
    }
  System.out.println();
  a=pow(a,a.length);
  for (int c=0;c<a.length;c++)
    {
    for (int b=0;b<a.length;b++)
      System.out.print(a[c][b]+"\t");
    System.out.println();
    }
  System.out.println();
  am=new Matrix(a);
  System.out.println(am);
  for (int c=0;c<d.length;c++)
    {
    for (int b=0;b<d.length;b++)
      System.out.print(d[c][b]+"\t");
    System.out.println();
    }
  System.out.println("ordine:"+am.order());
  am=new Matrix(d);
  System.out.println(am);
  a=pow(d,d.length);
  for (int c=0;c<a.length;c++)
    {
    for (int b=0;b<a.length;b++)
      System.out.print(a[c][b]+"\t");
    System.out.println();
    }
  System.out.println("ordine:"+am.order());
  am=new Matrix(am.pow(am.order()).getMatrix());
  System.out.println(am);
  }
}
