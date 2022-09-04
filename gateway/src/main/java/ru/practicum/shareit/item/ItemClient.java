package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.itemRequest.CommentRequest;
import ru.practicum.shareit.item.itemRequest.ItemCreateRequest;
import ru.practicum.shareit.item.itemRequest.ItemUpdateRequest;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object createItem(ItemCreateRequest request, Long userId) {
        return post("", userId, request);
    }

    public Object updateItem(ItemUpdateRequest request, Long itemId, Long userId) {
        return patch("/" + itemId, userId, request);
    }

    public Object getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public Object getAllItems(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder.fromPath("")
                .queryParam("from", "{from}")
                .queryParam("size", "{size}")
                .encode()
                .toUriString(), userId, parameters);
    }

    public Object search(long userId, String search, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", search,
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder.fromPath("/search")
                .queryParam("text", "{text}")
                .queryParam("from", "{from}")
                .queryParam("size", "{size}")
                .encode()
                .toUriString(), userId, parameters);
    }

    public Object addComment(long userId, CommentRequest commentRequest, long itemId) {
        return post("/" + itemId + "/comment", userId, commentRequest);
    }

}
