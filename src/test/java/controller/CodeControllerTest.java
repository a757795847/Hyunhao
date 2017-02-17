package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.wx.AesException;
import com.zy.gcode.utils.wx.WXBizMsgCrypt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by admin5 on 17/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
public class CodeControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void geAppid() throws Exception {

    }
   @Test
    public void code() throws Exception{
       File file = new File("/Users/admin5/play/ExportOrderList201612281719.csv");
       System.out.println(file.exists());
       FileInputStream inputStream = new FileInputStream(file);
       MockMultipartFile multipartFile = new MockMultipartFile("file",inputStream);
        this.mockMvc.perform(fileUpload("/order/parseCsv").file(multipartFile).
                accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void login() throws Exception{
        this.mockMvc.perform(post("/operator/login").content("{\"username\":\"zhuiyou123\",\"password\":\"123456\"}").contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print());
    }


}
