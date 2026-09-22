package teamdevhub.devhub.shared.member;

import java.util.List;

public record AdminMemberPage(List<AdminMemberView> content, int page, int size, long totalElements) {
}
