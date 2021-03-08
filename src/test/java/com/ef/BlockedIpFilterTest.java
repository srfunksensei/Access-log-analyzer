package com.ef;

import com.ef.entity.BlockedIpAddress;
import com.ef.entity.WebAccessLog;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

/**
 * @author Milan Brankovic
 */
public class BlockedIpFilterTest {

    @Test(expected = IllegalArgumentException.class)
    public void getBlockedIpAddresses_recordsNull() {
        new BlockedIpFilter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBlockedIpAddresses_startDateNull() {
        final BlockedIpFilter blockedIpFilter = new BlockedIpFilter(new ArrayList<>());
        blockedIpFilter.getBlockedIpAddresses(null, Duration.DAILY, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBlockedIpAddresses_durationNull() {
        final BlockedIpFilter blockedIpFilter = new BlockedIpFilter(new ArrayList<>());
        blockedIpFilter.getBlockedIpAddresses(new Date(), null, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBlockedIpAddresses_thresholdNegative() {
        final BlockedIpFilter blockedIpFilter = new BlockedIpFilter(new ArrayList<>());
        blockedIpFilter.getBlockedIpAddresses(new Date(), Duration.DAILY, -1000);
    }

    @Test
    public void getBlockedIpAddresses_recordsEmpty() {
        final BlockedIpFilter blockedIpFilter = new BlockedIpFilter(new ArrayList<>());
        final List<BlockedIpAddress> result = blockedIpFilter.getBlockedIpAddresses(new Date(), Duration.DAILY, 1000);
        Assert.assertTrue("Expected empty result", result.isEmpty());
    }

    @Test
    public void getBlockedIpAddresses_recordsOutOfRange() {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -10);

        final WebAccessLog wal = buildWebAccessLog("127.0.0.1", cal.getTime());
        final List<WebAccessLog> records = Collections.singletonList(wal);

        final BlockedIpFilter blockedIpFilter = new BlockedIpFilter(records);
        final List<BlockedIpAddress> result = blockedIpFilter.getBlockedIpAddresses(new Date(), Duration.DAILY, 1000);
        Assert.assertTrue("Expected empty result", result.isEmpty());
    }

    @Test
    public void getBlockedIpAddresses() {
        final Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -10);

        final WebAccessLog wal1 = buildWebAccessLog("127.0.0.1", cal.getTime()),
                wal2 = buildWebAccessLog("127.0.0.1", cal.getTime()),
                wal3 = buildWebAccessLog("192.168.0.1", cal.getTime());
        final List<WebAccessLog> records = Arrays.asList(wal1, wal2, wal3);

        final BlockedIpFilter blockedIpFilter = new BlockedIpFilter(records);
        cal.add(Calendar.DATE, -1);
        cal.add(Calendar.HOUR_OF_DAY, 1);

        final List<BlockedIpAddress> result = blockedIpFilter.getBlockedIpAddresses(cal.getTime(), Duration.DAILY, 1);
        Assert.assertFalse("Expected result with elements", result.isEmpty());
        Assert.assertEquals("Expected different number of elements", 1, result.size());
    }

    private WebAccessLog buildWebAccessLog(final String ip, final Date startDate) {
        final WebAccessLog wal = new WebAccessLog();
        wal.setIp(ip);
        wal.setDate(startDate);
        wal.setMethod("GET");
        wal.setHttpStatusCode(400);
        wal.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:86.0) Gecko/20100101 Firefox/86.0");
        return wal;
    }
}
