package zpSDK.zpSDK;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class UsbSocket {

    Context context;
    String TAG="zpSDK";
    static UsbSocket mUsbSocket=null;
    private int sysVersion;
    public static UsbSocket getInstance(Context context)
    {
        if (mUsbSocket==null)
            mUsbSocket=new UsbSocket(context);
        return mUsbSocket;
    }
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    UsbManager mUsbManager;

    UsbSocket(Context context)
    {
        this.context=context;
        sysVersion = Integer.parseInt(Build.VERSION.SDK);
//        if(sysVersion<19)
//            context.onNewIntent(context.getIntent());
        context.registerReceiver(mUsbReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
        context.registerReceiver(mUsbReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if(UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
            {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if(device.getVendorId()==1155&&device.getProductId()==22339)
                {
                    Log.e(TAG, "USB打印机已接入");
                    Toast.makeText(context,"USB打印机已接入", Toast.LENGTH_LONG).show();
                }
            }
            if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if(device.getVendorId()==1155&&device.getProductId()==22339)
                {
                    Log.e(TAG, "USB打印机已断开");
                    Toast.makeText(context,"USB打印机已断开", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    public void ConnectPrinter()
    {
        context.registerReceiver(mUsbReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_ATTACHED));
        context.registerReceiver(mUsbReceiver, new IntentFilter(UsbManager.ACTION_USB_DEVICE_DETACHED));
    }

   public String Write(byte[] prnData)
    {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while(deviceIterator.hasNext())
        {

            UsbDevice usbDevice = deviceIterator.next();
            if(usbDevice.getVendorId()!=1155||usbDevice.getProductId()!=22339)continue;
            if(writePort(usbManager,usbDevice,prnData))
            {
                Log.e(TAG, "USB打印成功");
                Toast.makeText(context,"USB打印成功", Toast.LENGTH_LONG).show();
                return "USB打印成功";
            }
            else
            {
                Log.e(TAG, "USB打印失败");
                Toast.makeText(context,"USB打印失败", Toast.LENGTH_LONG).show();
                return "USB打印失败";
            }
        }
        Log.e(TAG, "无法连接USB打印机");
        Toast.makeText(context,"无法连接USB打印机", Toast.LENGTH_LONG).show();
        return "无法连接USB打印机";
    }

    boolean writePort(UsbManager manager,UsbDevice usbDev, byte[] data)
    {
        boolean success=false;
        UsbEndpoint end_in = null;
        UsbEndpoint end_out = null;
        try {
            UsbInterface interf = usbDev.getInterface(0);

            int k = interf.getEndpointCount();
            for (int m = 0; m < k; m++)
            {
                UsbEndpoint localUsbEndpoint = interf.getEndpoint(m);
                if (localUsbEndpoint.getDirection() == 128)
                {
                    end_in = localUsbEndpoint;
                }
                else if (localUsbEndpoint.getDirection() == 0)
                {
                    end_out = localUsbEndpoint;
                }
            }

            if (end_out != null)
            {
                UsbDeviceConnection connection = manager.openDevice(usbDev);
                connection.claimInterface(interf, true);
                // bulkTransfer通过给定的endpoint来进行大量的数据传输，传输的方向取决于该节点的方向，
                // 传输成果返回传输字节数组的长度，失败返回负数
                int flag = connection.bulkTransfer(end_out, data,data.length, 0);
                if (flag >= data.length)success=true;
                if (connection != null)
                {
                    connection.releaseInterface(interf);
                    connection.close();
                }
                interf = null;
                connection = null;
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return success;
    }


    public  byte[] Read(int Timeout)
    {
//        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//        UsbDeviceConnection connection = manager.openDevice(usbDev);
        return null;



    }

}
