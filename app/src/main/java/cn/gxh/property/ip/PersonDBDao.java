package cn.gxh.property.ip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created  by gxh on 2019/5/30 10:17
 */
public class PersonDBDao {

    /**
     * 操作数据库和表的类
     */
    private PersonDBOpenHelper personDBOpenHelper;
    /**
     * 构造器
     */
    public PersonDBDao(Context context){
        personDBOpenHelper = new PersonDBOpenHelper(context);
    }
    /**
     * 增加用户
     * insert()方法的参数：
     * 参数一：表名
     * 参数二：如果分配一个NULL的话，就表示不能插入NULL值
     * 参数三：是一个类似于Map的类型,key为列名，value为列值
     * 返回值：插入记录的ID号
     */
    public void add(Person person){
        //从PersonDBOpenHelper中获取SQLiteDateBase类
        SQLiteDatabase db = personDBOpenHelper.getWritableDatabase();
        //插入一条记录到Person表中
        ContentValues values = new ContentValues();
        //ID为自增长
        values.put("username",person.getUsername());
        values.put("password",person.getPassword());
        long i = db.insert("person",null,values);
        System.out.println("i="+i);
        //关闭数据库
        db.close();
    }
    /**
     * 修改用户
     * update()方法：
     * 参数一：表名
     * 参数二：修改列名对应的列值，类似于Map<列名,值>
     * 参数三：where条件，支持?号占位符
     * 参数四：为where条件中的?号占位符设置值，类型为String[]
     */
    public void update(Person person){
        SQLiteDatabase db = personDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password",person.getPassword());
        db.update("person",values,"id=?",new String[]{person.getId()+""});
        db.close();
    }
    /**
     * 根据ID删除用户
     * delete from person where id = ?
     * delete()方法的参数：
     * 参数一：表名
     * 参数二：where条件，支持?占位符
     * 参数三：where条件的真实值，是String[]类型
     */
    public void delete(int id){
        SQLiteDatabase db = personDBOpenHelper.getWritableDatabase();
        db.delete("person","id=?",new String[]{id+""});
        db.close();
    }
    /**
     * 根据姓名和密码查询用户
     * select * from person where username=? and password=?
     * query()方法参数：
     * 参数一：表名
     * 参数一：查询哪些字段，null表示*，即所有字段
     * 参数一：where条件
     * 参数一：where条件的真实值
     * 参数一：分组，类似SQL中的group by，如果不分组的话，书写null
     * 参数一：分组过滤条件,如果不分组的话，书写null
     * 参数一：排序，例如：asc 或 desc ，如果不排序的话，书写null
     * 返回值：Cursor，理解为JDBC中的ResultSet对象，在默认情况下，游标指向第一条记录之前或上
     */
    public void queryByNameAndPass(Person person){
        SQLiteDatabase db = personDBOpenHelper.getReadableDatabase();
        Cursor c = db.query(
                "person",
                new String[]{"id","username","password"},
                "username=? and password=?",
                new String[]{person.getUsername(),person.getPassword()},
                null,
                null,
                null);
        //将游标移动到第一条记录
        c.moveToFirst();
        //按列名或索引号来获取每列的值
        int id = c.getInt(0);
        String username = c.getString(1);
        String password = c.getString(2);
        //在控制台显示
        System.out.println(id+"#"+username+"#"+password);
        c.close();
        db.close();
    }
    /**
     * 查询所有用户
     * select * from person
     */
    public List<Person> queryAll(){
        List<Person> personList = new ArrayList<Person>();

        SQLiteDatabase db = personDBOpenHelper.getReadableDatabase();
        Cursor c = db.query("person",new String[]{"id","username","password"},null,null,null,null,null);

        while(c.moveToNext()){
            int id = c.getInt(c.getColumnIndex("id"));
            String username = c.getString(c.getColumnIndex("username"));
            String password = c.getString(c.getColumnIndex("password"));
            Person person = new Person(id,username,password);
            personList.add(person);
        }

        c.close();
        db.close();

        return personList;
    }


    public List<Person> query(String content){
        List<Person> personList = new ArrayList<Person>();

        SQLiteDatabase db = personDBOpenHelper.getReadableDatabase();
        Cursor c = db.query("person",
                new String[]{"id","username","password"},
                "username LIKE ?  or password LIKE ? ",
                new String[]{"%" + content + "%" ,"%" + content + "%" },null,null,null);

        while(c.moveToNext()){
            int id = c.getInt(c.getColumnIndex("id"));
            String username = c.getString(c.getColumnIndex("username"));
            String password = c.getString(c.getColumnIndex("password"));
            Person person = new Person(id,username,password);
            personList.add(person);
        }

        c.close();
        db.close();

        return personList;
    }
}
