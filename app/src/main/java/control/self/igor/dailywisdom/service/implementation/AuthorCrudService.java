package control.self.igor.dailywisdom.service.implementation;

import java.util.NoSuchElementException;
import java.util.logging.Level;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import control.self.igor.dailywisdom.entity.Author;
import control.self.igor.dailywisdom.entity.AuthorDescription;
import control.self.igor.dailywisdom.repository.abstraction.AuthorDescriptionRepository;
import control.self.igor.dailywisdom.repository.abstraction.AuthorRepository;
import control.self.igor.dailywisdom.service.abstraction.AbstractAuthorCrudService;

@Service
@Transactional
public class AuthorCrudService extends AbstractAuthorCrudService {

    private AuthorDescriptionRepository authorDescriptionRepository;

    @Autowired
    public AuthorCrudService(AuthorRepository authorRepository,
	    AuthorDescriptionRepository authorDescriptionRepository) {
	super(authorRepository);
	this.authorDescriptionRepository = authorDescriptionRepository;
    }

    @Override
    public AuthorDescription getAuthorDescription(long id) {
	return authorDescriptionRepository.findByAuthorId(id);
    }

    @Override
    public String getImagePath(long id) {
	return getAuthorRepository().getAuthorImagePath(id);
    }

    @Override
    public boolean saveAuthorDescription(long id, AuthorDescription authorDescription) {
	try {
	    Author author = getAuthorRepository().findById(id).get();
	    AuthorDescription previousAuthorDescription = author.getAuthorDescription();
	    if (previousAuthorDescription != null) {
		authorDescription.setId(previousAuthorDescription.getId());
	    }
	    authorDescription.setAuthor(author);
	    authorDescriptionRepository.save(authorDescription);
	    return true;
	} catch (NoSuchElementException exception) {
	    LOGGER.log(Level.WARNING, exception.toString(), exception);
	    return false;
	}
    }

    @Override
    public boolean saveImagePath(long id, String imagePath) {
	return getAuthorRepository().updateImagePath(id, imagePath) > 0;
    }

}
