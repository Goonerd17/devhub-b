package teamdevhub.devhub.administration.api;

import java.util.List;

public record ApplicationFormView(String applicationFormGuid, String typeCd, String title,
                                  String helpText, boolean customized, boolean used, List<String> itemList) { }
