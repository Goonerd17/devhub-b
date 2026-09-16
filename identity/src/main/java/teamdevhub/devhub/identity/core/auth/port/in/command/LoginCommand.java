package teamdevhub.devhub.identity.core.auth.port.in.command;

import lombok.Builder;

@Builder
public record LoginCommand(String email, String password) {}
