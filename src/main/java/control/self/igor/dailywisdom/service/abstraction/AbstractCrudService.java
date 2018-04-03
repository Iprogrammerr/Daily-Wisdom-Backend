package control.self.igor.dailywisdom.service.abstraction;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.PagingAndSortingRepository;

import control.self.igor.dailywisdom.entity.Identifiable;

public abstract class AbstractCrudService<Entity extends Identifiable> {

    protected static final int DEFAULT_PAGE_SIZE = 50;

    public List<Entity> getEntities(PagingAndSortingRepository<Entity, Long> repository) {
	return (List<Entity>) repository.findAll();
    }

    public List<Entity> getEntities(PagingAndSortingRepository<Entity, Long> repository, Integer page, Integer size) {
	Sort sort = new Sort(Direction.ASC, "id");
	if (page == null || page < 1) {
	    return (List<Entity>) repository.findAll(sort);
	}

	if (size == null || size < 1) {
	    size = DEFAULT_PAGE_SIZE;
	}
	return repository.findAll(PageRequest.of(page - 1, size, sort)).getContent();
    }

    public long countEntities(PagingAndSortingRepository<Entity, Long> repository) {
	return repository.count();
    }

    public Entity getEntity(PagingAndSortingRepository<Entity, Long> repository, long id) {
	Optional<Entity> entity = repository.findById(id);
	return entity.get();
    }

    public long createEntity(PagingAndSortingRepository<Entity, Long> repository, Entity entity) {
	return repository.save(entity).getId();
    }

    public boolean updateEntity(PagingAndSortingRepository<Entity, Long> repository, Entity entity) {
	if (!repository.existsById(entity.getId())) {
	    return false;
	}
	return createEntity(repository, entity) > 0;
    }

    public void deleteEntity(PagingAndSortingRepository<Entity, Long> repository, long id) {
	repository.deleteById(id);
    }

    public boolean exists(PagingAndSortingRepository<Entity, Long> repository, long id) {
	return repository.existsById(id);
    }

}
