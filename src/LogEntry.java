import java.time.LocalDateTime;

public class LogEntry {
    private final LocalDateTime dateTime;
    private final String level;
    private final String logger;
    private final String message;

    public LogEntry(LocalDateTime dateTime, String level, String logger, String message) {
        this.dateTime = dateTime;
        this.level = level;
        this.logger = logger;
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public String getLevel() {
        return level;
    }
    public String getLogger() {
        return logger;
        }
    public String getMessage() {
        return message; 
    }
}
