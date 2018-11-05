package pl.rmachnik;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinVelocity;
import kotlin.text.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;
import static kotlin.text.Charsets.UTF_8;

public class Portfolio {
    static Logger LOG = LoggerFactory.getLogger(Portfolio.class);


    public static void main(String[] args) throws IOException {
        JavalinRenderer.register(JavalinVelocity.INSTANCE);
        int port = Integer.valueOf(System.getProperty("port", "80"));

        Javalin app = Javalin.create();
        app.enableStaticFiles("web");
        app.start(port);

        InputStream resourceAsStream = Portfolio.class.getResourceAsStream("/portfolio.json");
        List<Directory> directories = new Gson().fromJson(new InputStreamReader(resourceAsStream, UTF_8), new TypeToken<List<Directory>>() {}.getType());
        Collections.reverse(directories);
        Thumbs thumbsCache = new Thumbs(directories);


        LOG.info("App is ready.");

        app.get("/", ctx -> ctx.render("/web/index.vtl", model("directories", directories)));
        app.get("/thumb/", ctx -> {
            ByteArrayOutputStream image = thumbsCache.thumbs.get(ctx.queryParam("image"));
            ctx.contentType("image/jpeg");
            ctx.result(new ByteArrayInputStream(image.toByteArray()));
        });

        app.get("/pics/",ctx->{
            ByteArrayOutputStream image = thumbsCache.pics.get(ctx.queryParam("image"));
            ctx.contentType("image/jpeg");
            ctx.result(new ByteArrayInputStream(image.toByteArray()));
        });

    }
}
