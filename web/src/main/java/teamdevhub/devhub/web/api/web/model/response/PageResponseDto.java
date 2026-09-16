package teamdevhub.devhub.web.api.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.community.api.ReportPage;
import teamdevhub.devhub.administration.api.ApplicationFormPage;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponseDto {

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static PageResponseDto from(PageResult<?> pageResult) {
        return PageResponseDto.builder()
                .page(pageResult.page())
                .size(pageResult.size())
                .totalElements(pageResult.totalElements())
                .totalPages(pageResult.totalPages())
                .first(pageResult.first())
                .last(pageResult.last())
                .build();
    }

    public static PageResponseDto from(ApplicationFormPage pageResult) {
        return PageResponseDto.builder()
                .page(pageResult.page())
                .size(pageResult.size())
                .totalElements(pageResult.totalElements())
                .totalPages(pageResult.totalPages())
                .first(pageResult.first())
                .last(pageResult.last())
                .build();
    }
    public static PageResponseDto from(ReportPage p) {
        return PageResponseDto.builder().page(p.page()).size(p.size()).totalElements(p.totalElements()).totalPages(p.totalPages()).first(p.first()).last(p.last()).build();
    }
}
