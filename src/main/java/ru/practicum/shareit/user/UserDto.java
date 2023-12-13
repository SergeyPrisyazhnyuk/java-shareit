package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDto {

    private Long id;

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    @Email
    private String email;

    public UserDto(Long id) {
        this.id = id;
    }
}
