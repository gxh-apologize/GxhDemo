package cn.gxh.print;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import cn.gxh.base.Logger;


// 商米  常用指令封装
public class ESCUtil {

    public static final byte ESC = 0x1B;// Escape
    public static final byte FS = 0x1C;// Text delimiter
    public static final byte GS = 0x1D;// Group separator
    public static final byte DLE = 0x10;// data link escape
    public static final byte EOT = 0x04;// End of transmission
    public static final byte ENQ = 0x05;// Enquiry character
    public static final byte SP = 0x20;// Spaces
    public static final byte HT = 0x09;// Horizontal list
    public static final byte LF = 0x0A;//Print and wrap (horizontal orientation)
    public static final byte CR = 0x0D;// Home key
    public static final byte FF = 0x0C;// Carriage control (print and return to the standard mode (in page mode))
    public static final byte CAN = 0x18;// Canceled (cancel print data in page mode)

    //初始化打印机
    public static byte[] init_printer() {
        byte[] result = new byte[2];
        result[0] = ESC;
        result[1] = 0x40;
        return result;
    }

    //打印浓度指令
    public static byte[] setPrinterDarkness(int value) {
        byte[] result = new byte[9];
        result[0] = GS;
        result[1] = 40;
        result[2] = 69;
        result[3] = 4;
        result[4] = 0;
        result[5] = 5;
        result[6] = 5;
        result[7] = (byte) (value >> 8);
        result[8] = (byte) value;
        return result;
    }

    /**
     * 打印单个二维码 sunmi自定义指令
     *
     * @param code:       二维码数据
     * @param modulesize: 二维码块大小(单位:点, 取值 1 至 16 )
     * @param errorlevel: 二维码纠错等级(0 至 3)
     *                    0 -- 纠错级别L ( 7%)
     *                    1 -- 纠错级别M (15%)
     *                    2 -- 纠错级别Q (25%)
     *                    3 -- 纠错级别H (30%)
     */
    public static byte[] getPrintQRCode(String code, int modulesize, int errorlevel) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(setQRCodeSize(modulesize));
            buffer.write(setQRCodeErrorLevel(errorlevel));
            buffer.write(getQCodeBytes(code));
            buffer.write(getBytesForPrintQRCode(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    /**
     * 横向两个二维码 sunmi自定义指令
     *
     * @param code1:      二维码数据
     * @param code2:      二维码数据
     * @param modulesize: 二维码块大小(单位:点, 取值 1 至 16 )
     * @param errorlevel: 二维码纠错等级(0 至 3)
     *                    0 -- 纠错级别L ( 7%)
     *                    1 -- 纠错级别M (15%)
     *                    2 -- 纠错级别Q (25%)
     *                    3 -- 纠错级别H (30%)
     */
    public static byte[] getPrintDoubleQRCode(String code1, String code2, int modulesize, int errorlevel) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(setQRCodeSize(modulesize));
            buffer.write(setQRCodeErrorLevel(errorlevel));
            buffer.write(getQCodeBytes(code1));
            buffer.write(getBytesForPrintQRCode(false));
            buffer.write(getQCodeBytes(code2));

            //加入横向间隔
            buffer.write(new byte[]{0x1B, 0x5C, 0x18, 0x00});

            buffer.write(getBytesForPrintQRCode(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

//	/**
//	 * 光栅打印二维码
//	 */
//	public static byte[] getPrintQRCode2(String data, int size){
//		byte[] bytes1  = new byte[4];
//		bytes1[0] = GS;
//		bytes1[1] = 0x76;
//		bytes1[2] = 0x30;
//		bytes1[3] = 0x00;
//
//		byte[] bytes2 = BytesUtil.getZXingQRCode(data, size);
//		return BytesUtil.byteMerger(bytes1, bytes2);
//	}

    /**
     * 打印一维条形码
     */
//    public static byte[] getPrintBarCode(String data, int symbology, int height, int width, int textposition) {
//        if (symbology < 0 || symbology > 10) {
//            return new byte[]{LF};
//        }
//
//        if (width < 2 || width > 6) {
//            width = 2;
//        }
//
//        if (textposition < 0 || textposition > 3) {
//            textposition = 0;
//        }
//
//        if (height < 1 || height > 255) {
//            height = 162;
//        }
//
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        try {
//            buffer.write(new byte[]{0x1D, 0x66, 0x01, 0x1D, 0x48, (byte) textposition,
//                    0x1D, 0x77, (byte) width, 0x1D, 0x68, (byte) height, 0x0A});
//
//            byte[] barcode;
//            if (symbology == 10) {
//       //         barcode = BytesUtil.getBytesFromDecString(data);
//            } else {
//                barcode = data.getBytes("GB18030");
//            }
//
//            if (symbology > 7) {
//         //       buffer.write(new byte[]{0x1D, 0x6B, 0x49, (byte) (barcode.length + 2), 0x7B, (byte) (0x41 + symbology - 8)});
//            } else {
//                buffer.write(new byte[]{0x1D, 0x6B, (byte) (symbology + 0x41), (byte) barcode.length});
//            }
//            buffer.write(barcode);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return buffer.toByteArray();
//    }


    /**
     * 跳指定行数
     */
    public static byte[] nextLine(int lineNum) {
        byte[] result = new byte[lineNum];
        for (int i = 0; i < lineNum; i++) {
            result[i] = LF;
        }

        return result;
    }

    // ------------------------underline-----------------------------
    //设置下划线1点
    public static byte[] underlineWithOneDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 1;
        return result;
    }

    //设置下划线2点
    public static byte[] underlineWithTwoDotWidthOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 2;
        return result;
    }

    //取消下划线
    public static byte[] underlineOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 45;
        result[2] = 0;
        return result;
    }

    // ------------------------bold-----------------------------

    /**
     * 字体加粗
     */
    public static byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消字体加粗
     */
    public static byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0;
        return result;
    }

    // ------------------------character-----------------------------
    /*
     *单字节模式开启
     */
    public static byte[] singleByte() {
        byte[] result = new byte[2];
        result[0] = FS;
        result[1] = 0x2E;
        return result;
    }

    /*
     *单字节模式关闭
     */
    public static byte[] singleByteOff() {
        byte[] result = new byte[2];
        result[0] = FS;
        result[1] = 0x26;
        return result;
    }

    /**
     * 设置单字节字符集
     */
    public static byte[] setCodeSystemSingle(byte charset) {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 0x74;
        result[2] = charset;
        return result;
    }

    /**
     * 设置多字节字符集
     */
    public static byte[] setCodeSystem(byte charset) {
        byte[] result = new byte[3];
        result[0] = FS;
        result[1] = 0x43;
        result[2] = charset;
        return result;
    }

    // ------------------------Align-----------------------------

    /**
     * 居左
     */
    public static byte[] alignLeft() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 0;
        return result;
    }

