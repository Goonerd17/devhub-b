package teamdevhub.devhub.admin.outbound.community;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.admin.core.port.out.ReportPort;
import teamdevhub.devhub.shared.moderation.*;

@FeignClient(name = "community-server", configuration = InternalFeignConfiguration.class)
public interface ReportFeignClient extends ReportPort {
    @Override @GetMapping("/internal/reports")
    ReportPage findAll(@RequestParam int page, @RequestParam int size);
    @GetMapping("/internal/reports/received/{userGuid}")
    ReportPage findReceivedByUser(@PathVariable String userGuid, @RequestParam int page, @RequestParam int size);
    @Override default ReportPage findReceived(String userGuid, int page, int size) {
        return findReceivedByUser(userGuid, page, size);
    }
    @GetMapping("/internal/reports/submitted/{userGuid}")
    ReportPage findSubmittedByUser(@PathVariable String userGuid, @RequestParam int page, @RequestParam int size);
    @Override default ReportPage findSubmitted(String userGuid, int page, int size) {
        return findSubmittedByUser(userGuid, page, size);
    }
    @Override @PutMapping("/internal/reports/{reportGuid}/process")
    void process(@PathVariable String reportGuid);
}
