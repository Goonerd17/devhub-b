package teamdevhub.devhub.admin.api;

import java.util.List;

public record ApplicationFormDefinition(String typeCd, String title, String helpText, List<String> itemList) { }
