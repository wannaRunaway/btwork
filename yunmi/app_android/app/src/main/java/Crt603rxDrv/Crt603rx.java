package Crt603rxDrv;

import android.util.Log;

public class Crt603rx {

	public native int OpenDevice(char sPort[], int iBaudRate);
	public native int CloseDevice();
	public native int GetDllVersion(char VersMes[]);
	public native int SetBaudRate(int iMode);
	public native int Beel(int iBeelTime);
	public native int SetWorkMode(int bWire, int bAutoFindCard);
	public native int SetLed(int bStatus);
	public native int FindCard(int iMode, int iOutLen[], byte byOutData[]);
	public native int ReadBlock(int iKeyType, int iMode, int iBlock, int iKeyLen, byte byKeyData[], byte byOutData[]);
	public native int WriteBlock(int iKeyType, int iMode, int iBlock, int iKeyLen, byte byKeyData[], int iDataLen, byte byInData[]);
	public native int ReadSector(int iKeyType, int iMode, int iSector, int iKeyLen, byte byKeyData[], byte byOutData[]);
	public native int InitWallet(int iKeyType, int iMode, int iBlock, int iKeyLen, byte byKeyData[], int iInitValue);
	public native int ReadWallet(int iKeyType, int iMode, int iBlock, int iKeyLen, byte byKeyData[], int iOutValue[]);
	public native int AddWallet(int iKeyType, int iMode, int iBlock, int iKeyLen, byte byKeyData[], int iAddValue);
	public native int SubWallet(int iKeyType, int iMode, int iBlock, int iKeyLen, byte byKeyData[], int iSubValue);
	public native int BackupWallet(int iKeyType, int iMode, int iDataBlock, int iBackBlock, int iKeyLen, byte byKeyData[]);
	public native int MifareSleep();
	public native int LoadKey(int iKeyId, int iKeyLen, byte byKeyData[]);
	public native int ReadEEPROM(int iAddress, int iReadLen, byte byOutData[]);
	public native int WriteEEPROM(int iAddress, int iWriteLen, byte byInData[]);
	public native int typeAchipPower(int iOutAtrLen[], byte byOutAtrData[]);
	public native int typeBchipPower(int iOutAtrLen[], byte byOutAtrData[]);
	public native int RFchipPower(int iOutAtrLen[], byte byOutAtrData[]);
	public native int RFSendApdu(int iSendApduLen, byte bySendApduData[], 	int iRecvApduLen[], byte byRecvApduData[]);
	public native int ReadUltraLight(int iBlock, byte byOutData[]);
	public native int WriteUltraLight(int iBlock, int iDateLen, byte byWriteData[]);
	public native int RFCpuCardSleep();
	public native int SAM1ChipPower(int iOutAtrLen[], byte byOutAtrData[]);
	public native int SAM2ChipPower(int iOutAtrLen[], byte byOutAtrData[]);
	public native int SAM1SendApdu(int iSendApduLen, byte bySendApduData[], int iRecvApduLen[], byte byRecvApduData[]);
	public native int SAM2SendApdu(int iSendApduLen, byte bySendApduData[], int iRecvApduLen[], byte byRecvApduData[]);
	static
	{
        try {  

        	System.loadLibrary("crt603rx_drv");	
        }  

        catch (UnsatisfiedLinkError ule) {  
        	Log.e("Crt603rx", "Load crt603rx_drv Error!");
        }  
	}
}
