package com.leancloud.modules.feedback;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import java.io.File;
import java.util.Date;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;


@JSONType(asm = false)
public class Comment {

  public enum CommentType {
    DEV("dev"), USER("user");
    String type;

    private CommentType(String type) {
      this.type = type;
    }

    @Override
    public String toString() {
      return this.type;
    }
  }

  public Comment() {
    this(null, CommentType.USER);
  }

  public Comment(String comment, CommentType type) {
    this.content = comment;
    this.commentType = type;
    createdAt = new Date();
  }

  public Comment(String commentText) {
    this(commentText, CommentType.USER);
  }

  public Comment(File attachment) throws AVException {
    this(null, CommentType.USER);
    this.setAttachment(attachment);
  }

  Date createdAt;
  String objectId;
  String content;
  CommentType commentType;
  boolean synced = false;
  String type;

  AVFile attachment;

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  protected String getObjectId() {
    return objectId;
  }

  protected void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public CommentType getCommentType() {
    return commentType;
  }

  public void setType(String type) {
    this.type = type;
    if (CommentType.DEV.toString().equalsIgnoreCase(type)) {
      this.commentType = CommentType.DEV;
    } else {
      this.commentType = CommentType.USER;
    }
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCommentType(CommentType type) {
    this.commentType = type;
  }

  protected boolean isSynced() {
    return synced;
  }

  protected void setSynced(boolean synced) {
    this.synced = synced;
  }

  public AVFile getAttachment() {
    return attachment;
  }

  /**
   * 上传图片作为一个反馈信息
   * 
   * 这里只支持图片文件上传，非图片的文件会抛出一个AVException
   * 
   * @param attachment
   * @throws AVException
   */
  @JSONField(serialize = false)
  public void setAttachment(File attachment) throws AVException {
    if (AVFile.getMimeType(attachment.getAbsolutePath()).toLowerCase().startsWith("image")) {
      try {
        this.attachment = AVFile.withFile(attachment.getName(), attachment);
      } catch (Exception e) {
        throw new AVException(e);
      }
    } else {
      throw new AVException(-1, "Only image file supported");
    }
  }

  protected void setAttachment(AVFile attachment) throws AVException {
    this.attachment = attachment;
  }
}
