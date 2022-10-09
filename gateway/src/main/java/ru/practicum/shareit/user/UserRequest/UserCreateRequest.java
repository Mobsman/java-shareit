package ru.practicum.shareit.user.UserRequest;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    @NotNull
    @NotEmpty
    private String name;

    @Email
    @NotNull
    private String email;
}
