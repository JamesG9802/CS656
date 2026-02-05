import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Time {
    private static final DateTimeFormatter FORMAT =
        DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private Time() {}

    public static String now() {
        return LocalDateTime.now().format(FORMAT);
    }
}