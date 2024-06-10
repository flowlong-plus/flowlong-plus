package cn.yuencode.flowlongplus.workflow.listener;

import cn.hutool.core.util.ObjUtil;
import cn.yuencode.flowlongplus.workflow.entity.WflowInstanceActionRecord;
import cn.yuencode.flowlongplus.workflow.enums.WflowActionType;
import cn.yuencode.flowlongplus.workflow.mapper.WflowInstanceActionRecordMapper;
import com.aizuda.bpm.engine.core.FlowCreator;
import com.aizuda.bpm.engine.core.FlowLongContext;
import com.aizuda.bpm.engine.core.enums.EventType;
import com.aizuda.bpm.engine.entity.FlwExtInstance;
import com.aizuda.bpm.engine.entity.FlwTask;
import com.aizuda.bpm.engine.listener.TaskListener;
import com.aizuda.bpm.engine.model.NodeAssignee;
import com.aizuda.bpm.engine.model.NodeModel;
import com.aizuda.bpm.engine.model.ProcessModel;
import com.aizuda.bpm.mybatisplus.mapper.FlwExtInstanceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 流程全局任务监听 同步方式
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
@Component
@Slf4j
public class FlowTaskListener implements TaskListener {
    @Resource
    private WflowInstanceActionRecordMapper wflowInstanceActionRecordMapper;
    @Resource
    private FlwExtInstanceMapper flwExtInstanceMapper;

    @Override
    public boolean notify(EventType eventType, Supplier<FlwTask> supplier, FlowCreator flowCreator) {
        if (eventType.equals(EventType.cc)) {
            FlwTask flwTask = supplier.get();
            // 查询流程模型
            FlwExtInstance flwExtInstance = flwExtInstanceMapper.selectById(flwTask.getInstanceId());
            String modelContent = flwExtInstance.getModelContent();
            ProcessModel processModel = FlowLongContext.parseProcessModel(modelContent, null, false);
            NodeModel node = processModel.getNode(flwTask.getTaskName());
            if (ObjUtil.isNull(node)) {
                log.error("节点不存在 TaskName:{} InstanceId:{}", flwTask.getTaskName(), flwTask.getInstanceId());
                return true;
            }
            // 动作执行记录
            WflowInstanceActionRecord record = new WflowInstanceActionRecord();
            record.setInstanceId(flwTask.getInstanceId().toString());
            record.setRobot(true);
            List<String> userIds = node.getNodeAssigneeList().stream()
                    .map(NodeAssignee::getId).collect(Collectors.toList());
            record.setUserIds(userIds);
            record.setActionType(WflowActionType.copy.getValue());
            // 暂时解决此处时间小于示例添加时间，后续再改进
            record.setCreateTime(LocalDateTime.now().plusSeconds(5));
            wflowInstanceActionRecordMapper.insert(record);
        }
        return true;
    }
}