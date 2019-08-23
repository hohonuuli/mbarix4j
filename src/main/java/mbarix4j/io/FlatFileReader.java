// Title:        MBARI java library
// Version:
// Copyright:    Copyright (c) 1999
// Author:       Brian Schlining
// Company:      MBARI

/**
 * Description:  Reads flat files into a Double array.
 *
 *@author     Brian Schlining
 *@version    0.5, 27 Sep 1999
 *
 * 12 Jul 1999; Created FlatFileReader
 * 27 Aug 1999; Fixed a bug (off by one type) in <i>this.c</i> which prevented
 * data with the correct number of lines from being read
 */
package mbarix4j.io;

import java.io.*;
import java.util.*;

/**
 * Constructors initialize memory only, to actually read the data file call the
 * readFile method. This is done because some data files are very large (
 * ~100MB) and reading them may take awhile. This allows a programmer to
 * specify when the big performance hit occurs.<BR><BR>
 *
 * FlatFileReader is meant for files with equal numbers of values on each line.
 * Lines that do not have the same number of values are ignored. It currently
 * assumes that the most commonly occuring number of values on a line is the
 * correct number of values.<BR><BR>
 *
 * Lines with the first charater of '#' or '%' are assumed to be comments
 *
 *@author     brian
 *@created    October 4, 2002
 * @deprecated
 */
@Deprecated
public class FlatFileReader {

    // ///////////////////////////////////////
    // Constructors

    /**
     * Create a FlatFileReader and intialize memory.
     *
     *@param  s                Name of file to read
     *@exception  IOException  Description of the Exception
     */
    public FlatFileReader(String s) throws IOException {
        this.file = new File(s);

        this.init();
    }


    /**
     * Create a FlatFileReader and intialize memory.
     *
     *@param  f                File object representing the file to be read
     *@exception  IOException  Description of the Exception
     */
    public FlatFileReader(File f) throws IOException {
        this.file = f;

        this.init();
    }


    /**
     * Create a FlatFileReader and intialize memory.
     *
     *@param  s                Name of file to read
     *@param  n                Number of columns of data each line should have
     *@exception  IOException  Description of the Exception
     */
    public FlatFileReader(String s, int n) throws IOException {
        this.file = new File(s);

        this.init();
    }


    /**
     * Create a FlatFileReader and intialize memory.
     *
     *@param  f                File object representing the file to be read
     *@param  n                Number of columns of data each line should have
     *@exception  IOException  Description of the Exception
     */
    public FlatFileReader(File f, int n) throws IOException {
        this.file = f;

        this.init();
    }


    // ///////////////////////////////////////
    // Public Methods

