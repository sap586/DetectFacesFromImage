import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

import org.opencv.core.Core;
import org.opencv.highgui.VideoCapture;

public class CameraFrame extends JFrame {
	
	CameraPanel cp;
	VideoCapture list;
	static int flag;
	
	CameraFrame() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		list = new VideoCapture(0);
		//VideoCapture list = new VideoCapture(0);
		cp = new CameraPanel();
		Thread thread = new Thread(cp);
		JMenu camera = new JMenu("Camera");
		JMenuBar bar = new JMenuBar();
		bar.add(camera);
		int i = 1;
		while(list.isOpened()) {
			JMenuItem cam = new JMenuItem("Camera " + i);
			list.release();
			list = new VideoCapture(i);
			i++;
		}
		thread.start();
		add(cp);
		setJMenuBar(bar);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(400, 400);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Camera Frame Closed");
                CameraFrame.this.flag = 1;
            }
        });
		flag = 0;
	}

}
