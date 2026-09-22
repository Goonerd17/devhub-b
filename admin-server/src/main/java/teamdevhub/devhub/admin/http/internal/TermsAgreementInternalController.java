package teamdevhub.devhub.admin.http.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.api.terms.TermsAgreementItem;
import teamdevhub.devhub.admin.api.terms.AgreeTermsCommand;
import teamdevhub.devhub.admin.core.terms.port.in.usecase.TermsUseCase;

@RestController
@RequestMapping("/internal/terms/agreements")
@RequiredArgsConstructor
public class TermsAgreementInternalController {
    private final TermsUseCase termsUseCase;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody teamdevhub.devhub.shared.terms.AgreeTermsCommand command) {
        termsUseCase.saveTermsAgreement(new AgreeTermsCommand(command.userGuid(),
                command.termsAgreementItemList().stream()
                        .map(item -> new TermsAgreementItem(item.termsGuid(), item.agreed())).toList()));
        return ResponseEntity.noContent().build();
    }
}
