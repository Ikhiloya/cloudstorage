package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNote(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getNotesForUser(Integer userId);

    @Insert("INSERT INTO NOTES (noteTitle, noteDescription, " +
            "userId) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note note);


    @Update("Update NOTES set noteTitle=#{noteTitle}, " +
            " noteDescription=#{noteDescription} where noteId=#{noteId}")
    int update(Note note);

    @Delete("DELETE FROM NOTES where noteId = #{noteId}")
    int delete(@Param("noteId") Integer noteId);
}
