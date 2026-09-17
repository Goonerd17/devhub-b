package teamdevhub.devhub.admin.outbound.terms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.admin.outbound.terms.adapter.entity.TermsAgreementEntity;

public interface JpaTermsAgreementRepository extends JpaRepository<TermsAgreementEntity, String> {
}
