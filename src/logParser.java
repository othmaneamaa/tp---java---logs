

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class logParser {

    private static final DateTimeFormatter dtf =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static LogEntry parseLogLine(String line) {

        try {

            if (line == null || line.length() < 50) {
                return null;
            }


            String timestampStr = line.substring(0, 23);
            LocalDateTime dateTime = LocalDateTime.parse(timestampStr, dtf);



            int start = line.indexOf('[', 24);
            int end   = line.indexOf(']', start);

            if (start == -1 || end == -1) {
                return null;
            }

            String level = line.substring(start + 1, end);



            int loggerEnd = line.indexOf(" - ", end);
            if (loggerEnd == -1) {
                return null;
            }


            String logger = line.substring(end + 2, loggerEnd).trim();



            String message = line.substring(loggerEnd + 3).trim();



            return new LogEntry(dateTime, level, logger, message);
        }
        catch (DateTimeParseException e) {

            return null;
        }
        catch (Exception e) {

            return null;
        }
    }
}
