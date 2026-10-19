package org.example.myspringboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myspringboot.entity.MediCatalogInfo;
import org.example.myspringboot.entity.MediDrugInfo;
import org.example.myspringboot.mapper.MedicineDrugInfoMapper;
import org.example.myspringboot.mapper.MedicineInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 功能：Medi_DrugInfo表,药品信息
 * 作者：yangjia
 * 日期：2025/3/4 下午3:14
 */
@Service
public class MedicineDrugInfoImpl extends ServiceImpl<MedicineDrugInfoMapper, MediDrugInfo> {
}
