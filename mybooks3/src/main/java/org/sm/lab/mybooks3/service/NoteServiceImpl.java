package org.sm.lab.mybooks3.service;

import org.sm.lab.mybooks3.domain.Book;
import org.sm.lab.mybooks3.domain.Note;
import org.sm.lab.mybooks3.repository.BookRepository;
import org.sm.lab.mybooks3.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

	@Autowired
    NoteRepository noteRepository;
	
	@Autowired
    BookRepository bookRepository;
	
	@Override
	@PreAuthorize("@authorizationService.canAccessNote(principal, #id)")
	public Note findNote(String id) {
        return noteRepository.findOne(id);
    }
	
	@Override
	@PreAuthorize("@authorizationService.canAccessBook(principal, #bookId)")
	public Note saveNote(String bookId, Note note) {
		Book book = bookRepository.findOne(bookId);
		note.setBook(book);
        return noteRepository.save(note);
    }

	@Override
	@PreAuthorize("@authorizationService.canAccessNote(principal, #id)")
	public void deleteNote(String id) {
        noteRepository.delete(id);
    }
	
}