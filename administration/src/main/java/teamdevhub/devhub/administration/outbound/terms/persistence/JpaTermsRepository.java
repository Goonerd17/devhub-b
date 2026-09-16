package teamdevhub.devhub.administration.outbound.terms.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.administration.outbound.terms.adapter.entity.TermsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface JpaTermsRepository extends JpaRepository<TermsEntity, String> {

    Optional<TermsEntity> findByTermsGuid(String termsGuid);
    List<TermsEntity> findAllByTermsGuidInAndIsDeletedFalseAndIsUsedTrue(Collection<String> termsGuids);
    List<TermsEntity> findAll();
}
