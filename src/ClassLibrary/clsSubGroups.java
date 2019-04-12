/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassLibrary;
import java.util.ArrayList;

import java.io.IOException;

/**
 *
 * @author user
 */
public class clsSubGroups {
    
   ArrayList<Integer> [] getTouristClusters(clsGroupData groupdata){
       ArrayList<Integer> [] result;
       float [][] TouristCoordinates=getTouristCoordinates(groupdata.POI,groupdata.Person);
       int MaxNumberOfClusters=groupdata.NumberOfPersons-1;
       int NumberOfIterations=20;
       result=this.getTouristClustersInMaxRuns(TouristCoordinates, MaxNumberOfClusters, groupdata.NumberOfPersons, NumberOfIterations);
       this.printSubgroups(result);  
       return result;
   }
   ArrayList<Integer> [] getTouristClustersInMaxRuns(float [][] TouristCoordinates, int MaxNumberOfClusters,
           int NumberOfPersons,int NumberOfIterations){
       double [] ResultEvaluation=new double[1];
       ArrayList<Integer> [] result=this.getTouristClustersInSingleRun(TouristCoordinates, 
               MaxNumberOfClusters, NumberOfPersons, NumberOfIterations,ResultEvaluation);
       for(int i=0;i<NumberOfIterations;i++){
           double [] CurrenResultEvaluation=new double[1]; 
           ArrayList<Integer> [] CurrentResult=this.getTouristClustersInSingleRun(TouristCoordinates, MaxNumberOfClusters,
                    NumberOfPersons, NumberOfIterations, CurrenResultEvaluation);
           if(CurrenResultEvaluation[0]>ResultEvaluation[0]){
               ResultEvaluation[0]=CurrenResultEvaluation[0];
               result=new ArrayList[CurrentResult.length];
               System.arraycopy(CurrentResult, 0, result, 0, CurrentResult.length);
           }
//            this.printSubgroups(result);  
//            System.out.println("pseudoF = "+ResultEvaluation[0]);  
       }
       return result;
   }
   ArrayList<Integer> [] getTouristClustersInSingleRun(float [][] TouristCoordinates, int MaxNumberOfClusters,
        int NumberOfPersons,int NumberOfIterations,double [] ResultEvaluation){
        ArrayList<Integer> [] BestTouristClustersOfIteration;
        int InitialNumberOfClusters=2;
        clsCalinski_Harabasz_Pseudo_F_static pseudoF=new clsCalinski_Harabasz_Pseudo_F_static();
        BestTouristClustersOfIteration=getKmeansSubGroups(InitialNumberOfClusters,NumberOfPersons,TouristCoordinates);
        double Best_Pseudo_F_Evaluation=pseudoF.getFCH(NumberOfPersons, TouristCoordinates, BestTouristClustersOfIteration);
        for(int CurrentNumberOfClusters=3;CurrentNumberOfClusters<=MaxNumberOfClusters;CurrentNumberOfClusters++){
            ArrayList<Integer> [] CurrentTouristClusters=getKmeansSubGroups(CurrentNumberOfClusters,NumberOfPersons,TouristCoordinates);
            double Current_Pseudo_F_Evaluation=pseudoF.getFCH(NumberOfPersons, TouristCoordinates, CurrentTouristClusters);
            if(Current_Pseudo_F_Evaluation>Best_Pseudo_F_Evaluation){
                Best_Pseudo_F_Evaluation=Current_Pseudo_F_Evaluation;
                BestTouristClustersOfIteration=new ArrayList[CurrentTouristClusters.length];//Change the length of the array
                System.arraycopy(CurrentTouristClusters, 0, BestTouristClustersOfIteration, 0, CurrentTouristClusters.length);
            }
        }
        ResultEvaluation[0]=Best_Pseudo_F_Evaluation;
        return BestTouristClustersOfIteration;
   }
   
   ArrayList [] getKmeansSubGroups(int NumberOfClusters, int NumberOfPersons,float [][] TouristCoordinates){
        int [] Centroids= getRandomCentroids(NumberOfClusters, NumberOfPersons);
        float [][] CentroidCoordinates=this.getInitialCentroidCoordinates(Centroids, TouristCoordinates);
        ArrayList<Integer> [] BestSubGroups=this.getSubGroups(NumberOfClusters,CentroidCoordinates,TouristCoordinates,NumberOfPersons);
        boolean TouristsChangedGroup;
//        int TestIteration=0;
        do
        {
            CentroidCoordinates=this.getCentroidsCoordinates(NumberOfClusters, NumberOfPersons, BestSubGroups, TouristCoordinates);
            ArrayList<Integer> [] NewSubGroups=getSubGroups(NumberOfClusters,CentroidCoordinates,TouristCoordinates,NumberOfPersons);
            if(this.SubGropusAreChanged(NewSubGroups, BestSubGroups)){
                System.arraycopy(NewSubGroups, 0, BestSubGroups, 0, BestSubGroups.length);
                TouristsChangedGroup=true;
            }
            else
            {
                TouristsChangedGroup=false;
            }
//            System.out.println("TestIteration "+TestIteration);
//            TestIteration++;
        } 
        while (TouristsChangedGroup);   
        return BestSubGroups;
   }
  float [][] getInitialCentroidCoordinates(int [] Centroids,float [][] TouristCoordinates){
      float [][] result=new float[Centroids.length][4];
      for(int i=0;i<result.length;i++){
          for(int j=0;j<result[0].length;j++){
              result[i][j]=TouristCoordinates[Centroids[i]][j];
          }
      }
      return result;
  }

