package com.example.lenovo.entity;

import java.io.Serializable;
import java.util.Date;
public class Message implements Serializable {
    private Integer message_id;
    private Integer message_class;
    private Date message_time;
    private Integer message_sender;
    private Integer message_receiver;
    private String message_content;
    private String message_title;

    public Message(){}

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public Integer getMessage_class() {
        return message_class;
    }

    public void setMessage_class(Integer message_class) {
        this.message_class = message_class;
    }

    public Date getMessage_time() {
        return message_time;
    }

    public void setMessage_time(Date message_time) {
        this.message_time = message_time;
    }

    public Integer getMessage_sender() {
        return message_sender;
    }

    public void setMessage_sender(Integer message_sender) {
        this.message_sender = message_sender;
    }

    public Integer getMessage_receiver() {
        return message_receiver;
    }

    public void setMessage_receiver(Integer message_receiver) {
        this.message_receiver = message_receiver;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getMessage_title() {
        return message_title;
    }

    public void setMessage_title(String message_title) {
        this.message_title = message_title;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", message_class=" + message_class +
                ", message_time=" + message_time +
                ", message_sender=" + message_sender +
                ", message_receiver=" + message_receiver +
                ", message_content='" + message_content + '\'' +
                ", message_title='" + message_title + '\'' +
                '}';
    }

    public Message(Integer message_id, Integer message_class, Date message_time, Integer message_sender, Integer message_receiver, String message_content, String message_title) {
        this.message_id = message_id;
        this.message_class = message_class;
        this.message_time = message_time;
        this.message_sender = message_sender;
        this.message_receiver = message_receiver;
        this.message_content = message_content;
        this.message_title = message_title;
    }
}
