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
public class Mutor {
    
    private final double[] mutorRate={0.5,0.25,0.15,0.1};
    
    public Population run(Population w,Judger jj)
    {
        System.out.println("Mutor Begin");
        Population x=w;
        int l=x.staff.size();
        int fate;
        Wheel rou= new Wheel();
        rou.setRoul(4, mutorRate);
        
        for(int i=0;i<l;i++)
        {
            fate=rou.getRoulInt();
            if(x.staff.get(i).path.size()>=9)continue;
            if(fate!=0)
            {
                int start;
                if(x.staff.get(i).path.size()>fate)start=rou.RandomInt(x.staff.get(i).path.size()-fate);
                else start=0;
                int end=start+fate;
                if(end>=x.staff.get(i).path.size())end=x.staff.get(i).path.size()-1;
                int ep;
                while(true)
                {
                    ep=rou.RandomInt(jj.size);
                    int ok=1;
                    for(int j=0;j<x.staff.get(i).path.size();j++)
                    {
                        if(ep==x.staff.get(i).path.get(j))
                        {
                            ok=0;
                            break;
                        }
                    }
                    if(ep==0 || ep==1)ok=0;
                    if(ok==1)break;
                }
                Pheno newMan=new Pheno();
                for(int j=0;j<=start;j++)newMan.path.add(x.staff.get(i).path.get(j));
                newMan.path.add(ep);
                for(int j=end;j<x.staff.get(i).path.size();j++)newMan.path.add(x.staff.get(i).path.get(j));
                x.staff.add(newMan);
            }
        }
        System.out.println("Mutor End");
        return x;
    }
}