   void printSubgroups( ArrayList<Integer> [] SubGroups){
       for(int i=0;i<SubGroups.length;i++){
           System.out.println("Group "+(i+1));
           for(int j=0;j<SubGroups[i].size();j++){
               System.out.print(" "+(SubGroups[i].get(j)+1));
           }
           System.out.println();
       }
   }
   void printCentorids(int [] Centroids){
       System.out.print("Centroids: ");
       for(int i=0;i<Centroids.length;i++){
           System.out.print(Centroids[i]+" ");
       }
       System.out.println();
   }

   boolean SubGropusAreChanged( ArrayList<Integer> [] CurrentSubGroups, ArrayList<Integer> [] BestSubGroups){
       boolean result=false;
       OuterLoop: for(int i=0;i<CurrentSubGroups.length;i++)
       {
           if(CurrentSubGroups[i].size()==BestSubGroups[i].size()){
               for(int j=0;j<CurrentSubGroups[i].size();j++){
                    if(CurrentSubGroups[i].get(j) !=BestSubGroups[i].get(j)){
                        result=true;
                        break OuterLoop; 
                    }
               }
           }
           else
           {
               result=true;
               break; 
           }
       }
       return result;

   }
   ArrayList<Integer> [] getSubGroups(int NumberOfClusters, float [][] CentroidCoordinates, float [][] TouristCoordinates, int NumberOfPersons){
       ArrayList<Integer> [] result=new ArrayList[NumberOfClusters];
       for(int i=0;i<result.length;i++){
           result[i]=new ArrayList();
       }
      
       for(int i=0;i<NumberOfPersons;i++){
            int IndexOfClosestCentroidToTourist=this.getIndexOfClosestCentroidToTourist(i, CentroidCoordinates, TouristCoordinates);
            result[IndexOfClosestCentroidToTourist].add(i);         
       }
       return result;
   }
   ArrayList<Integer> getNonCentroids(int [] Centroids, int NumberOfPersons){
       ArrayList<Integer> result=new ArrayList();
       for(int i=0;i< NumberOfPersons;i++){
          boolean IsCentorid=false;
           for(int j=0;j<Centroids.length;j++){
               if(i==Centroids[j]){
                   IsCentorid=true;
                   break;
               }
           }
           if(!IsCentorid){
              result.add(i);
           }
       }
       return result;
   }
   int getIndexOfClosestCentroidToTourist(int TouristIndex,float CentroidCoordinates[][],float [][] TouristCoordinates){
       float MinimalDistance=this.getDistanceFromTouristToCentroid(CentroidCoordinates[0], TouristIndex, TouristCoordinates);
       int ClosestCentroid=0;
       for(int i=1;i<CentroidCoordinates.length;i++){
           float DistanceToCurrentCentroid=this.getDistanceFromTouristToCentroid(CentroidCoordinates[i], TouristIndex, TouristCoordinates);
           if(DistanceToCurrentCentroid<MinimalDistance){
               MinimalDistance=DistanceToCurrentCentroid;
               ClosestCentroid=i;
           }
       }
       return ClosestCentroid;
   }
   float getDistanceFromTouristToCentroid(float CentroidCoordinates[], int TouristIndex,float [][] TouristCoordinates ){
       float result;
        result=(float)(
        Math.sqrt(
                    Math.pow((CentroidCoordinates[0]-
                    TouristCoordinates[TouristIndex][0]),2)              
                    +Math.pow((CentroidCoordinates[1]-
                    TouristCoordinates[TouristIndex][1]),2)
                    +Math.pow((CentroidCoordinates[2]-
                    TouristCoordinates[TouristIndex][2]),2)
                    +Math.pow((CentroidCoordinates[3]-
                    TouristCoordinates[TouristIndex][3]),2)
                    )
        );
       return result;
   } 
   int [] getRandomCentroids(int NumberOfClusters,int NumberOfPersons){
       int result[]=new int[NumberOfClusters];
       ArrayList<Integer> PossibleCentroids=new ArrayList();
       for(int i=0;i<NumberOfPersons;i++){
           PossibleCentroids.add(i);
       }
       for(int i=0;i<NumberOfClusters;i++){
           int RandomIndex=clsGeneral.getRandomNumber(PossibleCentroids.size());
           result[i]=PossibleCentroids.get(RandomIndex);
           PossibleCentroids.remove(RandomIndex);
//           System.out.println("Centroid "+(CurrentNumberOfClusters+1)+":"+BestTouristClustersOfIteration[CurrentNumberOfClusters]);
       }
       return result;
   }
   float [][] getCentroidsCoordinates(int NumberOfClusters,int NumberOfPersons, 
                   ArrayList<Integer> [] SubGroups, float [][] TouristCoordinates){
       float result[][]=new float[NumberOfClusters][4];
       for(int i=0;i<SubGroups.length;i++){
           result[i]=this.getCentoridCoordinates(SubGroups[i], TouristCoordinates);
       }
       return result;
   }
   float [] getCentoridCoordinates(ArrayList<Integer> SubGroup,float [][] TouristCoordinates){
       float [] result= new float[4];
       for(int i=0;i<result.length;i++){
            result[i]=0;
       }
       for(int i=0;i<SubGroup.size();i++){
           for(int j=0;j<result.length;j++){
               result[j]+=TouristCoordinates[SubGroup.get(i)][j];
           }
       }
       for(int i=0;i<result.length;i++){
           result[i]/=SubGroup.size();
       }
       return result;
   }
   
