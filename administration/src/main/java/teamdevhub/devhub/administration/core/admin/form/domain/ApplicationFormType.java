package teamdevhub.devhub.administration.core.admin.form.domain;

import lombok.Getter;

@Getter
public enum ApplicationFormType {
	
	SHORTTEXT("textfield_100"),
	LONGTEXT("textfield_300"),
	TEXTAREA("textarea"),
	SELECTBOX("selectbox"),
	CHECKBOX("checkbox");
	
	private final String code;
	
	ApplicationFormType(String code) {
		this.code = code;
	}

}
