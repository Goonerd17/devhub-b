package teamdevhub.devhub.notification.core.domain;

import lombok.Getter;

public enum NotificationType {

    APPLICANT_ARRIVED(Category.NORMAL
            , "%s 프로젝트에 새로운 지원자가 있습니다."
            , "/mypage/projects/%s"
    ),
    COMMENT_ADDED(Category.NORMAL
            , "'%s' 게시글에 새로운 댓글이 달렸습니다."
            , "/mypage/boards/%s"
    ),
    PROJECT_APPROVED(Category.POSITIVE
            , "'%s' 프로젝트 참여가 승인되었습니다! 환영합니다."
            , "/mypage/projects/%s"
    ),
    PROJECT_REJECTED(Category.NEGATIVE
            , "아쉽게도 '%s' 프로젝트 참여가 거절되었습니다."
            , null
    ),
    ACCOUNT_RESTRICTED(Category.NEGATIVE
            , "운영 정책 위반으로 인해 계정이 이용 제한되었습니다."
            , null),
    DEADLINE_APPROACHING_D1(Category.NORMAL
            , "관심 프로젝트 '%s'의 모집 마감이 1일 남았습니다."
            , "/mypage/관심프로젝트/%s"
    ),
    DEADLINE_APPROACHING_D3(Category.NORMAL
            , "아직 지원 안 하셨나요? 관심 프로젝트 '%s' 마감 3일 전입니다."
            , "/projects/%s"
    ),
    PROJECT_FINISHED(Category.POSITIVE
            , "'%s' 프로젝트가 종료되었습니다. 팀원들을 평가해주세요!"
            , "/projects/%s/reviews"
    );

    private final Category category;
    private final String messageTemplate;
    private final String linkTemplate;

    NotificationType(Category category, String messageTemplate, String linkTemplate) {
        this.category = category;
        this.messageTemplate = messageTemplate;
        this.linkTemplate = linkTemplate;
    }

    public String generateMessage(Object... args) {
        return String.format(messageTemplate, args);
    }

    public String generateLink(Object... args) {
        if (linkTemplate == null) return null;
        return String.format(linkTemplate, args);
    }

    public String getCategoryCode() {
        return category.getCategoryCode();
    }

    @Getter
    public enum Category {
        NORMAL("001"), POSITIVE("002"), NEGATIVE("003");

        private final String categoryCode;

        Category(String categoryCode) {
            this.categoryCode = categoryCode;
        }

    }
}
