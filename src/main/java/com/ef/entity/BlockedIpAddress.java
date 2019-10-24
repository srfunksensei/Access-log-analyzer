package com.ef.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class representing blocked IP address for specific threshold 
 * and time duration
 * 
 * @author mb
 *
 */
@Entity
@Table(name = "blocked_ip_address")
public class BlockedIpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;
    
    private String ip;
    
    private String reason;
    
    private long threshold;
    
    private String duration;
    
    private Date startingFromDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getStartingFromDate() {
        return startingFromDate;
    }

    public void setStartingFromDate(Date startingFromDate) {
        this.startingFromDate = startingFromDate;
    }
}
