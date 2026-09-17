package teamdevhub.devhub.project.api;

import lombok.Getter;

@Getter
public enum ProjectRecruitStatus {
	
	RECRUITING("3201", "모집중"),
	COMPLETED("3202", "모집완료"),
	WAITING("3203", "모집전");
	
	private final String code;
	private final String message;
	
	ProjectRecruitStatus(String code, String message) {
		this.code= code;
		this.message = message;
	}
}
