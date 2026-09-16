package teamdevhub.devhub.administration.api.form;

import lombok.Builder;

@Builder
public record SearchApplicationFormCommand(String title, Boolean isUsed, Boolean isCustomized) {
}
