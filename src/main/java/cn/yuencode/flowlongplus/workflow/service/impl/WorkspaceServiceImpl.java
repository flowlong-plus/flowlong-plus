package cn.yuencode.flowlongplus.workflow.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.yuencode.flowlongplus.config.exception.CommonException;
import cn.yuencode.flowlongplus.config.mybatis.ApiContext;
import cn.yuencode.flowlongplus.entity.SysFileDetail;
import cn.yuencode.flowlongplus.entity.SysUser;
import cn.yuencode.flowlongplus.mapper.FileDetailMapper;
import cn.yuencode.flowlongplus.mapper.SysUserMapper;
import cn.yuencode.flowlongplus.util.page.PageResultConvert;
import cn.yuencode.flowlongplus.workflow.controller.req.*;
import cn.yuencode.flowlongplus.workflow.controller.res.CcRes;
import cn.yuencode.flowlongplus.workflow.controller.res.DoneRes;
import cn.yuencode.flowlongplus.workflow.controller.res.MyApplyRes;
import cn.yuencode.flowlongplus.workflow.controller.res.TaskRes;
import cn.yuencode.flowlongplus.workflow.dto.WFlowUserDto;
import cn.yuencode.flowlongplus.workflow.entity.WflowExtInstance;
import cn.yuencode.flowlongplus.workflow.entity.WflowInstanceActionRecord;
import cn.yuencode.flowlongplus.workflow.entity.WflowProcessTemplates;
import cn.yuencode.flowlongplus.workflow.enums.WflowActionType;
import cn.yuencode.flowlongplus.workflow.mapper.WflowExtInstanceMapper;
import cn.yuencode.flowlongplus.workflow.mapper.WflowInstanceActionRecordMapper;
import cn.yuencode.flowlongplus.workflow.mapper.WflowProcessTemplatesMapper;
import cn.yuencode.flowlongplus.workflow.service.WorkspaceService;
import com.aizuda.bpm.engine.Expression;
import com.aizuda.bpm.engine.FlowLongEngine;
import com.aizuda.bpm.engine.assist.Assert;
import com.aizuda.bpm.engine.assist.ObjectUtils;
import com.aizuda.bpm.engine.core.FlowCreator;
import com.aizuda.bpm.engine.core.enums.InstanceState;
import com.aizuda.bpm.engine.core.enums.TaskType;
import com.aizuda.bpm.engine.entity.*;
import com.aizuda.bpm.engine.model.ConditionNode;
import com.aizuda.bpm.engine.model.NodeAssignee;
import com.aizuda.bpm.engine.model.NodeModel;
import com.aizuda.bpm.mybatisplus.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作台 服务实现类
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
public class WorkspaceServiceImpl implements WorkspaceService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ApiContext apiContext;
    @Resource
    private FlowLongEngine flowLongEngine;
    @Resource
    private WflowProcessTemplatesMapper wflowProcessTemplatesMapper;
    @Resource
    private FlwTaskMapper flwTaskMapper;
    @Resource
    private FlwHisTaskMapper flwHisTaskMapper;
    @Resource
    private FlwTaskActorMapper flwTaskActorMapper;
    @Resource
    private FlwHisTaskActorMapper flwHisTaskActorMapper;
    @Resource
    private FlwHisInstanceMapper flwHisInstanceMapper;
    @Resource
    private FlwProcessMapper flwProcessMapper;
    @Resource
    private WflowInstanceActionRecordMapper wflowInstanceActionRecordMapper;
    @Resource
    private FileDetailMapper fileDetailMapper;
    @Resource
    private WflowExtInstanceMapper wflowExtInstanceMapper;
    @Resource
    private FlwExtInstanceMapper flwExtInstanceMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcess(StartProcessReq startProcessReq) {
        String loginId = (String) StpUtil.getLoginId();
        SysUser sysUser = sysUserMapper.selectById(loginId);
        if (null == sysUser) {
            log.error("用户:{} 不存在", loginId);
            throw new CommonException("非法用户");
        }
        FlowCreator creator = FlowCreator.of(String.valueOf(apiContext.getCurrentTenantId()),
                loginId, sysUser.getNickname());
        WflowProcessTemplates temp = wflowProcessTemplatesMapper.selectById(startProcessReq.getTemplateId());
        WflowProcessTemplates template = wflowProcessTemplatesMapper.selectOne(Wrappers.<WflowProcessTemplates>lambdaQuery()
                .eq(WflowProcessTemplates::getTemplateKey, temp.getTemplateKey())
                .apply("(template_key, template_version) in(" +
                        "SELECT template_key, MAX(template_version) " +
                        "FROM wflow_process_templates " +
                        "GROUP BY template_key)")
        );
        if (ObjectUtil.isNull(template)) {
            log.error("流程模板不存在:{}", startProcessReq.getTemplateId());
            throw new CommonException("流程模板不存在");
        }
        FlwProcess flwProcess = flowLongEngine.processService().getProcessById(Long.valueOf(template.getWorkflowId()));
        if (ObjectUtil.isNull(flwProcess)) {
            log.error("流程未接入流程引擎:{}", Long.valueOf(template.getWorkflowId()));
            throw new CommonException("流程未接入流程引擎");
        }
        if (!Objects.equals(template.getWorkflowVersion(), flwProcess.getProcessVersion())) {
            log.error("与流程引擎版本不一致");
            throw new CommonException("流程引擎版本不一致");
        }
        // 开启流程
        flowLongEngine.startInstanceById(flwProcess.getId(), creator, startProcessReq.getVariable()).ifPresent(instance -> {
            // action记录
            WflowInstanceActionRecord wflowInstanceActionRecord = new WflowInstanceActionRecord();
            wflowInstanceActionRecord.setInstanceId(String.valueOf(instance.getId()));
            wflowInstanceActionRecord.setActionType(WflowActionType.start.getValue());
            wflowInstanceActionRecord.setAuditorId(creator.getCreateId());
            wflowInstanceActionRecordMapper.insert(wflowInstanceActionRecord);

            // 添加拓展信息
            WflowExtInstance wflowExtInstance = new WflowExtInstance();
            wflowExtInstance.setId(String.valueOf(instance.getId()));
            wflowExtInstance.setTemplateId(startProcessReq.getTemplateId());
            wflowExtInstance.setProcessId(flwProcess.getId().toString());
            // 由于flowlong更新flw_process流程模版时不会更新id，所以这里保存一下version
            // 以便后续查询使用
            wflowExtInstance.setProcessVersion(flwProcess.getProcessVersion());
            wflowExtInstanceMapper.insert(wflowExtInstance);
        });

    }

    @Override
    public WflowProcessTemplates processDetail(String templateId) {
        WflowProcessTemplates template = wflowProcessTemplatesMapper.selectById(templateId);
        if (ObjectUtil.isNull(template)) {
            log.error("流程模板不存在:{}", templateId);
            throw new CommonException("非法数据");
        }

        FlwProcess flwProcess = flowLongEngine.processService().getProcessById(Long.valueOf(template.getWorkflowId()));
        if (ObjectUtil.isNull(flwProcess)) {
            log.error("流程未接入流程引擎:{}", Long.valueOf(template.getWorkflowId()));
            throw new CommonException("非法数据");
        }
        return template;
    }

    /**
     * 递归解析流程节点
     *
     * @param node            流程定义模型
     * @param processNodeList 流程节点列表
     * @param args            执行参数
     */
    void parseProcess(NodeModel node, List<Map<String, Object>> processNodeList, Map<String, Object> args) {
        if (node == null) {
            return;
        }
        NodeModel next = node.getChildNode();
        // 条件分支
        List<ConditionNode> conditionNodes = node.getConditionNodes();
        if (ObjectUtil.isNotEmpty(conditionNodes)) {
            Assert.illegal(ObjectUtils.isEmpty(args), "Execution parameter cannot be empty");
            Expression expression = flowLongEngine.getContext().getExpression();
            Assert.isNull(expression, "Interface Expression not implemented");
            Optional<ConditionNode> conditionNodeOptional = conditionNodes.stream().sorted(Comparator.comparing(ConditionNode::getPriorityLevel))
                    .filter(t -> expression.eval(t.getConditionList(), args)).findFirst();
            if (!conditionNodeOptional.isPresent()) {
                // 未发现满足条件分支，使用默认条件分支
                conditionNodeOptional = conditionNodes.stream().filter(t -> ObjectUtils.isEmpty(t.getConditionList())).findFirst();
                Assert.isFalse(conditionNodeOptional.isPresent(), "Not found executable ConditionNode");
            }
            ConditionNode conditionNode = conditionNodeOptional.orElse(null);
            parseProcess(conditionNode.getChildNode(), processNodeList, args);
        } else {
            Map<String, Object> map = BeanUtil.beanToMap(node,
                    "nodeName", "type", "examineMode", "nodeAssigneeList");
            // 结束节点
            if (node.getType() == -1) {
                return;
            }
            List<String> userIds = node.getNodeAssigneeList().stream().map(NodeAssignee::getId)
                    .collect(Collectors.toList());
            map.put("userIds", userIds);
            map.put("name", node.getNodeName());
            processNodeList.add(map);
        }
        parseProcess(next, processNodeList, args);
    }

    @Override
    public Object processPreview(StartProcessReq params) {
        List<Map<String, Object>> resp = new ArrayList<>();
        Map<String, Object> args = params.getVariable();
        WflowProcessTemplates template = wflowProcessTemplatesMapper.selectById(params.getTemplateId());
        if (ObjectUtil.isNull(template)) {
            log.error("流程模板不存在:{}", params.getTemplateId());
            throw new CommonException("非法数据");
        }
        FlwProcess flwProcess = flowLongEngine.processService().getProcessById(Long.valueOf(template.getWorkflowId()));
        if (ObjectUtil.isNull(flwProcess)) {
            log.error("流程未接入流程引擎:{}", Long.valueOf(template.getWorkflowId()));
            throw new CommonException("非法数据");
        }
        NodeModel currentNode = flwProcess.model().getNodeConfig();
        parseProcess(currentNode, resp, args);

        return resp;
    }

    @Override
    public Object getTodoTask(ProcessListReq queryParams) {
        Page<TaskRes> page = new Page<>();
        page.setCurrent(queryParams.getPageNum());
        page.setSize(queryParams.getPageSize());
        List<TaskRes> taskResList = new ArrayList<>();
        String loginId = (String) StpUtil.getLoginId();
        QueryWrapper<FlwHisInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (ObjectUtil.isNotNull(queryParams.getInstanceState())) {
            queryWrapper.eq("instance_state", queryParams.getInstanceState());
        }
        List<String> processIds = new ArrayList<>();
        // groupId不为空
        if (StrUtil.isNotEmpty(queryParams.getGroupId())) {
            WflowProcessTemplates queryWflowProcessTemplates = new WflowProcessTemplates();
            queryWflowProcessTemplates.setGroupId(queryParams.getGroupId());
            List<WflowProcessTemplates> list = wflowProcessTemplatesMapper.getList(queryWflowProcessTemplates);
            List<String> workFlowIds = list.stream().map(WflowProcessTemplates::getWorkflowId).collect(Collectors.toList());
            processIds.addAll(workFlowIds);
        }
        // processName不为空 进行添加条件
        if (StrUtil.isNotEmpty(queryParams.getProcessName())) {
            List<FlwProcess> flwProcesses = flwProcessMapper.selectList(Wrappers.<FlwProcess>lambdaQuery()
                    .like(FlwProcess::getProcessName, queryParams.getProcessName()));
            List<String> pIds = flwProcesses.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
            processIds.addAll(pIds);
        }
        if ((StrUtil.isNotEmpty(queryParams.getGroupId())
                || StrUtil.isNotEmpty(queryParams.getProcessName()))) {
            // 直接返回,减少数据库压力
            if (processIds.isEmpty()) {
                return PageResultConvert.convertMybatisPlus(page);
            }
            queryWrapper.in("process_id", processIds);
        }
        List<Long> flwHisInstances = flwHisInstanceMapper.selectList(queryWrapper).stream()
                .map(FlwHisInstance::getId).collect(Collectors.toList());

        LambdaQueryWrapper<FlwTaskActor> query = Wrappers.<FlwTaskActor>lambdaQuery()
                .eq(FlwTaskActor::getActorId, loginId)
                .eq(FlwTaskActor::getActorType, 0);
        // 添加条件
        if ((StrUtil.isNotEmpty(queryParams.getGroupId())
                || StrUtil.isNotEmpty(queryParams.getProcessName()))
                || ObjectUtil.isNotEmpty(queryParams.getInstanceState())) {
            if (flwHisInstances.isEmpty()) {
                return PageResultConvert.convertMybatisPlus(page);
            }
            query = query.in(FlwTaskActor::getInstanceId, flwHisInstances);
        }
        Page<FlwTaskActor> flwTaskActorPage = flwTaskActorMapper.selectPage(
                Page.of(queryParams.getPageNum(), queryParams.getPageSize()), query);

        if (flwTaskActorPage.getRecords().isEmpty()) {
            return PageResultConvert.convertMybatisPlus(page);
        }

        List<FlwTask> flwTasks = flwTaskMapper.selectList(Wrappers.<FlwTask>lambdaQuery()
                .orderByDesc(FlwTask::getCreateTime)
                .in(FlwTask::getId, flwTaskActorPage.getRecords().stream()
                        .map(FlwTaskActor::getTaskId).collect(Collectors.toList())));

        for (FlwTask flwTask : flwTasks) {
            // 查询流程实例信息
            FlwHisInstance histInstance = flowLongEngine.queryService().getHistInstance(flwTask.getInstanceId());
            if (ObjectUtil.isNull(histInstance)) {
                throw new CommonException("非法数据");
            }
            // 查询流程定义信息
            FlwProcess flwProcess = flwProcessMapper.selectById(histInstance.getProcessId());
            taskResList.add(TaskRes.builder()
                    .taskId(String.valueOf(flwTask.getId()))
                    .taskName(flwTask.getTaskName())
                    .instanceId(histInstance.getId().toString())
                    .processId(histInstance.getProcessId().toString())
                    .instanceState(histInstance.getInstanceState().toString())
                    .taskState("0")
                    .currentNode(histInstance.getCurrentNode())
                    .duration(histInstance.getDuration())
                    .processName(flwProcess.getProcessName())
                    .startTime(histInstance.getCreateTime())
                    .startUser(WFlowUserDto.builder()
                            .id(histInstance.getCreateId())
                            .name(histInstance.getCreateBy())
                            .build())
                    .taskStartTime(flwTask.getCreateTime())
                    .build());
        }

        page.setTotal(flwTaskActorPage.getTotal());
        page.setCurrent(flwTaskActorPage.getCurrent());
        page.setRecords(taskResList);
        return PageResultConvert.convertMybatisPlus(page);
    }

    @Override
    public Object getInstanceForm(String instanceId) {
        FlwHisInstance flwHisInstance = flwHisInstanceMapper.selectById(instanceId);
        if (ObjectUtil.isNull(flwHisInstance)) {
            throw new CommonException("非法数据");
        }
        WflowExtInstance wflowExtInstance = wflowExtInstanceMapper.selectOne(Wrappers.<WflowExtInstance>lambdaQuery()
                .eq(WflowExtInstance::getId, instanceId));
        WflowProcessTemplates wflowProcessTemplates = wflowProcessTemplatesMapper.selectOne(Wrappers.<WflowProcessTemplates>lambdaQuery()
                .eq(WflowProcessTemplates::getTemplateId, wflowExtInstance.getTemplateId()));
        Map<String, Object> resp = new HashMap<>();
        resp.put("formItems", wflowProcessTemplates.getFormItems());
        resp.put("formData", JSONUtil.parseObj(flwHisInstance.getVariable()));
        return resp;
    }

    @Override
    public void agree(ProcessAgreeReq processAgreeReq) {
        Long taskId = Long.valueOf(processAgreeReq.getTaskId());
        FlwTask flwTask = flwTaskMapper.getCheckById(taskId);
        if (ObjectUtil.isNull(flwTask)) {
            log.error("任务不存在:{}", taskId);
            throw new CommonException("未知任务");
        }
        flowLongEngine.executeTask(taskId, currentFlowCreator());
        // 动作执行记录
        WflowInstanceActionRecord record = new WflowInstanceActionRecord();
        record.setInstanceId(flwTask.getInstanceId().toString());
        record.setAuditorId(StpUtil.getLoginId().toString());
        record.setComment(processAgreeReq.getComment());
        record.setActionType(WflowActionType.approved.getValue());
        record.setAttachments(processAgreeReq.getAttachments());
        wflowInstanceActionRecordMapper.insert(record);
    }


    private FlowCreator currentFlowCreator() {
        String loginId = (String) StpUtil.getLoginId();
        SysUser sysUser = sysUserMapper.selectById(loginId);
        if (null == sysUser) {
            log.error("用户:{} 不存在", loginId);
            throw new CommonException("非法用户");
        }
        return FlowCreator.of(String.valueOf(apiContext.getCurrentTenantId()),
                loginId, sysUser.getNickname());
    }

    @Override
    public Object getApplyList(ProcessListReq queryParams) {
        IPage<MyApplyRes> myApplyRespPage = new Page<>();
        QueryWrapper<FlwHisInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("create_id", StpUtil.getLoginId());
        queryWrapper.orderByDesc("create_time");
        if (ObjectUtil.isNotNull(queryParams.getInstanceState())) {
            queryWrapper.eq("instance_state", queryParams.getInstanceState());
        }
        List<String> processIds = new ArrayList<>();
        // groupId不为空
        if (StrUtil.isNotEmpty(queryParams.getGroupId())) {
            WflowProcessTemplates queryWflowProcessTemplates = new WflowProcessTemplates();
            queryWflowProcessTemplates.setGroupId(queryParams.getGroupId());
            List<WflowProcessTemplates> list = wflowProcessTemplatesMapper.getList(queryWflowProcessTemplates);
            List<String> workFlowIds = list.stream().map(WflowProcessTemplates::getWorkflowId).collect(Collectors.toList());
            processIds.addAll(workFlowIds);
        }
        // processName不为空 进行添加条件
        if (StrUtil.isNotEmpty(queryParams.getProcessName())) {
            List<FlwProcess> flwProcesses = flwProcessMapper.selectList(Wrappers.<FlwProcess>lambdaQuery()
                    .like(FlwProcess::getProcessName, queryParams.getProcessName()));
            List<String> pIds = flwProcesses.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
            processIds.addAll(pIds);
        }
        if ((StrUtil.isNotEmpty(queryParams.getGroupId()) || StrUtil.isNotEmpty(queryParams.getProcessName()))) {
            // 直接返回,减少数据库压力
            if (processIds.isEmpty()) {
                myApplyRespPage.setCurrent(queryParams.getPageNum());
                myApplyRespPage.setSize(queryParams.getPageSize());
                return PageResultConvert.convertMybatisPlus(myApplyRespPage);
            }
            queryWrapper.in("process_id", processIds);
        }

        Page<FlwHisInstance> flwHisInstancePage = flwHisInstanceMapper.selectPage(
                Page.of(queryParams.getPageNum(), queryParams.getPageSize()), queryWrapper);
        myApplyRespPage.setTotal(flwHisInstancePage.getTotal());
        myApplyRespPage.setCurrent(flwHisInstancePage.getCurrent());
        myApplyRespPage.setSize(flwHisInstancePage.getSize());
        List<MyApplyRes> list = flwHisInstancePage.getRecords().stream().map(flwHisInstance -> {
            FlwProcess flwProcess = flwProcessMapper.selectById(flwHisInstance.getProcessId());
            return MyApplyRes.builder()
                    .processName(flwProcess.getProcessName())
                    .processId(flwHisInstance.getProcessId().toString())
                    .currentNode(flwHisInstance.getCurrentNode())
                    .instanceId(flwHisInstance.getId().toString())
                    .instanceState(flwHisInstance.getInstanceState().toString())
                    .startTime(flwHisInstance.getCreateTime())
                    .startUser(WFlowUserDto.builder()
                            .id(flwHisInstance.getCreateId())
                            .name(flwHisInstance.getCreateBy())
                            .build())
                    .duration(flwHisInstance.getDuration())
                    .build();
        }).collect(Collectors.toList());
        myApplyRespPage.setRecords(list);

        return PageResultConvert.convertMybatisPlus(myApplyRespPage);
    }

    @Override
    public void refuse(ProcessRefuseReq processRefuseReq) {
        Long taskId = Long.valueOf(processRefuseReq.getTaskId());
        FlwTask flwTask = flwTaskMapper.getCheckById(taskId);
        // todo: 分情况处理
        // 直接拒绝当前流程
        flowLongEngine.runtimeService().reject(flwTask.getInstanceId(), currentFlowCreator());
        // 退回上一个节点
//        flowLongEngine.taskService().rejectTask(flwTask, currentFlowCreator());
        // 退回指定节点

        // 动作执行记录
        WflowInstanceActionRecord record = new WflowInstanceActionRecord();
        record.setInstanceId(flwTask.getInstanceId().toString());
        record.setAuditorId(StpUtil.getLoginId().toString());
        record.setComment(processRefuseReq.getComment());
        record.setActionType(WflowActionType.rejected.getValue());
        record.setAttachments(processRefuseReq.getAttachments());
        wflowInstanceActionRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revoke(ProcessRevokeReq processRevokeReq) {
        Long instanceId = Long.valueOf(processRevokeReq.getInstanceId());
        FlwHisInstance histInstance = flowLongEngine.queryService().getHistInstance(instanceId);
        if (ObjectUtil.isNull(histInstance)) {
            log.error("流程实例不存在:{}", instanceId);
            throw new CommonException("当前流程实例不存在");
        }
        if (histInstance.getInstanceState() != 0) {
            throw new CommonException("当前流程实例已结束");
        }
        flowLongEngine.runtimeService().revoke(instanceId, currentFlowCreator());
        // 动作执行记录
        WflowInstanceActionRecord record = new WflowInstanceActionRecord();
        record.setInstanceId(instanceId.toString());
        record.setAuditorId(StpUtil.getLoginId().toString());
        record.setComment(processRevokeReq.getComment());
        record.setNodeId(histInstance.getCurrentNode());
        record.setActionType(WflowActionType.canceled.getValue());
        record.setAttachments(processRevokeReq.getAttachments());
        wflowInstanceActionRecordMapper.insert(record);
    }

    @Override
    public Object getCcList(ProcessListReq queryParams) {
        IPage<CcRes> page = new Page<>();
        page.setCurrent(queryParams.getPageNum());
        page.setSize(queryParams.getPageSize());
        QueryWrapper<FlwHisInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (ObjectUtil.isNotNull(queryParams.getInstanceState())) {
            queryWrapper.eq("instance_state", queryParams.getInstanceState());
        }
        List<String> processIds = new ArrayList<>();
        // groupId不为空
        if (StrUtil.isNotEmpty(queryParams.getGroupId())) {
            WflowProcessTemplates queryWflowProcessTemplates = new WflowProcessTemplates();
            queryWflowProcessTemplates.setGroupId(queryParams.getGroupId());
            List<WflowProcessTemplates> list = wflowProcessTemplatesMapper.getList(queryWflowProcessTemplates);
            List<String> workFlowIds = list.stream().map(WflowProcessTemplates::getWorkflowId).collect(Collectors.toList());
            processIds.addAll(workFlowIds);
        }
        // processName不为空 进行添加条件
        if (StrUtil.isNotEmpty(queryParams.getProcessName())) {
            List<FlwProcess> flwProcesses = flwProcessMapper.selectList(Wrappers.<FlwProcess>lambdaQuery()
                    .like(FlwProcess::getProcessName, queryParams.getProcessName()));
            List<String> pIds = flwProcesses.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
            processIds.addAll(pIds);
        }
        if ((StrUtil.isNotEmpty(queryParams.getGroupId())
                || StrUtil.isNotEmpty(queryParams.getProcessName()))) {
            // 直接返回,减少数据库压力
            if (processIds.isEmpty()) {
                return PageResultConvert.convertMybatisPlus(page);
            }
            queryWrapper.in("process_id", processIds);
        }
        List<Long> flwHisInstances = flwHisInstanceMapper.selectList(queryWrapper).stream()
                .map(FlwHisInstance::getId).collect(Collectors.toList());

        LambdaQueryWrapper<FlwHisTaskActor> query = Wrappers.<FlwHisTaskActor>lambdaQuery()
                .eq(FlwHisTaskActor::getActorType, 0)
                .eq(FlwHisTaskActor::getActorId, StpUtil.getLoginId());
        // 添加条件
        if ((StrUtil.isNotEmpty(queryParams.getGroupId())
                || StrUtil.isNotEmpty(queryParams.getProcessName()))
                || ObjectUtil.isNotEmpty(queryParams.getInstanceState())) {
            if (flwHisInstances.isEmpty()) {
                return PageResultConvert.convertMybatisPlus(page);
            }
            query = query.in(FlwHisTaskActor::getInstanceId, flwHisInstances);
        }
        List<FlwHisTaskActor> flwHisTaskActors = flwHisTaskActorMapper.selectList(query);

        List<Long> hisTaskIds = flwHisTaskActors.stream().map(FlwTaskActor::getTaskId).collect(Collectors.toList());
        if (!hisTaskIds.isEmpty()) {
            Page<FlwHisTask> flwHisTaskPage = flwHisTaskMapper.selectPage(Page.of(queryParams.getPageNum(), queryParams.getPageSize()),
                    Wrappers.<FlwHisTask>lambdaQuery()
                            .eq(FlwHisTask::getTaskType, 2)
                            .in(FlwHisTask::getId, hisTaskIds));

            page.setCurrent(flwHisTaskPage.getCurrent());
            page.setSize(flwHisTaskPage.getSize());
            page.setTotal(flwHisTaskPage.getTotal());

            List<CcRes> list = flwHisTaskPage.getRecords().stream().map(flwHisTask -> {
                // 查询流程实例信息
                FlwHisInstance histInstance = flowLongEngine.queryService().getHistInstance(flwHisTask.getInstanceId());
                if (ObjectUtil.isNull(histInstance)) {
                    throw new CommonException("非法数据");
                }
                // 查询流程定义信息
                FlwProcess flwProcess = flwProcessMapper.selectById(histInstance.getProcessId());
                return CcRes.builder()
                        .taskId(String.valueOf(flwHisTask.getId()))
                        .taskName(flwHisTask.getTaskName())
                        .instanceId(histInstance.getId().toString())
                        .processId(histInstance.getProcessId().toString())
                        .instanceState(histInstance.getInstanceState().toString())
                        .taskState(flwHisTask.getTaskState().toString())
                        .currentNode(histInstance.getCurrentNode())
                        .duration(histInstance.getDuration())
                        .processName(flwProcess.getProcessName())
                        .startTime(histInstance.getCreateTime())
                        .startUser(WFlowUserDto.builder()
                                .id(histInstance.getCreateId())
                                .name(histInstance.getCreateBy())
                                .build())
                        .taskStartTime(flwHisTask.getCreateTime())
                        .build();
            }).collect(Collectors.toList());
            page.setRecords(list);
        }
        return PageResultConvert.convertMybatisPlus(page);
    }

    @Override
    public Object getDoneList(ProcessListReq queryParams) {
        IPage<DoneRes> page = new Page<>();
        page.setCurrent(queryParams.getPageNum());
        page.setSize(queryParams.getPageSize());
        QueryWrapper<FlwHisInstance> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        if (ObjectUtil.isNotNull(queryParams.getInstanceState())) {
            queryWrapper.eq("instance_state", queryParams.getInstanceState());
        }
        List<String> processIds = new ArrayList<>();
        // groupId不为空
        if (StrUtil.isNotEmpty(queryParams.getGroupId())) {
            WflowProcessTemplates queryWflowProcessTemplates = new WflowProcessTemplates();
            queryWflowProcessTemplates.setGroupId(queryParams.getGroupId());
            List<WflowProcessTemplates> list = wflowProcessTemplatesMapper.getList(queryWflowProcessTemplates);
            List<String> workFlowIds = list.stream().map(WflowProcessTemplates::getWorkflowId).collect(Collectors.toList());
            processIds.addAll(workFlowIds);
        }
        // processName不为空 进行添加条件
        if (StrUtil.isNotEmpty(queryParams.getProcessName())) {
            List<FlwProcess> flwProcesses = flwProcessMapper.selectList(Wrappers.<FlwProcess>lambdaQuery()
                    .like(FlwProcess::getProcessName, queryParams.getProcessName()));
            List<String> pIds = flwProcesses.stream().map(item -> item.getId().toString()).collect(Collectors.toList());
            processIds.addAll(pIds);
        }
        if ((StrUtil.isNotEmpty(queryParams.getGroupId())
                || StrUtil.isNotEmpty(queryParams.getProcessName()))) {
            // 直接返回,减少数据库压力
            if (processIds.isEmpty()) {
                return PageResultConvert.convertMybatisPlus(page);
            }
            queryWrapper.in("process_id", processIds);
        }
        List<Long> flwHisInstances = flwHisInstanceMapper.selectList(queryWrapper).stream()
                .map(FlwHisInstance::getId).collect(Collectors.toList());

        LambdaQueryWrapper<FlwHisTaskActor> query = Wrappers.<FlwHisTaskActor>lambdaQuery()
                .eq(FlwHisTaskActor::getActorType, 0)
                .eq(FlwHisTaskActor::getActorId, StpUtil.getLoginId());
        // 添加条件
        if ((StrUtil.isNotEmpty(queryParams.getGroupId())
                || StrUtil.isNotEmpty(queryParams.getProcessName()))
                || ObjectUtil.isNotEmpty(queryParams.getInstanceState())) {
            if (flwHisInstances.isEmpty()) {
                return PageResultConvert.convertMybatisPlus(page);
            }
            query = query.in(FlwHisTaskActor::getInstanceId, flwHisInstances);
        }
        List<FlwHisTaskActor> flwHisTaskActors = flwHisTaskActorMapper.selectList(query);

        List<Long> hisTaskIds = flwHisTaskActors.stream().map(FlwTaskActor::getTaskId).collect(Collectors.toList());

        if (!hisTaskIds.isEmpty()) {
            Page<FlwHisTask> flwHisTaskPage = flwHisTaskMapper.selectPage(Page.of(queryParams.getPageNum(), queryParams.getPageSize()),
                    Wrappers.<FlwHisTask>lambdaQuery()
                            // 排除 抄送：2
                            .notIn(FlwHisTask::getTaskType, TaskType.cc.getValue(), TaskType.major.getValue())
                            .orderByDesc(FlwHisTask::getCreateTime)
                            .in(FlwHisTask::getId, hisTaskIds));

            page.setCurrent(flwHisTaskPage.getCurrent());
            page.setSize(flwHisTaskPage.getSize());
            page.setTotal(flwHisTaskPage.getTotal());

            List<DoneRes> list = flwHisTaskPage.getRecords().stream().map(flwHisTask -> {
                // 查询流程实例信息
                FlwHisInstance histInstance = flowLongEngine.queryService().getHistInstance(flwHisTask.getInstanceId());
                if (ObjectUtil.isNull(histInstance)) {
                    throw new CommonException("非法数据");
                }
                // 查询流程定义信息
                FlwProcess flwProcess = flwProcessMapper.selectById(histInstance.getProcessId());
                return DoneRes.builder()
                        .taskId(String.valueOf(flwHisTask.getId()))
                        .taskName(flwHisTask.getTaskName())
                        .instanceId(histInstance.getId().toString())
                        .processId(histInstance.getProcessId().toString())
                        .instanceState(histInstance.getInstanceState().toString())
                        .taskState(flwHisTask.getTaskState().toString())
                        .currentNode(histInstance.getCurrentNode())
                        .duration(histInstance.getDuration())
                        .processName(flwProcess.getProcessName())
                        .startTime(histInstance.getCreateTime())
                        .startUser(WFlowUserDto.builder()
                                .id(histInstance.getCreateId())
                                .name(histInstance.getCreateBy())
                                .build())
                        .taskStartTime(flwHisTask.getCreateTime())
                        .build();
            }).collect(Collectors.toList());
            page.setRecords(list);
        }

        return PageResultConvert.convertMybatisPlus(page);
    }

    @Override
    public Object getInstanceAction(String instanceId) {
        List<Map<String, Object>> list = new ArrayList<>();
        // 查询action
        List<WflowInstanceActionRecord> actions = wflowInstanceActionRecordMapper.selectList(Wrappers.<WflowInstanceActionRecord>lambdaQuery()
                .eq(WflowInstanceActionRecord::getInstanceId, instanceId)
                .orderByAsc(WflowInstanceActionRecord::getCreateTime));
        actions.forEach(action -> {
            Map<String, Object> map = BeanUtil.beanToMap(action);
            // 查询附件信息
            List<String> attachmentIds = action.getAttachments();
            List<Map<String, Object>> attachments = new ArrayList<>();
            if (ObjectUtil.isNotNull(attachmentIds)) {
                attachmentIds.forEach(attachmentId -> {
                    SysFileDetail sysFileDetail = fileDetailMapper.selectById(attachmentId);
                    Map<String, Object> attachment = new HashMap<>();
                    if (ObjectUtil.isNotNull(sysFileDetail)) {
                        attachment.put("id", sysFileDetail.getId());
                        attachment.put("filename", sysFileDetail.getFilename());
                        attachment.put("originalFilename", sysFileDetail.getOriginalFilename());
                        attachment.put("size", sysFileDetail.getSize());
                    }
                    attachments.add(attachment);
                });
            }
            // 更新附件信息
            map.put("attachments", attachments);
            list.add(map);
        });
        // 查询当前实例状态 如果是审批中 最后插入一个 审批中节点数据
        FlwHisInstance histInstance = flowLongEngine.queryService().getHistInstance(Long.valueOf(instanceId));
        if (ObjectUtil.isNull(histInstance)) {
            log.error("流程实例不存在:{}", instanceId);
            throw new CommonException("未知实例");
        }
        // 审批中
        if (histInstance.getInstanceState() == InstanceState.active.getValue()) {
            String currentNode = histInstance.getCurrentNode();
            // 查询流程定义
            Long processId = histInstance.getProcessId();
            FlwProcess flwProcess = flwProcessMapper.selectById(processId);
            NodeModel node = flwProcess.model().getNode(currentNode);
            if (ObjectUtil.isNull(node)) {
                log.error("未找到节点：{}", currentNode);
                throw new CommonException("未知节点");
            }
            Map<String, Object> map = BeanUtil.beanToMap(node,
                    "nodeName", "type", "examineMode", "nodeAssigneeList");
            List<String> userIds = node.getNodeAssigneeList().stream().map(NodeAssignee::getId)
                    .collect(Collectors.toList());
            map.put("userIds", userIds);
            map.put("underway", true);
            list.add(map);
        }
        return list;
    }

    @Override
    public Object getNodeInfo(String processId, String nodeId) {
        FlwProcess flwProcess = flwProcessMapper.selectById(Long.valueOf(processId));
        if (ObjectUtil.isNull(flwProcess)) {
            log.error("未找到流程定义：{}", processId);
            throw new CommonException("未知流程");
        }
        NodeModel node = flwProcess.model().getNode(nodeId);
        if (ObjectUtil.isNull(node)) {
            log.error("未找到节点：{}", nodeId);
            throw new CommonException("未知节点");
        }
        return BeanUtil.beanToMap(node, "nodeName", "callProcessKey", "type", "setType",
                "nodeAssigneeList", "examineLevel",
                "directorLevel", "selectMode", "term", "termMode", "examineMode",
                "directorMode", "passWeight", "remind", "allowSelection", "allowTransfer",
                "allowAppendNode", "allowRollback", "approveSelf", "extendConfig");
    }

    @Override
    public void comment(ProcessCommentReq params) {
        FlwHisInstance flwHisInstance = flwHisInstanceMapper.selectById(params.getInstanceId());
        if (ObjectUtil.isNull(flwHisInstance)) {
            log.error("未找到流程实例：{}", params.getInstanceId());
            throw new CommonException("未知实例");
        }
        // 动作执行记录
        WflowInstanceActionRecord record = new WflowInstanceActionRecord();
        record.setInstanceId(params.getInstanceId());
        record.setAuditorId(StpUtil.getLoginId().toString());
        record.setComment(params.getComment());
        record.setActionType(WflowActionType.comment.getValue());
        record.setAttachments(params.getAttachments());
        wflowInstanceActionRecordMapper.insert(record);
    }
}
