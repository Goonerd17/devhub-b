package teamdevhub.devhub.admin.api;

public interface ApplicationFormCatalogQuery {
    ApplicationFormPage search(ApplicationFormSearch search, int page, int size);
}
