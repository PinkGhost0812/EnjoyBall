package com.example.lenovo.entity;
<<<<<<< Updated upstream

=======
import java.io.Serializable;
>>>>>>> Stashed changes
import java.util.Date;

public class Message implements Serializable {
    private Integer id;
    private Integer cla;
    private Date time;
    private Integer sender;
    private Integer receiver;
    private String content;
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCla() {
        return cla;
    }

    public void setCla(Integer cla) {
        this.cla = cla;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", cla=" + cla +
                ", time=" + time +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public Message(Integer id, Integer cla, Date time, Integer sender, Integer receiver, String content, String title) {
        this.id = id;
        this.cla = cla;
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.title = title;
    }
}
