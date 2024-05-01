package zpSDK.zpSDK;

import android.graphics.Bitmap;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.util.Locale;



/* loaded from: zpSDK.jar:zpSDK/zpSDK/_PrinterPageImpl.class */
public class _PrinterPageImpl {
  private String printStr = "";
  private String LastprintStr = "";
  private int PrintTime = 1;
  private int mHeight;
  private int mWidth;

  public void Create(int width, int height) {
    this.printStr = "";
    this.mHeight = height;
    this.mWidth = width;
  }

  public void setPrintTime(int time) {
    this.PrintTime = time;
  }

  void add(String str) {
    this.printStr += str;
  }

  public void Create(int width, int height, int r, int gap) {
    this.printStr = "";
    this.mHeight = height;
    this.mWidth = width;
    switch (r) {
      case 0:
        add("ZPROTATE\r\n");
        break;
      case PrinterInterface.MSG_CONNECT_START /* 1 */:
        add("ZPROTATE90\r\n");
        break;
      case PrinterInterface.MSG_CONNECT_ING /* 2 */:
        add("ZPROTATE180\r\n");
        break;
      case PrinterInterface.MSG_CONNECT_FINISH /* 3 */:
        add("ZPROTATE270\r\n");
        break;
    }
    if (gap == 1) {
      add("GAP-SENSE\r\n");
    } else if (gap == 2) {
      add("BAR-SENSE LEFT\r\n");
    } else if (gap == 3) {
      add("BAR-SENSE\r\n");
    }
  }

  public String getPrintStr() {
    if (this.LastprintStr.contains("³")) {
      this.LastprintStr = this.LastprintStr.replace("³", "3");
    }
    return this.LastprintStr;
  }

  public void prefeed(int len) {
    add(String.format("PREFEED %d\r\n", Integer.valueOf(len)));
  }

  public void postfeed(int len) {
    add(String.format("POSTFEED %d\r\n", Integer.valueOf(len)));
  }

