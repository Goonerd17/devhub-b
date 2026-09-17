package teamdevhub.devhub.admin.core.admin.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.admin.core.admin.form.application.ApplicationFormService;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.fake.pure.application.port.out.admin.form.FakeApplicationFormItemRepository;
import teamdevhub.devhub.fake.pure.application.port.out.admin.form.FakeApplicationFormRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

public class ApplicationFormServiceTest {
	
	private ApplicationFormService applicationFormService;
	
	private FakeUuidIdentifierProvider identifierProvider;
	private FakeApplicationFormRepository applicationFormRepository;
	private FakeApplicationFormItemRepository applicationFormItemRepository;
	
	@BeforeEach
	void init() {
		identifierProvider = new FakeUuidIdentifierProvider("APPLICATION_UUID");
		applicationFormRepository = new FakeApplicationFormRepository();
		applicationFormItemRepository = new FakeApplicationFormItemRepository();
		
		applicationFormService = new ApplicationFormService(identifierProvider, applicationFormRepository, applicationFormItemRepository);
	}
	
	@Test
	@DisplayName("신청폼을 저장한다")
	void saveApplicationForms() {
		//given
		List<CreateApplicationFormCommand> input = List.of(
				CreateApplicationFormCommand.builder()
				.typeCd("selectbox")
				.title("성별")
				.helpText("성별을 선택하세요")
				.itemList(List.of("F", "M"))
				.build()
				);
		
		//when
		/**
		 * 리뷰
		 * result 값을 활용해서 값검증을 진행하면 더 좋을 것 같습니다
		 * ex) assertThat(result).containsExactly("F", "M");
		 */
		List<String> result = applicationFormService.saveApplicationForms(input);
		
		//then

		assertThat(applicationFormRepository.findByApplicationFormGuid("APPLICATION_UUID").getTypeCd()).isEqualTo("selectbox");
		assertThat(applicationFormRepository.findByApplicationFormGuid("APPLICATION_UUID").getTitle()).isEqualTo("성별");
		assertThat(applicationFormRepository.findByApplicationFormGuid("APPLICATION_UUID").getHelpText()).isEqualTo("성별을 선택하세요");
		assertThat(applicationFormItemRepository.findByFormGuid("APPLICATION_UUID").get(0).getFormItemGuid()).isEqualTo("APPLICATION_UUID");
	}

}
