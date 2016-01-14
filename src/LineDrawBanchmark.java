import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Random;

import static java.lang.Math.toIntExact;

public class LineDrawBanchmark extends MeinFenster{
    private static int ANZ_VERSUCHEN = 10;
    private static int ANZ_PUNKTE = 100;
    private Random randomGen = new Random();
    private int times[] = new int[ANZ_VERSUCHEN];
    private int timesMultpl[][] = new int[5][ANZ_VERSUCHEN];
    protected final static int WIDTH_MAX = 1280;
    protected final static int HEIGHT_MAX = 720;
    protected Graphics g;

    private int x1=0;
    private int x2=0;
    private int y1=0;
    private int y2=0;

    private int x1Arr[]=new int [ANZ_PUNKTE];
    private int x2Arr[]=new int [ANZ_PUNKTE];
    private int y1Arr[]=new int [ANZ_PUNKTE];
    private int y2Arr[]=new int [ANZ_PUNKTE];

    private static boolean DEBUG = false;

    private int versuchNummer=0;
    private int funcNumber = 0;

    interface DrawFigure{
        void draw();
    }

    private DrawFigure[] drawFunctions = new DrawFigure[]{
            ()->drawLineVerison1(),
            ()->drawLineBresenham(),
            ()->drawLineVersion2()
    };

