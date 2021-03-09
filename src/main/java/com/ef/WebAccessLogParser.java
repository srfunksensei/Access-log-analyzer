package com.ef;

import com.ef.entity.WebAccessLog;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * File parser which reads file and builds entities
 * 
 * @author mb
 *
 */
public class WebAccessLogParser {
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    public static List<WebAccessLog> parseFile(final String path) throws IOException, ParseException {
        final List<WebAccessLog> records = new ArrayList<>();
        
        try (final FileInputStream inputStream = new FileInputStream(path);
                final Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8)) {

            while (sc.hasNextLine()) {
                final String line = sc.nextLine();
                records.add(WebAccessLogParser.parseToWebAccessLogEntity(line));
            }

            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        }
        
        return records;
    }

    private static WebAccessLog parseToWebAccessLogEntity(final String line) throws ParseException {
        final String[] props = line.split("\\|");
        if (props.length < 5) {
            throw new IllegalArgumentException("Line does not contain enough params");
        }

        final WebAccessLog wal = new WebAccessLog();
        wal.setDate(DATE_FORMAT.parse(props[0]));
        wal.setIp(props[1]);
        wal.setMethod(props[2]);
        wal.setHttpStatusCode(Integer.parseInt(props[3]));
        wal.setUserAgent(props[4]);
        return wal;
    }
}
