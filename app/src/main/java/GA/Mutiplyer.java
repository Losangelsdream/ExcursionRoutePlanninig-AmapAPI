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
public class Mutiplyer {
    
    private final double rate=1.0; 
    private final double[] mutirRate={0.5,0.25,0.15,0.1};
    
    public Population run(Population w,Judger jj)
    {
        System.out.println("Muti Begin");
        Population x=w;
        int lt=(int) ((double)x.staff.size()*rate);
        Wheel rou= new Wheel();
        rou.setRoul(3, mutirRate);
        for(int i=0;i<=rate;i++)
        {
            int fa=rou.RandomInt(x.staff.size());
            int ma=fa;
            while(fa==ma)ma=rou.RandomInt(x.staff.size());
            
            int faStart,faEnd,maStart,maEnd;
            int faFate=rou.getRoulInt(),maFate=rou.getRoulInt();
            
            if(x.staff.get(fa).path.size()>faFate)faStart=rou.RandomInt(x.staff.get(fa).path.size()-faFate);
            else faStart=0;
            faEnd=faStart+faFate;
            if(faEnd>=x.staff.get(fa).path.size())faEnd=x.staff.get(fa).path.size()-1;
            
            if(x.staff.get(ma).path.size()>maFate)maStart=rou.RandomInt(x.staff.get(ma).path.size()-maFate);
            else maStart=0;
            maEnd=maStart+maFate;
            if(maEnd>=x.staff.get(ma).path.size())maEnd=x.staff.get(ma).path.size()-1;
            
            Pheno son1=new Pheno();
            for(int j=0;j<=faStart;j++)son1.path.add(x.staff.get(fa).path.get(j));
            for(int j=maStart;j<=maEnd;j++)son1.path.add(x.staff.get(ma).path.get(j));
            for(int j=faEnd;j<x.staff.get(fa).path.size();j++)son1.path.add(x.staff.get(fa).path.get(j));
            son1.scale=jj.judge(son1);
            x.staff.add(son1);
            
            Pheno son2=new Pheno();
            for(int j=0;j<=maStart;j++)son2.path.add(x.staff.get(ma).path.get(j));
            for(int j=faStart;j<=faEnd;j++)son2.path.add(x.staff.get(fa).path.get(j));
            for(int j=maEnd;j<x.staff.get(ma).path.size();j++)son2.path.add(x.staff.get(ma).path.get(j));
            son1.scale=jj.judge(son2);
            x.staff.add(son2);
            
        }
        System.out.println("Muti End");
        return w;
    }
}
