package com.java3y.austin.handler.action;

import com.java3y.austin.common.domain.TaskInfo;
import com.java3y.austin.common.dto.model.*;
import com.java3y.austin.common.pipeline.BusinessProcess;
import com.java3y.austin.common.pipeline.ProcessContext;
import com.java3y.austin.handler.config.SensitiveWordsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 敏感词过滤
 *
 * @author xiaoxiamao
 * @date 2024/08/17
 */
@Service
public class SensWordsAction implements BusinessProcess<TaskInfo> {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 过滤逻辑
     *
     * @param context
     *
     * @see com.java3y.austin.common.enums.ChannelType
     */
    @Override
    public void process(ProcessContext<TaskInfo> context) {
        // 获取敏感词典
        Set<String> sensDict = Optional.ofNullable(redisTemplate.opsForSet().members(SensitiveWordsConfig.SENS_WORDS_DICT))
                .orElse(Collections.emptySet());
        // 如果敏感词典为空，不过滤
        if (ObjectUtils.isEmpty(sensDict)) {
            return;
        }
        switch (context.getProcessModel().getMsgType()) {
            // IM
            case 10:
                // 无文本内容，暂不做过滤处理
                break;
            // PUSH
            case 20:
                PushContentModel pushContentModel =
                        (PushContentModel) context.getProcessModel().getContentModel();
                pushContentModel.setContent(filter(pushContentModel.getContent(), sensDict));
                break;
            // SMS
            case 30:
                SmsContentModel smsContentModel =
                        (SmsContentModel) context.getProcessModel().getContentModel();
                smsContentModel.setContent(filter(smsContentModel.getContent(), sensDict));
                break;
            // EMAIL
            case 40:
                EmailContentModel emailContentModel =
                        (EmailContentModel) context.getProcessModel().getContentModel();
                emailContentModel.setContent(filter(emailContentModel.getContent(), sensDict));
                break;
            // OFFICIAL_ACCOUNT
            case 50:
                // 无文本内容，暂不做过滤处理
                break;
            // MINI_PROGRAM
            case 60:
                // 无文本内容，暂不做过滤处理
                break;
            // ENTERPRISE_WE_CHAT
            case 70:
                EnterpriseWeChatContentModel enterpriseWeChatContentModel =
                        (EnterpriseWeChatContentModel) context.getProcessModel().getContentModel();
                enterpriseWeChatContentModel.setContent(filter(enterpriseWeChatContentModel.getContent(), sensDict));
                break;
            // DING_DING_ROBOT
            case 80:
                DingDingRobotContentModel dingDingRobotContentModel =
                        (DingDingRobotContentModel) context.getProcessModel().getContentModel();
                dingDingRobotContentModel.setContent(filter(dingDingRobotContentModel.getContent(), sensDict));
                break;
            // DING_DING_WORK_NOTICE
            case 90:
                DingDingWorkContentModel dingDingWorkContentModel =
                        (DingDingWorkContentModel) context.getProcessModel().getContentModel();
                dingDingWorkContentModel.setContent(filter(dingDingWorkContentModel.getContent(), sensDict));
                break;
            // ENTERPRISE_WE_CHAT_ROBOT
            case 100:
                EnterpriseWeChatRobotContentModel enterpriseWeChatRobotContentModel =
                        (EnterpriseWeChatRobotContentModel) context.getProcessModel().getContentModel();
                enterpriseWeChatRobotContentModel.setContent(filter(enterpriseWeChatRobotContentModel.getContent(), sensDict));
                break;
            // FEI_SHU_ROBOT
            case 110:
                FeiShuRobotContentModel feiShuRobotContentModel =
                        (FeiShuRobotContentModel) context.getProcessModel().getContentModel();
                feiShuRobotContentModel.setContent(filter(feiShuRobotContentModel.getContent(), sensDict));
                break;
            // ALIPAY_MINI_PROGRAM
            case 120:
                // 无文本内容，暂不做过滤处理
                break;
            default:
                break;
        }
    }

    /**
     * 敏感词替换成对应长度'*'
     *
     * @param content
     * @param sensDict
     * @return
     */
    private String filter(String content, Set<String> sensDict) {
        if (ObjectUtils.isEmpty(content) || ObjectUtils.isEmpty(sensDict)) {
            return content;
        }
        // 构建字典树
        TrieNode root = buildTrie(sensDict);
        StringBuilder result = new StringBuilder();
        int n = content.length();
        int i = 0;

        while (i < n) {
            TrieNode node = root;
            int j = i;
            int lastMatchEnd = -1;

            while (j < n && node != null) {
                node = node.children.get(content.charAt(j));
                if (node != null && node.isEnd) {
                    lastMatchEnd = j;
                }
                j++;
            }

            if (lastMatchEnd != -1) {
                // 找到敏感词，用'*'替换
                for (int k = i; k <= lastMatchEnd; k++) {
                    result.append('*');
                }
                i = lastMatchEnd + 1;
            } else {
                result.append(content.charAt(i));
                i++;
            }
        }

        return result.toString();
    }

    /**
     * 构建字典树
     *
     * @param sensDict
     * @return
     */
    private TrieNode buildTrie(Set<String> sensDict) {
        TrieNode root = new TrieNode();
        for (String word : sensDict) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.isEnd = true;
        }
        return root;
    }

    /**
     * 树节点
     */
    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        // 是否为叶子节点
        boolean isEnd = false;
    }

}
