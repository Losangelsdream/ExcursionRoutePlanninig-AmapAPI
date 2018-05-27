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
public class World {
    private Population population;
    public Population king;
    
    private Mutiplyer muti;
    private Mutor mutor;
    private Eliminater elimate;
    private Judger judger;
    private Wheel rou;
    
    private double[][] timeMutix;
    private double[][] moneyMutix;
    private double[] timeList;
    private double[] moneyList;
    private double exceptTime;
    private double exceptMoney;
    public int size;
    
    private int age;
    private final int initNumP=150;
    private final double numRate[]={0.25,0.5,0.25};
    
    private final int maxKing=5;
    
    public World(Preparer x) {
        age = 0;
        this.timeMutix=x.timeMutix;
        this.moneyMutix=x.moneyMutix;
        this.timeList=x.timeList;
        this.moneyList=x.moneyList;
        this.exceptTime=x.exceptTime;
        this.exceptMoney=x.exceptMoney;
        this.size=x.size;
    }

    public void initSetting()
    {
        rou=new Wheel();
        rou.setRoul(3,numRate);
        judger=new Judger(timeMutix,moneyMutix,timeList,moneyList,exceptTime,exceptMoney,size);
        muti=new Mutiplyer();
        mutor=new Mutor();
        elimate=new Eliminater();
        population=new Population();
        king = new Population();
    }

    public void initPopulation()
    {
        //0 start 1 end
        System.out.println("init Population Begin");
        int base=3;
//        if(this.exceptTime>7.5)
//        {
//            base=4;
//        }
//        if(this.exceptTime<=5)
//        {
//            base=2;
//        }

        for(int ss=1;ss<=initNumP;ss++)
        {
            System.out.println(ss);
            int fate=rou.getRoulInt();
            Pheno now=new Pheno();
            now.path.add(0);
            for(int xx=1;xx<=fate+base-1;xx++)
            {
                int next;
                while(true)
                {
                    next=rou.RandomInt(size);
                    int ok=1;
                    for(int i=0;i<now.path.size();i++)
                        if(next==now.path.get(i))
                        {
                            ok=0;
                            break;
                        }
                    if(next==0 || next==1)ok=0;
                    if(ok==1)break;
                }
                now.path.add(next);
            }
            now.path.add(1);
            population.staff.add(now);
        }
        for(int i=0;i<maxKing;i++)king.staff.add(population.staff.get(i));
         System.out.println("init Population End");
    }
    
    public void selectKing()
    {
        double minPoint=100;
        int minIndex=0;
        for(int i=0;i<king.staff.size();i++)
        {
            if(king.staff.get(i).scale<minPoint)
            {
                minPoint=king.staff.get(i).scale;
                minIndex=i;
            }
        }
        
        for(int i=0;i<population.staff.size();i++)
        {
            if(population.staff.get(i).scale>minPoint && !king.exist(population.staff.get(i)))
            {
                
                king.staff.remove(minIndex);
                king.staff.add(population.staff.get(i));
                minPoint=100;
                for(int j=0;j<king.staff.size();j++)
                {
                    if(king.staff.get(j).scale<minPoint)
                    {
                        minPoint=king.staff.get(j).scale;
                        minIndex=j;
                    }
                }
            }
        }
    }
    
    public boolean goNext()
    {
        if(age>2000)return false;
        return true;
    }
    
    public void Evolution()
    {
        System.out.println("N0."+age+" Begin");
         //population=muti.run(population,judger);
        population=mutor.run(population,judger);
         population=mutor.run(population,judger);
         population=elimate.run(population);
         System.out.println("Num of this G "+population.staff.size());
         selectKing();
         System.out.println("No."+age+" End");
         age++;
    }
    

    
}
