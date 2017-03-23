package serialPort;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.*;

import gnu.io.SerialPort;

public class Main implements ActionListener{
	JButton buttonOpenPort;
	JComboBox<String> comboBoxPortNum;
	String[] portArray;
	Boolean isPortOpen = false;
	SerialPort sp;
	JTextArea displayMessageField;
	JButton buttonSendDisplay;
	JButton buttonRelay1;
	JButton buttonRelay2;
	JTextArea SPKMessageField;
	JButton buttonSendSPK;
	JTextField fileField;
	JTextArea sendField;
	JButton buttonOpenFile;
	JButton buttonSendFile;
	JTextField addrField;
	JLabel fileLeft;
	Thread sendThread;
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		new Main().go();
	}
	
	public void go(){
		JFrame frame = new JFrame();
		JPanel panelTop = new JPanel();
		JPanel panelFileTransmit = new JPanel();
		JPanel panelSerial = new JPanel();
		JPanel panelRelay = new JPanel();
		JPanel panelSendDisplayMessage = new JPanel();
		JPanel panelSPKmessage = new JPanel();

		ParkingProtocol.setMainHandle(this);
		
		panelSerial.setLayout(new BoxLayout(panelSerial, BoxLayout.Y_AXIS));
		
		ArrayList<String> list =  SerialTool.findPort();
		portArray = new String[list.size()];
		list.toArray(portArray);
		comboBoxPortNum = new JComboBox<String>(portArray);
		buttonOpenPort = new JButton("�򿪴���");
		buttonOpenPort.addActionListener(this);
		panelSerial.add(comboBoxPortNum);
		panelSerial.add(buttonOpenPort);

		buttonRelay1 = new JButton("�����̵���1");
		buttonRelay2 = new JButton("�����̵���2");
		panelSerial.add(buttonRelay1);
		panelSerial.add(buttonRelay2);
		
		addrField = new JTextField(2);
		addrField.setText("1");
		JLabel labelAddr = new JLabel("��ַ");
		panelSerial.add(labelAddr);
		panelSerial.add(addrField);
		
		JPanel panelMessage = new JPanel();
		panelMessage.setLayout(new BoxLayout(panelMessage, BoxLayout.Y_AXIS));
//		panelMessage.setMaximumSize(new Dimension(50, 50));
		
		displayMessageField = new JTextArea(5,20);
		buttonSendDisplay = new JButton("������ʾ");
		panelSendDisplayMessage.add(displayMessageField);
		panelSendDisplayMessage.add(buttonSendDisplay);

		SPKMessageField = new JTextArea(5,20);
		buttonSendSPK = new JButton("ת������");
		panelSPKmessage.add(SPKMessageField);
		panelSPKmessage.add(buttonSendSPK);
		
		panelMessage.add(panelSendDisplayMessage);
		panelMessage.add(panelSPKmessage);


		
		fileField = new JTextField(35);
		buttonOpenFile = new JButton("���ļ�");
		buttonOpenFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int retval;
				JFileChooser chooser = new JFileChooser();
				retval = chooser.showOpenDialog(frame);
	            if(retval == JFileChooser.APPROVE_OPTION) {//���ɹ���
	                File file = chooser.getSelectedFile();//�õ�ѡ����ļ���
	                fileField.setText(file.toString());
	            }
			}
		});
		buttonSendFile = new JButton("�����ļ�");
		buttonSendFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						fileLeft.setText("123");
						ParkingProtocol.sendFile(fileField.getText(),Integer.parseInt(addrField.getText()) );
					}
				});
				sendThread.start();
				
			}
		});
		
		JButton buttonCloseSend = new JButton("��ֹ����");
		buttonCloseSend.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendThread.stop();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		fileLeft = new JLabel();
		panelFileTransmit.add(fileField);
		panelFileTransmit.add(buttonOpenFile);
		panelFileTransmit.add(buttonSendFile);
		
		frame.getContentPane().add(panelTop);
		panelTop.add(panelSerial);
		panelTop.add(panelMessage);
		panelTop.add(panelFileTransmit);
		panelTop.add(buttonCloseSend);
		panelTop.add(fileLeft);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(700, 350);
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent event) {
		try {
			String portName = (String)comboBoxPortNum.getSelectedItem();
			if(isPortOpen == false)
			{
				sp = SerialTool.openPort(portName, 9600);
				isPortOpen = true;
				buttonOpenPort.setText("�رմ���");
			}
			else
			{
				SerialTool.close();
				isPortOpen = false;
				buttonOpenPort.setText("�򿪴���");
			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "����ʧ��", "INFORMATION_MESSAGE",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
