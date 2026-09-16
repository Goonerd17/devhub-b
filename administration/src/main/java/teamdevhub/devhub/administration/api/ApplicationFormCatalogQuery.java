package teamdevhub.devhub.administration.api;

public interface ApplicationFormCatalogQuery {
    ApplicationFormPage search(ApplicationFormSearch search, int page, int size);
}
