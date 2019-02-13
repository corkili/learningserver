package com.corkili.learningserver.bo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private Long id;

    private Date createTime;

    private Date updateTime;

    /**
     * Format: (In PO)
     *   if is a image, store image path, start with {##image##}
     */
    private String content;

    private boolean isImage;

    private User receiver;

    private User sender;

    public void setContent(String formatContent) {
        if (formatContent.startsWith("{##image##}")) {
            this.isImage = true;
            formatContent = formatContent.substring("{##image##}".length());
        } else {
            this.isImage = false;
        }
        this.content = formatContent;
    }

    public void setContent(String content, boolean isImage) {
        this.content = content;
        this.isImage = isImage;
    }

    public String getFormatContent() {
        String prefix = "";
        if (isImage) {
            prefix += "{##image##}";
        }
        return prefix + this.content;
    }

}
