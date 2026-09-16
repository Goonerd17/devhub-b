package teamdevhub.devhub.web.core.project.port.in.facade.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;

@Getter
@SuperBuilder
@NoArgsConstructor
public class RequirementResponseDto {
	
    private String position;

    private String level;

    private int capacity;
    
    public static RequirementResponseDto fromDomain(ProjectRequirement requirement) {
    	return RequirementResponseDto.builder()
    			.position(requirement.getPositionCd())
    			.level(requirement.getLevelCd())
    			.capacity(requirement.getCapacity())
    			.build();
    }

}
