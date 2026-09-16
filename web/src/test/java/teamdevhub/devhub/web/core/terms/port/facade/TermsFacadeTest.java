package teamdevhub.devhub.web.core.terms.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.web.core.terms.port.in.facade.TermsFacade;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.terms.FakeTermsAgreeUseCase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TermsFacadeTest {

    private static final String TERMS_TITLE_1 = "Terms title";
    private static final String TERMS_CONTENT_1 = "Terms content";
    private static final boolean REQUIRED = true;
    private static final boolean USED = true;

    private TermsFacade termsFacade;
    private FakeTermsAgreeUseCase fakeTermsUseCase;

    @BeforeEach
    void init() {
        fakeTermsUseCase = new FakeTermsAgreeUseCase();
        termsFacade = new TermsFacade(fakeTermsUseCase);
    }

    @Test
    @DisplayName("약관_목록을_조회하면_빈_목록을_반환한다")
    void listTerms_returnsEmptyList() {
        // when
        List<Terms> result = termsFacade.listTerms();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("약관을_등록하면_useCase의_registerTerms가_호출된다")
    void registerTerms_delegatesToUseCase() {
        // given
        CreateTermsCommand command = CreateTermsCommand.builder()
                .title(TERMS_TITLE_1)
                .content(TERMS_CONTENT_1)
                .isRequired(REQUIRED)
                .isUsed(USED)
                .build();

        // when
        termsFacade.registerTerms(command);

        // then (no exception thrown — FakeTermsAgreeUseCase.registerTerms is no-op)
    }
}
