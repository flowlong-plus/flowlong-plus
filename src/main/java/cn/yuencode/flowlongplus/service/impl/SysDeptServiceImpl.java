package cn.yuencode.flowlongplus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.assist.Assert;
import cn.yuencode.flowlongplus.controller.req.SysDeptPageParamReq;
import cn.yuencode.flowlongplus.controller.res.DeptRes;
import cn.yuencode.flowlongplus.entity.SysDept;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.mapper.SysDeptMapper;
import cn.yuencode.flowlongplus.mapper.SysUserMapper;
import cn.yuencode.flowlongplus.service.SysDeptService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 部门 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public Object getList(SysDeptPageParamReq params) {
        return baseMapper.selectPage(Page.of(params.getPageNum(), params.getPageSize()), Wrappers.<SysDept>lambdaQuery().like(ObjUtil.isNotEmpty(params.getDeptName()), SysDept::getDeptName, params.getDeptName()).orderByDesc(SysDept::getOrderNum).orderByDesc(SysDept::getCreateTime));
    }

    @Override
    public Object getDeptTree() {
        List<SysDept> sysDepts = baseMapper.selectList(Wrappers.<SysDept>lambdaQuery().orderByDesc(SysDept::getOrderNum).orderByDesc(SysDept::getCreateTime));

        List<DeptRes> deptResList = new ArrayList<>();
        for (SysDept sysDept : sysDepts) {
            DeptRes deptRes = BeanUtil.copyProperties(sysDept, DeptRes.class);
            deptResList.add(deptRes);
        }
        return getChildPerms(deptResList, "0");
    }

    @Override
    public void addDept(SysDept sysDept) {
        sysDept.setDeptId(null);
        if (ObjUtil.isNotNull(sysDept.getParent()) && !sysDept.getParent().equals("0")) {
            SysDept parent = baseMapper.selectById(sysDept.getParent());
            if (ObjUtil.isNull(parent)) {
                throw new RuntimeException("未知的父部门");
            }
            sysDept.setLevel(parent.getLevel() + 1);
        } else {
            sysDept.setLevel(1);
            sysDept.setParent("0");
        }
        baseMapper.insert(sysDept);
    }

    @Override
    public void updateDept(SysDept sysDept) {
        SysDept dept = baseMapper.selectById(sysDept.getDeptId());
        Assert.isNull(dept, "未知部门");
        if (ObjUtil.isNotNull(sysDept.getParent()) && !sysDept.getParent().equals("0")) {
            SysDept parent = baseMapper.selectById(sysDept.getParent());
            if (ObjUtil.isNull(parent)) {
                throw new RuntimeException("未知的父部门");
            }
            sysDept.setLevel(parent.getLevel() + 1);
        } else {
            sysDept.setLevel(1);
            sysDept.setParent("0");
        }
        baseMapper.updateById(sysDept);
    }

    @Override
    public void deleteDept(String[] ids) {
        for (String id : ids) {
            Long l = sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getDeptId, id));
            if (l > 0) {
                SysDept sysDept = baseMapper.selectById(id);
                throw Assert.throwable("该部门 [" + sysDept.getDeptName() + "] 已被分配，不允许删除");
            }
        }
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Object getDeptInfo(String deptId) {
        SysDept sysDept = baseMapper.selectById(deptId);
        Assert.isNull(sysDept, "未知部门");
        return sysDept;
    }

    /**
     * 构建部门树
     */
    public List<DeptRes> getChildPerms(List<DeptRes> list, String parentId) {
        List<DeptRes> returnList = new ArrayList<>();
        for (DeptRes t : list) {
            if (t.getParent().equals(parentId)) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<DeptRes> list, DeptRes t) {
        // 得到子节点列表
        List<DeptRes> childList = getChildList(list, t);
        if (null != childList && !childList.isEmpty()) {
            t.setChildren(childList);
            for (DeptRes tChild : childList) {
                if (hasChild(list, tChild)) {
                    recursionFn(list, tChild);
                }
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<DeptRes> getChildList(List<DeptRes> list, DeptRes t) {
        List<DeptRes> tlist = new ArrayList<>();
        if (null != list && !list.isEmpty()) {
            for (DeptRes n : list) {
                if (n.getParent().equals(t.getDeptId())) {
                    tlist.add(n);
                }
            }
            return tlist;
        }
        return null;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<DeptRes> list, DeptRes t) {
        List<DeptRes> childList = getChildList(list, t);
        return null != childList && !childList.isEmpty();
    }

}
