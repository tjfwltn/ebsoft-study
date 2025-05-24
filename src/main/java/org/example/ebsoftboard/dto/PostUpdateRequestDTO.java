package org.example.ebsoftboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.example.ebsoftboard.constants.ValidationConstants.*;


@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequestDTO {

    @NotBlank
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH,
            message = USERNAME_SIZE_MESSAGE)
    private String username;

    @NotBlank
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH,
            message = PASSWORD_SIZE_MESSAGE)
    @Pattern(regexp = PASSWORD_REGEX,
            message = PASSWORD_PATTERN_MESSAGE)
    private String password;

    @NotBlank
    @Size(min = POST_TITLE_MIN_LENGTH, max = POST_TITLE_MAX_LENGTH,
            message = POST_TITLE_SIZE_MESSAGE)
    private String title;

    @NotBlank
    @Size(min = POST_CONTENT_MIN_LENGTH, max = POST_CONTENT_MAX_LENGTH,
            message = POST_CONTENT_SIZE_MESSAGE)
    private String content;

}
