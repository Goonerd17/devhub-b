package teamdevhub.devhub.platform.outbound.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.outbound.common.util.RelationChangeUtil;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RelationChangeUtilTest {

    @Test
    @DisplayName("기존_데이터와_새로운_데이터_Set_차이를_계산해서_추가해야할_데이터와_삭제해야할_데이터를_반환한다")
    void returnAddedAndRemovedDataWhenCalculatingDifferenceBetweenOldAndNewSets() {
        //given
        Set<String> existing = Set.of("A", "B", "C");
        Set<String> incoming = Set.of("B", "C", "D");

        // when
        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(existing, incoming);

        // then
        assertThat(change.toInsert()).containsExactly("D");
        assertThat(change.toDelete()).containsExactly("A");
        assertThat(change.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("기존_데이터와_새로운_데이터_Set_이_같으면_추가와_삭제가_없는_빈_RelationChange_를_반환한다")
    void returnEmptyRelationChangeWhenOldAndNewSetsAreEqual() {
        // given
        Set<String> existing = Set.of("X", "Y");
        Set<String> incoming = Set.of("X", "Y");

        // when
        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(existing, incoming);

        // then
        assertThat(change.isEmpty()).isTrue();
        assertThat(change.toInsert()).isEmpty();
        assertThat(change.toDelete()).isEmpty();
    }

    @Test
    @DisplayName("기존_데이터와_새로운_데이터_Set_이_둘다_빈셋이면_빈_RelationChange_를_반환한다")
    void returnEmptyRelationChangeWhenOldAndNewSetsAreBothEmpty() {
        // given
        Set<String> existing = Set.of();
        Set<String> incoming = Set.of();

        // when
        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(existing, incoming);

        // then
        assertThat(change.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("기존_데이터_Set_이_빈_Set_이면_모든_새로운_데이터가_추가될_대상으로_인식된다")
    void considerAllNewDataAsAddedWhenOldSetIsEmpty() {
        // given
        Set<String> existing = Set.of();
        Set<String> incoming = Set.of("A", "B");

        // when
        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(existing, incoming);

        // then
        assertThat(change.toInsert()).containsExactlyInAnyOrder("A", "B");
        assertThat(change.toDelete()).isEmpty();
    }

    @Test
    @DisplayName("새로운_데이터_Set_이_빈_Set_이면_모든_기존_데이터가_삭제될_대상으로_인식된다")
    void considerAllOldDataAsRemovedWhenNewSetIsEmpty() {
        // given
        Set<String> existing = Set.of("X", "Y");
        Set<String> incoming = Set.of();

        // when
        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(existing, incoming);

        // then
        assertThat(change.toInsert()).isEmpty();
        assertThat(change.toDelete()).containsExactlyInAnyOrder("X", "Y");
    }
}
