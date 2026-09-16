package teamdevhub.devhub.web.api.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchApplicationFormRequestDto {

	private String title;
	
	private String useYn;
	
	private String customYn;
	
	public SearchApplicationFormCommand toCommand() {
		return SearchApplicationFormCommand.builder()
				.title(title)
				.isUsed(useYn == null ? null : "Y".equals(useYn))
				.isCustomized(customYn == null ? null : "Y".equals(customYn))
				.build();
	}
}
