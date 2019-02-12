package pl.rmachnik.domain;

public class Image {
    public String name;
    public String path;

    public Image(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
