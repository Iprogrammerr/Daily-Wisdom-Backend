package control.self.igor.dailywisdom.controller.frontend.implementation;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import control.self.igor.dailywisdom.controller.frontend.abstraction.AbstractCrudAndSearchController;
import control.self.igor.dailywisdom.entity.Category;
import control.self.igor.dailywisdom.model.search.SearchByNameCriteria;
import control.self.igor.dailywisdom.service.abstraction.AbstractCrudService;
import control.self.igor.dailywisdom.service.abstraction.SearchService;
import control.self.igor.dailywisdom.service.abstraction.ValidationService;

@RestController
@RequestMapping("/category")
public class CategoryController extends AbstractCrudAndSearchController<Category, SearchByNameCriteria> {

    public static final Logger LOGGER = Logger.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(AbstractCrudService<Category> crudService,
	    SearchService<Category, SearchByNameCriteria> searchService, ValidationService validationService) {
	super(crudService, searchService, validationService);
    }

}