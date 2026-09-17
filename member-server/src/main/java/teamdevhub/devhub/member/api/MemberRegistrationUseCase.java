package teamdevhub.devhub.member.api;

public interface MemberRegistrationUseCase {
    void register(MemberRegistrationCommand command);
    boolean adminExists();
}
