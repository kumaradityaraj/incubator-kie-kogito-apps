import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;

public class GraphQLUIRouteFilter {

    @RouteFilter(10)
    void handleGraphQLUI(RoutingContext rc) {
        // Serve UI only for GET /graphql
        if ("/graphql".equals(rc.normalizedPath()) && rc.request().method().name().equals("GET")) {
            try (InputStream htmlStream = getClass().getClassLoader().getResourceAsStream("META-INF/resources/ui/index.html")) {
                if (htmlStream != null) {
                    String html = new String(htmlStream.readAllBytes(), StandardCharsets.UTF_8);
                    rc.response()
                            .putHeader("Content-Type", "text/html; charset=utf-8")
                            .end(html);
                } else {
                    rc.response().setStatusCode(404).end("Custom UI not found");
                }
            } catch (Exception e) {
                rc.response().setStatusCode(500).end("Error: " + e.getMessage());
            }
            return;
        }
        rc.next();
    }
}
