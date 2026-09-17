package teamdevhub.devhub.admin.core.admin.form.port.in.command;

import lombok.Builder;

@Builder
public record ApplicationFormItemCommand(String formItemGuid, String formGuid, String content) {

}
