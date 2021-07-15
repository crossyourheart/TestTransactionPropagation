package com.dawei.transaction.mapper;

import com.dawei.transaction.pojo.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author da wei
 */
@Mapper
public interface TeacherDao {
    @Insert("INSERT into teacher (name) values ( #{name} )")
    int insert(Teacher teacher);
    @Select("select * from teacher where id = #{id}")
    Teacher selectByPrimaryKey(Integer id);
}
