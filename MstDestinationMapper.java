package jp.co.internous.grapes.model.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.internous.grapes.model.domain.MstDestination;

@Mapper
public interface MstDestinationMapper {
	
	@Update("UPDATE mst_destination SET status = 0, updated_at = now() WHERE id = #{id}")
	int updateStatus(@Param("id") int id);
	
	@Insert("INSERT INTO mst_destination("
			+ "user_id, family_name, first_name, tel_number, address"
			+ ")"
			+ "VALUES(#{userId}, #{familyName}, #{firstName}, #{telNumber}, #{address})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	int insert(MstDestination mstDstination);
	
	@Select("SELECT * FROM mst_destination WHERE user_id = #{userId} AND status = 1 ORDER BY id ASC")
	List<MstDestination> findByUserId(@Param("userId") int userId);
	
	@Select("SELECT id FROM mst_destination WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT 0, 1")
	int findIdByUserId(@Param("userId") int userId);
	
}
