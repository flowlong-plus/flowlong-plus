package cn.yuencode.flowlongplus.workflow.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.yuencode.flowlongplus.entity.SysDept;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.service.SysUserService;
import cn.yuencode.flowlongplus.service.impl.SysDeptServiceImpl;
import cn.yuencode.flowlongplus.workflow.dto.OrgTreeDto;
import cn.yuencode.flowlongplus.workflow.service.OaService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 组织部门 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class OaServiceImpl implements OaService {

    @Resource
    private SysDeptServiceImpl departmentsService;

    @Resource
    private SysUserService sysUserService;

    /**
     * 查询组织架构树
     *
     * @param deptId 部门id
     * @return 组织架构树数据
     */
    @Override
    public Object getOrgTreeData(Integer deptId) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!ObjectUtils.isEmpty(deptId)) {
            LambdaQueryWrapper<SysDept> departmentsLambdaQueryWrapper = new LambdaQueryWrapper<>();
            departmentsLambdaQueryWrapper.eq(SysDept::getParent, deptId);

            List<OrgTreeDto> orgTreeDtoList = new LinkedList<>();

            departmentsService.list(departmentsLambdaQueryWrapper).forEach(dept -> {
                orgTreeDtoList.add(OrgTreeDto.builder()
                        .id(dept.getDeptId())
                        .name(dept.getDeptName())
                        .selected(false)
                        .type("dept")
                        .build());
            });
            sysUserService.list(lambdaQueryWrapper).forEach(user -> {
                orgTreeDtoList.add(OrgTreeDto.builder()
                        .id(user.getId())
                        .name(user.getNickname())
                        .nickname(user.getNickname())
                        .username(user.getAccount())
                        .avatar("")
                        .sex(user.getSex())
                        .type("user")
                        .tenantId(String.valueOf(user.getTenantId()))
                        .selected(false)
                        .build());
            });
            return orgTreeDtoList;
        }

        return Collections.EMPTY_LIST;
    }

    /**
     * 模糊搜索用户
     *
     * @param username 用户名
     * @return 匹配到的用户
     */
    @Override
    public Object getOrgTreeUser(String username) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        lambdaQueryWrapper.like(SysUser::getAccount, username).or().like(SysUser::getNickname, username);

        List<OrgTreeDto> list = new LinkedList<>();
        sysUserService.list(lambdaQueryWrapper).forEach(user -> {
            list.add(OrgTreeDto.builder().type("user")
                    .sex(user.getSex()).avatar("")
                    .name(user.getAccount() + "-" + user.getNickname()).id(user.getId())
                    .selected(false).build());
        });
        return list;
    }

    /**
     * 查询用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @Override
    public Object getUserInfo(String userId) {
        SysUser user = sysUserService.getById(Long.valueOf(userId));
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("avatar", "");
        userInfo.put("name", ObjectUtil.isNull(user.getNickname()) ? "" : user.getNickname());
        userInfo.put("mobile", user.getMobile());
        return userInfo;
    }
}
