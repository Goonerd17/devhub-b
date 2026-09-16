package teamdevhub.devhub.member.api;

import lombok.Getter;

@Getter
public enum UserStatus {
	
	NORMAL("7001", "정상"),
	DELETED("7002", "탈퇴"),
	BLOCKED("7003", "정지");
	
	private final String code;
	private final String message;
	
	UserStatus(String code, String message) {
		this.code= code;
		this.message = message;
	}
	
	 public static UserStatus from(boolean deleted, boolean blocked) {
	        if (deleted) return DELETED;
	        if (blocked) return BLOCKED;
	        return NORMAL;
	 }
}
