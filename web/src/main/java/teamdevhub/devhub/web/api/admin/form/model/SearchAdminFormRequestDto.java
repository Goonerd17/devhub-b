package teamdevhub.devhub.web.api.admin.form.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchAdminFormRequestDto {

	private String title;

	private Boolean isUsed;

	public SearchApplicationFormCommand toCommand() {
		return SearchApplicationFormCommand.builder()
				.title(this.title)
				.isUsed(this.isUsed)
				.isCustomized(null)
				.build();
	}
}
