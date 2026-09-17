package teamdevhub.devhub.notification.outbound.adapter;

import lombok.Getter;

@Getter
public enum EmailTemplateType {

    EMAIL_VERIFICATION(
            "[회원가입] 이메일 인증 코드",
            "email/verification",
            "5분"
    );

    private final String subject;
    private final String path;
    private final String expireTime;

    EmailTemplateType(String subject, String path, String expireTime) {
        this.subject = subject;
        this.path = path;
        this.expireTime = expireTime;
    }
}
