package serialPort;
import java.io.*;

import serialException.ReadDataFromSerialPortFailure;
import serialException.SendDataToSerialPortFailure;
import serialException.SerialPortInputStreamCloseFailure;
import serialException.SerialPortOutputStreamCloseFailure;

public class ParkingProtocol {
	static int sendIndex;
	static Main mainHandle;
	static String getHexString(byte[] buf){
		String s = "";
		for(byte b : buf){
			s += Integer.toHexString(b).toUpperCase();
		}
		return s;
	}
	
	static void setMainHandle(Main handle){
		mainHandle = handle;
	}
	
	static void sendFileFrame(byte[] buf,int index,int addr)
	{
		byte[] frame = new byte[133];
		byte checksum = 0;
		frame[0] = (byte) 0xF0;
		frame[1] = (byte) addr;
		frame[2] = (byte) (index/256);
		frame[3] = (byte) index;
		for(int i = 0; i < 128; i++)
		{
			frame[i+4] = buf[i];
		}
		for(int i = 0; i < 132; i++)
		{
			checksum += frame[i];
		}
		frame[132] = checksum;
		try {
			SerialTool.send(frame);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		int sleepCount = 0;
		while(true)
		{
			try {
				Thread.sleep(1);
				sleepCount++;
				if(sleepCount > 1000)
				{
					SerialTool.send(frame);
					sleepCount = 0;
				}
				byte[] readbuf = SerialTool.read();
				if(readbuf != null)
				{
					if(readbuf[0] == -16)
					{
						return;
					}
					else
					{
						SerialTool.send(frame);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	static void sendFile(String file,int addr){
		int numRead = 0;
		int readIndex = 0;
		byte[] buf = new byte[128];
		try {
			FileInputStream fs = new FileInputStream(file);
			while(true){
				mainHandle.fileLeft.setText(String.valueOf(fs.available()));
				numRead = fs.read(buf);
				if(numRead < 0)break;
				sendFileFrame(buf,readIndex,addr);
				readIndex++;
			} 
			fs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	static void sendRelay(int index){
		byte[] buf = new byte[54];
		buf[0] = (byte)0xa0;
		buf[0] = (byte)0xa0;
	}
}
