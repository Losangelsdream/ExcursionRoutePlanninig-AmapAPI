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
public class Pheno {
    public List<Integer> path;
    public double scale;
    public int live;
    public final double[] liveRate={0.25,0.5,0.25};
    
    Pheno()
    {
        Wheel r=new Wheel();
        r.setRoul(3, liveRate);
        this.live=3+r.getRoulInt()-1;
        path=new ArrayList<Integer>();
    }
    
    public boolean same(Pheno x)
    {
        if(path.size()!=x.path.size())return false;
        int ok=0;
        for(int i=0;i<path.size();i++)
        {
            if(path.get(i).compareTo(x.path.get(i))!=0)
            {
                ok=1;
                break;
            }
        }
        if(ok==0)return true;
        else return false;
    }
}
