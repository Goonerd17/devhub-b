package teamdevhub.devhub.admin.http.internal;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.outbound.admin.banner.persistence.JpaBannerRepository;
import teamdevhub.devhub.shared.home.HomeBannerView;

@RestController
@RequestMapping("/internal/home/banners")
@RequiredArgsConstructor
public class HomeBannerInternalController {
    private final JpaBannerRepository repository;

    @GetMapping
    public List<HomeBannerView> banners(@RequestParam boolean mainBanner, @RequestParam LocalDate today) {
        return repository.findByMainBannerAndUsedTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderBySortOrder(
                        mainBanner, today, today).stream()
                .map(entity -> new HomeBannerView(entity.getBannerGuid(), entity.getTitle(), entity.getImageFileGuid(),
                        entity.getLinkUrl(), entity.isMainBanner(), entity.isUsed(), entity.getStartDate(),
                        entity.getEndDate(), entity.getSortOrder()))
                .toList();
    }
}
