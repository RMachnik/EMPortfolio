package pl.rmachnik;

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinVelocity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class Portfolio {
    public static void main(String[] args) throws UnsupportedEncodingException {
        JavalinRenderer.register(JavalinVelocity.INSTANCE);

        Javalin app = Javalin.create();
        app.enableStaticFiles("web");

        int port = Integer.valueOf(System.getProperty("port", "80"));
        app.start(port);

        InputStream resourceAsStream = Portfolio.class.getResourceAsStream("/portfolio.json");
        List<Directory> directories = new Gson().fromJson(new InputStreamReader(resourceAsStream, "UTF-8"), List.class);
        Collections.reverse(directories);

        app.get("/", ctx -> ctx.render("/web/index.vtl", model("directories", directories)));

    }
}
