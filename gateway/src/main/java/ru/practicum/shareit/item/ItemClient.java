package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentIn;
import ru.practicum.shareit.item.dto.ItemIn;
import ru.practicum.shareit.item.dto.ItemUpd;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItem(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItems(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> postItem(ItemIn item, long userId) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> patchItem(long itemId, long userId, ItemUpd item) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> deleteItem(long itemId, Long userId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> addComment(long itemId, long userId, CommentIn comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }

}
