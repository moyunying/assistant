package cn.moyunying.assistant.entity;

import java.util.Date;

public class LoginTicket {

    private int id;
    private int userId;
    private String cookie;
    private int status;  // 0-有效; 1-无效;
    private Date expireTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "LoginTicket{" +
                "id=" + id +
                ", userId=" + userId +
                ", cookie='" + cookie + '\'' +
                ", status=" + status +
                ", expireTime=" + expireTime +
                '}';
    }
}
