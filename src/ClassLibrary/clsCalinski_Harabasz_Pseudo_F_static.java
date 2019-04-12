/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClassLibrary;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class clsCalinski_Harabasz_Pseudo_F_static {
    
    double getFCH(int NumberOfTourists,float TouristCoordinates[][],ArrayList<Integer> [] TouristClusters){
        double result;
        result=(this.getTotalSumOfSquares_SST(NumberOfTourists, TouristCoordinates)/
                this.getSumOfSquaredDistance_SSE(NumberOfTourists, TouristCoordinates, TouristClusters)-1)*
                ((NumberOfTourists-TouristClusters.length)/(TouristClusters.length-1));
        return result;
    }
    
    
    double getTotalSumOfSquares_SST(int NumberOfTourists,float TouristCoordinates[][]){
        double result=0;
         float [] OverallMeanValuesofVariables=this.getOverallMeanValuesOfVariables(TouristCoordinates);
         for(int i=0;i<NumberOfTourists;i++){
             result+=Math.pow(TouristCoordinates[i][0]-OverallMeanValuesofVariables[0],2)
                    +Math.pow(TouristCoordinates[i][1]-OverallMeanValuesofVariables[1],2)
                    +Math.pow(TouristCoordinates[i][2]-OverallMeanValuesofVariables[2],2)
                    +Math.pow(TouristCoordinates[i][3]-OverallMeanValuesofVariables[3],2);
         }
         return result;
    }

    
  float [] getOverallMeanValuesOfVariables(float TouristCoordinates[][]){
      float result[]=new float[4];
      for(int j=0;j<result.length;j++){
          result[j]=0;
      }
      for(int i=0;i<TouristCoordinates.length;i++){
         for(int j=0;j<result.length;j++){
            result[j]+=TouristCoordinates[i][j];
         }
      }
      for(int j=0;j<result.length;j++){
         result[j]/=TouristCoordinates.length;
      }
      return result;
  }
  
     double getSumOfSquaredDistance_SSE(int NumberOfTourists,float TouristCoordinates[][],ArrayList<Integer> [] TouristClusters){
        double result=0;
        for(int i=0;i<TouristClusters.length;i++){
            float [] ClusterMeanValuesOfVariables=this.getClusterMeanValuesOfVariables(TouristCoordinates, TouristClusters[i]);
            for(int j=0;j<TouristClusters[i].size();j++){
                result+=  Math.pow(TouristCoordinates[TouristClusters[i].get(j)][0]-ClusterMeanValuesOfVariables[0],2)
                         +Math.pow(TouristCoordinates[TouristClusters[i].get(j)][1]-ClusterMeanValuesOfVariables[1],2)
                         +Math.pow(TouristCoordinates[TouristClusters[i].get(j)][2]-ClusterMeanValuesOfVariables[2],2)
                         +Math.pow(TouristCoordinates[TouristClusters[i].get(j)][3]-ClusterMeanValuesOfVariables[3],2);
            }
        }
        return result;
    }
    
   float [] getClusterMeanValuesOfVariables(float TouristCoordinates[][],ArrayList<Integer> TouristCluster){
      float result[]=new float[4];
      for(int j=0;j<result.length;j++){
          result[j]=0;
      }
      for(int i=0;i<TouristCluster.size();i++){
        for(int j=0;j<result.length;j++){
            result[j]+=TouristCoordinates[TouristCluster.get(i)][j];
        }
      }
      for(int j=0;j<result.length;j++){
         result[j]/=TouristCluster.size();
      }
      return result;
  }
}
