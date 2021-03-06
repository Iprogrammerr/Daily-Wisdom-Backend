package control.self.igor.dailywisdom.controller.frontend.data;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonView;

import control.self.igor.dailywisdom.entity.Quote;
import control.self.igor.dailywisdom.entity.QuoteOwner;
import control.self.igor.dailywisdom.entity.UserRole.Role;
import control.self.igor.dailywisdom.exception.BadRequestException;
import control.self.igor.dailywisdom.exception.ForbiddenException;
import control.self.igor.dailywisdom.exception.NotFoundException;
import control.self.igor.dailywisdom.exception.WrongDataException;
import control.self.igor.dailywisdom.json.View;
import control.self.igor.dailywisdom.model.EntityCounter;
import control.self.igor.dailywisdom.model.Response;
import control.self.igor.dailywisdom.service.crud.EntityQuoteCrudService;
import control.self.igor.dailywisdom.service.validation.ValidationService;

public abstract class EntityQuoteCrudController<Entity extends QuoteOwner> {

    private static final Logger LOGGER = Logger.getLogger(EntityQuoteCrudController.class.getSimpleName());
    protected EntityQuoteCrudService<Entity> crudService;
    protected ValidationService validationService;

    public EntityQuoteCrudController(EntityQuoteCrudService<Entity> crudService,
	    ValidationService validationService) {
	this.crudService = crudService;
	this.validationService = validationService;
    }

    @JsonView(View.List.class)
    @GetMapping("/{id}/list")
    public List<Quote> getQuotes(@PathVariable("id") long id,
	    @RequestParam(value = "page", required = false) Integer page,
	    @RequestParam(value = "size", required = false) Integer size) {
	if (id > 0 && validationService.validatePageRequest(page, size)) {
	    return crudService.getQuotes(id, page, size);
	}
	throw new BadRequestException();
    }

    @GetMapping("/{id}/list/count")
    public EntityCounter countQuotes(@PathVariable("id") long id) {
	if (id < 1) {
	    throw new BadRequestException();
	}
	return new EntityCounter(crudService.countQuotes(id));
    }

    @GetMapping("/{id}/{quoteId}")
    @JsonView(View.Details.class)
    public Quote getQuote(@PathVariable("id") long id, @PathVariable("quoteId") long quoteId) {
	if (!validationService.validateIds(id, quoteId)) {
	    throw new BadRequestException();
	}
	Quote quote = crudService.getQuote(id, quoteId);
	if (quote == null) {
	    throw new NotFoundException();
	}
	return quote;
    }

    @PostMapping("/{id}")
    public Response createQuote(@PathVariable("id") long id, @Valid @RequestBody Quote quote) {
	if (id < 1) {
	    throw new WrongDataException();
	}
	validateRole();
	boolean quoteCreated;
	try {
	    quoteCreated = crudService.createQuote(id, quote) > 0;
	} catch (DataIntegrityViolationException exception) {
	    exception.printStackTrace();
	    quoteCreated = false;
	}
	if (!quoteCreated) {
	    throw new BadRequestException();
	}
	return new Response(Response.OK);
    }

    @PutMapping("/{id}/{quoteId}")
    public Response updateQuote(@PathVariable("id") long id, @PathVariable("quoteId") long quoteId,
	    @Valid @RequestBody Quote quote) {
	if (!validationService.validateIds(id, quoteId)) {
	    throw new WrongDataException();
	}
	validateRole();
	boolean updated = crudService.updateQuote(id, quote);
	if (!updated) {
	    throw new NotFoundException();
	}
	return new Response(Response.OK);
    }

    @DeleteMapping("/{id}/{quoteId}")
    public Response deleteQuote(@PathVariable("id") long id, @PathVariable("quoteId") long quoteId) {
	validateRole();
	boolean wrongData;
	try {
	    wrongData = !validationService.validateIds(id, quoteId) || !crudService.deleteQuote(id, quoteId);
	} catch (NoSuchElementException exception) {
	    LOGGER.log(Level.WARNING, exception.toString(), exception);
	    wrongData = true;
	}
	if (wrongData) {
	    throw new NotFoundException();
	}
	return new Response(Response.OK);
    }

    protected void validateRole() {
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	if (!validationService.validateRole(authentication, Role.ADMIN)) {
	    throw ForbiddenException.createAdminOnlyException();
	}
    }

}
