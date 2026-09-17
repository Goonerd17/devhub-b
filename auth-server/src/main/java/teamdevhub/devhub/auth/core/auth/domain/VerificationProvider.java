package teamdevhub.devhub.auth.core.auth.domain;

public enum VerificationProvider {
    EMAIL,
    GOOGLE,
    GITHUB,
    KAKAO,
    NAVER;

    public static VerificationProvider from(String provider) {
        return VerificationProvider.valueOf(provider.toUpperCase());
    }
}
