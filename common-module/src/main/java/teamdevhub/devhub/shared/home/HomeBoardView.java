package teamdevhub.devhub.shared.home;

import java.time.LocalDateTime;

public record HomeBoardView(String boardGuid, String title, String categoryCd, String username,
        int viewCount, long likeCount, LocalDateTime registeredDate) {
}
