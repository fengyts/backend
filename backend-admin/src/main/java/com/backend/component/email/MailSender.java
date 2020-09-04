package com.backend.component.email;

public interface MailSender {

    /**
     * 发送简单文本邮件
     * @param to 收件人
     * @param subject 主题
     * @param content 正文
     * @return
     */
    boolean sendSingleMail(String[] to, String subject, String content);

    /**
     * 发送HTML邮件
     * @param to 收件人
     * @param subject 主题
     * @param html HTML格式文本
     * @return
     */
    boolean sendHtmlMail(String[] to, String subject, String html);

    boolean sendInlineFileMail(String[] to, String subject, String html, String filePath);

    /**
     * 发送本地附件邮件
     * @param to
     * @param subject
     * @param html
     * @param filePath
     * @return
     */
    boolean sendAttachedFileMail(String[] to, String subject, String html, String filePath);

    /**
     *  发送网络资源文件附件邮件
     * @param to
     * @param subject
     * @param html
     * @param fileName 文件名称，需要携带文件后缀名
     * @param fileUrl 网络资源文件url地址
     * @return
     */
    void sendAttachedUriFileMail(String[] to, String subject, String html, String fileName, String fileUrl);

}
