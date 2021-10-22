package com.github.bobbobbob15;

import org.simplejavamail.api.mailer.config.TransportStrategy;

import java.io.Serializable;
import java.util.Arrays;

public class Person implements Serializable {
    private String username;
    private String password;

    public TransportStrategy getTransportStrategy() {
        return transportStrategy;
    }

    public void setTransportStrategy(TransportStrategy transportStrategy) {
        this.transportStrategy = transportStrategy;
    }

    public Person(String username, String password){
        this.username = username;
        this.password = password;
        new Person();
    }
    public Person(){

    }

    private TransportStrategy transportStrategy;

    public void setImapHost(String imapHost) {
        this.imapHost = imapHost;
    }

    public void setPopHost(String popHost) {
        this.popHost = popHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public void setImapPort(int imapPort) {
        this.imapPort = imapPort;
    }

    public void setPopPort(int popPort) {
        this.popPort = popPort;
    }

    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    private String imapHost;
    private String popHost;
    private String smtpHost;
    private int imapPort;
    private int popPort;
    private int smtpPort;

    public String getImapHost() {
        return imapHost;
    }

    public String getPopHost() {
        return popHost;
    }

    @Override
    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", imapHost='" + imapHost + '\'' +
                ", popHost='" + popHost + '\'' +
                ", smtpHost='" + smtpHost + '\'' +
                ", imapPort=" + imapPort +
                ", popPort=" + popPort +
                ", smtpPort=" + smtpPort +
                '}';
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public int getImapPort() {
        return imapPort;
    }

    public int getPopPort() {
        return popPort;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