  public void INVERSE(int x0, int y0, int x1, int y1, int width) {
    String str = String.format("INVERSE-LINE %d %d %d %d %d\r\n", Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width));
    add(str);
  }

  public void DrawBitmap(Bitmap bmp, int x, int y, boolean rotate) {
    int w = bmp.getWidth();
    int h = bmp.getHeight();
    int byteCountW = (w + 7) / 8;
    int[] bmpData = new int[w * h];
    byte[] outData = new byte[byteCountW * h];
    bmp.copyPixelsToBuffer(IntBuffer.wrap(bmpData));
    for (int yy = 0; yy < h; yy++) {
      for (int xx = 0; xx < w; xx++) {
        int c = bmpData[(yy * w) + xx];
        int i1 = (c >> 16) & 255;
        int g = (c >> 8) & 255;
        int b = c & 255;
        int gray = ((((i1 * 30) + (g * 59)) + (b * 11)) + 50) / 100;
        if (gray < 128) {
          outData[(byteCountW * yy) + (xx / 8)] = (byte) (outData[(byteCountW * yy) + (xx / 8)] | (128 >> (xx % 8)));
        }
      }
    }
    String cmd = "EG";
    if (rotate) {
      cmd = "VEG";
    }
    String strCmdHeader = String.format("%s %d %d %d %d ", cmd, Integer.valueOf(byteCountW), Integer.valueOf(h), Integer.valueOf(x), Integer.valueOf(y));
    String strData = "";
    for (byte b2 : outData) {
      strData = strData + ByteToString(b2);
    }
    add(strCmdHeader + strData + "000\r\n");
  }

  public void barcodeText(int font, int size, int offset, int rotate, int width, int ratio, int height, int x, int y, String data) {
    String cm = String.format("BARCODE 128 %d %d %d %d %d %s\r\n", Integer.valueOf(width), Integer.valueOf(ratio), Integer.valueOf(height), Integer.valueOf(x), Integer.valueOf(y), data);
    if (rotate == 1) {
      cm = String.format("VBARCODE 128 %d %d %d %d %d %s\r\n", Integer.valueOf(width), Integer.valueOf(ratio), Integer.valueOf(height), Integer.valueOf(x), Integer.valueOf(y), data);
    }
    String str = String.format("BARCODE-TEXT %d %d %d\r\n", Integer.valueOf(font), Integer.valueOf(size), Integer.valueOf(offset));
    add(str);
    add(cm);
    add("BARCODE-TEXT OFF\r\n");
  }

  public void DrawText(int text_x, int text_y, String text, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
    if (bold != 0) {
      setBold(bold);
    }
    if (underline) {
      underLine(true);
    }
    String cmd = rotate == 90 ? "VT" : "T";
    if (rotate == 180) {
      cmd = "T180";
    }
    if (rotate == 270) {
      cmd = "T270";
    }
    int f_size = 24;
    int f_height = 24;
    if (fontSize == 1) {
      f_size = 55;
      f_height = 16;
    }
    if (fontSize == 2) {
      f_size = 24;
      f_height = 24;
    }
    if (fontSize == 3) {
      f_size = 56;
      f_height = 32;
    }
    if (fontSize == 4) {
      f_size = 24;
      setMag(2, 2);
      f_height = 48;
    }
    if (fontSize == 5) {
      f_size = 56;
      setMag(2, 2);
      f_height = 64;
    }
    if (fontSize == 6) {
      f_size = 24;
      setMag(3, 3);
      f_height = 72;
    }
    if (fontSize == 7) {
      f_size = 32;
      setMag(4, 4);
      f_height = 96;
    }
    if (fontSize == 8) {
      f_size = 24;
      setMag(5, 5);
      f_height = 120;
    }
    if (fontSize == 9) {
      f_size = 32;
      setMag(6, 6);
      f_height = 192;
    }
    if (fontSize == 10) {
      f_size = 24;
      setMag(7, 7);
      f_height = 168;
    }
    if (fontSize == 11) {
      f_size = 32;
      setMag(8, 8);
      f_height = 256;
    }
    if (fontSize == 12) {
      f_size = 24;
      setMag(9, 9);
      f_height = 216;
    }
    String temp = String.format("%s %d %d %d %d %s\r\n", cmd, Integer.valueOf(f_size), 0, Integer.valueOf(text_x), Integer.valueOf(text_y), text);
    add(temp);
    if (reverse) {
      try {
        byte[] bytetext = text.getBytes("gbk");
        if (bytetext == null) {
          return;
        }
        int block_w = (f_height / 2) * bytetext.length;
        INVERSE(text_x, text_y, text_x + block_w, text_y, f_height);
      } catch (UnsupportedEncodingException e) {
        return;
      }
    }
    if (underline) {
      underLine(false);
    }
    setMag(0, 0);
    if (bold != 0) {
      setBold(0);
    }
  }

  public void SetPace() {
    add("PACE\r\n");
  }

  public void drawPrestorePic(int x, int y) {
    String str = String.format(Locale.ENGLISH, "PCX %d %d !<IMAGE0.PCX\r\n", Integer.valueOf(x), Integer.valueOf(y));
    add(str);
  }

  public void DrawText(int text_x, int text_y, String text, int fontType, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
    if (underline) {
      underLine(true);
    }
    if (bold != 0) {
      setBold(bold);
    }
    String cmd = rotate == 90 ? "VT" : "T";
    if (rotate == 180) {
      cmd = "T180";
    }
    if (rotate == 270) {
      cmd = "T270";
    }
    int f_height = fontSize;
    if (f_height == 0) {
      f_height = 24;
    }
    String temp = String.format("%s %d %d %d %d %s\r\n", cmd, Integer.valueOf(fontType), Integer.valueOf(fontSize), Integer.valueOf(text_x), Integer.valueOf(text_y), text);
    add(temp);
    if (reverse) {
      try {
        byte[] bytetext = text.getBytes("gbk");
        if (bytetext == null) {
          return;
        }
        int block_w = (f_height / 2) * bytetext.length;
        INVERSE(text_x, text_y, text_x + block_w, text_y, f_height);
      } catch (UnsupportedEncodingException e) {
      }
    }
  }

  public void DrawText(int text_x, int text_y, String text, String fontType, int fontSize, int rotate, int bold, boolean reverse, boolean underline) {
    if (underline) {
      underLine(true);
    }
    if (bold != 0) {
      setBold(bold);
    }
    String cmd = rotate == 90 ? "VT" : "T";
    if (rotate == 180) {
      cmd = "T180";
    }
    if (rotate == 270) {
      cmd = "T270";
    }
    int f_height = fontSize;
    if (f_height == 0) {
      f_height = 24;
    }
    String temp = String.format("%s %s %d %d %d %s\r\n", cmd, fontType, Integer.valueOf(fontSize), Integer.valueOf(text_x), Integer.valueOf(text_y), text);
    add(temp);
    if (reverse) {
      try {
        byte[] bytetext = text.getBytes("gbk");
        if (bytetext == null) {
          return;
        }
        int block_w = (f_height / 2) * bytetext.length;
        INVERSE(text_x, text_y, text_x + block_w, text_y, f_height);
      } catch (UnsupportedEncodingException e) {
        return;
      }
    }
    if (underline) {
      underLine(false);
    }
    setMag(0, 0);
    if (bold != 0) {
      setBold(0);
    }
  }

  public void count(int mun) {
    String str = String.format("COUNT %d\r\n", Integer.valueOf(mun));
    add(str);
  }

  public void setMag(int w, int h) {
    String str = String.format("SETMAG %d %d\r\n", Integer.valueOf(w), Integer.valueOf(h));
    add(str);
  }

  public void Drawbox(int x0, int y0, int x1, int y1, int width) {
    String str = String.format("BOX %d %d %d %d %d\r\n", Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width));
    add(str);
  }

  public void DrawLine(int x0, int y0, int x1, int y1, int width) {
    String str = String.format("LINE %d %d %d %d %d\r\n", Integer.valueOf(x0), Integer.valueOf(y0), Integer.valueOf(x1), Integer.valueOf(y1), Integer.valueOf(width));
    add(str);
  }

  public void DrawBarcode1D(String type, int x, int y, String text, int width, int height, int rotate) {
    String cmd = "BARCODE";
    if (rotate == 1) {
      cmd = "VBARCODE";
    }
    String str = String.format("%s %s %d 1 %d %d %d %s\r\n", cmd, type, Integer.valueOf(width - 1), Integer.valueOf(height), Integer.valueOf(x), Integer.valueOf(y), text);
    add(str);
  }

  public void DrawBarcodeQRcode(int x, int y, String text, int size, String errLevel, boolean rotate, int len) {
    String cmd = "BARCODE";
    if (rotate) {
      cmd = "VBARCODE";
    }
    String str = String.format("%s QR %d %d M %d U %d\r\n", cmd, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(len), Integer.valueOf(size));
    add(str);
    String tm = String.format("%sA,", errLevel);
    add(tm);
    add(text);
    add("\r\nENDQR\r\n");
  }

  public void alignLeft() {
    add("LEFT\r\n");
  }

  public void alignRight() {
    add("RIGHT\r\n");
  }

  public void alignCenter() {
    add("CENTER\r\n");
  }

  public void end() {
    add("END\r\n");
  }

  public void about() {
    add("ABOUT\r\n");
  }

  public void textConcatenation(int x, int y, int rotate, CONCAT... concats) {
    String temp = String.format("CONCAT %d %d\r\n", Integer.valueOf(x), Integer.valueOf(y));
    if (rotate == 1) {
      temp = String.format("VCONCAT %d %d\r\n", Integer.valueOf(x), Integer.valueOf(y));
    }
    add(temp);
    for (CONCAT concat : concats) {
      add(concat.getStr());
    }
    add("ENDCONCAT\r\n");
  }

  public void multLine(int height, int fontTpye, int fontSize, int x, int y, int rotate, String... strs) {
    String direction;
    add(String.format("ML %d\r\n", Integer.valueOf(height)));
    switch (rotate) {
      case 0:
        direction = "T";
        break;
      case 90:
        direction = "T90";
        break;
      case 180:
        direction = "T180";
        break;
      case 270:
        direction = "T270";
        break;
      default:
        direction = "T";
        break;
    }
    String temp = String.format("%s %d %d %d %d\r\n", direction, Integer.valueOf(fontTpye), Integer.valueOf(fontSize), Integer.valueOf(x), Integer.valueOf(y));
    add(temp);
    for (String s : strs) {
      add(String.format("%s\r\n", s));
    }
    add("ENDML\r\n");
  }

  public void contRast(int level) {
    add(String.format("CONTRAST %d\r\n", Integer.valueOf(level)));
  }

  public void speed(int level) {
    add(String.format("SPEED %d\r\n", Integer.valueOf(level)));
  }

  public void setBold(int level) {
    add(String.format("SETBOLD %d\r\n", Integer.valueOf(level)));
  }

  public void setSP(int spacing) {
    add(String.format("SETSP %d\r\n", Integer.valueOf(spacing)));
  }

  public void underLine(boolean mode) {
    if (mode) {
      add(String.format("UNDERLINE ON\r\n", new Object[0]));
    } else {
      add(String.format("UNDERLINE OFF\r\n", new Object[0]));
    }
  }

  public void pace() {
    add("PACE\r\n");
  }

  public void printWait(int time) {
    add(String.format("WAIT %d\r\n", Integer.valueOf(time)));
  }

  public void backGround(int level) {
    add(String.format("f %d\r\n", Integer.valueOf(level)));
  }

  public void bkText(int font, int size, int x, int y, int lev, String str, int rotate) {
    String direction;
    switch (rotate) {
      case 0:
        direction = "BKT";
        break;
      case 90:
        direction = "BKT90";
        break;
      case 180:
        direction = "BKT180";
        break;
      case 270:
        direction = "BKT270";
        break;
      default:
        direction = "BKT";
        break;
    }
    String temp = String.format("BACKGROUND %d\r\n", Integer.valueOf(lev)) + String.format("%s %d %d %d %d %s\r\n", direction, Integer.valueOf(font), Integer.valueOf(size), Integer.valueOf(x), Integer.valueOf(y), str) + "BACKGROUND 0\r\n";
    add(temp);
  }

  public void drawDATAMATRIX(int x, int y, int h, String str) {
    String begin = String.format("B DATAMATRIX %d %d H %d\r\n", Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(h));
    add(begin + str + "\r\nENDDATAMATRIX\r\n");
  }

  public void print(int skip) {
    switch (skip) {
      case PrinterInterface.MSG_CONNECT_START /* 1 */:
        add("GAP-SENSE\r\n");
        add("FORM\r\n");
        break;
      case PrinterInterface.MSG_CONNECT_ING /* 2 */:
        add("BAR-SENSE-LEFT\r\n");
        add("FORM\r\n");
        break;
      case PrinterInterface.MSG_CONNECT_FINISH /* 3 */:
        add("BAR-SENSE\r\n");
        add("FORM\r\n");
        break;
    }
    add("PRINT\r\n");
    String str = String.format("! 0 200 200 %d %d\r\nPAGE-WIDTH %d\r\n", Integer.valueOf(this.mHeight), Integer.valueOf(this.PrintTime), Integer.valueOf(this.mWidth));
    StringBuilder st = new StringBuilder(this.printStr);
    st.insert(0, str);
    this.LastprintStr = st.toString();
  }

  public void print() {
    add("FORM\r\n");
    add("PRINT\r\n");
    String str = String.format("! 0 200 200 %d %d\r\nPAGE-WIDTH %d\r\n", Integer.valueOf(this.mHeight), Integer.valueOf(this.PrintTime), Integer.valueOf(this.mWidth));
    StringBuilder st = new StringBuilder(this.printStr);
    st.insert(0, str);
    this.LastprintStr = st.toString();
  }

  private String IntToHex(byte data) {
    switch (data) {
      case 0:
      case PrinterInterface.MSG_CONNECT_START /* 1 */:
      case PrinterInterface.MSG_CONNECT_ING /* 2 */:
      case PrinterInterface.MSG_CONNECT_FINISH /* 3 */:
      case PrinterInterface.MSG_DISCONNECTED /* 4 */:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
        char ch = (char) (data + 48);
        String r = Character.toString(ch);
        return r;
      case 10:
        return "A";
      case 11:
        return "B";
      case 12:
        return "C";
      case 13:
        return "D";
      case 14:
        return "E";
      case 15:
        return "F";
      default:
        Log.d("long", "ch is error ");
        return "";
    }
  }

  private String ByteToString(byte data) {
    byte d1 = (byte) ((data >> 4) & 15);
    byte d2 = (byte) (data & 15);
    String str = String.valueOf(IntToHex(d1)) + IntToHex(d2);
    return str;
  }

  public String version() {
    return "V2.0";
  }
}

