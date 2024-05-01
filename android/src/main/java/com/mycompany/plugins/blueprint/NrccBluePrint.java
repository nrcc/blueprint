package com.mycompany.plugins.blueprint;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.Base64;
import java.util.Set;
import java.util.UUID;



import zpSDK.zpSDK.CONCAT;
import zpSDK.zpSDK.GZIPFrame;
import zpSDK.zpSDK.zpBluetoothPrinter;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.ByteArrayInputStream;

public class NrccBluePrint {

    public static String BStr = "gbk";
    String cpclString = "! 0 200 200 80 1" + "\n" +
            "PAGE-WIDTH 574" + "\n" +
            "T 24 0 200 10 " + "UROVO优博讯" + "\n" +
            "PRINT" + "\n";
    String cpclString2 = "! 0 200 200 400 1\n" +
            "GAP-SENSE\n" +
            "SETBOLD 2\n" +
            "SETMAG 2 2 \n" +
            "T 24 0 16 24 惠州-惠东1仓网格站\n" +
            "SETBOLD 1\n" +
            "SETMAG 1 1 \n" +
            "B 128 2 1 75 16 80 11234567890643\n" +
            "T 24 0 152 165 11234567890643\n" +
            "SETSP 2\n" +
            "ML 35\r\n" +
            "T 24 0 16 230 " +
            "托盘：TP00933\r\n" +
            "SKU：59\r\n" +
            "容器数：4\r\n" +
            "打印时间：2021-04-02T22:11:59.305\r\n" +
            "ENDML\r\n" +
            "SETSP 0\n" +
            "SETSP 2\n" +
            "ML 35\r\n" +
            "T 24 0 240 264 " +
            "SKU件数：519\r\n" +
            "打印人：卢子红\r\n" +
            "ENDML\r\n" +
            "SETSP 0\n" +
            "FORM\n" +
            "PRINT\n";
    /**
     * String:sdk,drawtext:UROVO优博讯
     * @param data
     * @param printType
     */
    public void print(String address,String data,String printType) {
        if (address == null) {
            throw new RuntimeException("no printer！");
        }

        switch (printType) {
            case "Feed":
                if (address != null) {
                    SDKFeed(address);
                } else {
                    throw new RuntimeException("no printer！");
                }
                break;
            case "String":
                if (address != null) {
                    SDKDemo(address,data);
                } else {
                    throw new RuntimeException("no printer！");
                }
                break;
            case "CPCL":
                if (address != null) {
                    sendText(address, data);
                } else {
                    throw new RuntimeException("no printer！");
                }
                break;
            case "ESC":
                if (address != null) {
                    printEscDemo(address);
                } else {
                    throw new RuntimeException("no printer！");
                }
                break;
            case "1Dplus":
                if (address != null) {
                    print1DBarcode(address);
                } else {
                    throw new RuntimeException("no printer！");
                }
                break;
            case "2Dplus":
                if (address != null) {
                    print2DBarcode(address);
                } else {
                    throw new RuntimeException("no printer！");
                }
                break;
            case "Pic":
                if (address != null) {
                    printPicture(address,data);
                } else {
                    throw new RuntimeException("no printer！");
                }
                break;
            case "Send":
                    sendText(address, data);
                break;
            case "Sample":
                try {
                    printSample(address);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void printSample(String mac) throws InterruptedException {
        final zpBluetoothPrinter printer = new zpBluetoothPrinter();
        if (!printer.connect(mac)) {
            throw new RuntimeException("connect fail------") ;
        }

        String str = "! 0 200 200 210 1\r\n" +
                "CONCAT 75 75\r\n" +
                "4 2 5 $\r\n" +
                "4 3 0 12\r\n" +
                "4 2 5 34\r\n" +
                "ENDCONCAT \r\n" +
                "FORM\r\n" +
                "PRINT\r\n";
          printer.pageSetup(200, 200);
        printer.drawText(10, 10, "打印机测试文字", 1, 0, 0, false, false);
        printer.drawText(10, 30, "打印机测试文字", 2, 0, 0, false, false);
        printer.drawText(10, 80, "打印机测试文字", 3, 0, 0, false, false);
        printer.drawText(10, 120, "打印机测试文字", 4, 0, 0, false, false);
        printer.drawText(10, 200, "打印机测试文字", 5, 0, 0, false, false);
        printer.drawText(10, 280, "打印机测试文字", 6, 0, 0, false, false);
        printer.drawText(10, 350, "打印机测试文字", 7, 0, 0, false, false);
        printer.drawText(10, 420, "打印机测试文字", 8, 0, 0, false, false);

        printer.print(0, 0);
        int a = printer.GetStatus(800);
        Log.e("zpSDK", String.valueOf(a));
        printer.disconnect();
    }


    //command
    public void sendText(String SelectedBDAddress, String input) {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter();
        if (!zpSDK.connect(SelectedBDAddress)) {
            throw new RuntimeException("connect fail------") ;
        }
        try {

            byte []b = input.getBytes(BStr);
            byte [] nb = GZIPFrame.codec(b);
            //zpSDK.Write(new byte[]{0x1B, 0x74, (byte) 0xff});
            zpSDK.Write(nb);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zpSDK.Write(new byte[]{0x0d, 0x0a});
        zpSDK.disconnect();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void SDKFeed(String SelectedBDAddress) {
        zpBluetoothPrinter mBigP = new zpBluetoothPrinter();
            if(mBigP.connect(SelectedBDAddress)) {
                {
                    mBigP.pageSetup(600, 300);
                    mBigP.drawText(300,120, "11111111",1,0,0,false,false);
                    mBigP.print(0, 1);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mBigP.disconnect();
            }

    }

    public void SDKDemo(String SelectedBDAddress, String input) {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter();
        if (!zpSDK.connect(SelectedBDAddress)) {
            throw new RuntimeException(("connect fail------"));
        }
        zpSDK.pageSetup(574, 110);
        zpSDK.drawText(20, 0, input, 2, 0, 0, false, false);
        zpSDK.print(0, 1);
        zpSDK.disconnect();
    }

    public void printEscDemo(String SelectedBDAddress) {
        zpBluetoothPrinter mESCPrinter=new zpBluetoothPrinter();
        if (!mESCPrinter.connect(SelectedBDAddress)) {
            throw new RuntimeException("connect fail------") ;
        }
        try {
            for (int i = 0; i < 1; i++) {
                mESCPrinter.Write(new byte[]{0x1B, 0x40});        //reseting printer,初始化打印机
                mESCPrinter.Write(new byte[]{0x1b, 0x21, 0x00});
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});
                mESCPrinter.Write(new byte[]{0x30, 0x31, 0x32, 0x33});
                mESCPrinter.Write(new byte[]{0x0d, 0x0a});
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x02});    //CHOOSE 32*32
                mESCPrinter.Write(new byte[]{0x1b, 0x61, 0x00}); //set MIDDLE，对齐方式（居中）
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline，设置/取消下划线打印
                mESCPrinter.Write(new byte[]{0x1b, 0x45, 0x01});    //set BOLD，设置粗体打印
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x14});    //line width 0.125*3，设置行间距
                mESCPrinter.Write("\n".getBytes("GBK"));
                mESCPrinter.Write("Illegal parking notice\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x45, 0x00});    //cancel BOLD，取消粗体打印
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x00});    //line width 0.125:0
                mESCPrinter.Write(new byte[]{0x1d, 0x68, 0x50});//bar code height set to 10mm，条码高度
                mESCPrinter.Write(new byte[]{0x1d, 0x77, 0x02});//bar code width set to 3，条码窄条比
                mESCPrinter.Write(new byte[]{0x1d, 0x48, 0x02});//识别符显示位置为正下方
                mESCPrinter.Write(new byte[]{0x1d, 0x66, 0x00});
                mESCPrinter.Write(new byte[]{0x1D, 0x6B, 0x49, 0x0A, '6', '0', '0', '0', '1', '0', '0', '0', '8', '6'});//:ӡcodebar�룺*6000100086*
                mESCPrinter.Write("\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x10});    //line width 0.125:8*2
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x01});    //choose 16*16 ziku
                mESCPrinter.Write("*  6  0  0  0  1  0  0  0  8  6  *\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x61, 0x0}); //choose the left
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x00});    //choose 24*24 ziku
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x8});    //line width 0.125*4
                mESCPrinter.Write("Law enforcement code:310118\n".getBytes("GBK"));
                mESCPrinter.Write("Vehicle brand:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("huE29146".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("     color:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("black\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("vehicle type:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("car\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("Illegal stopping time:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});//underline
                mESCPrinter.Write("2018".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("/".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("03".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});//cancel underline
                mESCPrinter.Write("/".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("31".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write(".".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("17".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("h".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("15".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("min\n".getBytes("GBK"));
                mESCPrinter.Write("Illegal parking:".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("The intersection of outside qingsong road and qinghu road is about n200 meters\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write(" If the motor vehicle parking at the above time and place violates the provisions of article 56 of road traffic safety, please present this notice to the traffic police detachment of qingpu district public security bureau for handling within 3 .s and 15 .s.(venue: 8938 north qingsong highway, time: Mon. to Satur., 8:30-16:30))\n".getBytes("GBK"));
                mESCPrinter.Write("\n".getBytes("GBK"));
                mESCPrinter.Write(String.format("%40s\n", "2018/3mon31").getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x4d, 0x01});    //choose 16*16 ziku
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x10});    //line width 0.125*8:2
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x01});    //underline
                mESCPrinter.Write("                                                                       \n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x1b, 0x33, 0x08});    //line width 0.125*8:1
                mESCPrinter.Write(new byte[]{0x1b, 0x2d, 0x00});    //cancel underline
                mESCPrinter.Write("Note: 1. If the domicile address or contact number of all levels of motor vehicles has changed, please apply to the vehicle management office at the place of registration for change and filing in time.\n".getBytes("GBK"));
                mESCPrinter.Write("2. Those who hold a peony card and have no objection to the illegal facts can handle the traffic violation and pay the penalty through telephone bank (95588), online bank (www.icbc.com.cn or www.sh.icbc.com.cn) and multimedia self-service terminal\n".getBytes("GBK"));
                mESCPrinter.Write(new byte[]{0x0d, 0x0a});
                mESCPrinter.Write(new byte[]{0x0d, 0x0a});
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }//直接发送输入框内容
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mESCPrinter.disconnect();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void print1DBarcode(String address) {
        zpBluetoothPrinter zpSDK = new zpBluetoothPrinter();
        if (!zpSDK.connect(address)) {
            throw new RuntimeException("connect fail------") ;
        }
        zpSDK.pageSetup(574, 200);
        zpSDK.drawBarCode(10, 40, "X11JMO016DR450170", 128, 0, 1, 50);
        zpSDK.drawText(10, 5, "X11JMO016DR450170", 1, 0, 0, false, false);
        zpSDK.barcodeText(0,1,5,0,1,0,50,20,20,"X11JMO016DR450170");
        zpSDK.drawBarCode(10, 40, "X11JMO016DR450170", 128, 0, 1, 50);
        zpSDK.print(0, 1);
        zpSDK.disconnect();

    }


    public void print2DBarcode(String address) {
        var zpSDK = new zpBluetoothPrinter();
        if (!zpSDK.connect(address)) {
            throw new RuntimeException("connect fail------") ;
        }
        zpSDK.pageSetup(574, 180);
        zpSDK.drawQrCode(230, 10, "http://en.urovo.com", 0, 4, 3);
        zpSDK.print(0, 0);
        zpSDK.disconnect();
    }

    public void printPicture(String address,String base64Str) {
        var zpSDK = new zpBluetoothPrinter();
        if (!zpSDK.connect(address)) {
            throw new RuntimeException("connect fail------") ;
        }

        /**---------------打印图片-------------------------*/

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opts.inDensity =60;
        opts.inTargetDensity = 60;
        byte[] imageBytes;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 对于API级别24及以上，使用java.util.Base64
            imageBytes = Base64.getDecoder().decode(base64Str);
        } else {
            // 对于API级别22到23，使用android.util.Base64
            imageBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT);
        }
        // 将字节数组转换为Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

        var b= GZIPFrame.Draw_Page_Bitmap_(bitmap);

        zpSDK.Write(b);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        zpSDK.disconnect();

    }


    /*-------------------------------------------------- */

    public String echo(String value) {
        Log.i("Echo android", value);
        return "android "+value;
    }

    BluetoothAdapter mBluetoothAdapter;
    private final UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @SuppressLint("MissingPermission")
    public JSONArray list(Context context) throws RuntimeException, JSONException {
        var pairedDevices = new JSONArray();
        if ((mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()) == null) {
            throw new RuntimeException("can not find the BT adaptor");
        }

        var mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() <= 0) return pairedDevices;
        for (BluetoothDevice mDevice : mPairedDevices) {
            JSONObject device = new JSONObject();
            device.put("name", mDevice.getName());
            device.put("address", mDevice.getAddress());
            int type = mDevice.getType();
            switch (type) {
                default:
                case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                    device.put("type", "unknown");
                    break;
                case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                    device.put("type", "classic");
                    break;
                case BluetoothDevice.DEVICE_TYPE_LE:
                    device.put("type", "le");
                    break;
                case BluetoothDevice.DEVICE_TYPE_DUAL:
                    device.put("type", "dual");
                    break;
            }
            pairedDevices.put(device);
        }

        return pairedDevices;
    }

    @SuppressLint("MissingPermission")
    public void connect(Context context, String address) throws RuntimeException {
        try {
            var zpSDK = new zpBluetoothPrinter();
            if (!zpSDK.connect(address)) {
                throw new RuntimeException("Could not connect to printer") ;
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not connect to printer", e);
        }
    }

    /**
     * 释放链接
     * @throws RuntimeException
     */
    public void disconnect() throws RuntimeException {
        try {
            var zpSDK = new zpBluetoothPrinter();
            zpSDK.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("Could not disconnect from printer", e);
        }
    }
}
