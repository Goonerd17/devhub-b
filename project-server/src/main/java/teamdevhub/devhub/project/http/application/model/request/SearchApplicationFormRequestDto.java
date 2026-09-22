package teamdevhub.devhub.project.http.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.shared.form.ApplicationFormSearch;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchApplicationFormRequestDto {

	private String title;
	
	private String useYn;
	
	private String customYn;
	
	public ApplicationFormSearch toCommand() {
		return new ApplicationFormSearch(title,
				useYn == null ? null : "Y".equals(useYn),
				customYn == null ? null : "Y".equals(customYn));
	}
}
