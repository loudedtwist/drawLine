import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class CurvesViewer extends LineDrawBanchmark{
    public static final double STEP = 0.05;
    static private int SIZE_OF_CTRLS = 12;
    MyPoint oldPoint;
    Color cBlack = Color.BLACK;
    MyPoint stCtrlPoints[] = new MyPoint[4];

    CurvesViewer(String windowTitel,int w, int h){
        super(windowTitel,w,h);
        initStartPoint();
        addMouseListener(
                new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e){
                        for (int i=0;i<stCtrlPoints.length ; i++) {
                            if(isHit(e, i)){
                                                                                                                        System.out.println(i + " Getroffen ja: " + e.getX() + " " + e.getY() + " "+SIZE_OF_CTRLS);
                                stCtrlPoints[i].set = true;
                                oldPoint = new MyPoint(e.getX(),e.getY());
                                return;
                            }
                                                                                                                        //System.out.println(i+" nicht getroffen: klicked: " + e.getX() + ",  ist am:  "+stCtrlPoints[i].x+" | klicked: " + e.getY()+" ist am : "+stCtrlPoints[i].y + " " + " "+SIZE_OF_CTRLS);
                        }

                    }
                    @Override
                    public void mouseReleased(MouseEvent e){
                        for (int i=0;i<stCtrlPoints.length ; i++) {
                            if(stCtrlPoints[i].set){
                                stCtrlPoints[i].set = false;
                            }
                        }
                        drawBezier();
                    }
                }
        );
        addMouseMotionListener(
                new MouseMotionAdapter() {
                    @Override
                    public void mouseMoved(MouseEvent e) {
                        int dx,dy;

                        for (int i=0;i<stCtrlPoints.length ; i++) {
                            if (stCtrlPoints[i].set) {

                                dx=e.getX()-oldPoint.x;
                                dy=e.getY()-oldPoint.y;
                                stCtrlPoints[i].x += dx;
                                stCtrlPoints[i].y += dy;
                                oldPoint.x=e.getX();
                                oldPoint.y=e.getY();
                                repaint();
                            }
                        }
                    }
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        mouseMoved(e);
                    }
                }
        );
    }

    private boolean isHit(MouseEvent e, int i) {
        return (e.getX()>=stCtrlPoints[i].x && e.getX()<=stCtrlPoints[i].getXMax()) &&
                        (e.getY()>=stCtrlPoints[i].y && e.getY()<=stCtrlPoints[i].getYMax());
    }

    private void initStartPoint() {
        stCtrlPoints[0] = new MyPoint(420,540,SIZE_OF_CTRLS);
        stCtrlPoints[1] = new MyPoint(750,460,SIZE_OF_CTRLS);
        stCtrlPoints[2] = new MyPoint(120,190,SIZE_OF_CTRLS);
        stCtrlPoints[3] = new MyPoint(240,430,SIZE_OF_CTRLS);
    }

    static public void main(String... args){
        CurvesViewer curvesFrame = new CurvesViewer("Curves",WIDTH_MAX,HEIGHT_MAX);
        curvesFrame.repaint();
    }

    @Override
    public void paint(Graphics g) {
        if(stCtrlPoints==null)return;
        drawCtrls();
        drawBezier();

    }

    private void drawCtrls(){
        for (int i=0;i<stCtrlPoints.length ; i++) {
            g.fillOval(stCtrlPoints[i].x,stCtrlPoints[i].y,SIZE_OF_CTRLS,SIZE_OF_CTRLS);
        }
    }
    public void drawBezier(){
        steps(
                new int[]{stCtrlPoints[0].x,stCtrlPoints[0].y},
                new int[]{stCtrlPoints[1].x,stCtrlPoints[1].y},
                new int[]{stCtrlPoints[2].x,stCtrlPoints[2].y},
                new int[]{stCtrlPoints[3].x,stCtrlPoints[3].y},
                STEP);
    }


    //SOURCE : https://github.com/kevinduraj/Hermite-Bezier-Curve/blob/master/src/graphics2/Bezier.java
    /*--------------------------------------------------------------------------
     *  Compute Bezier Cubic Points Derivatives
     *  +-----------------+   +-----------+
     *  | -1   3  -3   1  |   |  x0   y0  |
     *  |  3  -6   3   0  | * |  x1   y1  | * [t^3 t^2 t 1] = [x, y]
     *  | -3   3   0   0  |   |  x2   y2  |
     *  |  1   0   0   0  |   |  x3   y3  |
     *  +-----------------+   +-----------+
     -------------------------------------------------------------------------*/
    public int[] cubic(int[] P0, int[] P1, int[] P2, int[] P3)
    {
        /*------------------ Firt Column --------------------------*/
        int C3x = (-1*P0[0]) + (3*P1[0])  + (-3*P2[0]) + (1*P3[0]);
        int C2x = (3*P0[0])  + (-6*P1[0]) + (3*P2[0])  + (0);
        int C1x = (-3*P0[0]) + (3*P1[0])  + (0)        + (0);
        int C0x = (1*P0[0])  + (0)        + (0)        + (0);

        /*------------------ Second  Column -----------------------*/
        int C3y = (-1*P0[1]) + (3*P1[1])  + (-3*P2[1]) + (1*P3[1]);
        int C2y = (3*P0[1])  + (-6*P1[1]) + (3*P2[1])  + (0);
        int C1y = (-3*P0[1]) + (3*P1[1])  + (0)        + (0);
        int C0y = (1*P0[1])  + (0)        + (0)        + (0);

        /*---------------------------------------------------------*/

        int array[] = {C0x, C0y, C1x, C1y, C2x, C2y, C3x, C3y};

        return array;

    }
    //SOURCE : https://github.com/kevinduraj/Hermite-Bezier-Curve/blob/master/src/graphics2/Bezier.java
    /*------------------------------------------------------------------------
     * Create Steps Method
     * Increasing steps make curve smooth
     * Calculate "z" as 3rd dimension
     */
    public void steps(int[] P0, int[] P1, int[] P2, int[] P3, double step)
    {
        int array[] = cubic(P0, P1, P2, P3);
        int x, y, z;
        int C0x, C0y, C1x, C1y, C2x, C2y, C3x, C3y;

        C0x = array[0];
        C0y = array[1];
        C1x = array[2];
        C1y = array[3];
        C2x = array[4];
        C2y = array[5];
        C3x = array[6];
        C3y = array[7];

        int array2[][] = new int[(int) (1 / step)][2];
        int i = 0;

        for (double u = 0.00; u < 1; u += step) {
            x = (int) (C0x + C1x * u + C2x * u * u + C3x * u * u * u);
            y = (int) (C0y + C1y * u + C2y * u * u + C3y * u * u * u);

            array2[i][0] = x;
            array2[i][1] = y;
            i++;

            drawRect(x, y, 1, 1);
        }

        int j;
        for (j = 0; j < (int) (1 / step) - 1; j++) {

            drawLineBresenham( array2[j][0]
                    , array2[j][1]
                    , array2[j + 1][0]
                    , array2[j + 1][1]);
        }


        // Draw the last line to the end point
        drawLineBresenham(
                array2[j][0]
                , array2[j][1]
                , P3[0]
                , P3[1]);

    }
}
