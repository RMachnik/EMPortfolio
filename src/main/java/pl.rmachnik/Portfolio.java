package pl.rmachnik;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinVelocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class Portfolio {
    static Logger LOG = LoggerFactory.getLogger(Portfolio.class);

    public static void main(String[] args) throws UnsupportedEncodingException {
        JavalinRenderer.register(JavalinVelocity.INSTANCE);
        int port = Integer.valueOf(System.getProperty("port", "80"));

        Javalin app = Javalin.create();
        app.enableStaticFiles("web");
        app.start(port);

        InputStream resourceAsStream = Portfolio.class.getResourceAsStream("/portfolio.json");
        List<Directory> directories = new Gson().fromJson(new InputStreamReader(resourceAsStream, "UTF-8"), List.class);
        Collections.reverse(directories);

        app.get("/", ctx -> ctx.render("/web/index.vtl", model("directories", directories)));

    }
}
