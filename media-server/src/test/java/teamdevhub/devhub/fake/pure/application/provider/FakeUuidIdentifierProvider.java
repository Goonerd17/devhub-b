package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.shared.identifier.IdentifierProvider;

public class FakeUuidIdentifierProvider implements IdentifierProvider {

    private final String fixedUuidValue;

    public FakeUuidIdentifierProvider(String fixedUuidValue) {
        this.fixedUuidValue = fixedUuidValue;
    }

    @Override
    public String generateIdentifier() {
        return fixedUuidValue;
    }
}
