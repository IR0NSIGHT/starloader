import java.io.InputStream;

public class NamedInputStream {
    InputStream stream;
    String name;

    public NamedInputStream(InputStream stream, String name) {
        this.stream = stream;
        this.name = name;
    }

    public InputStream getStream() {
        return stream;
    }

    public String getName() {
        return name;
    }
}
