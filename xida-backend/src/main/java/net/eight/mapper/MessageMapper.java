package net.eight.mapper;

import net.eight.model.domain.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author
* @description 针对表【message】的数据库操作Mapper
* @createDate 2023-06-21 17:39:30
* @Entity net.eight.model.domain.Message
*/
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




