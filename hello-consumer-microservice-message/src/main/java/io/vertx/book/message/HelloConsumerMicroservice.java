package io.vertx.book.message;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

    @Override
    public void start() {
        EventBus bus = vertx.eventBus();
        Single<JsonObject> obs1 = bus.<JsonObject>rxSend("hello", "Luke").map(Message::body);
        Single<JsonObject> obs2 = bus.<JsonObject>rxSend("hello", "Leia").map(Message::body);

        Single.zip(obs1, obs2,
                (luke, leia) -> new JsonObject()
                .put("Luke", luke.getString("message"))
                .put("Leia", leia.getString("message")))
                .subscribe(x -> System.out.println(x.encode()), Throwable::printStackTrace);
    }

}
