package teamdevhub.devhub.query.outbound.home.http;

import java.time.LocalDate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teamdevhub.devhub.query.core.port.out.HomeProjectPort;
import teamdevhub.devhub.shared.home.HomeProjectView;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "project-server", contextId = "homeProjectClient",
        configuration = InternalFeignConfiguration.class)
public interface HomeProjectFeignClient extends HomeProjectPort {
    @Override
    @GetMapping("/internal/home/projects")
    List<HomeProjectView> findRecent(@RequestParam LocalDate today, @RequestParam int limit);
}
