package pl.rmachnik;

import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Util {
    public static void main(String[] args) throws IOException {
        Util util = new Util();
        List<String> resourceFiles = util.getResourceFiles("/web/portfolio");
        List<Directory> dirs = new ArrayList<>();
        for (String directory : resourceFiles) {
            List<String> images = util.getResourceFiles("/web/portfolio/" + directory);
            Directory dir = new Directory(directory, images.stream().map(img -> new Image(img, "/portfolio/" + directory + "/" + img)).collect(toList()));
            dirs.add(dir);
        }
        Gson gson =new Gson();
        String dirsJson = gson.toJson(dirs);
        FileWriter fileWriter = new FileWriter("portfolio.json");
        fileWriter.write(dirsJson);
        fileWriter.close();
    }

    private List<String> getResourceFiles(String path) throws IOException {
        List<String> filenames = new ArrayList<>();

        try (
        InputStream in = getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        }

        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
