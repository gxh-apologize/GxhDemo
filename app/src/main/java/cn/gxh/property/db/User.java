package cn.gxh.property.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

    @Id
    public Long id;

    @Index(unique = true)
    public String userid;

    public String name;
    public String password;
    public String avatar;
    @Generated(hash = 2041619651)
    public User(Long id, String userid, String name, String password,
            String avatar) {
        this.id = id;
        this.userid = userid;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    

}
