import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LogFileThread extends Thread {
    private final Path file;
    private final List<LogEntry> globalLogList;

    public LogFileThread(Path file, List<LogEntry> globalLogList) {
        this.file = file;
        this.globalLogList = globalLogList;
    }

    @Override
public void run() {
    try (Stream<String> lines = Files.lines(file)) {

        List<LogEntry> localEntries = lines
                .map(logParser::parseLogLine)
                .filter(e -> e != null)
                .toList();


        globalLogList.addAll(localEntries);

    } catch (IOException e) {
        System.err.println("Erreur de lecture du fichier : " + file + " → " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Erreur pendant le parsing du fichier : " + file + " → " + e.getMessage());
        }
    }
}

