package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.requests.requst.ItemRequest;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public Object createItemRequest(long userId, ItemRequest itemRequest) {
        return post("", userId, itemRequest);
    }

    public Object getItemRequestById(long itemRequestId, long userId) {
        return get("/" + itemRequestId, userId);
    }

    public Object getAllItemRequestsOfUser(long userId) {
        return get("", userId);
    }

    public Object getAllItemRequests(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(UriComponentsBuilder.fromPath("/all")
                .queryParam("from", "{from}")
                .queryParam("size", "{size}")
                .encode()
                .toUriString(), userId, parameters);
    }

}
