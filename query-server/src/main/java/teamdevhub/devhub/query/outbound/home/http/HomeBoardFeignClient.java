package teamdevhub.devhub.query.outbound.home.http;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teamdevhub.devhub.query.core.port.out.HomeBoardPort;
import teamdevhub.devhub.shared.home.HomeBoardView;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "community-server", configuration = InternalFeignConfiguration.class)
public interface HomeBoardFeignClient extends HomeBoardPort {
    @Override
    @GetMapping("/internal/home/boards")
    List<HomeBoardView> findPopular(@RequestParam int limit, @RequestParam boolean sortByLike);
}