   float [][] getTouristCoordinates(float POI[][],int Person[][]){
        float result[][]=new float[Person.length][4];
        int MaxScore=this.getMaximalScore(POI, Person.length);
        int MaxRelationship=this.getMaximalRelationship(Person);
        float MaxTypeCoordinates[]=this.getMaxTypeCoordinates(Person);
        float BudgetLimitCoordinates[]=this.getBudgetLimitCoordinates(Person);
        for(int i=0;i<Person.length;i++){
            float SfSum=0;
            for(int j=0;j<POI.length;j++){
                SfSum+=POI[j][4+i];
            }
            result[i][0]=SfSum/POI.length;//SF coordinate
            result[i][0]=100*(result[i][0]/((float)MaxScore));
            
            float SrSum=0;
            for(int j=0;j<Person.length-1;j++){
                SrSum+=Person[i][2+j];
            }
            result[i][1]=SrSum/(Person.length-1);
            result[i][1]=100*(result[i][1]/((float)MaxRelationship));
            result[i][2]=MaxTypeCoordinates[i];
            result[i][3]=BudgetLimitCoordinates[i];
        }
        return result;
    }   
   int getMaximalScore(float POI[][], int NumberOfPersons){
       int Max=(int)POI[0][4];
       for(int i=0;i<POI.length;i++){
           for(int j=4;j<4+NumberOfPersons;j++){
               if(POI[i][j]>Max){
                   Max=(int)POI[i][j];
               }
           }
       }
       return Max;
   }
      int getMaximalRelationship( int Person[][]){
       int Max=Person[0][2+2*(Person.length-1)];
       for(int i=0;i<Person.length;i++){
           for(int j=2;j<2+Person.length-1;j++){
               if(Person[i][j]>Max){
                   Max=Person[i][j];
               }
           }
       }
       return Max;
   }
   float [] getMaxTypeCoordinates(int Person[][]){
       int MaxValue=this.getTypeMaximalValue(Person);
       int MeanValue=MaxValue/2;
       float TypeCoordinates[]=new float[Person.length];
       for(int i=0;i<Person.length;i++){
           TypeCoordinates[i]=0;
           for(int j=2+2*(Person.length-1);j<Person[0].length;j++){
               int CurrentTypeValue=Person[i][j];
               if(CurrentTypeValue>MeanValue && CurrentTypeValue<=2*MeanValue)
                    TypeCoordinates[i]+=(2*MeanValue-CurrentTypeValue);
               else
                   TypeCoordinates[i]+=(CurrentTypeValue);
           }
           TypeCoordinates[i]=100*(TypeCoordinates[i]/(10*MeanValue));
       }
       return TypeCoordinates;
       //System.out.println("Test");
   }
   
   int getTypeMaximalValue( int Person[][]){
       int Max=Person[0][2+2*(Person.length-1)];
       for(int i=0;i<Person.length;i++){
           for(int j=2+2*(Person.length-1);j<Person[0].length;j++){
               if(Person[i][j]>Max){
                   Max=Person[i][j];
               }
           }
       }
       return Max;
   }
   float [] getBudgetLimitCoordinates(int Person[][]){
       int MaxValue=this.getBudgetMaxValue(Person);
       float BudgetLimitCoordinates[]=new float[Person.length];
       for(int i=0;i<Person.length;i++){
          BudgetLimitCoordinates[i]=100*(Person[i][1]/((float)MaxValue));
       }
       return BudgetLimitCoordinates;
       //System.out.println("Test");
   }
   int getBudgetMaxValue( int Person[][]){
       int Max=Person[0][1];
       for(int i=0;i<Person.length;i++){
           if(Person[i][1]>Max){
               Max=Person[i][1];
           }
       }
       return Max;
   }
   
}
