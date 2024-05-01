package zpSDK.zpSDK;


import android.graphics.Bitmap;

/* loaded from: zpSDK.jar:zpSDK/zpSDK/PrinterInterface.class */
public interface PrinterInterface {
    public static final int MSG_CONNECT_START = 1;
    public static final int MSG_CONNECT_ING = 2;
    public static final int MSG_CONNECT_FINISH = 3;
    public static final int MSG_DISCONNECTED = 4;

    /* loaded from: zpSDK.jar:zpSDK/zpSDK/PrinterInterface$BarcodeType.class */
    public enum BarcodeType {
        JAN3_EAN13,
        JAN8_EAN8,
        CODE39,
        CODE93,
        CODE128,
        CODABAR,
        ITF,
        UPC_A,
        UPC_E,
        EAN13Plus2,
        EAN13Plus5,
        EAN8Plus2,
        EAN8Plus5,
        UPCAPlus2,
        UPCAPlus5,
        UPCEPlus2,
        UPCEPlus5,
        Postnet,
        MSI,
        QR
    }

    /* loaded from: zpSDK.jar:zpSDK/zpSDK/PrinterInterface$PrintManufacturer.class */
    public enum PrintManufacturer {
    }

    boolean connect(String str);

    void disconnect();

    boolean print(int i, int i2);

    boolean print();

    void pageSetup(int i, int i2);

    void pageSetup(int i, int i2, int i3, int i4);

    void drawBox(int i, int i2, int i3, int i4, int i5);

    void drawLine(int i, int i2, int i3, int i4, int i5, boolean z);

    void drawText(int i, int i2, String str, int i3, int i4, int i5, boolean z, boolean z2);

    void drawText(int i, int i2, String str, int i3, int i4, int i5, int i6, boolean z, boolean z2);

    void drawText(int i, int i2, String str, String str2, int i3, int i4, int i5, boolean z, boolean z2);

    void drawBarCode(int i, int i2, String str, int i3, int i4, int i5, int i6);

    void drawQrCode(int i, int i2, String str, int i3, int i4, int i5);

    void drawGraphic(int i, int i2, int i3, int i4, Bitmap bitmap);

    void drawINVERSE(int i, int i2, int i3, int i4, int i5);

    int GetStatus(int i);

    boolean Write(byte[] bArr);

    void Read(byte[] bArr, int i, int i2);

    void prefeed(int i);

    void postfeed(int i);

    void alignLeft();

    void alignRight();

    void alignCenter();

    void end();

    void about();

    void count(int i);

    void setMag(int i, int i2);

    void barcodeText(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, String str);

    void textConcatenation(int i, int i2, int i3, CONCAT... concatArr);

    void multLine(int i, int i2, int i3, int i4, int i5, int i6, String... strArr);

    void contRast(int i);

    void speed(int i);

    void setBold(int i);

    void printWait(int i);

    void backGround(int i);

    void bkText(int i, int i2, int i3, int i4, int i5, String str, int i6);

    void setSP(int i);

    void setPace();

    void drawDATAMATRIX(int i, int i2, int i3, String str);

    void setPrintTime(int i);

    void drawPrestorePic(int i, int i2);

    String version();
}