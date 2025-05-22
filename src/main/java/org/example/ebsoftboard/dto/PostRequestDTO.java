package org.example.ebsoftboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDTO {
    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:;\"'<>?,./\\\\]).{4,}$";

    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(min = 3, max = 4,
            message = "사용자 이름은 3글자 이상, 5글자 미만이어야 합니다.")
    private String username;

    @NotBlank
    @Size(min = 4, max = 15,
            message = "비밀번호는 4글자 이상, 16글자 미만이어야 합니다.")
    @Pattern(regexp = PASSWORD_REGEX,
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank
    @Size(min = 4, max = 99,
            message = "제목은 4글자 이상, 100글자 미만이어야 합니다.")
    private String title;
    @NotBlank
    @Size(min = 4, max = 1999,
            message = "내용은 4글자 이상, 2000글자 미만이어야 합니다.")
    private String content;

    private List<MultipartFile> files;

}
