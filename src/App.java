import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) throws InterruptedException {
        Path logDir = Paths.get("logs");

        List<Path> files;
        try (Stream<Path> paths = Files.list(logDir)) {
            files = paths.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Erreur accès dossier logs : " + e.getMessage());
            return;
        }

        System.out.println("Traitement avec ExecutorService...");

        List<LogEntry> allEntries = new CopyOnWriteArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );

        for (Path file : files) {
            executor.submit(new LogFileThread(file, allEntries));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("->Traitement terminé !");
        System.out.println("Nombre total d’entrées : " + allEntries.size());

        afficherStatistiques(allEntries);
    }

    private static void afficherStatistiques(List<LogEntry> entries) {

        Map<String, Long> logsParNiveau = entries.stream()
                .collect(Collectors.groupingBy(LogEntry::getLevel, Collectors.counting()));

        Map<String, Long> logsParLogger = entries.stream()
                .collect(Collectors.groupingBy(LogEntry::getLogger, Collectors.counting()));

        Optional<Map.Entry<String, Long>> loggerLePlusActif = logsParLogger.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        System.out.println("Niveau ERROR : " + logsParNiveau.getOrDefault("ERROR", 0L));
        System.out.println("Niveau INFO  : " + logsParNiveau.getOrDefault("INFO", 0L));
        System.out.println("Niveau WARN  : " + logsParNiveau.getOrDefault("WARN", 0L));

        loggerLePlusActif.ifPresent(entry ->
            System.out.println("Logger le plus actif : " + entry.getKey() + " avec " + entry.getValue() + " logs")
        );

        long nombreError = logsParNiveau.getOrDefault("ERROR", 0L);
        System.out.println("Nombre de logs ERROR : " + nombreError);
    }
}
