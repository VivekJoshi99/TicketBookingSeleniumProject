package org.testing;

import java.util.List;

public class Utils {
    public void compare(List<?> list1, List<?> list2){
        if(list1.size()==list2.size()){
            for(int i=0 ; i<list1.size(); i++){
                if(!(list1.get(i).equals(list2.get(i)))){
                    System.out.println("Elements of actual and expected not matching" + "actual is : " + list1.get(i) + " and expected is : " + list2.get(i));
                }
            }
        }
        else throw new RuntimeException("List size doesn't match");
    }

}
