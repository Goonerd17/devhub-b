package teamdevhub.devhub.admin.outbound.admin.form.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationFormItem;
import teamdevhub.devhub.admin.core.admin.form.port.out.ApplicationFormItemRepository;
import teamdevhub.devhub.admin.outbound.admin.form.adapter.entity.ApplicationFormItemEntity;
import teamdevhub.devhub.admin.outbound.admin.form.adapter.mapper.ApplicationFormItemMapper;
import teamdevhub.devhub.admin.outbound.admin.form.persistence.JpaApplicationFormItemRepository;

@Component
@RequiredArgsConstructor
public class ApplicationFormItemAdapter implements ApplicationFormItemRepository{
	
	private final JpaApplicationFormItemRepository jpaApplicationFormItemRepository;

	@Override
	public void saveAll(Set<ApplicationFormItem> items) {
		List<ApplicationFormItemEntity> entityList = items.stream()
				.map(item -> ApplicationFormItemMapper.toEntity(item))
				.toList();
		jpaApplicationFormItemRepository.saveAll(entityList);
	}

	@Override
	public void deleteByApplicationFormGuid(List<String> applicationFormGuids) {
		jpaApplicationFormItemRepository.deleteAllByApplicationFormGuid(applicationFormGuids);
	}

	@Override
	public List<ApplicationFormItem> findByFormGuid(String applicationFormGuid) {
		List<ApplicationFormItemEntity> entityList = jpaApplicationFormItemRepository.findByFormGuid(applicationFormGuid);
		return entityList.stream()
				.map(ApplicationFormItemMapper::toApplicationFormItem)
				.toList();
	}

}
