package com.duol.util;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Duolaimon
 * 18-7-25 下午5:47
 */
public class FTPUtilTest {

    @Test
    public void uploadFile() throws IOException {
        File file = new File("/home/deity/图片/bbb.jpg");
        FTPUtil.uploadFile(Lists.newArrayList(file));
    }
}