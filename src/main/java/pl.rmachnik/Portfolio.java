package pl.rmachnik;

import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinVelocity;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.javalin.rendering.template.TemplateUtil.model;
import static java.util.Arrays.asList;

public class Portfolio {
    public static void main(String[] args) {
        JavalinRenderer.register(JavalinVelocity.INSTANCE);

        Javalin app = Javalin.create();
        app.enableStaticFiles("web");
        app.get("/", ctx -> ctx.result("Hello World"));

        app.start(7000);

        File folder = new File(Thread.currentThread().getContextClassLoader().getResource("web/images").getPath());
        List<File> sortedFiles = Arrays.asList(folder.listFiles());
        sortedFiles.sort(File::compareTo);
        List<Directory> directories = new ArrayList<>();
        for (File file : sortedFiles) {
            if (file.isDirectory()) {
                List<Image> folderContent = asList(file.listFiles())
                .stream()
                .map(f -> new Image(f.getName().split("\\.")[0], file.getName() + "/" + f.getName()))
                .collect(Collectors.toList());
                folderContent.sort(Comparator.comparing(a -> a.name));
                Directory directory = new Directory(file.getName(), folderContent);
                directories.add(directory);
            }
        }
        Collections.reverse(directories);
        app.get("/template", ctx -> ctx.render("template.vtl", model("directories", directories)));
    }

}
