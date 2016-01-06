import java.awt.*;
import java.awt.event.*;

class MeinFenster extends Frame{
    MeinFenster(String title, int w, int h){
        this.setTitle(title);
        this.setSize(w,h);
        this.addWindowListener(new MyWindowListener());
        this.setVisible(true);
        //center the window
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((d.width-w)/2,(d.height-h)/2);
    }
    class MyWindowListener extends WindowAdapter{
        @Override
        public void windowClosing(WindowEvent e){
            System.exit(0);
        }
    }
}
