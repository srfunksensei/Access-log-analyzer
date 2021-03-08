package com.ef;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ef.entity.HibernateUtil;
import com.ef.entity.WebAccessLog;

public class Parser {

    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss", Locale.US);

    public static void main(String[] args) {
        if (args.length > 0) {
            Date startDate = null;
            Duration duration = null;
            long threshold = -1;
            String filePath = "access.log";

            for (final String s : args) {
                final String[] params = s.split("=");

                switch (params[0]) {

                case "--startDate":
                    try {
                        startDate = format.parse(params[1]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case "--duration":
                    if (Duration.findByLabel(params[1].toUpperCase()) == null) {
                        throw new IllegalArgumentException("Argument" + params[0] + " must be an hourly or daily");
                    }
                    duration = Duration.findByLabel(params[1].toUpperCase());
                    break;

                case "--threshold":
                    if (Integer.valueOf(params[1]) <= 0) {
                        throw new IllegalArgumentException("Argument" + params[0] + " must be an integer");
                    }
                    threshold = Long.valueOf(params[1]);
                    break;

                case "--accesslog":
                    filePath = params[1];
                    break;
                }
            }

            if (startDate == null || duration == null) {
                throw new IllegalArgumentException("Not all required params passed");
            }
            
            if (duration.equals(Duration.HOURLY)) {
                threshold = 200;
            } else if (duration.equals(Duration.DAILY)) {
                threshold = 500;
            } 

            final Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();

                final List<WebAccessLog> records = WebAccessLogParser.parseFile(filePath);
                records.forEach(e -> session.saveOrUpdate(e));

                final BlockedIpFilter filter = new BlockedIpFilter(records);
                filter.getBlockedIpAddresses(startDate, duration, threshold).forEach(e -> {
                    System.out.println(e.getIp());
                    session.saveOrUpdate(e);
                });

                transaction.commit();
            } catch (Exception ex) {
                transaction.rollback();
                ex.printStackTrace();
            } finally {
                session.close();
            }
        }
    }
}
