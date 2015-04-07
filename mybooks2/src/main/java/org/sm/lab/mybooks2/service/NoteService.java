package org.sm.lab.mybooks2.service;
import java.util.List;

import org.sm.lab.mybooks2.domain.Book;
import org.sm.lab.mybooks2.domain.Note;

public interface NoteService {

	public abstract long countAllNotes();


	public abstract void deleteNote(Note note);


	public abstract Note findNote(Long id);


	public abstract List<Note> findAllNotes();


	public abstract List<Note> findNoteEntries(int firstResult, int maxResults);


	public abstract void saveNote(Note note);


	public abstract Note updateNote(Note note);
	
	public abstract List<Note> findByBook(Book book, int firstResult, int maxResults);
	
	public abstract List<Note> findByKeyword(String keyword, int firstResult, int maxResults);

	
}