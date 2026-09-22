package teamdevhub.devhub.admin.http.admin.form.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.admin.api.form.SearchApplicationFormCommand;

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

