package pl.rmachnik.domain;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Directory implements Comparable<Directory> {

    public final String name;
    public final List<Image> images;

    public Directory(String name, List<Image> images) {
        this.name = name.split("_")[1];
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public List<Image> getImages() {
        return images;
    }

    @Override
    public int compareTo(@NotNull Directory o) {
        return name.compareTo(o.name);
    }
}
