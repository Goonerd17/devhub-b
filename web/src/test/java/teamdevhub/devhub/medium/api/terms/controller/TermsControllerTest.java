package teamdevhub.devhub.web.api.terms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.terms.controller.TermsController;
import teamdevhub.devhub.web.api.terms.model.CreateTermsRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.web.core.terms.port.in.facade.TermsFacade;
import teamdevhub.devhub.web.core.terms.port.in.facade.model.TermsResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static teamdevhub.devhub.constant.TermsTestConstant.*;

class TermsControllerTest {

    private TermsController termsController;
    private TermsFacade termsFacade;

    @BeforeEach
    void init() {
        termsFacade = Mockito.mock(TermsFacade.class);
        termsController = new TermsController(termsFacade);
    }

    @Test
    @DisplayName("약관_목록_조회에_성공하면_READ_SUCCESS_코드와_목록을_반환한다")
    void list_returnsReadSuccessWithTermsList() {
        // given
        Terms terms = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
        when(termsFacade.listTerms()).thenReturn(List.of(terms));

        // when
        ResponseEntity<DataListApiResponseDto<TermsResponseDto>> response = termsController.list();

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(response.getBody().getDataList()).hasSize(1);
        assertThat(response.getBody().getDataList().get(0).getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(response.getBody().getDataList().get(0).getTitle()).isEqualTo(TERMS_TITLE_1);

        verify(termsFacade).listTerms();
    }

    @Test
    @DisplayName("약관_목록이_비어_있으면_빈_목록을_반환한다")
    void list_whenEmpty_returnsEmptyList() {
        // given
        when(termsFacade.listTerms()).thenReturn(List.of());

        // when
        ResponseEntity<DataListApiResponseDto<TermsResponseDto>> response = termsController.list();

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDataList()).isEmpty();
    }

    @Test
    @DisplayName("약관_등록에_성공하면_CREATE_SUCCESS_코드를_반환한다")
    void register_returnsCreateSuccess() {
        // given
        CreateTermsRequestDto requestDto = CreateTermsRequestDto.builder()
                .title(TERMS_TITLE_1)
                .content(TERMS_CONTENT_1)
                .isRequired(REQUIRED)
                .isUsed(USED)
                .build();
        doNothing().when(termsFacade).registerTerms(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = termsController.register(requestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());

        verify(termsFacade).registerTerms(any());
    }

    @Test
    @DisplayName("여러_약관이_있을_때_목록_조회시_전체_항목이_반환된다")
    void list_multipleTerms_returnsAll() {
        // given
        Terms terms1 = Terms.of(TERMS_GUID_1, TERMS_TITLE_1, TERMS_CONTENT_1, REQUIRED, USED, NOT_DELETED);
        Terms terms2 = Terms.of(TERMS_GUID_2, TERMS_TITLE_2, TERMS_CONTENT_2, OPTIONAL, USED, NOT_DELETED);
        when(termsFacade.listTerms()).thenReturn(List.of(terms1, terms2));

        // when
        ResponseEntity<DataListApiResponseDto<TermsResponseDto>> response = termsController.list();

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDataList()).hasSize(2);
    }
}
