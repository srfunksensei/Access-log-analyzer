package com.ef;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ef.entity.BlockedIpAddress;
import com.ef.entity.WebAccessLog;

/**
 * Class representing filter for blocked IPs
 * 
 * @author mb
 *
 */
public class BlockedIpFilter {
    
    private final List<WebAccessLog> records;
    private final Date startDate;
    private final Date endDate;
    private final Duration duration;
    private final long threshold;
    
    public BlockedIpFilter(final List<WebAccessLog> records, final Date startDate, final Duration duration, final long threshold) {
        this.startDate = startDate;
        this.endDate = getEndDate(startDate, duration);
        this.duration = duration;
        this.threshold = threshold;
        
        this.records = getRecordsBetween(records, startDate, endDate);
    }
    
    
    private List<WebAccessLog> getRecordsBetween(final List<WebAccessLog> records, final Date startDate, final Date endDate) {
        return records.stream()
                .filter(record -> record.getDate().after(startDate) && record.getDate().before(endDate))
                .collect(Collectors.toList());
    }

    private Date getEndDate(final Date startDate, final Duration duration) {
        int type = duration.equals(Duration.DAILY) ? Calendar.HOUR_OF_DAY : Calendar.DATE;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(type, 1);
        return cal.getTime();
    }
    
    private Map<Object, Long> groupRecordsCountByIpAddress(){
        return records.stream().map(WebAccessLog::getIp).collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }
    
    private List<Object> getBlockedIps() {
        return groupRecordsCountByIpAddress().entrySet().stream()
                .filter(e -> e.getValue() > threshold)
                .map(Map.Entry::getKey)
                .distinct()
                .collect(Collectors.toList());
    }
    
    public List<BlockedIpAddress> getBlockedIpAddresses() {
        final List<BlockedIpAddress> blocked = new ArrayList<>();
    
        getBlockedIps().forEach(e -> blocked.add(buildBlockedIpAddress((String) e)));
    
        return blocked;
    }
    
    private BlockedIpAddress buildBlockedIpAddress(final String ip)  {
        final BlockedIpAddress blocked = new BlockedIpAddress();
        blocked.setIp(ip);
        blocked.setDuration(duration.name());
        blocked.setStartingFromDate(startDate);
        blocked.setReason("Exceeded threshold limit of" + threshold);

       return blocked;
    }
}
