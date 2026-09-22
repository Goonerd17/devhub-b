package teamdevhub.devhub.admin.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.shared.moderation.ReportPage;

@Getter
@Builder
@AllArgsConstructor
public class PageResponseDto {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public static PageResponseDto from(PageResult<?> result) {
        return PageResponseDto.builder().page(result.page()).size(result.size())
                .totalElements(result.totalElements()).totalPages(result.totalPages()).build();
    }
    public static PageResponseDto from(ReportPage result) { return builder().page(result.page()).size(result.size()).totalElements(result.totalElements()).totalPages(result.totalPages()).build(); }
}
