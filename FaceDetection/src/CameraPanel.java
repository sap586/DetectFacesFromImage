import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

public class CameraPanel extends JPanel implements Runnable, ActionListener {
	
	BufferedImage image;
	VideoCapture capture;
	JButton screenshot;
	CascadeClassifier faceDetector;
	MatOfRect faceDetections;
	
	CameraPanel() {
		faceDetector = new CascadeClassifier("C:\\cascade\\lbpcascade_frontalface.xml");
		faceDetections = new MatOfRect();
		screenshot = new JButton("screenshot");
		screenshot.addActionListener(this);
		add(screenshot);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		File output = new File("screenshot1.png");
		int i = 0;
		while(output.exists()) {
			i++;
			output = new File("screenshot" + i + ".png");
		}
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		capture = new VideoCapture(0);
		Mat webcam_image = new Mat();
		if (capture.isOpened()) {
			while(true) {
				capture.read(webcam_image);
				if (!webcam_image.empty()) {
					JFrame topFrame = (JFrame)SwingUtilities.getWindowAncestor(this);
					topFrame.setSize(webcam_image.width() + 40, webcam_image.height() + 110);
					matToBufferedImage(webcam_image);
					faceDetector.detectMultiScale(webcam_image, faceDetections);
					repaint();
				}
			}
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			return;
		}
		g.drawImage(image, 10, 40, image.getWidth(), image.getHeight(), null);
		g.setColor(Color.GREEN);
		for (Rect rect : faceDetections.toArray()) {
			g.drawRect(rect.x, rect.y, rect.width, rect.height);
		}
	}

	private void matToBufferedImage(Mat matBGR) {
		int width = matBGR.width();
		int height = matBGR.height();
		int channels = matBGR.channels();
		byte[] source = new byte[width * height * channels];
		matBGR.get(0, 0, source);
		image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		final byte[] target = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
		System.arraycopy(source, 0, target, 0, source.length);
	}

}
