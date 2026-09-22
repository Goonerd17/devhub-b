package teamdevhub.devhub.auth.http.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.terms.TermsAgreementItem;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgreeTermsRequestDto {

    @NotBlank
    private String termsGuid;

    @NotNull
    private boolean agreed;

    public TermsAgreementItem toTermsAgreementItem() {
        return new TermsAgreementItem(
                this.termsGuid,
                this.agreed
        );
    }
}
