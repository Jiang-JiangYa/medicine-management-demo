package org.example.myspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myspringboot.entity.MediRefundingBillDetail;
import org.example.myspringboot.entity.MediRefundingBillMaster;

@Mapper
public interface MediRefundingBillMasterMapper extends BaseMapper<MediRefundingBillMaster> {
}
