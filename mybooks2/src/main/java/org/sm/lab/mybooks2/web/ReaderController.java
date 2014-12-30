package org.sm.lab.mybooks2.web;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.sm.lab.mybooks2.domain.Reader;
import org.sm.lab.mybooks2.enums.SystemRole;
import org.sm.lab.mybooks2.service.BookService;
import org.sm.lab.mybooks2.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/readers")
@Controller
public class ReaderController {

	@Autowired
    ReaderService readerService;

	@Autowired
    BookService bookService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Reader reader, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, reader);
            return "readers/create";
        }
        uiModel.asMap().clear();
        readerService.saveReader(reader);
        return "redirect:/readers/" + encodeUrlPathSegment(reader.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Reader());
        return "readers/create";
    }
	
	@RequestMapping(params = "register", method = RequestMethod.POST, produces = "text/html")
    public String register(@Valid Reader reader, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, reader);
            return "readers/register";
        }
        uiModel.asMap().clear();
        reader.setSystemRole(SystemRole.Common);
        readerService.saveReader(reader);
        return "redirect:/readers/" + encodeUrlPathSegment(reader.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "register", produces = "text/html")
    public String registerForm(Model uiModel) {
        populateEditForm(uiModel, new Reader());
        return "readers/register";
    }		

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") String id, Model uiModel) {
        uiModel.addAttribute("reader", readerService.findReader(id));
        uiModel.addAttribute("itemId", id);
        return "readers/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("readers", readerService.findReaderEntries(firstResult, sizeNo));
            float nrOfPages = (float) readerService.countAllReaders() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("readers", readerService.findAllReaders());
        }
        return "readers/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Reader reader, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, reader);
            return "readers/update";
        }
        uiModel.asMap().clear();
        readerService.updateReader(reader);
        return "redirect:/readers/" + encodeUrlPathSegment(reader.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") String id, Model uiModel) {
        populateEditForm(uiModel, readerService.findReader(id));
        return "readers/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") String id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Reader reader = readerService.findReader(id);
        readerService.deleteReader(reader);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/readers";
    }

	void populateEditForm(Model uiModel, Reader reader) {
        uiModel.addAttribute("reader", reader);
        uiModel.addAttribute("books", bookService.findAllBooks());
        uiModel.addAttribute("systemroles", Arrays.asList(SystemRole.values()));
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
