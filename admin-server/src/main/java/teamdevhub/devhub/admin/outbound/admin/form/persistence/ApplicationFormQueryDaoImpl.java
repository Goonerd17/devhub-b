package teamdevhub.devhub.admin.outbound.admin.form.persistence;

import static teamdevhub.devhub.admin.outbound.admin.form.adapter.entity.QApplicationFormEntity.applicationFormEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.admin.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.admin.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.admin.outbound.admin.form.adapter.entity.ApplicationFormEntity;
import teamdevhub.devhub.admin.outbound.admin.form.adapter.entity.ApplicationFormItemEntity;
import teamdevhub.devhub.admin.outbound.admin.form.adapter.mapper.ApplicationFormMapper;

@Repository
@RequiredArgsConstructor
public class ApplicationFormQueryDaoImpl implements ApplicationFormQueryDao {

	private final JPAQueryFactory queryFactory;
	private final JpaApplicationFormItemRepository jpaApplicationFormItemRepository;

	@Override
	public Page<ApplicationForm> listApplicationForm(SearchApplicationFormCommand searchApplicationFormCommand,
			Pageable pageable) {

		BooleanExpression titleContains = titleCond(searchApplicationFormCommand.title());
		BooleanExpression useYnEquals = useYnCond(searchApplicationFormCommand.isUsed());
		BooleanExpression customYnEquals = customYnCond(searchApplicationFormCommand.isCustomized());

		List<ApplicationFormEntity> content = queryFactory
				.selectFrom(applicationFormEntity)
				.where(titleContains, useYnEquals, customYnEquals)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long total = queryFactory
				.select(applicationFormEntity.count())
				.from(applicationFormEntity)
				.where(titleContains, useYnEquals, customYnEquals)
				.fetchOne();

		List<ApplicationForm> domainList = content.stream()
				.map(ApplicationFormMapper::toApplicationForm)
				.toList();

		return new PageImpl<>(domainList, pageable, total != null ? total : 0);
	}

	@Override
	public Page<ApplicationForm> listApplicationFormWithoutItem(SearchApplicationFormCommand searchApplicationFormCommand) {

		BooleanExpression titleContains = titleCond(searchApplicationFormCommand.title());
		BooleanExpression useYnEquals = useYnCond(searchApplicationFormCommand.isUsed());
		BooleanExpression customYnEquals = customYnCond(searchApplicationFormCommand.isCustomized());

		List<ApplicationFormEntity> applicationFormEntities = queryFactory
				.selectFrom(applicationFormEntity)
				.where(titleContains, useYnEquals, customYnEquals)
				.fetch();

		List<ApplicationForm> content = applicationFormEntities.stream()
				.map(ApplicationFormMapper::toApplicationForm)
				.toList();

		return new PageImpl<>(content);
	}

	@Override
	public List<ApplicationForm> listApplicationFormsWithItems(SearchApplicationFormCommand searchApplicationFormCommand) {
		BooleanExpression titleContains = titleCond(searchApplicationFormCommand.title());
		BooleanExpression useYnEquals = useYnCond(searchApplicationFormCommand.isUsed());
		BooleanExpression customYnEquals = customYnCond(searchApplicationFormCommand.isCustomized());

		List<ApplicationFormEntity> formEntities = queryFactory
				.selectFrom(applicationFormEntity)
				.where(titleContains, useYnEquals, customYnEquals)
				.fetch();

		List<String> formGuids = formEntities.stream()
				.map(ApplicationFormEntity::getApplicationFormGuid)
				.toList();

		Map<String, List<String>> itemsByFormGuid = jpaApplicationFormItemRepository.findByFormGuidIn(formGuids)
				.stream()
				.collect(Collectors.groupingBy(
						ApplicationFormItemEntity::getFormGuid,
						Collectors.mapping(ApplicationFormItemEntity::getContent, Collectors.toList())
				));

		return formEntities.stream()
				.map(entity -> ApplicationForm.builder()
						.applicationFormGuid(entity.getApplicationFormGuid())
						.typeCd(entity.getTypeCd())
						.title(entity.getTitle())
						.helpText(entity.getHelpText())
						.isCustomized(entity.isCustomized())
						.isUsed(entity.isUsed())
						.items(itemsByFormGuid.getOrDefault(entity.getApplicationFormGuid(), List.of()))
						.build())
				.toList();
	}

	private BooleanExpression titleCond(String searchTitle) {
		if (searchTitle == null || searchTitle.isBlank()) return null;
		return applicationFormEntity.title.contains(searchTitle);
	}

	private BooleanExpression useYnCond(Boolean isUsed) {
		if (isUsed == null) return null;
		return applicationFormEntity.isUsed.eq(isUsed);
	}

	private BooleanExpression customYnCond(Boolean isCustomized) {
		if (isCustomized == null) return null;
		return applicationFormEntity.isCustomized.eq(isCustomized);
	}
}
