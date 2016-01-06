import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MausKlick extends MeinFenster {
    Random randomGen = new Random();
    final static int WIDTH_MAX = 1280;
    final static int HEIGHT_MAX = 720;

    public MausKlick(String title, int w, int h) {
        super(title, w, h);
        setSize(w, h);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setPixel(e.getX(), e.getY());
                //drawLine(e.getX(), e.getY() , e.getX()+40, e.getY()+ 20);
                drawLineVerison1(e.getX(), e.getY(), e.getX() + 40, e.getY() + 20);
            }
        });

    }

    public static void main(String[] args) {
        MausKlick frame = new MausKlick("MausKlick Fenster", WIDTH_MAX, HEIGHT_MAX);

    }

    private void setPixelBody(int x, int y) {
        Graphics g = getGraphics();
        g.setColor(Color.black);
        g.fillRect(x, y, 1, 1);//drawRect
    }

    private void setPixel(int x, int y) {
        setPixelBody(x, y);
    }

    private void setPixel(double x, double y) {
        setPixelBody((int) (x + 0.5), (int) (y + 0.5));
    }

    //gleitkommaarith. fuer x und y
    private void drawLineVerison1(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        double step = 1.0 / Math.sqrt(dx * dx + dy * dy);

        for (double t = 0.0; t < 1.0; t += step) {
            setPixel(x1 + t * dx, y1 + t * dy);
        }
        setPixel(x2, y2);
    }

    private void drawLineVersion2(int x1, int y1, int x2, int y2){
        if (x2<=x1){ drawLineVersion2(x2, y2, x1, y1);}

        double m = (double)(y2 - y1)/(double)(x2 - x1);
        double b = (double)(y1*x2-y2*x1)/(double)(x2-x1);

        for (int x=x1; x<=x2; x++){
            setPixel(x, m*x+b);
        }

    }

    private void drawRandomLines() {
        int x1 = randomGen.nextInt(WIDTH_MAX);
        int x2 = randomGen.nextInt(WIDTH_MAX);
        int y1 = randomGen.nextInt(HEIGHT_MAX);
        int y2 = randomGen.nextInt(HEIGHT_MAX);
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        int width = x2 - x1;
        int height = y2 - y1;
        float amountStep = (float) height / width;
        System.out.println(Float.toString(amountStep));
        float pY = y1;

        for (int pX = x1; pX <= x2; pX++) {
            setPixel(pX, (int) (pY + 0.5));
            pY += amountStep;
        }
    }
}