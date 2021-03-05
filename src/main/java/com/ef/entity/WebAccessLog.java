package com.ef.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity class representing Web Access Log line
 *
 * @author mb
 *
 */
@Entity
@Table(name = "web_access_log")
public class WebAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "date", updatable = false, nullable = false)
    private Date date;

    @Column(name = "ip", updatable = false, nullable = false)
    private String ip;

    @Column(name = "method", updatable = false, nullable = false)
    private String method;

    @Column(name = "http_status_code", updatable = false, nullable = false)
    private int httpStatusCode;

    @Column(name = "user_agent", updatable = false, nullable = false)
    private String userAgent;

    public long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(final String method) {
        this.method = method;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(final int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(final String userAgent) {
        this.userAgent = userAgent;
    }
}
