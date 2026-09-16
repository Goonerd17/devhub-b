package teamdevhub.devhub.administration.core.admin.form.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationFormItem;
import teamdevhub.devhub.administration.core.admin.form.port.in.command.ApplicationFormCommand;
import teamdevhub.devhub.administration.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.administration.core.admin.form.port.in.command.SaveApplicationFormCommand;
import teamdevhub.devhub.administration.core.admin.form.port.in.usecase.ApplicationFormUseCase;
import teamdevhub.devhub.administration.core.admin.form.port.out.ApplicationFormItemRepository;
import teamdevhub.devhub.administration.core.admin.form.port.out.ApplicationFormRepository;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.administration.api.ApplicationFormDeletion;
import teamdevhub.devhub.administration.api.ApplicationFormCreation;
import teamdevhub.devhub.administration.api.ApplicationFormDefinition;
import teamdevhub.devhub.administration.api.ApplicationFormQuery;
import teamdevhub.devhub.administration.api.ApplicationFormView;
import teamdevhub.devhub.administration.api.ApplicationFormCatalogQuery;
import teamdevhub.devhub.administration.api.ApplicationFormPage;
import teamdevhub.devhub.administration.api.ApplicationFormSearch;
import teamdevhub.devhub.administration.core.admin.form.port.in.usecase.ApplicationFormQueryUseCase;
import teamdevhub.devhub.administration.api.form.SearchApplicationFormCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

@Service
@Transactional
public class ApplicationFormService implements ApplicationFormUseCase, ApplicationFormDeletion, ApplicationFormCreation, ApplicationFormQuery, ApplicationFormCatalogQuery {

    private final ApplicationFormQueryUseCase applicationFormQueryUseCase;

    @Autowired
    public ApplicationFormService(ApplicationFormQueryUseCase applicationFormQueryUseCase,
                                  IdentifierProvider identifierProvider,
                                  ApplicationFormRepository applicationFormRepository,
                                  ApplicationFormItemRepository applicationFormItemRepository) {
        this.applicationFormQueryUseCase = applicationFormQueryUseCase;
        this.identifierProvider = identifierProvider;
        this.applicationFormRepository = applicationFormRepository;
        this.applicationFormItemRepository = applicationFormItemRepository;
    }

    /** 기존 단위 테스트와 내부 조립 코드의 생성 호환성을 유지한다. */
    public ApplicationFormService(IdentifierProvider identifierProvider,
                                  ApplicationFormRepository applicationFormRepository,
                                  ApplicationFormItemRepository applicationFormItemRepository) {
        this(null, identifierProvider, applicationFormRepository, applicationFormItemRepository);
    }

    @Override
    public ApplicationFormPage search(ApplicationFormSearch search, int page, int size) {
        PageResult<ApplicationForm> result = applicationFormQueryUseCase.getApplicationFormsWithoutItem(
                new SearchApplicationFormCommand(search.title(), search.used(), search.customized()));
        List<ApplicationFormView> views = result.content().stream()
                .map(form -> new ApplicationFormView(form.getApplicationFormGuid(), form.getTypeCd(), form.getTitle(),
                        form.getHelpText(), form.isCustomized(), form.isUsed(), List.of()))
                .toList();
        return new ApplicationFormPage(views, result.page(), result.size(), result.totalElements(), result.totalPages(), result.first(), result.last());
    }

    @Override
    public List<ApplicationFormView> findStandard(List<String> formGuids) {
        return getNoCustomizedFormById(formGuids).stream()
                .map(form -> new ApplicationFormView(form.getApplicationFormGuid(), form.getTypeCd(), form.getTitle(), form.getHelpText(), false, form.isUsed(), List.of()))
                .toList();
    }

    @Override
    public List<ApplicationFormView> findCustomized(List<String> formGuids) {
        return getCustomizedFormById(formGuids).stream()
                .map(form -> new ApplicationFormView(form.getApplicationFormGuid(), form.getTypeCd(), form.getTitle(), form.getHelpText(), true, form.isUsed(), form.getItemList()))
                .toList();
    }

