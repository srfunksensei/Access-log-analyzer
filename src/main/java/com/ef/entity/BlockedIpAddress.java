package com.ef.entity;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "ip", updatable = false, nullable = false)
    private String ip;

    @Column(name = "reason", updatable = false, nullable = false)
    private String reason;

    @Column(name = "threshold")
    private long threshold;

    @Column(name = "duration", updatable = false, nullable = false)
    private String duration;

    @Column(name = "starting_from_date", updatable = false, nullable = false)
    private Date startingFromDate;

    public long getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(final String reason) {
        this.reason = reason;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(final long threshold) {
        this.threshold = threshold;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    public Date getStartingFromDate() {
        return startingFromDate;
    }

    public void setStartingFromDate(final Date startingFromDate) {
        this.startingFromDate = startingFromDate;
    }
}
