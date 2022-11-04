package serviceImpl;

import entity.Notecard;
import mapper.NotecardMapper;
import service.INotecardService;
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
public class NotecardServiceImpl extends ServiceImpl<NotecardMapper, Notecard> implements INotecardService {

}
