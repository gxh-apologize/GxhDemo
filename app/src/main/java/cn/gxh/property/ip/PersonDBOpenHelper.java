package cn.gxh.property.ip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created  by gxh on 2019/5/30 10:18
 */
public class PersonDBOpenHelper extends SQLiteOpenHelper {

    /**
     * 创建数据库需要的构造器
     * @param context 上下文对象
     * @param name 数据库名，以db结尾，例如：itcast.db ， 重要
     * @param factory 查询使用的游标，如果不用的话，就写null
     * @param version 数据库的版本号，从1开始，重要，只能升
     */
    public PersonDBOpenHelper(Context context) {
        super(context,"itcast.db",null,1);
    }
    /**
     * 创建数据库中的表
     * 因为先执行构造器创建数据库，后执行onCreate()创建表，系统会将创建好的数据库传入onCreate方法中
     *
     * 如果当前应用下有数据库itcast.db了 ，就不会执行onCreate()方法了;
     * 如果当前应用下无数据库itcast.db了 ，就会创建onCreate()方法了;
     */
    @Override
    public void onCreate(SQLiteDatabase db) {//后
        System.out.println("onCreate()");
        //创建表
        String sql = "create table person(id integer primary key autoincrement," +
                "username varchar not null," +
                "password varchar not null)";
        //执行SQL语句
        db.execSQL(sql);
    }
    /**
     * 如果数据库版本更新了，就会调用onUpgrade()方法，项目中对表结构修改
     * 如果数据库版本不更新了，就会不调用onUpgrade()方法，
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("onUpgrade()");
        //String sql = "alter table person add hiredate varchar default '2016-10-1'";
        //String sql = "alter table person drop hiredate";查询资料
        //String sql = "drop table person";
        //db.execSQL(sql);
    }
}
