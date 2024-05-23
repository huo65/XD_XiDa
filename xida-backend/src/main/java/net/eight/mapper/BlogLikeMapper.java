package net.eight.mapper;

import net.eight.model.domain.BlogLike;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author  
* @description 针对表【blog_like】的数据库操作Mapper
* @createDate 2023-06-05 21:54:55
* @Entity net.eight.model.domain.BlogLike
*/
@Mapper
public interface BlogLikeMapper extends BaseMapper<BlogLike> {

}




