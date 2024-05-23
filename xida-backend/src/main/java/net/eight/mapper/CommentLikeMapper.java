package net.eight.mapper;

import net.eight.model.domain.CommentLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author  
* @description 针对表【comment_like】的数据库操作Mapper
* @createDate 2023-06-08 16:24:28
* @Entity net.eight.model.domain.CommentLike
*/
@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {

}




