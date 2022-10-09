package ru.practicum.shareit.requests.requst;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {

    private Long id;
    private String description;
    private User requester;
    private LocalDateTime created;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ItemRequest> items;

}
