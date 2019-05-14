package cn.gxh.view.base;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileIOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created  by gxh on 2018/12/12 13:58
 * <p>
 * order.txt 保存的是每个记录的时间，以分号分隔
 * 单个记录都是单文件保存  time.data
 */
public class RecordUtil {

    public List<OrderRegist> mRegister = new ArrayList<>();
    boolean mUpgrade = false;
//    String mDBPath = "/data/data/\"\n" +
//            "                + context.getPackageName() + \"/databases\"";

    String mDBPath="";

    public RecordUtil(String mDBPath) {
        this.mDBPath = mDBPath;
    }

    public class OrderRegist {
        public String mTime;
        public Map<String, SaveAccessRecord> mCartList;

        public OrderRegist(String time) {
            mTime = time;
            mCartList = new LinkedHashMap<>();
        }
    }


    /**
     * 加载所有time
     *
     * @return
     */
    private boolean loadTime(String type) {
        //already load
        if (!mRegister.isEmpty()) {
            return false;
        }

        String content = FileIOUtils.readFile2String(mDBPath + type);

        if(TextUtils.isEmpty(content)){
            return false;
        }

        String[] split = content.split(";");
        if (split != null && split.length != 0) {

            for (String str : split) {
                if (new File(mDBPath + "/" + str + ".txt").exists()) {
                    mRegister.add(new OrderRegist(new String(str)));
                }
            }
        }
        return true;
    }


    /**
     * load orders
     *
     * @return
     */
    public boolean loadRecords(String type) {
        if (loadTime(type)) {
            try {
                for (OrderRegist order : mRegister) {
                    FileInputStream fs = new FileInputStream(mDBPath + "/" + order.mTime + ".txt");
                    ObjectInputStream ois = new ObjectInputStream(fs);
                    SaveAccessRecord cartListBean = (SaveAccessRecord) ois.readObject();
                    order.mCartList.put(order.mTime, cartListBean);

                    fs.close();
                    ois.close();
                }
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    public void addOrder(String time, SaveAccessRecord bean, String type) {
        try {
            boolean add = true;//是否需要添加
            for (OrderRegist orderRegist : mRegister) {
                if (orderRegist.mTime.equals(time)) {
                    orderRegist.mCartList.put(time, bean);//如果时间一样，则保存最后一次
                    add = false;
                    break;
                }
            }
            if (add) {
                OrderRegist frface = new OrderRegist(time);
                frface.mCartList.put(time, bean);
                mRegister.add(frface);
            }

            //update all names
            boolean isOK=FileIOUtils.writeFileFromString(mDBPath + type, time + ";", true);

            //save new feature
            FileOutputStream fs = new FileOutputStream(mDBPath + "/" + time + ".txt", true);
            ObjectOutputStream oos = new ObjectOutputStream(fs);
            oos.writeObject(bean);
            oos.close();
            fs.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(String time, String type) {
        try {
            //check if already registered.
            boolean find = false;
            for (OrderRegist frface : mRegister) {
                if (frface.mTime.equals(time)) {
                    File delfile = new File(mDBPath + "/" + time + ".txt");
                    if (delfile.exists()) {
                        delfile.delete();
                    }
                    mRegister.remove(frface);
                    find = true;
                    break;
                }
            }

            if (find) {
                //整个文件要重写
                File file = new File(mDBPath + type);
                if (file.exists()) {
                    file.delete();
                }

                for (OrderRegist orderRegist : mRegister) {
                    FileIOUtils.writeFileFromString(mDBPath + type, orderRegist.mTime + ";", true);
                }
            }
            return find;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getFileContent(String type){
        return FileIOUtils.readFile2String(mDBPath + type);
    }
}
