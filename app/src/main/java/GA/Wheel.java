/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GA;

import java.util.Random;

/**
 *
 * @author Fish
 */
public class Wheel {
    
    private Random random;
    private double[] roul;
    private int rouLen;
    
    public Wheel()
    {
        random = new Random();
    }
    public int RandomInt(int x)
    {
        return Math.abs(random.nextInt())%x;
    }
    public void setRoul(int len,double[] x)
    {
        rouLen=len;
        roul=x;
    }
    public int getRoulInt()
    {
        int m=RandomInt(1000);
        double sum=0;
        for(int i=0;i<rouLen;i++)
        {
            sum+=roul[i];
            if(m<=sum*1000)return i;
        }
        return rouLen-1;
    }
    
    
    
    
    
    
    public static void main(String[] args)
    {
        Wheel a =  new Wheel();
        double[] x={0.25,0.5,0.25};
        a.setRoul(3, x);
        int q=0,w=0,e=0;
        int m;
        for(int i=1;i<=1600;i++)
        {
            m=a.getRoulInt();
            if(m==0)q++;
            if(m==1)w++;
            if(m==2)e++;
        }
        System.out.println(q+" "+w+" "+e);
    }
    
}
