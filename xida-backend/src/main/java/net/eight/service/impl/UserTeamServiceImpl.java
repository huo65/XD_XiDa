package net.eight.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.eight.mapper.UserTeamMapper;
import net.eight.model.domain.UserTeam;
import net.eight.service.UserTeamService;
import org.springframework.stereotype.Service;

/**
 * @author
 * @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
 * @createDate 2023-05-14 11:45:06
 */
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
        implements UserTeamService {

}