    /**
     * 居中对齐
     */
    public static byte[] alignCenter() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 1;
        return result;
    }

    /**
     * 居右
     */
    public static byte[] alignRight() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 97;
        result[2] = 2;
        return result;
    }

    //切刀
    public static byte[] cutter() {
        byte[] data = new byte[]{0x1d, 0x56, 0x01};
        return data;
    }

    //走纸到黑标
    public static byte[] gogogo() {
        byte[] data = new byte[]{0x1C, 0x28, 0x4C, 0x02, 0x00, 0x42, 0x31};
        return data;
    }


    ////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////          private                /////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////
    private static byte[] setQRCodeSize(int modulesize) {
        //二维码块大小设置指令
        byte[] dtmp = new byte[8];
        dtmp[0] = GS;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x43;
        dtmp[7] = (byte) modulesize;
        return dtmp;
    }

    private static byte[] setQRCodeErrorLevel(int errorlevel) {
        //二维码纠错等级设置指令
        byte[] dtmp = new byte[8];
        dtmp[0] = GS;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x45;
        dtmp[7] = (byte) (48 + errorlevel);
        return dtmp;
    }


    private static byte[] getBytesForPrintQRCode(boolean single) {
        //打印已存入数据的二维码
        byte[] dtmp;
        if (single) {        //同一行只打印一个QRCode， 后面加换行
            dtmp = new byte[9];
            dtmp[8] = 0x0A;
        } else {
            dtmp = new byte[8];
        }
        dtmp[0] = 0x1D;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x51;
        dtmp[7] = 0x30;
        return dtmp;
    }

    private static byte[] getQCodeBytes(String code) {
        //二维码存入指令
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            byte[] d = code.getBytes("GB18030");
            int len = d.length + 3;
            if (len > 7092) len = 7092;
            buffer.write((byte) 0x1D);
            buffer.write((byte) 0x28);
            buffer.write((byte) 0x6B);
            buffer.write((byte) len);
            buffer.write((byte) (len >> 8));
            buffer.write((byte) 0x31);
            buffer.write((byte) 0x50);
            buffer.write((byte) 0x30);
            for (int i = 0; i < d.length && i < len; i++) {
                buffer.write(d[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    //--------------------------------------自己补充的

    public static byte[] strToBytes(String str) {
        byte[] b = new byte[0];
        try {
            b = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return b;
    }

    //居中
    public static byte[] middle() {
        byte[] result = new byte[3];
        result[0] = 0x1b;
        result[1] = 0x61;
        result[2] = 0x01;
        return result;
    }

    public static byte[] right() {
        byte[] result = new byte[3];
        result[0] = 0x1b;
        result[1] = 0x61;
        result[2] = 0x02;
        return result;
    }

    //居左
    public static byte[] left() {
        byte[] result = new byte[3];
        result[0] = 0x1b;
        result[1] = 0x61;
        result[2] = 0x00;
        return result;
    }

    public static byte[] fontSize() {
        byte[] result = new byte[3];
        result[0] = 0x1b;
        result[1] = 0x21;
        result[2] = 0x10;
        return result;
    }


    /**
     * 左侧汉字最多显示几个文字
     */
    private static final int LEFT_TEXT_MAX_LENGTH = 8;
    private static final int LEFT_LENGTH = 16;
    private static final int RIGHT_LENGTH = 16;

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        Logger.d("gxh", marginBetweenLeftAndMiddle + ";" + marginBetweenMiddleAndRight + ";" + leftTextLength + ";" + leftText);

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    @SuppressLint("NewApi")
    public static int getBytesLength(String msg) {
        int length = msg.getBytes(Charset.forName("GBK")).length;
        if (msg.contains("¥") && length >= 4) {
            length=length-3;
        }
        return length;
    }
}