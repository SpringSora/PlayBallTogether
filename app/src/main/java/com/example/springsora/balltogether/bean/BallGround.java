package com.example.springsora.balltogether.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JJBOOM on 2016/4/29.
 */
public class BallGround implements Serializable {
    //��id;
    int p_id;
    //������
    String GroundName;
    //������
    int BallType;
    //������
    int GroundNum;
    //��ʣ������
    int GroundLeft;
    //�򳡼۸�
    float GroundPrice;
    //����Ϣ
    String GroundInfo;
    //�򳡵绰
    String GroundPhone;
    //�򳡾���
    double lng;
    //��γ��
    double lat;
    //�����ڳ���
    String city;
    //��λ��
    String address;
    //������
    Seller seller;
    //���ͼƬ1·��
    String BallGroundPic1Path;
    //���ͼƬ2·��
    String BallGroundPic2Path;
    //���ͼƬ3·��
    String BallGroundPic3Path;

    int isBusiness;

    List<Comment> comments;



    public BallGround(String address, String ballGroundPic1Path, String ballGroundPic2Path, String ballGroundPic3Path, int ballType, String city, String groundInfo, int groundLeft, String groundName, int groundNum, String groundPhone, float groundPrice, int isBusiness, double lat, double lng, int p_id, Seller seller,List<Comment> comments) {
        this.address = address;
        BallGroundPic1Path = ballGroundPic1Path;
        BallGroundPic2Path = ballGroundPic2Path;
        BallGroundPic3Path = ballGroundPic3Path;
        BallType = ballType;
        this.city = city;
        GroundInfo = groundInfo;
        GroundLeft = groundLeft;
        GroundName = groundName;
        GroundNum = groundNum;
        GroundPhone = groundPhone;
        GroundPrice = groundPrice;
        this.isBusiness = isBusiness;
        this.lat = lat;
        this.lng = lng;
        this.p_id = p_id;
        this.seller = seller;
        this.comments = comments;
    }
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public String getGroundName() {
        return GroundName;
    }
    public void setGroundName(String groundName) {
        GroundName = groundName;
    }
    public int getBallType() {
        return BallType;
    }
    public void setBallType(int ballType) {
        BallType = ballType;
    }
    public int getGroundNum() {
        return GroundNum;
    }
    public void setGroundNum(int groundNum) {
        GroundNum = groundNum;
    }
    public float getGroundPrice() {
        return GroundPrice;
    }
    public void setGroundPrice(float groundPrice) {
        GroundPrice = groundPrice;
    }
    public String getGroundInfo() {
        return GroundInfo;
    }
    public void setGroundInfo(String groundInfo) {
        GroundInfo = groundInfo;
    }
    public String getGroundPhone() {
        return GroundPhone;
    }
    public void setGroundPhone(String groundPhone) {
        GroundPhone = groundPhone;
    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public Seller getSeller() {
        return seller;
    }
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    public String getBallGroundPic1Path() {
        return BallGroundPic1Path;
    }
    public void setBallGroundPic1Path(String ballGroundPic1Path) {
        BallGroundPic1Path = ballGroundPic1Path;
    }
    public String getBallGroundPic2Path() {
        return BallGroundPic2Path;
    }
    public void setBallGroundPic2Path(String ballGroundPic2Path) {
        BallGroundPic2Path = ballGroundPic2Path;
    }
    public String getBallGroundPic3Path() {
        return BallGroundPic3Path;
    }
    public void setBallGroundPic3Path(String ballGroundPic3Path) {
        BallGroundPic3Path = ballGroundPic3Path;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getGroundLeft() {
        return GroundLeft;
    }
    public void setGroundLeft(int groundLeft) {
        GroundLeft = groundLeft;
    }

    public int getIsBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(int isBusiness) {
        this.isBusiness = isBusiness;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
