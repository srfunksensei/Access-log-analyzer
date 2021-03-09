package com.ef;

import com.ef.entity.WebAccessLog;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

/**
 * @author Milan Brankovic
 */
public class WebAccessLogParserTest {

    @Test(expected = FileNotFoundException.class)
    public void parseFile_nonExistingFile() throws IOException, ParseException {
        WebAccessLogParser.parseFile("not-existing.txt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseFile_invalidFormatDataInFile_tooFewData() throws IOException, ParseException {
        String filePath = null;
        try {
            final String line =
                    "2017-01-01 00:00:11.763|192.168.234.82";
            filePath = createFileWithData(line);
        } catch (IOException ex) {
            Assert.fail("Expected to create file");
        }

        WebAccessLogParser.parseFile(filePath);
    }

    @Test(expected = ParseException.class)
    public void parseFile_invalidFormatDataInFile_invalidDateFormat() throws IOException, ParseException {
        String filePath = null;
        try {
            final String line =
                    "2017-01-01|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";
            filePath = createFileWithData(line);
        } catch (IOException ex) {
            Assert.fail("Expected to create file");
        }

        WebAccessLogParser.parseFile(filePath);
    }

    @Test(expected = NumberFormatException.class)
    public void parseFile_invalidFormatDataInFile_invalidHttpStatusCode() throws IOException, ParseException {
        String filePath = null;
        try {
            final String line =
                    "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|absc|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";
            filePath = createFileWithData(line);
        } catch (IOException ex) {
            Assert.fail("Expected to create file");
        }

        WebAccessLogParser.parseFile(filePath);
    }

    @Test
    public void parseFile_emptyFile() throws IOException, ParseException {
        String filePath = null;
        try {
            filePath = createFileWithData("");
        } catch (IOException ex) {
            Assert.fail("Expected to create file");
        }

        final List<WebAccessLog> result = WebAccessLogParser.parseFile(filePath);
        Assert.assertTrue("Expected empty result", result.isEmpty());
    }

    @Test
    public void parseFile() throws IOException, ParseException {
        String filePath = null;
        try {
            final String line =
                    "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";
            filePath = createFileWithData(line);
        } catch (IOException ex) {
            Assert.fail("Expected to create file");
        }

        final List<WebAccessLog> result = WebAccessLogParser.parseFile(filePath);
        Assert.assertFalse("Expected result", result.isEmpty());
        Assert.assertEquals("Expected different number of entries", 1, result.size());
    }

    private String createFileWithData(final String line) throws IOException {
        final Path path = Files.createTempFile("test-file", ".log");
        final File file = path.toFile();
        Files.write(path, line.getBytes(StandardCharsets.UTF_8));
        file.deleteOnExit();
        return file.getAbsolutePath();
    }
}
