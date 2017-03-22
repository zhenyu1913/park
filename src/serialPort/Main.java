package serialPort;

import java.awt.*;
import java.awt.event.*;
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
	JButton buttonOpenFile;
	JButton buttonSendFile;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		
		ArrayList<String> list =  SerialTool.findPort();
		portArray = new String[list.size()];
		list.toArray(portArray);
		comboBoxPortNum = new JComboBox<String>(portArray);
		buttonOpenPort = new JButton("�򿪴���");
		buttonOpenPort.addActionListener(this);
		panelSerial.add(comboBoxPortNum);
		panelSerial.add(buttonOpenPort);
		
		displayMessageField = new JTextArea(5,20);
		buttonSendDisplay = new JButton("������ʾ");
		panelSendDisplayMessage.add(displayMessageField);
		panelSendDisplayMessage.add(buttonSendDisplay);

		buttonRelay1 = new JButton("�����̵���1");
		buttonRelay2 = new JButton("�����̵���2");
		panelRelay.add(buttonRelay1);
		panelRelay.add(buttonRelay2);

		SPKMessageField = new JTextArea(5,20);
		buttonSendSPK = new JButton("ת������");
		panelSPKmessage.add(SPKMessageField);
		panelSPKmessage.add(buttonSendSPK);
		
		fileField = new JTextField(20);
		buttonOpenFile = new JButton("���ļ�");
		buttonSendFile = new JButton("�����ļ�");
		panelFileTransmit.add(fileField);
		panelFileTransmit.add(buttonOpenFile);
		panelFileTransmit.add(buttonSendFile);
		
		frame.getContentPane().add(panelTop);
		panelTop.add(panelSerial);
		panelTop.add(panelSendDisplayMessage);
		panelTop.add(panelRelay);
		panelTop.add(panelSPKmessage);
		panelTop.add(panelFileTransmit);

		
		frame.setSize(600, 350);
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
				SerialTool.closePort(sp);
				isPortOpen = false;
				buttonOpenPort.setText("�򿪴���");
			}
		} catch (Exception e) {
			// TODO: handle exception
			JOptionPane.showMessageDialog(null, "����ʧ��", "INFORMATION_MESSAGE",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
