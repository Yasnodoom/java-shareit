package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public ResponseEntity<Object> create(long userId, ItemDto item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> get(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAll(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> update(long userId, long itemId, ItemDto item) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> find(long userId, String text) {
        return get("/search", userId, Map.of("text", text));
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, String comment) {
        return post("/" + itemId + "/comment", userId, Map.of("text", comment));
    }

}
