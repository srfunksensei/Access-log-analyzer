package com.ef;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.ef.entity.WebAccessLog;

/**
 * File parser which reads file and builds entities
 * 
 * @author mb
 *
 */
public class WebAccessLogParser {
    
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    public static List<WebAccessLog> parseFile(final String path) throws FileNotFoundException, IOException, ParseException {
        final List<WebAccessLog> records = new ArrayList<>();
        
        try (final FileInputStream inputStream = new FileInputStream(path);
                final Scanner sc = new Scanner(inputStream, "UTF-8")) {

            while (sc.hasNextLine()) {
                final String line = sc.nextLine();
                records.add(WebAccessLogParser.buildWebAccessLogEntity(line));
            }

            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }
        
        return records;
    }

    private static WebAccessLog buildWebAccessLogEntity(final String line) throws ParseException {
        final String[] props = line.split("\\|");

        final WebAccessLog wal = new WebAccessLog();
        wal.setDate(format.parse(props[0]));
        wal.setIp(props[1]);
        wal.setMethod(props[2]);
        wal.setHttpStatusCode(Integer.parseInt(props[3]));
        wal.setUserAgent(props[4]);

        return wal;
    }
}
