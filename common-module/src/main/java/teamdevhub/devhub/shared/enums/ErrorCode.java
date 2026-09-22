package teamdevhub.devhub.shared.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    VALIDATION_FAIL("ERR.DVH.000", "검증 오류가 발생했습니다", BAD_REQUEST),

    READ_FAIL("ERR.DVH.0001", "조회 오류가 발생했습니다", INTERNAL_SERVER_ERROR),
    CREATE_FAIL("ERR.DVH.0002", "작성 오류가 발생했습니다",INTERNAL_SERVER_ERROR),
    UPDATE_FAIL("ERR.DVH.0003", "수정 오류가 발생했습니다",INTERNAL_SERVER_ERROR),
    DELETE_FAIL("ERR.DVH.0004", "삭제 오류가 발생했습니다",INTERNAL_SERVER_ERROR),

    TOKEN_EXPIRED("ERR.DVH.0010", "토큰이 만료되었습니다", UNAUTHORIZED),
    TOKEN_INVALID("ERR.DVH.0011", "유효하지 않은 토큰입니다", UNAUTHORIZED),
    TOKEN_UNSUPPORTED("ERR.DVH.0012", "지원되지 않는 토큰 형식입니다", UNAUTHORIZED),
    MISSING_AUTH_HEADER("ERR.DVH.0013", "Authorization Header 가 없습니다", BAD_REQUEST),
    USER_NOT_FOUND("ERR.DVH.0014", "로그인된 사용자가 존재하지 않습니다", UNAUTHORIZED),
    REFRESH_TOKEN_INVALID("ERR.DVH.0015", "유효하지 않은 토큰입니다", UNAUTHORIZED),
    AUTH_INVALID("ERR.DVH.0016", "유효하지 않은 인증 정보입니다", UNAUTHORIZED),
    AUTH_FAIL("ERR.DVH.0017", "인증에 실패했습니다", UNAUTHORIZED),

    SIGNUP_FAIL("ERR.DVH.0018", "회원가입에 실패했습니다", BAD_REQUEST),
    LOGIN_FAIL("ERR.DVH.0019", "로그인에 실패했습니다", BAD_REQUEST),
    DUPLICATED_ACCOUNT("ERR.DVH.0020", "중복된 계정이 존재합니다", BAD_REQUEST),

    VERIFICATION_ALREADY_SENT("ERR.DVH.0021", "본인인증 코드가 이미 발송되었습니다 잠시 후 재시도해주세요.", BAD_REQUEST),
    VERIFICATION_FAIL("ERR.DVH.0022", "본인인증에 실패했습니다", BAD_REQUEST),
    VERIFICATION_INVALID("ERR.DVH.0023", "본인인증 코드가 일치하지 않습니다", BAD_REQUEST),
    VERIFICATION_NOT_EXISTED("ERR.DVH.0024", "본인인증 내역이 존재하지 않습니다", BAD_REQUEST),
    VERIFICATION_TYPE_INVALID("ERR.DVH.0025",  "인증 유형이 잘못되었습니다", BAD_REQUEST),
    VERIFICATION_VALUE_REQUIRED("ERR.DVH.0026", "인증 값은 필수입니다", BAD_REQUEST),
    INVALID_EMAIL_FORMAT("ERR.DVH.0027", "이메일 형식이 올바르지 않습니다", BAD_REQUEST),
    INVALID_PHONE_NUMBER_FORMAT("ERR.DVH.0028", "휴대폰 번호 형식이 올바르지 않습니다", BAD_REQUEST),
    OTP_BLANK("ERR.DVH.0029", "OTP 값은 비어 있을 수 없습니다", BAD_REQUEST),

    USER_ID_FAIL("ERR.DVH.0030", "사용자 ID 값이 잘못되었습니다",BAD_REQUEST),
    USER_PASSWORD_FAIL("ERR.DVH.0031", "사용자 비밀번호 값이 잘못되었습니다",BAD_REQUEST),
    ALREADY_DELETED("ERR.DVH.0032", "이미 탈퇴한 회원입니다", BAD_REQUEST ),
    USER_POSITION_REQUIRED("ERR.DVH.0033", "관심 포지션은 필수입니다", BAD_REQUEST),
    USER_SKILL_REQUIRED("ERR.DVH.0034", "보유 스킬목록은 필수입니다", BAD_REQUEST),
    USER_WITHDRAWN("ERR.DVH.0035", "탈퇴한 회원입니다", BAD_REQUEST),
    USER_BLOCKED("ERR.DVH.0036", "정지된 회원입니다", BAD_REQUEST),
    USER_ALREADY_BANNED("ERR.DVH.0037", "이미 정지된 회원입니다", BAD_REQUEST),
    USER_NOT_BANNED("ERR.DVH.0038", "정지 중이 아닌 회원입니다", BAD_REQUEST),

    OAUTH_FAIL("ERR.DVH.0040", "지원하지 않는 OAuth 로그인입니다", BAD_REQUEST),
    OAUTH_STATE_INVALID("ERR.DVH.0041", "유효하지 않은 OAuth state 값입니다", BAD_REQUEST),

    NOTIFICATION_SEND_FAIL("ERR.DVH.0050", "발송이 실패했습니다",INTERNAL_SERVER_ERROR),

    FILE_EMPTY("ERR.DVH.0060", "업로드할 파일이 비어 있습니다", BAD_REQUEST),
    FILE_NAME_REQUIRED("ERR.DVH.0061", "파일명은 필수입니다", BAD_REQUEST),
    FILE_EXTENSION_REQUIRED("ERR.DVH.0062", "확장자는 필수입니다", BAD_REQUEST),
    FILE_SIZE_INVALID("ERR.DVH.0063", "파일 크기는 0보다 커야 합니다", BAD_REQUEST),
    FILE_READ_FAIL("ERR.DVH.0064", "파일을 읽는 중 오류가 발생했습니다", INTERNAL_SERVER_ERROR),
    FILE_EXTENSION_INVALID("ERR.DVH.0065", "지원하지 않는 파일 확장자입니다", BAD_REQUEST),
    FILE_NAME_TOO_LONG("ERR.DVH.0066", "파일명이 허용 길이를 초과했습니다", BAD_REQUEST),

    INVALID_TERMS("ERR.DVH.0067", "유효하지 않은 약관입니다", INTERNAL_SERVER_ERROR),
    INVALID_TERMS_AGREEMENT("ERR.DVH.0068", "유효하지 않은 약관 동의입니다", BAD_REQUEST),
    NOT_EXISTED_TERMS_AGREEMENT("ERR.DVH.0069", "존재하지 않는 약관입니다", BAD_REQUEST),

    BOOLEAN_CONVERT_FAIL("ERR.DVH.0070", "Boolean 값 변환에 실패했습니다",INTERNAL_SERVER_ERROR),
    STRING_LENGTH_INVALID("ERR.DVH.0071", "문자열 최대 길이는 0 이상이어야 합니다", BAD_REQUEST),

    APPLICATION_FORM_NOT_FOUND("ERR.DVH.0079", "지원 폼을 찾을 수 없습니다", BAD_REQUEST),

    PROJECT_NOT_FOUND("ERR.DVH.0080", "프로젝트를 찾을 수 없습니다", BAD_REQUEST),
    PROJECT_NOT_COMPLETED("ERR.DVH.0081", "완료되지 않은 프로젝트입니다", BAD_REQUEST),
    REVIEW_NOT_A_MEMBER("ERR.DVH.0082", "프로젝트 멤버만 리뷰할 수 있습니다", BAD_REQUEST),
    REVIEW_SELF_NOT_ALLOWED("ERR.DVH.0083", "자기 자신에게 리뷰할 수 없습니다", BAD_REQUEST),
    REVIEW_DUPLICATE("ERR.DVH.0084", "이미 해당 프로젝트에서 리뷰한 사용자입니다", BAD_REQUEST),
    REVIEW_SCORE_INVALID("ERR.DVH.0085", "리뷰 점수는 1.0~5.0 사이의 0.5 단위 값이어야 합니다", BAD_REQUEST),
    APPLICATION_SELF_NOT_ALLOWED("ERR.DVH.0086", "본인이 등록한 프로젝트에는 지원할 수 없습니다", BAD_REQUEST),
    APPLICATION_PROJECT_NOT_RECRUITING("ERR.DVH.0087", "모집 중인 프로젝트가 아닙니다", BAD_REQUEST),
    APPLICATION_NOT_CANCELABLE("ERR.DVH.0088", "이미 처리된 지원은 취소할 수 없습니다", BAD_REQUEST),
    REPORT_DUPLICATE("ERR.DVH.0091", "이미 동일 대상을 신고했습니다", BAD_REQUEST),
    REPORT_TARGET_MISMATCH("ERR.DVH.0093", "해당 게시글의 댓글만 신고할 수 있습니다.", BAD_REQUEST),

    FILE_INVALID("ERR.DVH.1000", "파일처리 과정에서 오류가 발생했습니다", INTERNAL_SERVER_ERROR),
    UNKNOWN_FAIL("ERR.DVH.9999", "원인 미상의 에러가 발생했습니다",BAD_REQUEST);


    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
