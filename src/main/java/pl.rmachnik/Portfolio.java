package pl.rmachnik;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
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

        Javalin app = Javalin.create(config -> config.server(Portfolio::createHttp2Server));
        app.config.addStaticFiles("web");
        app.config.autogenerateEtags = true;
        app.start(port);

        InputStream resourceAsStream = Portfolio.class.getResourceAsStream("/portfolio.json");
        List<Directory> directories = new Gson().fromJson(new InputStreamReader(resourceAsStream, UTF_8), new TypeToken<List<Directory>>() {
        }.getType());
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

        app.get("/pics/", ctx -> {
            ByteArrayOutputStream image = thumbsCache.getImage(ctx.queryParam("image"));
            ctx.contentType("image/jpeg");
            ctx.result(new ByteArrayInputStream(image.toByteArray()));
        });
    }

    private static Server createHttp2Server() {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        // HTTP Configuration
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSecureScheme("https");
        httpConfig.setSecurePort(8443);

        // SSL Context Factory for HTTPS and HTTP/2
        SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStorePath(Portfolio.class.getResource("/keystore.jks").toExternalForm()); // replace with your real keystore
        sslContextFactory.setKeyStorePassword("password"); // replace with your real password
        sslContextFactory.setCipherComparator(HTTP2Cipher.COMPARATOR);
        sslContextFactory.setProvider("Conscrypt");

        // HTTPS Configuration
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());

        // HTTP/2 Connection Factory
        HTTP2ServerConnectionFactory h2 = new HTTP2ServerConnectionFactory(httpsConfig);
        ALPNServerConnectionFactory alpn = new ALPNServerConnectionFactory();
        alpn.setDefaultProtocol("h2");

        // SSL Connection Factory
        SslConnectionFactory ssl = new SslConnectionFactory(sslContextFactory, alpn.getProtocol());

        // HTTP/2 Connector
        ServerConnector http2Connector = new ServerConnector(server, ssl, alpn, h2, new HttpConnectionFactory(httpsConfig));
        http2Connector.setPort(8443);
        server.addConnector(http2Connector);

        return server;
    }
}
