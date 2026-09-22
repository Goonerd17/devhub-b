package teamdevhub.devhub.admin.http.terms.port.in.facade.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import teamdevhub.devhub.admin.core.terms.domain.Terms;

@Getter
@AllArgsConstructor
public class TermsResponseDto {

    private final String termsGuid;
    private final String title;
    private final String content;
    private final boolean isRequired;
    private final boolean isUsed;
    private final boolean isDeleted;

    public static TermsResponseDto fromDomain(Terms terms) {
        return new TermsResponseDto(
                terms.getTermsGuid(),
                terms.getTitle(),
                terms.getContent(),
                terms.isRequired(),
                terms.isUsed(),
                terms.isDeleted()
        );
    }
}

