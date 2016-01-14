import java.awt.*;

class MyPoint extends Point {
    public int size;
    boolean set = false;

    MyPoint(int x, int y, int size) {
        super(x, y);
        this.size = size;
    }
    MyPoint(int x, int y) {
        super(x, y);
        this.size = 1;
    }

    public int getXMax() {
        return x + size;
    }

    public int getYMax() {
        return y + size;
    }

    @Override
    public String toString() {
        return "Koordinats: X: " + getX() + " Y: " + getY();
    }
}

