package teamdevhub.devhub.admin.http.admin.form.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.admin.api.ApplicationFormDefinition;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateApplicationFormRequestDto {

    @NotBlank(message = "타입은 필수입니다.")
    private String typeCd;

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String helpText;

    private List<@NotBlank(message = "선택항목 내용은 필수입니다.") String> itemList;

    public ApplicationFormDefinition toDefinition() {
        return new ApplicationFormDefinition(typeCd, title, helpText, itemList);
    }
}

