package teamdevhub.devhub.admin.outbound.admin.banner.adapter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.audit.BaseEntity;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.converter.BooleanToYNConverter;

import java.time.LocalDate;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(name = "banner")
public class BannerEntity extends BaseEntity {

    @Id
    @Column(name = "banner_guid", length = 32, nullable = false, unique = true)
    private String bannerGuid;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "image_file_guid", length = 32)
    private String imageFileGuid;

    @Column(name = "link_url", length = 500)
    private String linkUrl;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "main_banner_yn", nullable = false)
    private boolean mainBanner;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "use_yn", nullable = false)
    private boolean used;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;
}
