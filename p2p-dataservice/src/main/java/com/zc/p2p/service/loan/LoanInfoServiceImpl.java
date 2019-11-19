package com.zc.p2p.service.loan;

import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.mapper.loan.LoanInfoMapper;
import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.model.vo.PageinatinoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:LoanInfoServiceImpl
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/10/28 20:00
 * @author:youxiang@163.com
 */
@Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements  LoanInfoService{

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Override
    public Double queryHistoryAverageRate() {

        //先去redis缓存中获取该值，有的话直接使用，没有去数据库中查询并存放到redis缓存中

        //修改key值的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //获取操作redis中key=value的数据的reids的操作对象,并获取指定key的value值
        Double historyAverageRate = (Double) redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);

        //判断是否有值
        if (null == historyAverageRate) {
            //没有值去数据库查询
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();

            historyAverageRate = 1.0;

            //将该值存放到redis缓存中
            redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE,historyAverageRate,15, TimeUnit.MINUTES);
        }


        return historyAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {

        return loanInfoMapper.selectLoanInfoByPage(paramMap);
    }

    @Override
    public PageinatinoVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap) {
        PageinatinoVO<LoanInfo> pageinatinoVO = new PageinatinoVO<>();

        Long total = loanInfoMapper.selectTotal(paramMap);

        //查询总记录数
        pageinatinoVO.setTotal(total);

        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByPage(paramMap);
        //查询显示数据
        pageinatinoVO.setDataList(loanInfoList);
        return pageinatinoVO;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {
        return loanInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByStatus(int status) {

        return loanInfoMapper.selectLoanInfoByStatus(status);
    }


}
