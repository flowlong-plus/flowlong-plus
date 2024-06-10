package cn.yuencode.flowlongplus.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.assist.Assert;
import cn.yuencode.flowlongplus.controller.req.SysPostPageParmaReq;
import cn.yuencode.flowlongplus.entity.SysPost;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.mapper.SysPostMapper;
import cn.yuencode.flowlongplus.mapper.SysUserMapper;
import cn.yuencode.flowlongplus.service.SysPostService;
import cn.yuencode.flowlongplus.util.page.PageResultConvert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 岗位/职务 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public Object getList(SysPostPageParmaReq params) {
        Page<SysPost> page = baseMapper.selectPage(Page.of(params.getPageNum(), params.getPageSize()),
                Wrappers.<SysPost>lambdaQuery()
                        .like(ObjUtil.isNotEmpty(params.getPostName()), SysPost::getPostName, params.getPostName())
                        .like(ObjUtil.isNotEmpty(params.getPostCode()), SysPost::getPostCode, params.getPostCode())
                        .eq(ObjUtil.isNotEmpty(params.getStatus()), SysPost::getStatus, params.getStatus())
                        .orderByDesc(SysPost::getOrderNum)
                        .orderByDesc(SysPost::getCreateTime));
        return PageResultConvert.convertMybatisPlus(page);
    }

    @Override
    public void addPost(SysPost sysPost) {
        sysPost.setPostId(null);
        sysPost.setTenantId(null);
        baseMapper.insert(sysPost);
    }

    @Override
    public void updatePost(SysPost sysPost) {
        sysPost.setTenantId(null);
        SysPost post = baseMapper.selectById(sysPost.getPostId());
        Assert.isNull(post, "未知岗位");
        baseMapper.updateById(sysPost);
    }

    @Override
    public void deletePost(String[] ids) {
        for (String id : ids) {
            Long l = sysUserMapper.selectCount(Wrappers.<SysUser>lambdaQuery()
                    .eq(SysUser::getPostId, id));
            if (l > 0) {
                SysPost sysPost = baseMapper.selectById(id);
                throw Assert.throwable("该岗位 [" + sysPost.getPostName() + "] 已被分配，不允许删除");
            }
        }
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Object getInfo(String postId) {
        SysPost sysPost = baseMapper.selectById(postId);
        Assert.isNull(sysPost, "未知岗位");
        return sysPost;
    }

    @Override
    public Object options() {
        List<SysPost> sysPosts = baseMapper.selectList(Wrappers.<SysPost>lambdaQuery()
                .orderByDesc(SysPost::getOrderNum));
        List<Map<String, Object>> resp = new ArrayList<>();

        for (SysPost sysPost : sysPosts) {
            Map<String, Object> map = new HashMap<>();
            map.put("postId", sysPost.getPostId());
            map.put("postName", sysPost.getPostName());
            map.put("postCode", sysPost.getPostCode());
            resp.add(map);
        }
        return resp;
    }
}
