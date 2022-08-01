package ru.practicum.shareit.user;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class User {

    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @Email
    @NotNull
    private String email;

}
