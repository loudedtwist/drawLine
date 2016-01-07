import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Random;
import static java.lang.Math.toIntExact;

public class MausKlick extends MeinFenster{
    Random randomGen = new Random();
    int times[] = new int[100];
    int timesMultpl[][] = new int[5][100];
    final static int WIDTH_MAX = 1280;
    final static int HEIGHT_MAX = 720;

    int x1=0;
    int x2=0;
    int y1=0;
    int y2=0;

    static boolean DEBUG = false;

    int versuchNummer=0;

    interface DrawFigure{
        void draw();
    }

    private DrawFigure[] drawLines = new DrawFigure[]{
            ()->drawLineVerison1(),
            ()->drawLineVersion2()
    };

    public static void main(String[] args){
        MausKlick frame = new MausKlick("Lines speed test",WIDTH_MAX,HEIGHT_MAX);

    }

    public MausKlick(String title, int w , int h){
        super(title,w,h);
        setSize(w,h);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                fullTest();
                printTestResult();
            }
        });

    }

    private void printTestResult() {
        Graphics g = getGraphics();
        g.setColor(Color.black);
        g.fillRect(450,250,400,200);//drawRect
        g.setColor(Color.white);
        g.drawString("Die Testwerte: ",500,360);
        BoxWhisker boxW = new BoxWhisker(timesMultpl);
        int qu[] = boxW.getQues(0);
        g.drawString("Version 1: Q1:  "+ qu[0] + " Q2 : " + qu[1] + "Q3 : " + qu[2],500,360+20);
        int qu2[] = boxW.getQues(1);
        g.drawString("Version 2: Q1:  "+ qu2[0] + " Q2 : " + qu2[1] + "Q3 : " + qu2[2],500,360+20+20);
    }

    private void fullTest() {
        for(int funcNumber = 0 ; funcNumber<drawLines.length; funcNumber++) {

            for (versuchNummer = 0; versuchNummer < times.length; versuchNummer++) {
                final int finalFuncNumber = funcNumber;
                drawRandomLines(() -> drawLines[finalFuncNumber].draw());
            }
            for (versuchNummer = 0; versuchNummer < times.length; versuchNummer++) {
                timesMultpl[funcNumber][versuchNummer] = times[versuchNummer];
            }
            if (DEBUG) {
                for (long time : times) {
                    System.out.println("Versuch " + Long.toString(time));
                }
            }
            versuchNummer = 0;
        }
    }

    private void setPixelBody(int x , int y){
        Graphics g = getGraphics();
        g.setColor(Color.black);
        g.fillRect(x,y,1,1);//drawRect
    }
    private void setPixel(int x, int y){
        setPixelBody(x,y);
    }
    private void setPixel(double x, double y){
        setPixelBody((int)(x+0.5),(int)(y+0.5));
    }
    private void drawLine(int x1, int y1, int x2, int y2){
        int width = x2 - x1;
        int height = y2 - y1;
        float amountStep = (float)height / width; System.out.println(Float.toString(amountStep));
        float pY=y1;

        for(int pX = x1; pX<=x2; pX++){
            setPixel(pX,(int)(pY+0.5));
            pY += amountStep;
        }
    }

    //gleitkommaarith. fuer x und y
    private void drawLineVerison1(){
        int dx = x2 - x1;
        int dy = y2 - y1;
        double step = 1.0/Math.sqrt(dx*dx + dy*dy);

        for(double t=0.0 ; t<1.0; t+=step){
            setPixel(x1+t*dx,y1+t*dy);
        }
        setPixel(x2,y2);
    }
    private void drawLineVersion2(){
        int xl1=x1; int yl1=y1; int xl2=x2; int yl2=y2;
        if (x2<=x1){ drawLineVersion2(x2, y2, x1, y1);}
        else drawLineVersion2(x1, y1, x2, y2);
    }

    private void drawLineVersion2(int x1, int y1, int x2, int y2){
        double m = (double)(y2 - y1)/(double)(x2 - x1);
        double b = (double)(y1*x2-y2*x1)/(double)(x2-x1);

        for (int x=x1; x<=x2; x++){
            setPixel(x, m*x+b);
        }
    }

    private void drawRandomLines(Runnable function){
        long timeMilliBevor = getCurrentTimeMilliSec();
        for(int i = 0; i<100;i++) {
            x1 = randomGen.nextInt(WIDTH_MAX);
            x2 = randomGen.nextInt(WIDTH_MAX);
            y1 = randomGen.nextInt(HEIGHT_MAX);
            y2 = randomGen.nextInt(HEIGHT_MAX);
            function.run();
        }

        long timeMilliDanach = getCurrentTimeMilliSec();
        times[versuchNummer]=toIntExact(timeMilliDanach-timeMilliBevor);
        if(DEBUG)                                                       System.out.println("Bevor " +Long.toString(timeMilliDanach));
        if(DEBUG)                                                       System.out.println("Vergangen " +Long.toString(times[versuchNummer]));
    }

    private long getCurrentTimeMilliSec() {
        Calendar now = Calendar.getInstance();
        return now.getTimeInMillis();
    }



}