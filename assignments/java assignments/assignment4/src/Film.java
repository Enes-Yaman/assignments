import java.nio.file.Path;

public class Film {
    String Name;
    Path TrailerPath;
    int Duration;

    Film(String Name, Path TrailerPath, int Duration) {
        this.Duration = Duration;
        this.Name = Name;
        this.TrailerPath = TrailerPath;
    }
}
