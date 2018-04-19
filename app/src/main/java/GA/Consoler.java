package GA;
import java.util.*;
/**
 * Created by Fish on 2018/4/17.
 */

public class Consoler {


    public static void main(String[] args)
    {
        Random r=new Random();
        double[][] tM=new double[20][20];
        double[][] mM=new double[20][20];
        double[] tL=new double[20];
        double[] mL=new double[20];
        for(int i=0;i<20;i++)
        {
            for(int j=0;j<20;j++)
            {
                tM[i][j]=(double)(r.nextInt()%50)/25.0;
                tM[i][j]=Math.abs(tM[i][j]);
                mM[i][j]=(double)(r.nextInt()%50)/25.0;
                mM[i][j]=Math.abs(mM[i][j]);
            }
        }
        for(int i=0;i<20;i++)
        {
            tL[i]=(double)(r.nextInt()%50)/25.0;;
            tL[i]=Math.abs(tL[i]);
            mL[i]=(double)(r.nextInt()%50)/25.0;;
            mL[i]=Math.abs(mL[i]);
        }
        double eT=10.0;
        double eM=15.0;
        int s=20;

        Preparer x=new Preparer(tM,mM,tL,mL,eT,eM,s);
        World y=new World(x);
        y.initSetting();
        y.initPopulation();
        while(y.goNext())y.Evolution();

        for(int i=0;i<y.king.staff.size();i++)
        {
            System.out.println(i+" path");
            for(int j=0;j<y.king.staff.get(i).path.size();j++)
            {
                System.out.print(y.king.staff.get(i).path.get(j)+" ");
            }
            System.out.println();
        }
    }
}
