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
public class Judger {
    
    private double[][] timeMutix;
    private double[][] moneyMutix;
    private double[] timeList;
    private double[] moneyList;
    private double time;
    private double money;
    public int size;
    
    Judger(double[][] tM,double[][] mM,double[] tL,double[] mL,double time,double money,int y)
    {
        this.time=time;
        this.money=money;
        size=y;
        timeMutix=tM;
        moneyMutix=mM;
        timeList=tL;
        moneyList=mL;
    }
    
    public double judge(Pheno x)
    {
        double sumTime=0,roadTime=0,difTime,timeRate;
        double sumMoney=0,difMoney=0;
        for(int i=0;i<x.path.size()-1;i++)
        {
            int now=x.path.get(i);
            int next=x.path.get(i+1);
            sumTime+=timeList[now];
            sumTime+=timeMutix[now][next];
            sumMoney+=moneyList[now];
            sumMoney+=moneyMutix[now][next];
            roadTime+=timeMutix[now][next];
        }
        difTime=Math.abs(time-sumTime);
        difMoney=Math.abs(money-sumMoney);
        timeRate=(sumTime-roadTime)/sumTime;
        double timeRateUsed=0;
        if(timeRate>=2/3)
        {
            timeRateUsed=1-(1-timeRate)*1/3;
        }
        else {
            timeRateUsed=timeRate+1/6;
        }
        
        double xtime=difTime/time;
        double xmoney=difMoney/money;
        
        double timeScale=3-Math.exp(1.5*xtime);
        timeRate=2-Math.exp(1-timeRate);
        double moneyScale=1/(1+Math.exp(25-5*xmoney));
        
        double sumScale=3*(timeScale)+timeRate;
        return sumScale;
    }
}
