package org.example.ebsoftboard.constants;

public final class ValidationConstants {

    private ValidationConstants() {
        throw new AssertionError("constant class");
    }

    /**
     * 두 개의 PostRequestDTO 클래스 때문에 validate 상수들을 관리하는 것인데, 유지보수는 쉽겠지만
     * 각각의 DTO 클래스 안에서 알아보기 힘들지 않을까? 변수명을 어떻게 바꿀지
     */

    public static final String PASSWORD_REGEX =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}:;\"'<>?,./\\\\]).{4,}$";

    public static final int USERNAME_MIN_LENGTH = 3;
    public static final int USERNAME_MAX_LENGTH = 4;
    public static final String USERNAME_SIZE_MESSAGE = "사용자 이름은 3글자 이상, 5글자 미만이어야 합니다.";

    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 15;
    public static final String PASSWORD_SIZE_MESSAGE = "비밀번호는 4글자 이상, 16글자 미만이어야 합니다.";
    public static final String PASSWORD_PATTERN_MESSAGE = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.";

    public static final int POST_TITLE_MIN_LENGTH = 4;
    public static final int POST_TITLE_MAX_LENGTH = 99;
    public static final String POST_TITLE_SIZE_MESSAGE = "제목은 4글자 이상, 100글자 미만이어야 합니다.";

    public static final int POST_CONTENT_MIN_LENGTH = 4;
    public static final int POST_CONTENT_MAX_LENGTH = 1999;
    public static final String POST_CONTENT_SIZE_MESSAGE = "내용은 4글자 이상, 2000글자 미만이어야 합니다.";

    public static final int COMMENT_CONTENT_MIN_LENGTH = 1;
    public static final int COMMENT_CONTENT_MAX_LENGTH = 1999;
    public static final String COMMENT_CONTENT_SIZE_MESSAGE = "댓글은 1글자 이상, 2000글자 미만이어야 합니다.";
}