    public static void main(String[] args){
        LineDrawBanchmark frame = new LineDrawBanchmark("Lines speed test",WIDTH_MAX,HEIGHT_MAX);
        frame.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                //fullTest();
                frame.fullTestNew();
                frame.printTestResult();
            }
        });

    }

    public LineDrawBanchmark(String title, int w , int h){
        super(title,w,h);
        setSize(w,h);
        g = getGraphics();
        g.setColor(Color.BLACK);
    }
    public LineDrawBanchmark(){
        super("Window",WIDTH_MAX,HEIGHT_MAX);
        setSize(WIDTH_MAX,HEIGHT_MAX);
    }

    private void printTestResult() {
        final int placeX = 440;

        final int placeY = 620;
        final int placeYBoxerW = 580;
        final int mainInfoBoxX = 420;
        int breighteBox = 20;
        int shift = 100;


        g.setColor(Color.black);
        //g.fillRect(450,250,420,200);//drawRect HINTERGRUND
        g.fillRect(mainInfoBoxX,0,mainInfoBoxX,720);//drawRect HINTERGRUN
        g.setColor(Color.white);
        BoxWhisker boxW = new BoxWhisker(timesMultpl);

        //BOX
        drawBoxAndWBoxes(placeX, placeYBoxerW, mainInfoBoxX, breighteBox, shift, g, boxW);

        g.drawString("Die Testwerte: ",placeX,placeY);
        int qu[] = boxW.getQues(0);
        g.drawString("All floats(x,y): Q1:  "+ qu[0] + " Q2 : " + qu[1] + " Q3 : " + qu[2]+ " Min: " + boxW.getMin(0)+ " Max: " + boxW.getMax(0),placeX,placeY+20);
        int qu2[] = boxW.getQues(1);
        g.drawString("Bresenham: Q1: "+ qu2[0] + " Q2 : " + qu2[1] + " Q3: " + qu2[2] + " Min: " + boxW.getMin(1)+ " Max: " + boxW.getMax(1),placeX,placeY+20+20);
        int qu3[] = boxW.getQues(2);
        g.drawString("Float(y), int(x): Q1:  "+ qu3[0] + " Q2 : " + qu3[1] + " Q3 : " + qu3[2]+ " Min: " + boxW.getMin(2)+ " Max: " + boxW.getMax(2),placeX,placeY+20+20+20);
        g.drawString("Anzahl von Versuchen: "+ ANZ_VERSUCHEN,placeX,placeY+20+20+20+20);

    }

    private void drawBoxAndWBoxes(int placeX, int placeYBoxerW, int mainInfoBoxX, int breighteBox, int shift, Graphics g, BoxWhisker boxW) {
        g.drawLine(mainInfoBoxX,placeYBoxerW+15,mainInfoBoxX + 420,placeYBoxerW+15);
        for (int j=0,counter = 0; j<placeYBoxerW-70; j+=10,counter++){
            int laenge = 5;
            if(counter%5==0){
                g.drawString(" "+counter*10 , placeX,placeYBoxerW - j+4);
                laenge=10;
            }
            else laenge=5;
            g.drawLine(mainInfoBoxX, placeYBoxerW - j, mainInfoBoxX+laenge, placeYBoxerW - j);
        }
        for(int i = 0; i < drawFunctions.length;i++){
            int qu[] = boxW.getQues(i);
            for(int q : qu){
                g.drawLine(placeX+shift,placeYBoxerW-q,placeX+shift+ breighteBox,placeYBoxerW-q);
            }
            g.drawLine(placeX+shift,placeYBoxerW-boxW.getMin(i),placeX+shift+breighteBox,placeYBoxerW-boxW.getMin(i));
            g.drawLine(placeX+shift,placeYBoxerW-boxW.getMax(i),placeX+shift+breighteBox,placeYBoxerW-boxW.getMax(i));

            g.drawLine(placeX+shift,placeYBoxerW-qu[1],placeX+shift,+placeYBoxerW-qu[2]);
            g.drawLine(placeX+shift+breighteBox,placeYBoxerW-qu[1],placeX+shift+breighteBox,placeYBoxerW-qu[2]);
            shift+=70+breighteBox;
        }
    }

    private void fullTest() {
        for(int funcNumber = 0; funcNumber< drawFunctions.length; funcNumber++) {
            for (versuchNummer = 0; versuchNummer < times.length; versuchNummer++) {
                final int finalFuncNumber = funcNumber;
                drawRandomLines(() -> drawFunctions[finalFuncNumber].draw());
                drawRect(0,HEIGHT_MAX-20,WIDTH_MAX,20);
                drawString("versuch nummer: "+ (versuchNummer+1) + ", algorithm: " + (funcNumber+1), 0,HEIGHT_MAX-8);
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
    private void fullTestNew() {


            for (versuchNummer = 0; versuchNummer < times.length; versuchNummer++) {
                generateCoordsArray();

                for(funcNumber = 0; funcNumber< drawFunctions.length; funcNumber++) {
                    final int finalFuncNumber = funcNumber;
                    drawRandomLinesFromArray(() -> drawFunctions[finalFuncNumber].draw());
                }
            }

            if (DEBUG) {
                for (long time : times) {
                    System.out.println("Versuch " + Long.toString(time));
                }
            }
            versuchNummer = 0;
    }
    private void drawRandomLinesFromArray(Runnable function){
        long timeMilliBevor = getCurrentTimeMilliSec();
        for(int i = 0; i<100;i++) {
            x1=x1Arr[i];
            x2=x2Arr[i];
            y1=y1Arr[i];
            y2=y2Arr[i];
            function.run();
        }

        long timeMilliDanach = getCurrentTimeMilliSec();
        timesMultpl[funcNumber][versuchNummer]=toIntExact(timeMilliDanach-timeMilliBevor);
        if(DEBUG)                                                       System.out.println("Bevor " +Long.toString(timeMilliDanach));
        if(DEBUG)                                                       System.out.println("Vergangen " +Long.toString(times[versuchNummer]));
    }

    private void setPixelBody(int x , int y){
        Graphics g = getGraphics();
        g.setColor(new Color(x1%220, x2%220, y1%220));
        g.fillRect(x,y,1,1);//drawRect
    }
    protected void setPixel(int x, int y){
        setPixelBody(x,y);
    }
    protected void setPixel(double x, double y){
        setPixelBody((int)(x+0.5),(int)(y+0.5));
    }

    //gleitkommaarith. fuer x und y
    protected void drawLineVerison1(){
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

    protected void drawLineVersion2(int x1, int y1, int x2, int y2){
        double m = (double)(y2 - y1)/(double)(x2 - x1);
        double b = (double)(y1*x2-y2*x1)/(double)(x2-x1);

        for (int x=x1; x<=x2; x++){
            setPixel(x, m*x+b);
        }
    }

    protected void drawString(String text, int x, int y){
        Graphics g = getGraphics();
        g.setColor(Color.black);
        g.drawString(text,x,y);
    }
    protected void drawRect(int x , int y , int w, int h){
        Graphics g = getGraphics();
        g.setColor(Color.white);
        g.fillRect(x,y,w,h);
    }
    protected void drawRect( int x , int y , int w, int h, Color c1){
        Graphics g = getGraphics();
        g.setColor(c1);
        g.fillRect(x,y,w,h);
    }
    protected void drawRandomLines(Runnable function){
        long timeMilliBevor = getCurrentTimeMilliSec();
        for(int i = 0; i<100;i++) {
            generateCoords();
            function.run();
        }

        long timeMilliDanach = getCurrentTimeMilliSec();
        times[versuchNummer]=toIntExact(timeMilliDanach-timeMilliBevor);
        if(DEBUG)                                                       System.out.println("Bevor " +Long.toString(timeMilliDanach));
        if(DEBUG)                                                       System.out.println("Vergangen " +Long.toString(times[versuchNummer]));
    }

    public void bresenhamX(int x1, int y1, int x2, int y2, int dx, int dy, int error, int delta, int schritt) {
        int x = x1, y = y1;
        int inc_x = (dx > 0) ? 1 : -1;
        int inc_y = (dy > 0) ? 1 : -1;
        while (x != x2) {
            setPixel(x, y);
            x += inc_x;
            error = error + delta;
            if (error > 0) {
                y += inc_y;
                error += schritt;
            }
        }
    }

    public void bresenhamY(int x1, int y1, int x2, int y2, int dx, int dy, int error, int delta, int schritt) {
        int x = x1, y = y1;
        int inc_x = (dx > 0) ? 1 : -1;
        int inc_y = (dy > 0) ? 1 : -1;
        while (y != y2) {
            setPixel(x, y);
            y += inc_y;
            error = error + delta;
            if (error > 0) {
                x += inc_x;
                error += schritt;
            }
        }
    }

    public void drawLineBresenham() {
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (Math.abs(dy) < Math.abs(dx))
            bresenhamX(x1, y1, x2, y2, dx, dy, -Math.abs(dx), 2 * Math.abs(dy), 2 * -Math.abs(dx));
        else bresenhamY(x1, y1, x2, y2, dx, dy, -Math.abs(dy), 2 * Math.abs(dx), 2 * -Math.abs(dy));
        setPixel(x2, y2);
    }

    public void drawLineBresenham(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        if (Math.abs(dy) < Math.abs(dx))
            bresenhamX(x1, y1, x2, y2, dx, dy, -Math.abs(dx), 2 * Math.abs(dy), 2 * -Math.abs(dx));
        else bresenhamY(x1, y1, x2, y2, dx, dy, -Math.abs(dy), 2 * Math.abs(dx), 2 * -Math.abs(dy));
        setPixel(x2, y2);
    }

    public void drawLineBresenham(int x1, int y1, int x2, int y2,Color c)  {
        g.setColor(c);
        drawLineBresenham(x1, y1, x2, y2);
    }
    private void generateCoords() {
        x1 = randomGen.nextInt(WIDTH_MAX);
        x2 = randomGen.nextInt(WIDTH_MAX);
        y1 = randomGen.nextInt(HEIGHT_MAX);
        y2 = randomGen.nextInt(HEIGHT_MAX);

    }
    private void generateCoordsArray() {
        for(int i =0; i<ANZ_PUNKTE; i++) {
            x1Arr[i] = randomGen.nextInt(WIDTH_MAX);
            x2Arr[i] = randomGen.nextInt(WIDTH_MAX);
            y1Arr[i] = randomGen.nextInt(HEIGHT_MAX);
            y2Arr[i] = randomGen.nextInt(HEIGHT_MAX);
        }
    }

    private long getCurrentTimeMilliSec() {
        Calendar now = Calendar.getInstance();
        return now.getTimeInMillis();
    }



}