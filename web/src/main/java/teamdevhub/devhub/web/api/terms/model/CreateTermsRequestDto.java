package teamdevhub.devhub.web.api.terms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.administration.core.terms.port.in.command.CreateTermsCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTermsRequestDto {

    private String title;
    private String content;
    private boolean isRequired;
    private boolean isUsed;
    private boolean isDeleted;

    public CreateTermsCommand toCreateTermsCommand() {
        return CreateTermsCommand.builder()
                .title(this.title)
                .content(this.content)
                .isRequired(this.isRequired)
                .isUsed(this.isUsed)
                .isDeleted(this.isDeleted)
                .build();
    }
}
