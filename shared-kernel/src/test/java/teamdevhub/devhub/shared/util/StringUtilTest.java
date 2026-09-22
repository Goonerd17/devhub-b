package teamdevhub.devhub.shared.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.util.StringUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StringUtilTest {

    @Test
    @DisplayName("문자열이_빈_문자열이면_isEmpty_는_true_를_반환한다")
    void shouldReturnTrueForIsEmptyWhenStringIsNullOrEmpty() {
        // given, when, then
        assertThat(StringUtil.isEmpty("")).isTrue();
        assertThat(StringUtil.isEmpty("   ")).isTrue();
    }

    @Test
    @DisplayName("문자열에_내용이_있으면_isEmpty_는_false_를_반환한다")
    void shouldReturnFalseForIsEmptyWhenStringHasContent() {
        // given, when, then
        assertThat(StringUtil.isEmpty("A")).isFalse();
        assertThat(StringUtil.isEmpty(" hello ")).isFalse();
    }

    @Test
    @DisplayName("문자열이_빈문자열이면_isNotEmpty_는_false_를_반환한다")
    void shouldReturnFalseForIsNotEmptyWhenStringIsNullOrEmpty() {
        // given, when, then
        assertThat(StringUtil.isNotEmpty("")).isFalse();
        assertThat(StringUtil.isNotEmpty("   ")).isFalse();
    }

    @Test
    @DisplayName("문자열에_내용이_있으면_isNotEmpty_는_true_를_반환한다")
    void shouldReturnTrueForIsNotEmptyWhenStringHasContent() {
        // given, when, then
        assertThat(StringUtil.isNotEmpty("A")).isTrue();
        assertThat(StringUtil.isNotEmpty(" hello ")).isTrue();
    }

    @Test
    @DisplayName("문자열이_null_이거나_빈문자열이면_defaultIfEmpty_는_기본값을_반환한다")
    void shouldReturnDefaultValueForDefaultIfEmptyWhenStringIsNullOrEmpty() {
        // given, when, then
        assertThat(StringUtil.defaultIfEmpty(null, "default")).isEqualTo("default");
        assertThat(StringUtil.defaultIfEmpty("", "default")).isEqualTo("default");
        assertThat(StringUtil.defaultIfEmpty("   ", "default")).isEqualTo("default");
    }

    @Test
    @DisplayName("문자열에_내용이_있으면_defaultIfEmpty_는_원본문자열을_반환한다")
    void shouldReturnOriginalStringForDefaultIfEmptyWhenStringHasContent() {
        // given, when, then
        assertThat(StringUtil.defaultIfEmpty("hello", "default")).isEqualTo("hello");
        assertThat(StringUtil.defaultIfEmpty(" world ", "default")).isEqualTo(" world ");
    }

    @Test
    @DisplayName("문자열_길이가_maxLength_보다_작거나_같으면_truncate_는_원본문자열을_반환한다")
    void shouldReturnOriginalStringForTruncateWhenLengthIsLessThanOrEqualMax() {
        // given, when, then
        assertThat(StringUtil.truncate("abc", 5)).isEqualTo("abc");
        assertThat(StringUtil.truncate("abc", 3)).isEqualTo("abc");
    }

    @Test
    @DisplayName("문자열_길이가_maxLength_보다_크면_truncate_는_maxLength_까지_문자열을_잘라서_반환한다")
    void shouldTruncateStringToMaxLengthWhenLengthExceedsMax() {
        // given, when, then
        assertThat(StringUtil.truncate("maxLength", 3)).isEqualTo("max");
        assertThat(StringUtil.truncate(" hello world ", 5)).isEqualTo(" hell");
    }

    @Test
    @DisplayName("maxLength_가_0보다_작으면_truncate_는_예외를_던진다")
    void shouldThrowExceptionForTruncateWhenMaxLengthIsNegative() {
        // given, when, then
        assertThatThrownBy(() -> StringUtil.truncate("abc", -1))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("문자열 최대 길이는 0 이상이어야 합니다");
    }

    @Test
    @DisplayName("문자열이_null_이면_truncate_는_null_을_반환한다")
    void shouldReturnNullForTruncateWhenStringIsNull() {
        // given, when, then
        assertThat(StringUtil.truncate(null, 5)).isNull();
    }

    @Test
    @DisplayName("문자열이_빈_문자열이면_truncate_는_빈_문자열을_반환한다")
    void shouldReturnEmptyStringForTruncateWhenStringIsEmpty() {
        // given, when, then
        assertThat(StringUtil.truncate("", 5)).isEmpty();
        assertThat(StringUtil.truncate("   ", 2)).isEqualTo("   ");
    }
}
