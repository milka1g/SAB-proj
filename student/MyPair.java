/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import rs.etf.sab.operations.PackageOperations.Pair;

/**
 *
 * @author mn170387d
 */
public class MyPair<A,B> implements Pair{
    private A first;
    private B second;
    
    public MyPair(A f, B s){
        first = f;
        second = s;
    }
    
    @Override
    public Object getFirstParam() {
        return first;
    }

    @Override
    public Object getSecondParam() {
        return second;
    }
    
}
