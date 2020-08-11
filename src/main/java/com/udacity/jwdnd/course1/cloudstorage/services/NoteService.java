package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {
    private Logger logger = LoggerFactory.getLogger(NoteService.class);
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void saveNote(NoteForm noteForm) throws Exception {
        if (noteForm.getNoteId() == null) {
            logger.info("creating a new note");
            //new note
            addNote(noteForm);
        } else {
            logger.info("updating an existing note");
            //existing note
            updateNote(noteForm);
        }
    }

    public void addNote(NoteForm noteForm) throws Exception {
        try {
            noteMapper.insert(
                    new Note(
                            noteForm.getNoteTitle(),
                            noteForm.getNoteDescription(),
                            noteForm.getUserId()
                    )
            );
        } catch (Exception ex) {
            throw new Exception("There was an error adding your Note. Please try again.");
        }
    }

    public void updateNote(NoteForm noteForm) throws Exception {
        Note note = noteMapper.getNote(noteForm.getNoteId());
        Optional.ofNullable(note).map(currentNote -> {
            currentNote.setNoteTitle(noteForm.getNoteTitle());
            currentNote.setNoteDescription(noteForm.getNoteDescription());
            return noteMapper.update(note);

        }).orElseThrow(() -> new Exception("There was an error updating your Note. Please try again."));
    }


    public List<Note> getNotesForUser(Integer userId) {
        return noteMapper.getNotesForUser(userId);
    }


    public int deleteNote(NoteForm noteForm) {
        return noteMapper.delete(noteForm.getNoteId());
    }
}
