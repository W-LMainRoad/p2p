package com.zc.p2p.service.loan;

import com.zc.p2p.common.constant.Constants;
import com.zc.p2p.mapper.loan.BidInfoMapper;
import com.zc.p2p.mapper.loan.LoanInfoMapper;
import com.zc.p2p.mapper.user.FinanceAccountMapper;
import com.zc.p2p.model.loan.BidInfo;
import com.zc.p2p.model.loan.LoanInfo;
import com.zc.p2p.model.vo.BidUserTop;
import com.zc.p2p.model.vo.PageinatinoVO;
import com.zc.p2p.model.vo.ResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * ClassName:BidInfoServiceImpl
 * Package:com.zc.p2p.service.loan
 * Description:
 *
 * @date:2019/10/29 18:58
 * @author:youxiang@163.com
 */
@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService{
    @Autowired
    private BidInfoMapper bidInfoMapper;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private LoanInfoMapper loanInfoMapper;

    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    @Override
    public Double queryAllBidMoney() {

        //获取指定key的操作对象
        BoundValueOperations<Object, Object> boundValueOps = redisTemplate.boundValueOps(Constants.ALL_BID_MONEY);
        //获取指定key的value值
        Double allBidMoney = (Double) boundValueOps.get();

        //判断是否有值
        if (null == allBidMoney) {
            //取数据库查找该值
            allBidMoney = bidInfoMapper.selectAllBidMoney();

            boundValueOps.set(allBidMoney,15, TimeUnit.MINUTES);
        }

        return allBidMoney;
    }

    @Override
    public List<BidInfo> queryBidInfoListByLoanId(Integer loanId) {

        return bidInfoMapper.selectBidInfoListByLoanId(loanId);
    }

    @Override
    public ResultObject invest(Map<String, Object> paramMap) {
        ResultObject resultObject = new ResultObject();
        resultObject.setErrorCode(Constants.SUCCESS);

        //超卖现象，实际销售的数量超过了原有销售数量
        //使用数据库乐观锁解决超卖现象

        //获取产品的版本号
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey((Integer) paramMap.get("loanId"));
        paramMap.put("version",loanInfo.getVersion());

        //更新产品剩余可投金额
        int updateLeftProductMoneyCount = loanInfoMapper.updateLeftProductMoneyByLoanId(paramMap);
        if (updateLeftProductMoneyCount > 0) {

            //更新账户可用余额
            int updateFinanceAcount = financeAccountMapper.updateFinanceAccountByUid(paramMap);
            if (updateFinanceAcount>0){
                //新增投资记录
                BidInfo bidInfo = new BidInfo();
                bidInfo.setUid((Integer) paramMap.get("uid"));
                bidInfo.setLoanId((Integer) paramMap.get("loanId"));
                bidInfo.setBidMoney((Integer)paramMap.get("bidMoney") + 0.0);
                bidInfo.setBidTime(new Date());
                bidInfo.setBidStatus(1);
                int insertBidCount = bidInfoMapper.insertSelective(bidInfo);

                if (insertBidCount>0) {
                    //再次查看产品的剩余可投金额是否为0

                        //为0，更新产品的状态及满标时间
                    LoanInfo loanDetail = loanInfoMapper.selectByPrimaryKey((Integer) paramMap.get("loanId"));
                    if (0 == loanDetail.getLeftProductMoney()) {
                        //更新产品的状态和满标时间
                        LoanInfo updateLoanIfo = new LoanInfo();
                        updateLoanIfo.setId(loanDetail.getId());
                        updateLoanIfo.setProductFullTime(new Date());
                        updateLoanIfo.setProductStatus(1);

                        int updateLoanInfoCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanIfo);
                        if (updateLoanInfoCount < 0) {
                            resultObject.setErrorCode(Constants.FAIL);
                        }
                    }

                    //将用户的投资金额存放到redis缓存中
                    redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,paramMap.get("phone"),(Integer)paramMap.get("bidMoney"));

                } else {
                    resultObject.setErrorCode(Constants.FAIL);
                }

            }else {
                resultObject.setErrorCode(Constants.FAIL);
            }

        } else {
            resultObject.setErrorCode(Constants.FAIL);
        }

        return resultObject;
    }

    @Override
    public List<BidUserTop> queryBidUserTop() {
        List<BidUserTop> bidUserTopList = new ArrayList<>();

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 9);

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();

        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String phone = (String) next.getValue();
            Double score = next.getScore();
            BidUserTop bidUserTop = new BidUserTop();
            bidUserTop.setPhone(phone);
            bidUserTop.setScore(score);

            bidUserTopList.add(bidUserTop);
        }

        return bidUserTopList;
    }

    @Override
    public List<BidInfo> queryInvestListByUid(Map<String, Object> paramMap) {

        return bidInfoMapper.selectInvestListByUid(paramMap);
    }

    @Override
    public PageinatinoVO<BidInfo> queryBidInfoByPage(Map<String, Object> paramMap) {
        PageinatinoVO<BidInfo> pageinatinoVO = new PageinatinoVO<>();

        Long total = bidInfoMapper.selectTotal(paramMap);
        pageinatinoVO.setTotal(total);

        List<BidInfo> bidInfoList = bidInfoMapper.selectInvestListByUid(paramMap);
        pageinatinoVO.setDataList(bidInfoList);

        return pageinatinoVO;
    }

}
