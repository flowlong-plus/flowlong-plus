package cn.yuencode.flowlongplus.workflow.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.yuencode.flowlongplus.config.exception.CommonException;
import cn.yuencode.flowlongplus.config.mybatis.ApiContext;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.mapper.SysUserMapper;
import cn.yuencode.flowlongplus.util.R;
import cn.yuencode.flowlongplus.workflow.controller.req.ProcessTemplateReq;
import cn.yuencode.flowlongplus.workflow.controller.res.TemplateGroupRes;
import cn.yuencode.flowlongplus.workflow.dto.DeployDto;
import cn.yuencode.flowlongplus.workflow.dto.TemplateGroupDto;
import cn.yuencode.flowlongplus.workflow.entity.WflowProcessGroups;
import cn.yuencode.flowlongplus.workflow.entity.WflowProcessTemplates;
import cn.yuencode.flowlongplus.workflow.entity.WflowTemplateGroup;
import cn.yuencode.flowlongplus.workflow.mapper.WflowProcessTemplatesMapper;
import cn.yuencode.flowlongplus.workflow.mapper.WflowTemplateGroupMapper;
import cn.yuencode.flowlongplus.workflow.service.SettingService;
import cn.yuencode.flowlongplus.workflow.service.WflowProcessGroupsService;
import cn.yuencode.flowlongplus.workflow.service.WflowTemplateGroupService;
import com.aizuda.bpm.engine.FlowLongEngine;
import com.aizuda.bpm.engine.core.FlowCreator;
import com.aizuda.bpm.engine.entity.FlwProcess;
import com.aizuda.bpm.engine.model.NodeModel;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 流程模版设置 服务实现类
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Service
@Slf4j
public class SettingServiceImpl implements SettingService {
    @Resource
    private FlowLongEngine flowLongEngine;
    @Resource
    private WflowProcessTemplatesMapper wflowProcessTemplatesMapper;
    @Resource
    private WflowTemplateGroupService wflowTemplateGroupService;
    @Resource
    private WflowProcessGroupsService wflowProcessGroupsService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ApiContext apiContext;
    @Resource
    private WflowTemplateGroupMapper wflowTemplateGroupMapper;

    @Override
    public Object getTemplateGroups() {
        List<TemplateGroupDto> templateGroupDtoList = wflowTemplateGroupMapper.getAllFormAndGroups();
        Map<String, List<TemplateGroupDto>> coverMap = new LinkedHashMap<>();
        templateGroupDtoList.forEach(fg -> {
            List<TemplateGroupDto> bos = coverMap.get(fg.getGroupId());
            if (CollectionUtil.isEmpty(bos)) {
                List<TemplateGroupDto> list = new ArrayList<>();
                list.add(fg);
                coverMap.put(fg.getGroupId(), list);
            } else {
                bos.add(fg);
            }
        });
        List<TemplateGroupRes> results = new ArrayList<>();
        coverMap.forEach((key, val) -> {
            List<TemplateGroupRes.Template> templates = new ArrayList<>();
            val.forEach(v -> {
                if (ObjectUtil.isNotNull(v.getTemplateId())) {
                    templates.add(TemplateGroupRes.Template.builder().templateId(v.getTemplateId()).templateName(v.getTemplateName()).tgId(v.getId()).remark(v.getRemark()).logo(v.getLogo()).status(v.getStatus()).updateTime(DateFormatUtils.format(v.getUpdateTime(), "yyyy年MM月dd日 HH时mm分ss秒")).templateId(v.getTemplateId()).build());
                }
            });
            results.add(TemplateGroupRes.builder().id(key).name(val.get(0).getGroupName()).items(templates).build());
        });
        return R.success(results);
    }

