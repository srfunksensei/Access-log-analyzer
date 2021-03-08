package com.ef;

import com.ef.entity.BlockedIpAddress;
import com.ef.entity.WebAccessLog;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class representing filter for blocked IPs
 * 
 * @author mb
 *
 */
public class BlockedIpFilter {
    
    private final List<WebAccessLog> records;

    public BlockedIpFilter(final List<WebAccessLog> records) {
        if (records == null) {
            throw new IllegalArgumentException("Expected list with values");
        }
        this.records = records;
    }

    public List<BlockedIpAddress> getBlockedIpAddresses(final Date startDate, final Duration duration, final long threshold) {
        if (startDate == null) {
            throw new IllegalArgumentException("Expected start date");
        }
        if (duration == null) {
            throw new IllegalArgumentException("Expected duration");
        }
        if (threshold < 0) {
            throw new IllegalArgumentException("Expected positive threshold value");
        }

        final Date endDate = getEndDate(startDate, duration);
        final List<WebAccessLog> recordsInTimeRange = filterRecordsBetween(records, startDate, endDate);
        final List<String> ipsExceedingThreshold = filterIpsExceedingThreshold(recordsInTimeRange, threshold);

        final List<BlockedIpAddress> blocked = new ArrayList<>();
        ipsExceedingThreshold.forEach(ip -> blocked.add(buildBlockedIpAddress(ip, duration, startDate, threshold)));
        return blocked;
    }

    private Date getEndDate(final Date startDate, final Duration duration) {
        int type = Duration.DAILY.equals(duration) ? Calendar.DATE : Calendar.HOUR_OF_DAY;
        
        final Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(type, 1);
        return cal.getTime();
    }

    private List<WebAccessLog> filterRecordsBetween(final List<WebAccessLog> records, final Date startDate, final Date endDate) {
        return records.stream()
                .filter(record -> record.getDate().after(startDate) && record.getDate().before(endDate))
                .collect(Collectors.toList());
    }

    private List<String> filterIpsExceedingThreshold(final List<WebAccessLog> records, final long threshold) {
        final Map<String, Long> stringLongMap = groupRecordsCountByIpAddress(records);
        return stringLongMap.entrySet().stream()
                .filter(e -> e.getValue() > threshold)
                .map(Map.Entry::getKey)
                .distinct()
                .collect(Collectors.toList());
    }

    private Map<String, Long> groupRecordsCountByIpAddress(final List<WebAccessLog> records){
        return records.stream().map(WebAccessLog::getIp)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    private BlockedIpAddress buildBlockedIpAddress(final String ip, final Duration duration, final Date startDate, final long threshold)  {
        final BlockedIpAddress blocked = new BlockedIpAddress();
        blocked.setIp(ip);
        blocked.setDuration(duration.name());
        blocked.setStartingFromDate(startDate);
        blocked.setReason("Exceeded threshold limit of " + threshold);
       return blocked;
    }
}
