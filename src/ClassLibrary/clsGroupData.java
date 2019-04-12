/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassLibrary;

import java.io.*;

/**
 *
 * @author user
 */
public class clsGroupData {

     int NumberOfTours,
                NumberOfVertices,
                NumberOfPersons,// Change for MCTOPMTW
                MinimumNumberOfPersonsInSubgroup=1;// Change for MCTOPMTW
    
     float StartPoint []=new float [7];// Seven details for the start point
     float POI[][];//details for each point of interest
     int Person[][]; 

    clsGroupData(String filename)throws IOException{
       readGroupDataFromFile(filename);
      }
   
    void readGroupDataFromFile(String filename) throws IOException {
       Reader r = new BufferedReader(new FileReader(filename));
        StreamTokenizer stok = new StreamTokenizer(r);
        stok.parseNumbers();
        stok.nextToken();
        while (stok.ttype != StreamTokenizer.TT_EOF) {
            if (stok.ttype == StreamTokenizer.TT_NUMBER)
            {      
                this.NumberOfTours=(int)stok.nval;
            }
            else
                System.out.println("Nonnumber: " + stok.sval);
            stok.nextToken();
            if (stok.ttype == StreamTokenizer.TT_NUMBER)
            {      
                this.NumberOfVertices=(int)stok.nval;
            }
            else
                System.out.println("Nonnumber: " + stok.sval);
            stok.nextToken();

            if (stok.ttype == StreamTokenizer.TT_NUMBER)
            {      
                this.NumberOfPersons=(int)stok.nval;
            }
            else
                System.out.println("Nonnumber: " + stok.sval);
            stok.nextToken();

            if (stok.ttype == StreamTokenizer.TT_NUMBER)
            {      
                this.MinimumNumberOfPersonsInSubgroup=(int)stok.nval;
            }
            else
                System.out.println("Nonnumber: " + stok.sval);
            stok.nextToken();
           
            for(int i=0;i<StartPoint.length;i++){
                    if (stok.ttype == StreamTokenizer.TT_NUMBER)
                    {      
                         StartPoint[i]=(float)stok.nval;
                    }
                    else
                        System.out.println("Nonnumber: " + stok.sval);
                    stok.nextToken();   
            }
            
            POI=new float [NumberOfVertices][21+NumberOfPersons];
            for(int i=0;i<POI.length;i++){
                for(int j=0;j<POI[0].length;j++){
                    if (stok.ttype == StreamTokenizer.TT_NUMBER)
                    {      
                         POI[i][j]=(float)stok.nval;
                    }
                    else
                        System.out.println("Nonnumber: " + stok.sval);
                    stok.nextToken();   
                }
            }
//            System.out.println("Test");
            Person=new int[NumberOfPersons][12+2*(NumberOfPersons-1)];
            for(int i=0;i<NumberOfPersons;i++){
                for(int j=0;j<12+2*(NumberOfPersons-1);j++){
                    if (stok.ttype == StreamTokenizer.TT_NUMBER)
                    {      
                        Person[i][j]=(int)stok.nval;
                    }
                    else
                        System.out.println("Nonnumber: " + stok.sval);
                    stok.nextToken();  
                }    
            }
            //System.out.println("Test");
        }
        
   }
}
