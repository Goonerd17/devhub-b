package teamdevhub.devhub.project.http.project.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectApplicationFormResponseDto {
    private String projectApplicationFormGuid;
    private String applicationFormGuid;
    private String typeCd;
    private String title;
    private String helpText;
    private boolean isCustomized;
    private boolean isUsed;
    private List<String> itemList;
}
