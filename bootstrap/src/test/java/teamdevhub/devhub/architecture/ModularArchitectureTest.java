package teamdevhub.devhub.architecture;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModularArchitectureTest {
    private static final List<String> CONTEXTS = List.of(
            "identity", "member", "project", "community",
            "administration", "media", "notification", "readmodel");
    private static final Pattern CONTEXT_IMPORT = Pattern.compile(
            "^import(?: static)? teamdevhub\\.devhub\\.(identity|member|project|community|administration|media|notification|readmodel)\\.(.+);");

    @Test
    void business_contexts_only_access_other_contexts_through_api_packages() throws IOException {
        List<String> violations = new ArrayList<>();
        for (SourceLine source : businessSourceLines()) {
            Matcher matcher = CONTEXT_IMPORT.matcher(source.line());
            if (matcher.matches() && !matcher.group(1).equals(source.module())
                    && !matcher.group(2).startsWith("api.") && !approvedReadmodelQuery(source, matcher)) {
                violations.add(source.location() + " -> " + source.line());
            }
        }
        assertTrue(violations.isEmpty(), () -> "Cross-context internal dependencies: " + violations);
    }

    private boolean approvedReadmodelQuery(SourceLine source, Matcher matcher) {
        return source.module().equals("readmodel")
                && source.path().contains("outbound")
                && matcher.group(2).matches("outbound\\..*\\.adapter\\.entity\\.Q[A-Za-z0-9_]+\\..*");
    }

    @Test
    void business_contexts_do_not_depend_on_web_or_http_types() throws IOException {
        List<String> violations = businessSourceLines().stream()
                .filter(source -> source.path().contains("core"))
                .filter(source -> source.line().startsWith("import teamdevhub.devhub.web.")
                        || source.line().startsWith("import org.springframework.web.")
                        || source.line().startsWith("import jakarta.servlet."))
                .map(source -> source.location() + " -> " + source.line())
                .toList();
        assertTrue(violations.isEmpty(), () -> "HTTP dependencies in business contexts: " + violations);
    }

    @Test
    void web_does_not_access_context_outbound_packages() throws IOException {
        List<String> violations = sourceLines("web").stream()
                .filter(source -> source.line().matches("^import teamdevhub\\.devhub\\.(identity|member|project|community|administration|media|notification|readmodel)\\.outbound\\..*"))
                .map(source -> source.location() + " -> " + source.line())
                .toList();
        assertTrue(violations.isEmpty(), () -> "Persistence dependencies in web: " + violations);
    }

    @Test
    void platform_is_business_neutral() throws IOException {
        List<String> violations = sourceLines("platform").stream()
                .filter(source -> CONTEXT_IMPORT.matcher(source.line()).matches()
                        || source.line().startsWith("import teamdevhub.devhub.web."))
                .map(source -> source.location() + " -> " + source.line())
                .toList();
        assertTrue(violations.isEmpty(), () -> "Business dependencies in platform: " + violations);
    }

    private List<SourceLine> businessSourceLines() throws IOException {
        List<SourceLine> result = new ArrayList<>();
        for (String context : CONTEXTS) {
            result.addAll(sourceLines(context));
        }
        return result;
    }

    private List<SourceLine> sourceLines(String module) throws IOException {
        Path source = repositoryRoot().resolve(module).resolve("src/main/java");
        if (!Files.exists(source)) {
            return List.of();
        }
        List<SourceLine> result = new ArrayList<>();
        try (var paths = Files.walk(source)) {
            for (Path path : paths.filter(file -> file.toString().endsWith(".java")).toList()) {
                try (var lines = Files.lines(path)) {
                    lines.forEach(line -> result.add(new SourceLine(module,
                            source.relativize(path).toString(), line.trim())));
                }
            }
        }
        return result;
    }

    private Path repositoryRoot() {
        Path root = Path.of(System.getProperty("user.dir")).toAbsolutePath();
        return Files.exists(root.resolve("settings.gradle")) ? root : root.getParent();
    }

    private record SourceLine(String module, String path, String line) {
        String location() {
            return module + "/" + path;
        }
    }
}
