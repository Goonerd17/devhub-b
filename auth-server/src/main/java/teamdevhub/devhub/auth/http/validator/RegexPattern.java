package teamdevhub.devhub.auth.http.validator;

public enum RegexPattern {

    AUTH_EMAIL("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", "올바른 이메일 형식이 아닙니다"),
    AUTH_PASSWORD("^(?=.*[!@#$%^&*()_\\-+=<>?{}\\[\\]~])[A-Za-z\\d!@#$%^&*()_\\-+=<>?{}\\[\\]~]{8,20}$", "8~20자, 특수문자 1개 이상을 포함해야 합니다"),
    USERNAME("^[a-zA-Z0-9가-힣_]{1,15}$", "사용자명은 한글,영문,숫자,_만 사용 가능하며 최대 15자까지 가능합니다");

    private final String regexp;
    private final String message;

    RegexPattern(String regexp, String message) {
        this.regexp = regexp;
        this.message = message;
    }

    public String regexp() {
        return regexp;
    }
    public String message() {
        return message;
    }
}
