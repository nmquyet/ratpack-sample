package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.http.HttpMethod;
import ratpack.http.client.HttpClient;
import ratpack.spring.config.EnableRatpack;

import java.net.URI;

@SpringBootApplication
@EnableRatpack
public class Main {

    @Bean
    Action<Chain> actionChain() {
        return chain -> chain
            .all(ctx -> {
                ctx.getResponse().beforeSend(response -> {
                    System.out.println("Before send");
                });
                ctx.next();
            })

            .all(ctx -> {
                ctx.get(HttpClient.class).requestStream(
                    new URI("http://google.com"),
                    requestSpec -> {
                        requestSpec.method(HttpMethod.GET);
                    }
                ).then(streamedResponse -> {
                    streamedResponse.forwardTo(ctx.getResponse());
                });
            })
            ;
    }
    public static void main(String... args) throws InterruptedException {
        SpringApplication.run(Main.class, args);
    }
}
