package org.sang;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sang.bean.User;
import org.sang.mongodb.dataobject.ArticleDO;
import org.sang.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogserverApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class BlogserverApplicationTests {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mvc;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testLogin() throws Exception {
//        param("username", "sang")
//                .param("password", "123")
        User user = new User();
        user.setPassword("123");
        user.setUsername("sang");
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        log.info("testLogin response:" + response);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()); // 响应状态码 200
    }

    @Test
    public void testInsertArticle() throws Exception {
        ArticleDO articleDO = new ArticleDO();
        articleDO.setCreateTime(new Date());
        articleDO.setAuthor("chuckchan");
        articleDO.setContent("haha");
        articleDO.setPublishDate(dateFormat.format(new Date()));
        articleDO.setSubTitle("flutter demo test");
        articleDO.setSummary("flutter");
        articleDO.setTags("flutter");
        articleDO.setTitle("flutter 教程");
        articleDO.setType("blog");
        articleDO.setStatus(ArticleDO.STATUS_STORED);

        // 获得指定用户编号的用户
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/article/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleDO)));
        // 校验结果
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()); // 响应状态码 200
    }

    @Test
    public void testGetArticle() throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/article/blog"));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        log.info("testGetArticle response:" + response);
        if (!Utils.isEmpty(response)) {
            CommonResult<List<ArticleDO>> commonResult = objectMapper.readValue(response, CommonResult.class);
            assert (commonResult != null && commonResult.isSuccess() && commonResult.getData() != null);
            List<ArticleDO> articleResult = commonResult.getData();
            assert (articleResult != null && articleResult.size() > 0);
        }
    }

    @Test
    public void testUpdateArticle() throws Exception {
        ArticleDO articleDO = new ArticleDO();
        articleDO.setCreateTime(new Date());
        articleDO.setAuthor("chuckchan");
        articleDO.setContent("haha");
        articleDO.setPublishDate(dateFormat.format(new Date()));
        articleDO.setSubTitle("flutter demo test");
        articleDO.setSummary("flutter");
        articleDO.setTags("flutter");
        articleDO.setTitle("flutter tutorial");
        articleDO.setType("blog");
        articleDO.setId(3);

        // 获得指定用户编号的用户
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/article/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleDO)));
        // 校验结果
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()); // 响应状态码 200
    }
}