package com.java3y.austin.handler.domain.wechat.robot;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 企业微信自定义机器人 入参
 * https://developer.work.weixin.qq.com/document/path/91770#%E6%96%87%E6%9C%AC%E7%B1%BB%E5%9E%8B
 *
 * @author 3y
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseWeChatRobotParam {

    /**
     * msgtype
     */
    @JSONField(name = "msgtype")
    private String msgType;
    /**
     * text
     */
    @JSONField(name = "text")
    private TextDTO text;
    /**
     * markdown
     */
    @JSONField(name = "markdown")
    private MarkdownDTO markdown;

    /**
     * markdown
     */
    @JSONField(name = "image")
    private ImageDTO image;
    /**
     * news
     */
    @JSONField(name = "news")
    private NewsDTO news;
    /**
     * file
     */
    @JSONField(name = "file")
    private FileDTO file;
    /**
     * templateCard
     */
    @JSONField(name = "template_card")
    private TemplateCardDTO templateCard;

    /**
     * TextDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TextDTO {
        /**
         * content
         */
        @JSONField(name = "content")
        private String content;
        /**
         * mentionedList
         */
        @JSONField(name = "mentioned_list")
        private List<String> mentionedList;
        /**
         * mentionedMobileList
         */
        @JSONField(name = "mentioned_mobile_list")
        private List<String> mentionedMobileList;
    }

    /**
     * MarkdownDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class MarkdownDTO {
        /**
         * content
         */
        @JSONField(name = "content")
        private String content;
    }

    /**
     * ImageDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class ImageDTO {
        /**
         * base64
         */
        @JSONField(name = "base64")
        private String base64;

        @JSONField(name = "md5")
        private String md5;
    }

    /**
     * NewsDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class NewsDTO {
        /**
         * articles
         */
        @JSONField(name = "articles")
        private List<ArticlesDTO> articles;

        /**
         * ArticlesDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class ArticlesDTO {
            /**
             * title
             */
            @JSONField(name = "title")
            private String title;
            /**
             * description
             */
            @JSONField(name = "description")
            private String description;
            /**
             * url
             */
            @JSONField(name = "url")
            private String url;
            /**
             * picurl
             */
            @JSONField(name = "picurl")
            private String picurl;
        }
    }

    /**
     * FileDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class FileDTO {
        /**
         * mediaId
         */
        @JSONField(name = "media_id")
        private String mediaId;
    }

    /**
     * TemplateCardDTO
     */
    @NoArgsConstructor
    @Data
    @Builder
    @AllArgsConstructor
    public static class TemplateCardDTO {
        /**
         * cardType
         */
        @JSONField(name = "card_type")
        private String cardType;
        /**
         * source
         */
        @JSONField(name = "source")
        private SourceDTO source;
        /**
         * mainTitle
         */
        @JSONField(name = "main_title")
        private MainTitleDTO mainTitle;
        /**
         * emphasisContent
         */
        @JSONField(name = "emphasis_content")
        private EmphasisContentDTO emphasisContent;
        /**
         * quoteArea
         */
        @JSONField(name = "quote_area")
        private QuoteAreaDTO quoteArea;
        /**
         * subTitleText
         */
        @JSONField(name = "sub_title_text")
        private String subTitleText;
        /**
         * horizontalContentList
         */
        @JSONField(name = "horizontal_content_list")
        private List<HorizontalContentListDTO> horizontalContentList;
        /**
         * jumpList
         */
        @JSONField(name = "jump_list")
        private List<JumpListDTO> jumpList;
        /**
         * cardAction
         */
        @JSONField(name = "card_action")
        private CardActionDTO cardAction;

        /**
         * SourceDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class SourceDTO {
            /**
             * iconUrl
             */
            @JSONField(name = "icon_url")
            private String iconUrl;
            /**
             * desc
             */
            @JSONField(name = "desc")
            private String desc;
            /**
             * descColor
             */
            @JSONField(name = "desc_color")
            private Integer descColor;
        }

        /**
         * MainTitleDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class MainTitleDTO {
            /**
             * title
             */
            @JSONField(name = "title")
            private String title;
            /**
             * desc
             */
            @JSONField(name = "desc")
            private String desc;
        }

        /**
         * EmphasisContentDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class EmphasisContentDTO {
            /**
             * title
             */
            @JSONField(name = "title")
            private String title;
            /**
             * desc
             */
            @JSONField(name = "desc")
            private String desc;
        }

        /**
         * QuoteAreaDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class QuoteAreaDTO {
            /**
             * type
             */
            @JSONField(name = "type")
            private Integer type;
            /**
             * url
             */
            @JSONField(name = "url")
            private String url;
            /**
             * appid
             */
            @JSONField(name = "appid")
            private String appid;
            /**
             * pagepath
             */
            @JSONField(name = "pagepath")
            private String pagepath;
            /**
             * title
             */
            @JSONField(name = "title")
            private String title;
            /**
             * quoteText
             */
            @JSONField(name = "quote_text")
            private String quoteText;
        }

        /**
         * CardActionDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class CardActionDTO {
            /**
             * type
             */
            @JSONField(name = "type")
            private Integer type;
            /**
             * url
             */
            @JSONField(name = "url")
            private String url;
            /**
             * appid
             */
            @JSONField(name = "appid")
            private String appid;
            /**
             * pagepath
             */
            @JSONField(name = "pagepath")
            private String pagepath;
        }

        /**
         * HorizontalContentListDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class HorizontalContentListDTO {
            /**
             * keyname
             */
            @JSONField(name = "keyname")
            private String keyname;
            /**
             * value
             */
            @JSONField(name = "value")
            private String value;
            /**
             * type
             */
            @JSONField(name = "type")
            private Integer type;
            /**
             * url
             */
            @JSONField(name = "url")
            private String url;
            /**
             * mediaId
             */
            @JSONField(name = "media_id")
            private String mediaId;
        }

        /**
         * JumpListDTO
         */
        @NoArgsConstructor
        @Data
        @Builder
        @AllArgsConstructor
        public static class JumpListDTO {
            /**
             * type
             */
            @JSONField(name = "type")
            private Integer type;
            /**
             * url
             */
            @JSONField(name = "url")
            private String url;
            /**
             * title
             */
            @JSONField(name = "title")
            private String title;
            /**
             * appid
             */
            @JSONField(name = "appid")
            private String appid;
            /**
             * pagepath
             */
            @JSONField(name = "pagepath")
            private String pagepath;
        }
    }
}
