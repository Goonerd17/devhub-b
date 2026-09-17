package teamdevhub.devhub.admin.api.form;

import lombok.Builder;

@Builder
public record SearchApplicationFormCommand(String title, Boolean isUsed, Boolean isCustomized) {
}
