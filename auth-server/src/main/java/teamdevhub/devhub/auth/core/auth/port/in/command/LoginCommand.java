package teamdevhub.devhub.auth.core.auth.port.in.command;

import lombok.Builder;

@Builder
public record LoginCommand(String email, String password) {}
