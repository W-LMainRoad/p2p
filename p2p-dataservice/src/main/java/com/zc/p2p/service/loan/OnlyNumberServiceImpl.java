package com.zc.p2p.service.loan;

import com.zc.p2p.common.constant.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * ClassName:OnlyNumberServiceImpl
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/11/12 14:19
 * @author:youxiang@163.com
 */
@Service("onlyNumberServiceImpl")
public class OnlyNumberServiceImpl implements OnlyNumberService{

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Long getOnylNumber() {
        return redisTemplate.opsForValue().increment(Constants.ONLY_NUMBER,1);
    }
}