    @Override
    public List<String> create(List<ApplicationFormDefinition> forms) {
        return saveApplicationForms(forms.stream().map(form -> CreateApplicationFormCommand.builder()
                .typeCd(form.typeCd()).title(form.title()).helpText(form.helpText()).itemList(form.itemList()).build()).toList());
    }

    private final IdentifierProvider identifierProvider;
    private final ApplicationFormRepository applicationFormRepository;
    private final ApplicationFormItemRepository applicationFormItemRepository;

	@Override
	public List<String> saveApplicationForms(List<CreateApplicationFormCommand> applitionalFormCommandList) {
		List<String> applicationFormGuids = new ArrayList<>();
		applitionalFormCommandList.stream()
				.forEach(applicationFormCommand -> {
				ApplicationForm applicationForm = createApplicationForm(applicationFormCommand);
				applicationFormRepository.save(applicationForm);
				if (applicationFormCommand.getItemList() != null && applicationFormCommand.getItemList().size() > 0) {
					saveApplcationFormItems(applicationForm.getApplicationFormGuid(), applicationFormCommand.getItemList());
				}
				applicationFormGuids.add(applicationForm.getApplicationFormGuid());
				});
		return applicationFormGuids;
	}

	@Override
	public void saveApplicationForm(SaveApplicationFormCommand command) {
		if (command.isInsert()) {
			String newGuid = identifierProvider.generateIdentifier();
			ApplicationForm form = ApplicationForm.createCustomApplicationForm(command, newGuid);
			applicationFormRepository.save(form);
			if (command.getItemList() != null && !command.getItemList().isEmpty()) {
				saveApplcationFormItems(form.getApplicationFormGuid(), command.getItemList());
			}
		} else {
			String guid = command.getApplicationFormGuid();
			ApplicationForm form = applicationFormRepository.findByApplicationFormGuid(guid);
			form.update(command.getTitle(), command.getHelpText(), command.isUsed(), !"Y".equals(command.getDefaultFieldYn()));
			applicationFormRepository.update(form);
			applicationFormItemRepository.deleteByApplicationFormGuid(List.of(guid));
			if (command.getItemList() != null && !command.getItemList().isEmpty()) {
				saveApplcationFormItems(guid, command.getItemList());
			}
		}
	}

	private ApplicationForm createApplicationForm(CreateApplicationFormCommand createApplicationFormCommand) {
		String applicationFormGuid = identifierProvider.generateIdentifier();
		return ApplicationForm.createCustomApplicationForm(createApplicationFormCommand, applicationFormGuid);
	}

	private void saveApplcationFormItems(String applicationFormGuid, List<String> itemList) {
		Set<ApplicationFormItem> items = itemList.stream()
				.map(item -> {
					String applicationFormItemGuid = identifierProvider.generateIdentifier();
					return ApplicationFormItem.createApplicationFormItem(applicationFormItemGuid, applicationFormGuid, item);
				})
				.collect(Collectors.toUnmodifiableSet());
		applicationFormItemRepository.saveAll(items);
	}

	@Override
	public void deleteApplicationForms(List<String> deleteApplicationFormGuids) {
		List<ApplicationForm> applicationFormList = applicationFormRepository.findByIdAndIsCustomized(deleteApplicationFormGuids);
		List<String> applicationFormGuids = applicationFormList.stream()
												.map(ApplicationForm::getApplicationFormGuid)
												.toList();
		applicationFormItemRepository.deleteByApplicationFormGuid(applicationFormGuids);
		applicationFormRepository.deleteByApplicationFormGuid(applicationFormGuids);
	}

	@Override
	public List<ApplicationForm> getNoCustomizedFormById(List<String> formList) {
		return applicationFormRepository.findByIdAndIsNotCustomized(formList);
	}

	@Override
	public List<ApplicationFormCommand> getCustomizedFormById(List<String> formList) {
		List<ApplicationForm> applicationFormList = applicationFormRepository.findByIdAndIsCustomized(formList);
		List<ApplicationFormCommand> commandList = applicationFormList.stream()
				.map(form -> {
					List<ApplicationFormItem> itemList = applicationFormItemRepository.findByFormGuid(form.getApplicationFormGuid());
					return ApplicationFormCommand.fromDomain(form, itemList);
					})
				.toList();
		return commandList;
	}

}
