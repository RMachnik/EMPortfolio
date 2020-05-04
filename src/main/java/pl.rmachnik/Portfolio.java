package pl.rmachnik;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.rmachnik.domain.Directory;
import pl.rmachnik.domain.Thumbs;

import java.io.*;
import java.util.Collections;
import java.util.List;

import static io.javalin.plugin.rendering.template.JavalinVelocity.INSTANCE;
import static io.javalin.plugin.rendering.template.TemplateUtil.model;
import static kotlin.text.Charsets.UTF_8;

public class Portfolio {
    private static Logger LOG = LoggerFactory.getLogger(Portfolio.class);


    public static void main(String[] args) throws IOException {
        JavalinRenderer.register(INSTANCE);
        int port = Integer.parseInt(System.getProperty("port", "0"));

        Javalin app = Javalin.create();
        app.config.addStaticFiles("web");
        app.config.autogenerateEtags = true;
        app.start(port);

        InputStream resourceAsStream = Portfolio.class.getResourceAsStream("/portfolio.json");
        List<Directory> directories = new Gson().fromJson(new InputStreamReader(resourceAsStream, UTF_8), new TypeToken<List<Directory>>() {}.getType());
        Collections.reverse(directories);

        LOG.info("Loading cache.");
        Thumbs thumbsCache = new Thumbs(directories);
        LOG.info("Cache ready, app is ready.");

        app.get("/", ctx -> ctx.render("/web/index.vtl", model("directories", directories)));
        app.get("/thumb/", ctx -> {
            ByteArrayOutputStream image = thumbsCache.getThumb(ctx.queryParam("image"));
            ctx.contentType("image/jpeg");
            ctx.result(new ByteArrayInputStream(image.toByteArray()));
        });

        app.get("/pics/",ctx->{
            ByteArrayOutputStream image = thumbsCache.getImage(ctx.queryParam("image"));
            ctx.contentType("image/jpeg");
            ctx.result(new ByteArrayInputStream(image.toByteArray()));
        });
    }
}
