package cn.gxh.view.base;

import java.io.Serializable;

public class SaveAccessRecord implements Serializable{
    private  String UserId;
    private  String MobileId;
    private  String PicPath;
    private  int UserType;
    private  int TurnOver;
    public  float Similar;
    public  String getUserId(){return UserId;}
    public  String getmobileId(){return MobileId;}
    public  String getbase64(){return PicPath;}
    public  float getSimilar(){return Similar;}
    public   int getUserType(){return  UserType;}
    public  int getTurnOver(){return TurnOver; };
    public SaveAccessRecord(String _userId,String _mobileId,float _similar,String _base64,int _userType,int _turnOver)
    {
        this.UserId=_userId;
        this.PicPath=_base64;
        this.MobileId=_mobileId;
        this.Similar=_similar;
        this.UserType=_userType;
        this.TurnOver=_turnOver;
    }
}