    @Override
    @Transactional
    public Object templateGroupsSort(List<TemplateGroupRes> groups) {
        int i = 0, j = 0;
        try {
            for (TemplateGroupRes group : groups) {
                wflowProcessGroupsService.updateById(WflowProcessGroups.builder().groupId(group.getId()).sortNum(group.getId().equals(0L) ? 999999 : i + 2).build());
                for (TemplateGroupRes.Template item : group.getItems()) {
                    wflowTemplateGroupService.updateById(WflowTemplateGroup.builder().id(item.getTgId()).groupId(group.getId()).templateId(item.getTemplateId()).sortNum(j + 1).build());
                    j++;
                }
                i++;
                j = 0;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return R.fail("排序异常 " + e.getMessage());
        }
        return R.success("排序成功");
    }

    @Override
    public Object updateTemplateGroupName(String id, String name) {
        if ("1".equals(id)) {
            return R.fail("基础分组不允许修改");
        }
        wflowProcessGroupsService.updateById(WflowProcessGroups.builder().groupId(id).groupName(name.trim()).build());
        return R.success("修改成功");
    }

    @Override
    public Object createTemplateGroup(String name) {
        LambdaQueryWrapper<WflowProcessGroups> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(WflowProcessGroups::getGroupName, name);
        if (wflowProcessGroupsService.count(lambdaQueryWrapper) > 0) {
            return R.fail("分组名称 [" + name + "] 已存在");
        }
        Date date = new Date();
        WflowProcessGroups wflowProcessGroups = WflowProcessGroups.builder().groupName(name).sortNum(1).created(date).updated(date).build();
        wflowProcessGroupsService.save(wflowProcessGroups);
        return R.success("添加分组 " + name + " 成功");
    }

    @Override
    @Transactional
    public Object deleteTemplateGroup(Integer id) {
        if (id < 2) {
            return R.fail("基础分组不允许删除");
        }
        LambdaUpdateWrapper<WflowTemplateGroup> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(WflowTemplateGroup::getGroupId, 1);
        lambdaUpdateWrapper.eq(WflowTemplateGroup::getGroupId, id);
        wflowTemplateGroupService.update(lambdaUpdateWrapper);
        wflowProcessGroupsService.removeById(id);
        return R.success("删除分组成功");
    }


    @Override
    public Object getGroupOptions() {
        List<WflowProcessGroups> list = wflowProcessGroupsService.list(new LambdaQueryWrapper<WflowProcessGroups>().orderByAsc(WflowProcessGroups::getSortNum));
        return list.stream().map(item -> {
            JSONObject object = new JSONObject();
            object.put("label", item.getGroupName());
            object.put("value", item.getGroupId());
            return object;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTemplate(ProcessTemplateReq processTemplateReq) {

        List<WflowProcessTemplates> keys = wflowProcessTemplatesMapper.selectList(Wrappers.<WflowProcessTemplates>lambdaQuery().eq(WflowProcessTemplates::getTemplateKey, processTemplateReq.getTemplateKey()));
        if (!keys.isEmpty()) {
            log.error("标识重复:{}", processTemplateReq.getTemplateKey());
            throw new CommonException("标识重复");
        }
        String groupId = processTemplateReq.getGroupId();

        WflowProcessGroups group = wflowProcessGroupsService.getById(groupId);
        if (null == group) {
            log.error("分组:{} 不存在", groupId);
            throw new CommonException("非法数据");
        }

        String loginId = (String) StpUtil.getLoginId();
        SysUser sysUser = sysUserMapper.selectById(loginId);
        if (null == sysUser) {
            log.error("用户:{} 不存在", loginId);
            throw new CommonException("非法用户");
        }
        FlowCreator creator = FlowCreator.of(String.valueOf(apiContext.getCurrentTenantId()), loginId, sysUser.getNickname());
        DeployDto deployDTO = new DeployDto();
        deployDTO.setKey(processTemplateReq.getTemplateKey());
        deployDTO.setName(processTemplateReq.getTemplateName());
        deployDTO.setInstanceUrl("");
        deployDTO.setNodeConfig(JSONObject.parseObject(processTemplateReq.getProcess(), NodeModel.class));

        String jsonString = JSONObject.toJSONString(deployDTO, SerializerFeature.WriteMapNullValue);
        Long deployId = flowLongEngine.processService().deploy(jsonString, creator, false);
        FlwProcess flwProcess = flowLongEngine.processService().getProcessById(deployId);
        WflowProcessTemplates processTemplate = new WflowProcessTemplates();
        processTemplate.setWorkflowId(deployId.toString());
        processTemplate.setWorkflowVersion(flwProcess.getProcessVersion());
        processTemplate.setTemplateName(processTemplateReq.getTemplateName());
        processTemplate.setTemplateKey(processTemplateReq.getTemplateKey());
        processTemplate.setGroupId(groupId);
        processTemplate.setProcess(processTemplateReq.getProcess());
        processTemplate.setFormItems(processTemplateReq.getFormItems());
        processTemplate.setSettings(processTemplateReq.getSettings());
        processTemplate.setRemark(processTemplateReq.getRemark());
        wflowProcessTemplatesMapper.insert(processTemplate);

        WflowTemplateGroup wflowTemplateGroup = new WflowTemplateGroup();
        wflowTemplateGroup.setTemplateId(processTemplate.getTemplateId());
        wflowTemplateGroup.setGroupId(groupId);
        wflowTemplateGroupService.save(wflowTemplateGroup);
    }

    @Override
    public Object getTemplateDetail(String templateId) {
        WflowProcessTemplates temp = wflowProcessTemplatesMapper.selectById(templateId);
        WflowProcessTemplates processTemplate = wflowProcessTemplatesMapper.selectOne(Wrappers.<WflowProcessTemplates>lambdaQuery().eq(WflowProcessTemplates::getTemplateKey, temp.getTemplateKey()).apply("(template_key, template_version) in(" + "SELECT template_key, MAX(template_version) " + "FROM wflow_process_templates " + "GROUP BY template_key)"));
        if (null == processTemplate) {
            throw new CommonException("非法数据");
        }
        return processTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(ProcessTemplateReq processTemplateReq) {
        processTemplateReq.setTemplateKey(null);
        String templateId = processTemplateReq.getTemplateId();
        WflowProcessTemplates temp = wflowProcessTemplatesMapper.selectById(templateId);
        WflowProcessTemplates template = wflowProcessTemplatesMapper.selectOne(Wrappers.<WflowProcessTemplates>lambdaQuery()
                .eq(WflowProcessTemplates::getTemplateKey, temp.getTemplateKey())
                .apply("(template_key, template_version) in("
                        + "SELECT template_key, MAX(template_version) "
                        + "FROM wflow_process_templates " + "GROUP BY template_key)"));
        if (null == template) {
            log.error("模版不存在 templateId:{}", processTemplateReq.getTemplateId());
            throw new CommonException("非法数据");
        }
        if (!Objects.equals(processTemplateReq.getTemplateVersion(), template.getTemplateVersion())) {
            log.error("版本不存在 templateId:{} version:{}", processTemplateReq.getTemplateId(), processTemplateReq.getTemplateVersion());
            throw new CommonException("非法数据");
        }

        String groupId = processTemplateReq.getGroupId();

        WflowProcessGroups group1 = wflowProcessGroupsService.getById(groupId);
        if (null == group1) {
            log.error("分组:{} 不存在", groupId);
            throw new CommonException("非法数据");
        }

        // 部署流程
        String loginId = (String) StpUtil.getLoginId();
        SysUser sysUser = sysUserMapper.selectById(loginId);
        if (null == sysUser) {
            log.error("用户 {} 不存在", loginId);
            throw new CommonException("非法用户");
        }
        FlowCreator creator = FlowCreator.of(apiContext.getCurrentTenantId(), loginId, sysUser.getNickname());

        DeployDto deployDTO = new DeployDto();
        deployDTO.setKey(template.getTemplateKey());
        deployDTO.setName(processTemplateReq.getTemplateName());
        deployDTO.setInstanceUrl("");
        deployDTO.setNodeConfig(JSONObject.parseObject(processTemplateReq.getProcess(), NodeModel.class));

        String jsonString = JSONObject.toJSONString(deployDTO, SerializerFeature.WriteMapNullValue);
        // flowlong bug: repeat为false不会更新流程定义
        Long deployId = flowLongEngine.processService().deploy(jsonString, creator, true);
        FlwProcess flwProcess = flowLongEngine.processService().getProcessById(deployId);
        // 更新模板, version+1
        WflowProcessTemplates processTemplate = new WflowProcessTemplates();
        processTemplate.setWorkflowId(deployId.toString());
        processTemplate.setWorkflowVersion(flwProcess.getProcessVersion());
        processTemplate.setTemplateName(processTemplateReq.getTemplateName());
        processTemplate.setTemplateKey(template.getTemplateKey());
        processTemplate.setGroupId(processTemplateReq.getGroupId());
        processTemplate.setProcess(processTemplateReq.getProcess());
        processTemplate.setFormItems(processTemplateReq.getFormItems());
        processTemplate.setSettings(processTemplateReq.getSettings());
        processTemplate.setRemark(processTemplateReq.getRemark());
        processTemplate.setTemplateVersion(template.getTemplateVersion() + 1);
        wflowProcessTemplatesMapper.insert(processTemplate);

        // 更新分组
        WflowTemplateGroup group = wflowTemplateGroupService.getOne(Wrappers.<WflowTemplateGroup>lambdaQuery().eq(WflowTemplateGroup::getGroupId, template.getGroupId()).eq(WflowTemplateGroup::getTemplateId, templateId));
        if (null == group) {
            throw new CommonException("非法数据");
        }
        group.setGroupId(processTemplateReq.getGroupId());
        group.setTemplateId(processTemplate.getTemplateId());
        wflowTemplateGroupService.updateById(group);
    }
}
