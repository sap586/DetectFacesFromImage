import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;


public class FaceDetectFromImage {
	
	static BufferedImage i;

	public static void main(String[] args) {
//		System.load(Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary("opencv_java340");
		
		
		CascadeClassifier faceClassifier = new CascadeClassifier("C:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
		System.out.println(faceClassifier.empty());
		
		JFrame f = new JFrame("frame");
		f.setSize(400, 400);
		f.setVisible(true);
		
//		new FileChooser("C:\\Users\\sagar\\Desktop\\Omron\\2.jpg");
		
	}
	
	public static void matToBufferedImage(Mat m) {
        int w = m.width();
        int h = m.height();
        int ch = m.channels();
        byte[] source = new byte[w * h * ch];
        m.get(0, 0, source);
        i = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
        byte[] target = ((DataBufferByte) i.getRaster().getDataBuffer()).getData();
        System.arraycopy(source, 0, target, 0, source.length);
    }
	
	

}

class FileChooser extends JPanel implements Runnable {
	
	CascadeClassifier cc;
	MatOfRect mr;
	BufferedImage img;
    
    FileChooser(String filepath) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame(filepath);
                frame.setDefaultCloseOperation(3);
                img = null;
                try {
                    img = ImageIO.read(new File(filepath));
                } catch(Exception e) {
                    System.out.println("Exception");
                }
                
        		cc = new CascadeClassifier("C:\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        		mr = new MatOfRect();
                
                JLabel lbl = new JLabel();
                lbl.setIcon(new ImageIcon(img));
                frame.getContentPane().add(lbl, BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    public void run() {
    	Mat imgMat = img2Mat(img);
    	JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    	topFrame.setSize(imgMat.width() + 40, imgMat.height() + 110) ;
    	cc.detectMultiScale(imgMat, mr);
    	repaint();
    }
    
    public static Mat img2Mat(BufferedImage in)
    {
          Mat out;
          byte[] data;
          int r, g, b;

          if(in.getType() == BufferedImage.TYPE_INT_RGB)
          {
              out = new Mat(240, 320, CvType.CV_8UC3);
              data = new byte[320 * 240 * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, 320, 240, null, 0, 320);
              for(int i = 0; i < dataBuff.length; i++)
              {
                  data[i*3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                  data[i*3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                  data[i*3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
              }
          }
          else
          {
              out = new Mat(240, 320, CvType.CV_8UC1);
              data = new byte[320 * 240 * (int)out.elemSize()];
              int[] dataBuff = in.getRGB(0, 0, 320, 240, null, 0, 320);
              for(int i = 0; i < dataBuff.length; i++)
              {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte)((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
              }
           }
           out.put(0, 0, data);
           return out;
     } 
    
}