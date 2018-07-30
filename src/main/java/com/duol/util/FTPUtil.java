package com.duol.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Created by geely
 */
public class FTPUtil {

    private static  final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip,int port,String user,String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }
    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("imgs",fileList);
        logger.info("结束上传,上传结果:{}",result);
        return result;
    }

    public static Path createDirectory(String path) {
        File fileDir = new File(path);
        Path rootLocation = fileDir.toPath();
        if (!Files.isDirectory(rootLocation)) {
            try {
                Files.createDirectory(rootLocation);
            } catch (IOException e) {
                logger.error("create director fault:", e);
            }
        }
        return rootLocation;
    }


    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = true;
        InputStream fis = null;
        //连接FTP服务器
        if(connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//                ftpClient.enterLocalPassiveMode();   会出错
                ftpClient.setControlEncoding("UTF-8");
                boolean change = ftpClient.changeWorkingDirectory(remotePath);
                logger.info("ftp改变工作路径：{}",change);
                ftpClient.setBufferSize(1024);
                int replyCode = ftpClient.getReplyCode();
                logger.info("ftp.code:{}", replyCode);
                if(!FTPReply.isPositiveCompletion(replyCode)){
                    logger.error("连接失败");
                }

                for(File fileItem : fileList){
                    logger.info("文件名：{}",fileItem.getName());
                    fis = new FileInputStream(fileItem);
//                    OutputStream fos = ftpClient.storeFileStream(fileItem.getName());
//                    byte[] b = new byte[1024];
//                    int len;
//                    while ((len = fis.read(b)) != -1) {
//                        fos.write(b,0,len);
//                    }
                    boolean storeSuccess = ftpClient.storeFile(fileItem.getName(), fis);
                    if (!storeSuccess)logger.error("文件上传失败");
                }

            } catch (IOException e) {
                logger.error("上传文件异常",e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                Objects.requireNonNull(fis).close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }



    private boolean connectServer(String ip,int port,String user,String pwd){

        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip,port);
            isSuccess = ftpClient.login(user,pwd);
            if (!isSuccess) throw new IOException("登录失败");
        } catch (IOException e) {
            logger.error("连接FTP服务器异常",e);
        }
        return isSuccess;
    }











    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
