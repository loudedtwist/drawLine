import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MausKlick extends MeinFenster{
    int x1;
    int x2;
    int y1;
    int y2;

    Random randomGen = new Random();
    final static int WIDTH_MAX = 1280;
    final static int HEIGHT_MAX = 720;

    interface DrawFigure{
       void draw();
    }

    public void drawLine(){
        int width = x2 - x1;
        int height = y2 - y1;
        float amountStep = (float)height / width; System.out.println(Float.toString(amountStep));
        float pY=y1;

        for(int pX = x1; pX<=x2; pX++){
            setPixel(pX,(int)(pY+0.5));
            pY += amountStep;
        }
    }

    public void drawLine1(){
        if (x2<=x1){ drawLine();}

        double m = (double)(y2 - y1)/(double)(x2 - x1);
        double b = (double)(y1*x2-y2*x1)/(double)(x2-x1);

        for (int x=x1; x<=x2; x++){
            setPixel(x, m*x+b);
        }

    }

    private DrawFigure[] lineFigures = new DrawFigure[] {
            () -> drawLine(),
            () -> drawLine1()
    };


    public void draw(int index){
        lineFigures[index].draw();
    }

    public MausKlick(String title, int w , int h){
        super(title,w,h);
        setSize(w,h);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                setPixel(e.getX(),e.getY());
                drawRandomLines();
            }
        });

    }


    public static void main(String[] args){
        MausKlick frame = new MausKlick("MausKlick Fenster",WIDTH_MAX,HEIGHT_MAX);
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

    //gleitkommaarith. fuer x und y
    private void drawLineVerison1(int x1, int y1, int x2, int y2){
        int dx = x2 - x1;
        int dy = y2 - y1;
        double step = 1.0/Math.sqrt(dx*dx + dy*dy);

        for(double t=0.0 ; t<1.0; t+=step){
            setPixel(x1+t*dx,y1+t*dy);
        }
        setPixel(x2,y2);
    }
    private void drawRandomLines(){

        for(int i = 0; i<100;i++) {
            x1 = randomGen.nextInt(WIDTH_MAX);
            x2 = randomGen.nextInt(WIDTH_MAX);
            y1 = randomGen.nextInt(HEIGHT_MAX);
            y2 = randomGen.nextInt(HEIGHT_MAX);
            System.out.println(Integer.toString(x1)+"   "+Integer.toString(x2)+"   "+Integer.toString(y1)+"   "+Integer.toString(y2));
            draw(0);
        }
    }




}