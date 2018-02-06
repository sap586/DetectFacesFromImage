import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

class CreateBaseFrame extends JFrame {
	
	JTextField pathField = new JTextField(20);
	
	CreateBaseFrame() {
		setTitle("Base Frame");
		JButton yesButton = new JButton("Upload Image");
		JButton noButton = new JButton("Open Image");
		JButton camButton = new JButton("Camera");
		pathField.setText("Enter Image Path");
//		JLabel l;

		yesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddImageFrame(0, CreateBaseFrame.this);
			}
		});
		noButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AddImageFrame(1, CreateBaseFrame.this);
			}
		});
		camButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new CameraFrame();
			}
		});
		
		add(yesButton);
		add(noButton);
		add(camButton);
		add(pathField);
		setLayout(new FlowLayout());
		setSize(400, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Base Frame Closed");
                System.exit(0);
            }
        });
	}
	
}
