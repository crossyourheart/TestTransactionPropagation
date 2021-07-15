package com.dawei.transaction.mapper;

import com.dawei.transaction.pojo.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author da wei
 */
@Mapper
public interface StudentDao {
    @Insert("INSERT into student (name) values ( #{name} )")
    int insert(Student student);
    @Select("select * from student where id = #{id}")
    Student selectByPrimaryKey(Integer id);
}
