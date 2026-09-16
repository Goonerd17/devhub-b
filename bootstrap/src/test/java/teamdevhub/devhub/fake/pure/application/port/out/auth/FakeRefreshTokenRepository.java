package teamdevhub.devhub.fake.pure.application.port.out.auth;

import teamdevhub.devhub.identity.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.identity.core.auth.port.out.token.RefreshTokenRepository;

import java.util.HashMap;
import java.util.Optional;

public class FakeRefreshTokenRepository implements RefreshTokenRepository {

    private final HashMap<String, RefreshToken> store = new HashMap<>();

    @Override
    public void save(RefreshToken refreshToken) {
        store.put(refreshToken.userGuid(), refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByUserGuid(String userGuid) {
        return Optional.ofNullable(store.get(userGuid));
    }

    @Override
    public void deleteByUserGuid(String userGuid) {
        store.remove(userGuid);
    }

    public void givenRefreshToken(RefreshToken refreshToken) {
        store.put(refreshToken.userGuid(), refreshToken);
    }

    public boolean contains(String userGuid) {
        return store.containsKey(userGuid);
    }
}
