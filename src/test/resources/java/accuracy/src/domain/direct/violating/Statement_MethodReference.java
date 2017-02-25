package domain.direct.violating;

import java.lang.System;
import java.util.function.Consumer;

import java.util.ArrayList;
import java.util.List;

public class Statement_MethodReference{

    public Statement_MethodReference(String[] args) {
        List names = new ArrayList();
            names.add("David");
            names.add("Richard");
            names.add("Samuel");
            names.add("Rose");
            names.add("John");
            
            Statement_MethodReference.printNames(names,System.out::println);
    }
 
    private static void printNames(List list, Consumer c ){
        list.forEach(x -> c.accept(x));
    }
}