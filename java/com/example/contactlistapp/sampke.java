package com.example.contactlistapp;

import java.util.ArrayList;
import java.util.Set;

public class sampke {

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    String abc;

    public int[] getNumbers(){
        int arr[]= new int[100];
        for (int i=0; i<100; i++){
            arr[i]= i+1;
        }

        return arr;

    }

    /*public void convertToSet(int[] arr){
        int index=0;
        ArrayList<Set<Integer>> list= new ArrayList<Set<Integer>>();


        do{
            for (int i=index;i<100; i++){
                for(int j=0;j<10;j++){
                    //set[i]= arr[i];
                    if(i%9==0){
                        //list.add(set);
                        index=i+1;
                        break;
                    }
                    if(list.size()%5==0){
                        for (i:
                             list) {
                           // print(i);
                        }
                    }
                }

            }

        }while(index>=100);

    }*/

    void main(String[] args){

        System.out.println();


    }
}
