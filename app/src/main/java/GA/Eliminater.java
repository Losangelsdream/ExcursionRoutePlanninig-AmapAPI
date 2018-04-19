/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

/**
 *
 * @author Fish
 */
public class Eliminater {
    
    private final int maxSize=200;
    
    public Population run(Population w)
    {
        System.out.println("Elimi Begin");
        if(w.staff.size()<=maxSize)return w;
        Population ans=new Population();
        for(int i=0;i<maxSize;i++)
        {
            double maxPoint=0;
            int maxIndex=0;
            for(int j=0;j<w.staff.size();j++)
            {
                if(w.staff.get(j).scale>maxPoint)
                {
                    maxPoint=w.staff.get(j).scale;
                    maxIndex=j;
                }
            }
            ans.staff.add(w.staff.get(maxIndex));
            w.staff.remove(maxIndex);
        }
        Population ans1=new Population();
        for(int i=0;i<ans.staff.size();i++)
        {
            ans.staff.get(i).live--;
            if(ans.staff.get(i).live>0)ans1.staff.add(ans.staff.get(i));
        }
        System.out.println("Elimi End");
        return ans1;
    }
}
