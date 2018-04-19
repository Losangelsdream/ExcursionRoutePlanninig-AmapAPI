/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fish
 */
public class Population {
    public List<Pheno> staff;
    public int size;
    
    Population()
    {
        staff=new ArrayList<Pheno>();
    }
    
    public boolean exist(Pheno x)
    {
        for(int i=0;i<staff.size();i++)
        {
            if(staff.get(i).same(x))return true;
        }
        return false;
    }
}
