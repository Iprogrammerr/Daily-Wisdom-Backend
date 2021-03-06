package control.self.igor.dailywisdom.service.implementation;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import control.self.igor.dailywisdom.entity.Category;
import control.self.igor.dailywisdom.repository.CategoryRepository;
import control.self.igor.dailywisdom.service.abstraction.AbstractCrudServiceTest;
import control.self.igor.dailywisdom.service.crud.CrudService;
import control.self.igor.dailywisdom.service.crud.CategoryCrudService;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CategoryCrudServiceTest extends AbstractCrudServiceTest<Category> {

    @TestConfiguration
    static class CategoryCrudServiceTestConfiguration {

	@Autowired
	private CategoryRepository categoryRepository;

	@Bean
	public CrudService<Category> crudService() {
	    return new CategoryCrudService(categoryRepository);
	}

    }

    public CategoryCrudServiceTest() {
	super(Category.class);
    }

}
