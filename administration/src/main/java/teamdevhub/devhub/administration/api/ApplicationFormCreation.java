package teamdevhub.devhub.administration.api;

import java.util.List;

public interface ApplicationFormCreation {
    List<String> create(List<ApplicationFormDefinition> forms);
}
