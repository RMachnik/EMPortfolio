package pl.rmachnik;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.util.stream.Collectors.toMap;

public class Thumbs {

    static Logger LOG = LoggerFactory.getLogger(Thumbs.class);

    final Map<String, ByteArrayOutputStream> thumbs = new ConcurrentHashMap<>();
    final Map<String, ByteArrayOutputStream> pics = new ConcurrentHashMap<>();

    public Thumbs(Collection<Directory> portfolioDirs) throws IOException {

        Map<String, InputStream> imageToStream = portfolioDirs.stream()
        .map(Directory::getImages)
        .flatMap(Collection::stream)
        .collect(toMap(Image::getPath, img -> Thumbs.class.getResourceAsStream("/web" + img.path)));

        populateCache(imageToStream);
        LOG.info("Thumbs are ready.");
    }

    private void populateCache(Map<String, InputStream> imageToStream) throws IOException {
        for (Map.Entry<String, InputStream> entry : imageToStream.entrySet()) {
            BufferedImage loadedImage = ImageIO.read(entry.getValue());
            if (loadedImage.getHeight() > loadedImage.getWidth()) {
                thumbs.put(entry.getKey(), ImageResizer.scaleImage(loadedImage, TYPE_INT_RGB, 400, 600));
                pics.put(entry.getKey(), ImageResizer.scaleImage(loadedImage, TYPE_INT_RGB, 768, 1366));
            } else {
                thumbs.put(entry.getKey(), ImageResizer.scaleImage(loadedImage, TYPE_INT_RGB, 600, 400));
                pics.put(entry.getKey(), ImageResizer.scaleImage(loadedImage, TYPE_INT_RGB, 1366, 768));
            }
        }
    }
}
