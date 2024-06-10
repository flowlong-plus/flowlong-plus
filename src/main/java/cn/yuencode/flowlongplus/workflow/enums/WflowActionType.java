package cn.yuencode.flowlongplus.workflow.enums;

/**
 * 流程动作枚举
 *
 * <p>
 * 尊重知识产权，不允许非法使用，后果自负
 * </p>
 *
 * @author 贾小宇
 * @since 1.0
 */
public enum WflowActionType {
    start(0), // "发起"
    auto_rejected(1), // "自动拒绝"
    auto_approved(2), // "自动通过"
    rejected(3), // "拒绝"
    approved(4), // "通过"
    canceled(5), // "撤销"
    assign(6), // "转交"
    back(7), // "回退"
    add_sign(8), // "加签"
    del_sign(9), // "减签"
    add_before_sign(10), // "前加签"
    add_after_sign(11), // "后加签"
    copy(12), // "抄送"
    forward(13), // "转发"
    comment(14), // "评论"
    transact(15); // "办理"

    private final int value;

    WflowActionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
