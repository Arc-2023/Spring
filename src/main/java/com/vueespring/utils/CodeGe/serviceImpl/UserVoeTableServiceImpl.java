package serviceImpl;

import entity.UserVoeTable;
import mapper.UserVoeTableMapper;
import service.IUserVoeTableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-28
 */
@Service
public class UserVoeTableServiceImpl extends ServiceImpl<UserVoeTableMapper, UserVoeTable> implements IUserVoeTableService {

}