    /**
     * This method does the actual reading of the data file. It tokenizes the
     * line by using white spaces
     *
     *@exception  IOException
     */
    public void readFile() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(this.file));
        String line;
        int row = 0;
        int good = 0;
        int bad = 0;

        while ((line = in.readLine()) != null) {
            if (line.startsWith("#") || line.startsWith("%")) {

                // ignore comments
            } else {
                StringTokenizer st = new StringTokenizer(line);
                int ncol = st.countTokens();

                if (ncol == this.c) {
                    int col = 0;

                    while (st.hasMoreTokens()) {
                        this.data[row][col] =
                                new Double(st.nextToken()).doubleValue();
                        col++;
                    }

                    row++;
                    good++;
                } else {
                    bad++;
                }
            }
        }

        in.close();
    }


    /*
     *  This method does the actual reading of the data file. It tokenizes each
     *  line by using the delimiter specified in the method
     *
     *  @param delims  A string specifying the character/delimiter between each
     *  data point.
     *  @exception IOException
     */
    // public void readFile(String delims) throws IOException {}

    /**
     * This method is used to replace a single value in the array of data read
     * from a flat file. The array is accessed as data[row][column]
     *
     *@param  row     Row of desired value
     *@param  column  Column of desired value
     *@param  n       The value to be set at data[row][column]
     */
    public void setData(int row, int column, double n) {
        this.data[row][column] = n;
    }


    // ///////////////////////////////////////
    // Accessors

    /**
     * Accesor method to access the data in the flat file
     *
     *@return    data  A double array of the data contained in the flat file
     */
    public double[][] getData() {
        return this.data;
    }


    /**
     * Accesor method to access a single data point in the flat file
     *
     *@param  column  Column of data array
     *@param  row     Description of the Parameter
     *@return         d A single double value from data[row][column]
     */
    public double getData(int row, int column) {
        return this.data[row][column];
    }


    /**
     * Accesor method to get the filename of read by the FlatFileReader
     *
     *@return    filename  The name of the file used to construct a FlatFileReader
     * instance.
     */
    public String getFilename() {
        return this.file.getName();
    }


    /**
     * Return a specific column of data.
     *
     *@param  column                              The column of data to fetch.
     *@return                                     The column of data specified by <i>c</i>
     */
    public double[] getColumn(int column) {
        double[] out = new double[this.countRows()];

        for (int row = 0; row < this.countRows(); row++) {
            out[row] = this.data[row][column];
        }

        return out;
    }


    /**
     * Method to get the number of rows in the data array
     *
     *@return    The number of rows of data read.
     */
    public int countRows() {
        return this.r;
    }


    /**
     * Method to get the number of columns in the data array
     *
     *@return    The number of columns of data read.
     */
    public int countColumns() {
        return this.c;
    }


    // ///////////////////////////////////////
    // Protected Methods

    /**
     * Method declaration
     *
     *@see
     */
    protected void ID() {
        System.out.print("FlatFileReader: ");
    }


    // ///////////////////////////////////////
    // Private Methods

    /**
     * Sets up the correct size array, called by the constructor
     *
     *@exception  IOException  Description of the Exception
     */
    private void init() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        this.initializeArray(in);
        in.close();
    }


    /**
     * Makes a quick pass through the file and makes an array of the correct size.
     * It find the length of the rows that occur most and assumes that other rows
     * are erroneous and ignores them.
     *
     *@param  in               Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    private void initializeArray(BufferedReader in) throws IOException {
        int[] vec = null;
        int n = 0;
        boolean FirstLine = true;
        String line;

        // Loop though each line and sum up the occurences of each length of line
        while ((line = in.readLine()) != null) {
            if (line.startsWith("#") || line.startsWith("%")) {

                // ignore comments
            } else {
                StringTokenizer st = new StringTokenizer(line);

                n = st.countTokens();

                if (FirstLine) {
                    vec =
                            new int[n + 100];
                    // Assume that the number of columns possible is
                    FirstLine = false;
                    // length of the first row + 100
                }

                vec[n]++;
            }
        }

        // Find the length of line that occurs the most
        this.r = vec[this.c];
        // Start at Zero (this.c = 0)

        for (int i = 0; i < vec.length; i++) {
            if (vec[i] > this.r) {
                this.r = vec[i];
                this.c = i + 1;
            }
        }

        this.c = this.c - 1;
        // Don't know why I need to subtract  but it works
        this.data = new double[this.r][this.c];
    }


    /**
     * Makes a quick pass through the file and makes an array of the correct size.
     * It counts the number of rows that occur containing the specified number of
     * values.
     *
     *@param  in               Stream from the file to be read
     *@param  Col              Number of values contianed in the lines to be read
     *@exception  IOException  Description of the Exception
     */
    private void initializeArray(BufferedReader in,
            int Col) throws IOException {
        int[] vec = null;
        int n = 0;
        boolean FirstLine = true;
        String line;

        // Loop though each line and sum up the occurences of each length of line
        while ((line = in.readLine()) != null) {
            if (line.startsWith("#") || line.startsWith("%")) {

                // ignore comments
            } else {
                StringTokenizer st = new StringTokenizer(line);

                n = st.countTokens();

                if (FirstLine) {
                    vec =
                            new int[n + 100];
                    // Assume that the number of columns possible is
                    FirstLine = false;
                    // length of the first row + 100
                }

                vec[n]++;
            }
        }

        this.r = vec[Col];
        this.c = Col;
        this.data = new double[this.r][this.c];
    }


    // Debugging method

    /**
     * Method declaration
     *
     *@param  args
     *@see
     */
    public static void main(String[] args) {

        /*
         *  if (args.length != 1) {
         *  System.out.println(" ");
         *  System.out.println("Usage: java org.mbari.oasis.FlatFileReader Filename");
         *  System.out.println(" ");
         *  System.out.println(" Inputs: Filename = name of data file to be read");
         *  System.out.println(" ");
         *  } else {
         */
        FlatFileReader flatFileReader;

        try {
            flatFileReader = new FlatFileReader("d:/brian/temp/m1.specppr.temp");

            flatFileReader.readFile();
        } catch (IOException e) {}

        // }
    }


    // ///////////////////////////////////////
    // Class Variables
    private int c = 0, r = 0;
    /**
     *  Description of the Field
     */
    protected File file;
    /**
     *  Description of the Field
     */
    protected double[][] data;
}


