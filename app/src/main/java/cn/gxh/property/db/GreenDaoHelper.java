package cn.gxh.property.db;

import android.content.Context;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import cn.gxh.base.Logger;

public class GreenDaoHelper {

    private static GreenDaoHelper mInstance;
    private UserDao userDao;

    public static GreenDaoHelper getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoHelper.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoHelper();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {

        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "newFace", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();
    }

    public boolean insertUser(User user) {
        if (userDao != null) {
            if (getUserById(user.getUserid()).isEmpty()) {
                long result = userDao.insert(user);
                return result > 0 ? true : false;
            } else {
                Logger.d("gxh",user.getUserid()+"存在");
                //userDao.update(userGreenBean);
                return true;
            }
        }
        return false;
    }

    public List<User> getUserById(String id) {
        if (userDao != null) {
            return userDao.queryBuilder().where(UserDao.Properties.Userid.eq(id)).list();
        }
        return null;
    }

    public void deleteUserById(String id) {
        if (userDao != null) {
            userDao.queryBuilder().where(UserDao.Properties.Userid.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
        }
    }

    public void getUserByCount2() {
        if (userDao != null) {
            final Query<User> query = userDao.queryBuilder().build();
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<User> list = query.forCurrentThread().list();
                                Logger.d("gxh_getUserByCount2",list.size()+"");
                            } catch (Exception ex) {

                            }
                        }
                    }
            ).start();
        }
    }

    public void getUserByCount() {
        if (userDao != null) {
            List<User> list = userDao.queryBuilder().build().list();
            Logger.d("gxh_getUserByCount2",list.size()+"");
        }
    }
}
