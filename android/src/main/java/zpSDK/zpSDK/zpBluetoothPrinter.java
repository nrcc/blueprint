package zpSDK.zpSDK;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.util.Log;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.io.UnsupportedEncodingException;
        import java.nio.IntBuffer;
        import java.util.UUID;

/* loaded from: zpSDK.jar:zpSDK/zpSDK/zpBluetoothPrinter.class */
public class zpBluetoothPrinter implements PrinterInterface {

    static int w;
    static int h;
    private static BluetoothAdapter myBluetoothAdapter;
    private static BluetoothDevice myDevice;
    private static OutputStream myOutStream = null;
    private static InputStream myInStream = null;
    private static BluetoothSocket mySocket = null;
    private static _PrinterPageImpl impl = new _PrinterPageImpl();
    String TAG = "zpSDK";
    String Encode = "gbk";
    private UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public void setEncode(String str) {
        this.Encode = str;
    }

    public zpBluetoothPrinter() {
    }

    @SuppressLint("MissingPermission")
    private boolean SPPOpen(BluetoothAdapter BluetoothAdapter, BluetoothDevice btDevice) {
        Log.e(this.TAG, "SPPOpen");
        myBluetoothAdapter = BluetoothAdapter;
        myDevice = btDevice;
        if (!myBluetoothAdapter.isEnabled()) {
            return false;
        }
        myBluetoothAdapter.cancelDiscovery();
        try {
            mySocket = myDevice.createRfcommSocketToServiceRecord(this.SPP_UUID);
            try {
                mySocket.connect();
                try {
                    myOutStream = mySocket.getOutputStream();
                    try {
                        myInStream = mySocket.getInputStream();
                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException e) {
                        }
                        Log.e(this.TAG, "SPPOpen OK");
                        return true;
                    } catch (IOException e2) {
                        try {
                            mySocket.close();
                            return false;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                            return false;
                        }
                    }
                } catch (IOException e4) {
                    try {
                        mySocket.close();
                        return false;
                    } catch (IOException e5) {
                        e5.printStackTrace();
                        return false;
                    }
                }
            } catch (IOException e6) {
                return false;
            }
        } catch (IOException e7) {
            e7.printStackTrace();
            return false;
        }
    }

    private static void zp_printer_status_detect() {
        byte[] data = {29, -103, 0, 0};
        SPPWrite(data, 4);
    }

    private static boolean SPPWrite(byte[] Data, int DataLen) {
        flush();
        try {
            myOutStream.write(Data, 0, DataLen);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String printerStatus() {
        zp_printer_status_detect();
        return null;
    }

    private static int zp_printer_status_get(int timeout) {
        byte[] readata = new byte[4];
        int a = 0;
        if (!SPPReadTimeout(readata, 4, timeout) || readata[0] != 29 || readata[1] != -103 || readata[3] != -1) {
            return -1;
        }
        byte status = readata[2];
        if ((status & 1) != 0) {
            a = 1;
        }
        if ((status & 2) != 0) {
            a = 2;
        }
        return a;
    }

    private static boolean SPPReadTimeout(byte[] Data, int DataLen, int Timeout) {
        for (int i = 0; i < Timeout / 5; i++) {
            try {
                if (myInStream.available() >= DataLen) {
                    try {
                        myInStream.read(Data, 0, DataLen);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                }
                try {
                    Thread.sleep(5L);
                } catch (InterruptedException e2) {
                    return false;
                }
            } catch (IOException e3) {
                return false;
            }
        }
        return false;
    }

    private void SPPClose() {
        try {
            mySocket.close();
            Log.e(this.TAG, "SPPCLose2");
        } catch (IOException e) {
        }
    }

    private static byte[] replaceZero(byte[] bytes) {
        int len = 0;
        int i = 0;
        while (true) {
            if (i >= bytes.length) {
                break;
            }
            if (bytes[i] != 0) {
                i++;
            } else {
                len = i;
                break;
            }
        }
        byte[] b = new byte[len];
        System.arraycopy(bytes, 0, b, 0, len);
        if (new String(b).equals("0")) {
            return null;
        }
        return b;
    }

    private static void flush() {
        try {
            myInStream.skip(myInStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void zp_printer_model_detect() {
        byte[] data = {29, 73, 67};
        Write(data);
    }

    private void zp_printer_customer_detect() {
        byte[] data = {29, 73, 66};
        Write(data);
    }

    private void zp_printer_frimware_detect() {
        byte[] data = {29, 73, 65};
        Write(data);
    }

    private boolean isEmpty(byte[] a) {
        return null == a;
    }

    public static byte[] Read(int Timeout) {
        flush();
        float rest = Timeout;
        while (rest > 0.0f) {
            try {
                if (myInStream != null) {
                    int available = myInStream.available();
                    if (available > 0) {
                        byte[] data = new byte[available];
                        myInStream.read(data);
                        return data;
                    }
                    Thread.sleep(100L);
                    rest -= 0.1f;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean connect(String address) {
        if (address == "") {
            return false;
        }
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (myBluetoothAdapter == null) {
            return false;
        }
        myDevice = myBluetoothAdapter.getRemoteDevice(address);
        if (myDevice == null || !SPPOpen(myBluetoothAdapter, myDevice)) {
            return false;
        }
        try {
            Thread.sleep(500L);
            return true;
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
            return true;
        }
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawDATAMATRIX(int x, int y, int h2, String str) {
        impl.drawDATAMATRIX(x, y, h2, str);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void disconnect() {
        SPPClose();
    }

    @Override
    public boolean print(int horizontal, int skip) {
        impl.print(skip);
        try {
            byte[] b = impl.getPrintStr().getBytes(this.Encode);
            if (b == null || !Write(b, b.length)) {
                return false;
            }
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setPrintTime(int time) {
        impl.setPrintTime(time);
    }

    public void noPrint(int horizontal, int skip) {
        impl.print(skip);
    }

    public void noPrint() {
        impl.print();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public boolean print() {
        impl.print();
        try {
            byte[] b = impl.getPrintStr().getBytes(this.Encode);
            if (b == null || !Write(b, b.length)) {
                return false;
            }
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void printPrintStr() {
        Log.e(this.TAG, impl.getPrintStr());
    }

    public String getprintPrintStr() {
        return impl.getPrintStr();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void pageSetup(int pageWidth, int pageHeight) {
        w = pageWidth;
        h = pageHeight;
        impl.Create(pageWidth, pageHeight);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void pageSetup(int pageWidth, int pageHeight, int r, int gap) {
        w = pageWidth;
        h = pageHeight;
        impl.Create(pageWidth, pageHeight, r, gap);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawBox(int lineWidth, int top_left_x, int top_left_y, int bottom_right_x, int bottom_right_y) {
        impl.Drawbox(top_left_x, top_left_y, bottom_right_x, bottom_right_y, lineWidth);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawLine(int lineWidth, int start_x, int start_y, int end_x, int end_y, boolean fullline) {
        impl.DrawLine(start_x, start_y, end_x, end_y, lineWidth);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawText(int text_x, int text_y, String text, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
        impl.DrawText(text_x, text_y, text, fontSize, rotate, bold, reverse, underline);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawText(int text_x, int text_y, String text, int fontType, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
        impl.DrawText(text_x, text_y, text, fontType, fontSize, rotate, bold, reverse, underline);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawText(int text_x, int text_y, String text, String fontType, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
        impl.DrawText(text_x, text_y, text, fontType, fontSize, rotate, bold, reverse, underline);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawBarCode(int start_x, int start_y, String text, int type, int rotate, int linewidth, int height) {
        String type_ = "128";
        if (type == 0) {
            type_ = "39";
        }
        if (type == 1) {
            type_ = "128";
        }
        if (type == 2) {
            type_ = "93";
        }
        if (type == 3) {
            type_ = "CODABAR";
        }
        if (type == 4) {
            type_ = "EAN8";
        }
        if (type == 5) {
            type_ = "EAN13";
        }
        if (type == 6) {
            type_ = "UPCA";
        }
        if (type == 7) {
            type_ = "UPCE";
        }
        if (type == 8) {
            type_ = "I2OF5";
        }
        impl.DrawBarcode1D(type_, start_x, start_y, text, linewidth, height, rotate);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawQrCode(int start_x, int start_y, String text, int rotate, int ver, int lel) {
        impl.DrawBarcodeQRcode(start_x, start_y, text, ver, "M", false, lel);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawGraphic(int start_x, int start_y, int bmp_size_x, int bmp_size_y, Bitmap bmp) {
        impl.DrawBitmap(bmp, start_x, start_y, false);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawINVERSE(int x0, int y0, int x1, int y1, int width) {
        impl.INVERSE(x0, y0, x1, y1, width);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void prefeed(int len) {
        impl.prefeed(len);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void postfeed(int len) {
        impl.postfeed(len);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void alignLeft() {
        impl.alignLeft();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void alignRight() {
        impl.alignRight();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void alignCenter() {
        impl.alignCenter();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void end() {
        impl.end();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void about() {
        impl.about();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void count(int mun) {
        impl.count(mun);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void setMag(int w2, int h2) {
        impl.setMag(w2, h2);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void setSP(int spacing) {
        impl.setSP(spacing);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void setPace() {
        impl.SetPace();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void contRast(int level) {
        impl.contRast(level);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void speed(int level) {
        impl.speed(level);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void setBold(int level) {
        impl.setBold(level);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void printWait(int time) {
        impl.printWait(time);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void barcodeText(int font, int size, int offset, int rotate, int width, int ratio, int height, int x, int y, String data) {
        impl.barcodeText(font, size, offset, rotate, width, ratio, height, x, y, data);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void textConcatenation(int x, int y, int rotate, CONCAT... concats) {
        impl.textConcatenation(x, y, rotate, concats);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void multLine(int height, int fontType, int fontSize, int x, int y, int rotate, String... strs) {
        impl.multLine(height, fontType, fontSize, x, y, rotate, strs);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void backGround(int level) {
        impl.backGround(level);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void bkText(int font, int size, int x, int y, int lev, String str, int rotate) {
        impl.bkText(font, size, x, y, lev, str, rotate);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void drawPrestorePic(int x, int y) {
        impl.drawPrestorePic(x, y);
    }

    public void upLoadPic(Bitmap bitmap) {
        byte[] listData = new byte[4566581];
        byte[] ESCCmdStart = {28, 113, 1};
        System.arraycopy(ESCCmdStart, 0, listData, 0, ESCCmdStart.length);
        int listLen = 0 + ESCCmdStart.length;
        int myBitmapWidth = bitmap.getWidth();
        int myBitmapHeight = bitmap.getHeight();
        int xlen = ((myBitmapWidth + 7) / 8) * 8;
        int ylen = (myBitmapHeight + 7) / 8;
        byte[] data = new byte[xlen * ylen];
        int ndata = 0;
        int[] bmpData = new int[myBitmapWidth * myBitmapHeight];
        bitmap.copyPixelsToBuffer(IntBuffer.wrap(bmpData));
        for (int ii = 0; ii < xlen; ii++) {
            for (int j = 0; j < ylen; j++) {
                data[ndata + j] = 0;
            }
            for (int j2 = 0; j2 < myBitmapHeight; j2++) {
                if ((j2 * myBitmapWidth) + ii < bmpData.length) {
                    int color = bmpData[(j2 * myBitmapWidth) + ii];
                    int b = (color >> 0) & 255;
                    int g = (color >> 8) & 255;
                    int r = (color >> 16) & 255;
                    int grey = ((r + g) + b) / 3;
                    if (grey < 153) {
                        int i = ndata + (j2 / 8);
                        data[i] = (byte) (data[i] | ((byte) (128 >> (j2 % 8))));
                    }
                }
            }
            ndata += ylen;
        }
        int Width = (bitmap.getWidth() + 7) / 8;
        int Height = (bitmap.getHeight() + 7) / 8;
        int DataNum = ((((Width * 8) * Height) * 8) / 8) + 4;
        byte[] ESCCmd = new byte[DataNum];
        ESCCmd[0] = (byte) (Width & 255);
        ESCCmd[1] = (byte) (Width >> 8);
        ESCCmd[2] = (byte) (Height & 255);
        ESCCmd[3] = (byte) (Height >> 8);
        for (int jj = 0; jj < DataNum - 4; jj++) {
            ESCCmd[jj + 4] = data[jj];
        }
        System.arraycopy(ESCCmd, 0, listData, listLen, ESCCmd.length);
        Write(listData, listLen + ESCCmd.length);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public String version() {
        return impl.version();
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public int GetStatus(int time) {
        printerStatus();
        int a = zp_printer_status_get(time);
        return a;
    }

    private String zp_printer_get_customer(int timeout) {
        zp_printer_customer_detect();
        byte[] readata = Read(timeout);
        if (isEmpty(readata)) {
            return null;
        }
        String string = new String(readata);
        return string.substring(0, string.length() - 1);
    }

    private String zp_printer_get_model(int timeout) {
        zp_printer_model_detect();
        byte[] readata = Read(timeout);
        if (isEmpty(readata)) {
            return null;
        }
        String string = new String(readata);
        return string;
    }

    private String zp_printer_get_bluetoothname(int timeout) {
        byte[] data = {29, 73, 73};
        Write(data);
        byte[] readata = Read(timeout);
        if (isEmpty(readata)) {
            return null;
        }
        String string = new String(readata);
        return string;
    }

    private int zp_printer_set_selftest() {
        byte[] data = {31, 82, 22, 0, 48, 2, 16, 0, 0, 0, 117, 105, 95, 112, 114, 110, 95, 115, 101, 108, 102, 116, 101, 115, 116, 0};
        Write(data);
        byte[] readata = Read(8000);
        if (isEmpty(readata)) {
            return -1;
        }
        return 1;
    }

    public boolean Write(byte[] Data, int len) {
        return SPPWrite(Data, len);
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public boolean Write(byte[] Data) {
        return SPPWrite(Data);
    }

    private static boolean SPPWrite(byte[] Data) {
        try {
            myOutStream.write(Data);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void WriteSlow(byte[] Data) {
        int datalen = Data.length;
        int times = datalen / 4096;
        byte[] mdata = new byte[4096];
        for (int i = 0; i < times; i++) {
            System.arraycopy(Data, i * 4096, mdata, 0, 4096);
            SPPWrite(mdata, mdata.length);
            datalen -= 4096;
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (datalen != 0) {
            System.arraycopy(Data, times * 4096, mdata, 0, datalen);
            SPPWrite(mdata, datalen);
        }
    }

    @Override // zpSDK.zpSDK.PrinterInterface
    public void Read(byte[] Data, int len, int timeout) {
        SPPReadTimeout(Data, len, timeout);
    }
}
