package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File geFileById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE fileName = #{fileName} and userId = #{userId}")
    File geFileByNameAndUserId(String fileName, Integer userId);


    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getFilesForUser(Integer userId);

    @Insert("INSERT INTO FILES (fileName, contentType, userId, fileData) " +
            "VALUES(#{fileName}, #{contentType}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    @Update("Update FILES set fileName=#{fileName}, contentType=#{contentType},  fileData=#{fileData}  where fileId=#{fileId}")
    int update(File file);

    @Delete("DELETE FROM FILES where fileId = #{fileId}")
    int delete(@Param("fileId") Integer fileId);
}
