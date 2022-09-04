package ru.practicum.shareit.user.UserRequest;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    private Long id;

    private String name;

    private String email;

}