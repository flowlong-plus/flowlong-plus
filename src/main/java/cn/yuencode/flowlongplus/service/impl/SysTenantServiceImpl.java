package cn.yuencode.flowlongplus.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.yuencode.flowlongplus.assist.Assert;
import cn.yuencode.flowlongplus.config.mybatis.ApiContext;
import cn.yuencode.flowlongplus.controller.req.PageSysTenantReq;
import cn.yuencode.flowlongplus.controller.req.SysTenantReq;
import cn.yuencode.flowlongplus.controller.res.SysTenantListRes;
import cn.yuencode.flowlongplus.entity.SysTenant;
import cn.yuencode.flowlongplus.mapper.SysTenantMapper;
import cn.yuencode.flowlongplus.service.SysTenantService;
import cn.yuencode.flowlongplus.util.page.PageResult;
import cn.yuencode.flowlongplus.util.page.PageResultConvert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 租户信息 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {
    @Resource
    private ApiContext apiContext;

    @Override
    public Object options() {
        List<SysTenant> tenantList = baseMapper.getOptions();

        List<SysTenantListRes> list = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(tenantList)) {
            for (SysTenant sysTenant : tenantList) {
                SysTenantListRes sysTenantListRes = new SysTenantListRes();
                sysTenantListRes.setTenantCode(sysTenant.getTenantCode());
                sysTenantListRes.setTenantName(sysTenant.getTenantName());
                sysTenantListRes.setTenantId(sysTenant.getId());
                list.add(sysTenantListRes);
            }
        }
        return list;
    }

    @Override
    public void updateTenant(SysTenantReq sysTenantReq) {
        SysTenant sysTenant = new SysTenant();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sysTenant.setTenantCode(sysTenantReq.getTenantCode());
        sysTenant.setTenantName(sysTenantReq.getTenantName());
        sysTenant.setBeginDate(LocalDateTime.parse(sysTenantReq.getBeginDate(), formatter));
        sysTenant.setEndDate(LocalDateTime.parse(sysTenantReq.getEndDate(), formatter));
        sysTenant.setStatus(sysTenantReq.getStatus());
        baseMapper.updateById(sysTenant);
    }

    @Override
    public PageResult<SysTenant> getList(PageSysTenantReq pageSysTenantReq) {
        QueryWrapper<SysTenant> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotEmpty(pageSysTenantReq.getTenantName())) {
            queryWrapper.like("tenant_name", pageSysTenantReq.getTenantName());
        }
        if (StrUtil.isNotEmpty(pageSysTenantReq.getTenantCode())) {
            queryWrapper.like("tenant_code", pageSysTenantReq.getTenantCode());
        }
        if (ObjectUtil.isNotEmpty(pageSysTenantReq.getStatus())) {
            queryWrapper.eq("status", pageSysTenantReq.getStatus());
        }

        IPage<SysTenant> sysTenantPage = baseMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                        pageSysTenantReq.getPageNum(), pageSysTenantReq.getPageSize()),
                queryWrapper);
        return PageResultConvert.convertMybatisPlus(sysTenantPage);
    }

    @Override
    public void deleteIds(String[] ids) {
        for (String id : ids) {
            Assert.isTrue(id.equals(apiContext.getCurrentTenantId()), "不能操作自己");
        }
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void addTenant(SysTenantReq sysTenantReq) {
        SysTenant sysTenant = new SysTenant();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        sysTenant.setTenantCode(sysTenantReq.getTenantCode());
        sysTenant.setTenantName(sysTenantReq.getTenantName());
        sysTenant.setBeginDate(LocalDateTime.parse(sysTenantReq.getBeginDate(), formatter));
        sysTenant.setEndDate(LocalDateTime.parse(sysTenantReq.getEndDate(), formatter));
        sysTenant.setStatus(sysTenantReq.getStatus());
        baseMapper.insert(sysTenant);
    }

    @Override
    public Object getInfo(String tenantId) {
        SysTenant sysTenant = baseMapper.selectById(tenantId);
        Assert.isNull(sysTenant, "未知租户");
        return sysTenant;
    }
}
