package teamdevhub.devhub.member.outbound.user.adapter;

import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.platform.outbound.common.util.RelationChangeUtil;

import java.util.Set;
import java.util.stream.Collectors;

public abstract class UserRelationAdapter<T> {

    protected final IdentifierProvider identifierProvider;

    protected UserRelationAdapter(IdentifierProvider identifierProvider) {
        this.identifierProvider = identifierProvider;
    }

    protected void syncItems(Set<T> previousItems, Set<T> currentItems) {
        String userGuid = extractUserGuid(currentItems);

        Set<String> oldCodes = extractCodes(previousItems);
        Set<String> newCodes = extractCodes(currentItems);

        RelationChangeUtil.RelationChange<String> change = RelationChangeUtil.change(oldCodes, newCodes);
        if (change.isEmpty()) {
            return;
        }

        deleteByUserGuidAndCodes(userGuid, change.toDelete());
        insertByUserGuidAndCodes(userGuid, change.toInsert());
    }

    protected abstract String extractUserGuid(Set<T> items);

    protected abstract Set<String> extractCodes(Set<T> items);

    protected abstract void deleteByUserGuidAndCodes(String userGuid, Set<String> codes);

    protected abstract void insertByUserGuidAndCodes(String userGuid, Set<String> codes);
}
