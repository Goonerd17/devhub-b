package teamdevhub.devhub.shared.form;

import java.util.List;

public record ApplicationFormPage(List<ApplicationFormView> content, int page, int size, long totalElements,
                                  int totalPages, boolean first, boolean last) {
}
