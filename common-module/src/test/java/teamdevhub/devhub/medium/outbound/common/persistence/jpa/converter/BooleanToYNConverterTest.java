package teamdevhub.devhub.shared.outbound.common.persistence.jpa.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.converter.BooleanToYNConverter;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BooleanToYNConverterTest {

    private final BooleanToYNConverter booleanToYNConverter = new BooleanToYNConverter();

    @Test
    @DisplayName("true_??Y_濡?蹂?섎맂??")
    void convertTrueToY() {
        // given, when
        String result = booleanToYNConverter.convertToDatabaseColumn(true);

        // then
        assertThat(result).isEqualTo("Y");
    }

    @Test
    @DisplayName("false_??N_?쇰줈_蹂?섎맂??")
    void convertFalseToN() {
        // given, when
        String result = booleanToYNConverter.convertToDatabaseColumn(false);

        // then
        assertThat(result).isEqualTo("N");
    }

    @Test
    @DisplayName("null_?_N_?쇰줈_蹂?섎맂??")
    void convertNullToN() {
        // given, when
        String result = booleanToYNConverter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isEqualTo("N");
    }

    @Test
    @DisplayName("Y_??true_濡?蹂?섎맂??")
    void convertYToTrue() {
        // given, when
        Boolean result = booleanToYNConverter.convertToEntityAttribute("Y");

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("N_?_false_濡?蹂?섎맂??")
    void convertNToFalse() {
        // given, when
        Boolean result = booleanToYNConverter.convertToEntityAttribute("N");

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("null_?_false_濡?蹂?섎맂??")
    void convertNullToFalse() {
        // given, when
        Boolean result = booleanToYNConverter.convertToEntityAttribute(null);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Y_N_?몄쓽_媛믪씠硫??덉쇅媛_諛쒖깮?쒕떎")
    void throwIfValueIsNotYOrN() {
        // given
        String invalidValue = "X";

        // then
        assertThatThrownBy(
                // when
                () -> booleanToYNConverter.convertToEntityAttribute(invalidValue))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.BOOLEAN_CONVERT_FAIL.getMessage());
    }
}
