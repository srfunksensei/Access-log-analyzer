package com.ef;

import com.ef.entity.HibernateUtil;
import com.ef.entity.WebAccessLog;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Parser {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss", Locale.US);

    public static void main(String[] args) throws ParseException {
        if (args.length > 0) {
            Date startDate = null;
            Duration duration = null;
            long threshold = -1;
            String filePath = "access.log";

            for (final String s : args) {
                final String[] params = s.split("=");

                switch (params[0]) {

                    case "--startDate":
                        startDate = DATE_FORMAT.parse(params[1]);
                        break;

                    case "--duration":
                        final Duration durationParsed = Duration.findByLabel(params[1].toUpperCase());
                        if (durationParsed == null) {
                            throw new IllegalArgumentException("Argument" + params[0] + " must be an hourly or daily");
                        }
                        duration = durationParsed;
                        break;

                    case "--threshold":
                        if (Long.parseLong(params[1]) <= 0) {
                            throw new IllegalArgumentException("Argument" + params[0] + " must be positive number");
                        }
                        threshold = Long.parseLong(params[1]);
                        break;

                    case "--accesslog":
                        filePath = params[1];
                        break;
                }
            }

            if (startDate == null || duration == null) {
                throw new IllegalArgumentException("Not all required params passed");
            }

            if (threshold <= 0) {
                if (Duration.HOURLY.equals(duration)) {
                    threshold = 200;
                } else if (Duration.DAILY.equals(duration)) {
                    threshold = 500;
                }
            }

            final Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                final List<WebAccessLog> records = WebAccessLogParser.parseFile(filePath);
                records.forEach(session::saveOrUpdate);

                final BlockedIpFilter filter = new BlockedIpFilter(records);
                filter.getBlockedIpAddresses(startDate, duration, threshold).forEach(e -> {
                    System.out.println(e.getIp());
                    session.saveOrUpdate(e);
                });

                transaction.commit();
            } catch (Exception ex) {
                if (transaction != null) {
                    transaction.rollback();
                }
                ex.printStackTrace();
            } finally {
                session.close();
            }
        }
    }
}
