package teamdevhub.devhub.project.core.application.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationAnswer {

	private String projectApplicationFormGuid;
	private String applicationAnswerGuid;
	private String applicationGuid;
	private String applicationFormGuid;
	private String projectGuid;
	private String fileGuid;
	private String content;

	// 지원자 유저 정보
	private String userName;
	private String email;
	private double mannerDegree;
	private List<String> userSkillList;
	private String positionCd;
	private String introduction;

	/**
	 * 리뷰
	 * 오탈자 수정했습니다.(aply-> apply)
	 */
	private String applyDate;
}
