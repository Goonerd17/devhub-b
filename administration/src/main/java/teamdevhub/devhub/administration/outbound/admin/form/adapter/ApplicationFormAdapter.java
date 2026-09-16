package teamdevhub.devhub.administration.outbound.admin.form.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.core.admin.form.port.out.ApplicationFormRepository;
import teamdevhub.devhub.administration.outbound.admin.form.adapter.mapper.ApplicationFormMapper;
import teamdevhub.devhub.administration.outbound.admin.form.persistence.JpaApplicationFormRepository;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class ApplicationFormAdapter implements ApplicationFormRepository {

	private final JpaApplicationFormRepository jpaApplicationFormRepository;

	@Override
	public void save(ApplicationForm applicationForm) {
		jpaApplicationFormRepository.save(ApplicationFormMapper.toEntity(applicationForm));
	}

	@Override
	public void update(ApplicationForm applicationForm) {
		save(applicationForm);
	}

	@Override
	public ApplicationForm findByApplicationFormGuid(String applicationFormGuid) {
		return jpaApplicationFormRepository.findByApplicationFormGuid(applicationFormGuid)
				.map(ApplicationFormMapper::toApplicationForm)
				.orElseThrow(() -> AdapterDataException.of(ErrorCode.APPLICATION_FORM_NOT_FOUND));
	}

	@Override
	public void deleteByApplicationFormGuid(List<String> applicationFormGuids) {
		jpaApplicationFormRepository.deleteAllById(applicationFormGuids);
	}

	@Override
	public List<ApplicationForm> findByIdAndIsCustomized(List<String> deleteApplicationFormGuids) {
		return jpaApplicationFormRepository.findByIdAndIsCustomized(deleteApplicationFormGuids).stream()
				.map(ApplicationFormMapper::toApplicationForm)
				.toList();
	}

	@Override
	public List<ApplicationForm> findByIdAndIsNotCustomized(List<String> formList) {
		return jpaApplicationFormRepository.findByIdAndIsNotCustomized(formList).stream()
				.map(ApplicationFormMapper::toApplicationForm)
				.toList();
	}

}
