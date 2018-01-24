import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import static org.opencv.highgui.Highgui.imread;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import static org.opencv.core.Core.rectangle;
import static org.opencv.core.Core.ellipse;
import static org.opencv.core.Core.putText;
import static org.opencv.core.Core.FONT_HERSHEY_TRIPLEX;

public class Main {
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		DetectFacesFromImage d = new DetectFacesFromImage();
		d.detect();
	}

}

class DetectFacesFromImage {
	
	public static void detect() {

//    	CascadeClassifier fc = new CascadeClassifier("C:\\opencv\\sources\\data\\lbpcascades\\lbpcascade_frontalface.xml");
    	CascadeClassifier fc = new CascadeClassifier("C:\\cascade\\lbpcascade_frontalface.xml");
    	String filePath = null;
    	
    	System.out.println("Do you want to upload the file ????");
    	Scanner s = new Scanner(System.in);
    	char choice = s.next().charAt(0);
    	if (choice == 'y' || choice == 'Y') {
    		JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(fileChooser);
            if(returnVal == JFileChooser.APPROVE_OPTION) {
                filePath = fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                System.out.println("Cancel");
                System.exit(1);
            }
    	} else if (choice == 'n' || choice == 'N') {
    		String fileLoc = "";
        	Scanner textscan=new Scanner(System.in);
            System.out.println("Copy image path from file:");
            fileLoc=textscan.nextLine();
            filePath = "C:/Users/sagar/Desktop/Omron/docs/" + fileLoc + ".jpg";
            textscan.close();
    	}
    	s.close();
    	Mat image = imread(filePath);
        
        MatOfRect faceDetections = new MatOfRect();
        fc.detectMultiScale(image, faceDetections);

        System.out.println(String.format("Detected %s face(s)", faceDetections.toArray().length));
        int num = 0;
        for (Rect rect : faceDetections.toArray()) {
//            rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        	ellipse(image, new Point(rect.x + (rect.width/2), rect.y + (rect.height/2)), new Size(rect.width/2, rect.height/2), 0, 360, 0, new Scalar(0, 255, 0));
        	System.out.println("Major & Minor Axis Radii : " + rect.width/2 + ", " + rect.height/2 + ", Center X : " + rect.x + ", Center Y : " + rect.y);
        	putText(image, "(" + ++num + ")", new Point(rect.x, rect.y), FONT_HERSHEY_TRIPLEX, 1, new Scalar(0, 0, 255));
        }
        
        JFrame jf = new JFrame(faceDetections.toArray().length + " Face/s Detected");
        JLabel jl = new JLabel();
        jl.setIcon(new ImageIcon(matToBufferedImage(image)));
        jf.getContentPane().add(jl, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closed");
                System.exit(0);
            }
        });

    }
	
	public static BufferedImage matToBufferedImage(Mat matBGR) {
        int width = matBGR.width();
        int height = matBGR.height();
        int channels = matBGR.channels();
        byte[] source = new byte[width * height * channels];
        matBGR.get(0, 0, source);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] target = ((DataBufferByte)bufferedImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(source, 0, target, 0, source.length);
        return bufferedImage;
    }

	
}