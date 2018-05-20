package com.tokenbank.tokeninformation.model;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class Article {


    /**
     * hid : 21
     * title : TokenPocket released a new version
     * lang : en
     * author : TokenPocket Team
     * url : https://www.mytokenpocket.vip/2018/03/11/en/iosupdatev002/index.html
     * img_url : http://www.mytokenpocket.vip/images/tokenpocket_logo.jpg
     * status : 0
     * create_time : 2018-03-13T00:56:32Z
     */

    private int hid;
    private String title;
    private String lang;
    private String author;
    private String url;
    @SerializedName("img_url")
    private String imageUrl;
    private int status;
    @SerializedName("create_time")
    private String createTime;

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
