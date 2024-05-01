package zpSDK.zpSDK;

public class CONCAT {
    String str;

    public CONCAT(int font, int size, int offset, String str) {
        this.str = String.format("%d %d %d %s\r\n", Integer.valueOf(font), Integer.valueOf(size), Integer.valueOf(offset), str);
    }

    public String getStr() {
        return this.str;
    }
}