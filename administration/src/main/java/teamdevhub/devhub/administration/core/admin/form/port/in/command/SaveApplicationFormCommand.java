package teamdevhub.devhub.administration.core.admin.form.port.in.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveApplicationFormCommand {

	private String applicationFormGuid;
	private String typeCd;
	private String title;
	private String helpYn;
	private String helpText;
	private boolean isUsed;
	private String defaultFieldYn;
	private boolean insert;
	private List<String> itemList;
}
