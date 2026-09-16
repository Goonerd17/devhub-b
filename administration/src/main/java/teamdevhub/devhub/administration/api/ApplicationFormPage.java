package teamdevhub.devhub.administration.api;

import java.util.List;

public record ApplicationFormPage(List<ApplicationFormView> content, int page, int size, long totalElements,
                                  int totalPages, boolean first, boolean last) { }
