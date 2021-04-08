package org.sang;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.internal.Utils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sang.config.utils.StringUtils;
import org.sang.dataobject.UserDO;
import org.sang.mongodb.dataobject.ArticleDO;
import org.sang.vo.ArticleDataObject;
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
import java.util.Random;


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
    public void testFormLogin() throws Exception {
        UserDO userDO = new UserDO();
        userDO.setPassword("chuckchan");
        userDO.setUsername("chuck");

        String body = "{\"username\":\"chuck\",\"password\":\"chuckchan\"}";
        log.info("testFormLogin body:" + body);

        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        log.info("testFormLogin response:" + response);
    }

    @Test
    public void testLogin() throws Exception {
//        param("username", "sang")
//                .param("password", "123")
        UserDO userDO = new UserDO();
        userDO.setPassword("chuckchan");
        userDO.setUsername("chuck");
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
//                .header("token", "eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSKitNTcrJT1fSUUosTQHykzNKk7OBvNSKAiUrQzMjQ2MzQzNDUx2lzMQSkIChuTlUoCg_JxWowdHF19NPqRYAYK-x-1AAAAA.0HGOj3MnUsEkghrwPZOnFbM_gtZnxE_NTd9j0Wiojk8")
                .content(objectMapper.writeValueAsString(userDO)));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        log.info("testLogin response:" + response);
        if (!Utils.isEmpty(response)) {
            CommonResult<UserDO> commonResult = objectMapper.readValue(response, CommonResult.class);
            assert (commonResult != null && commonResult.isSuccess() && commonResult.getData() != null);
            UserDO loginResult = objectMapper.convertValue(commonResult.getData(), UserDO.class);
            assert (loginResult != null && !StringUtils.isEmpty(loginResult.getToken()));
        }
    }

    @Test
    public void testLogout() throws Exception {
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/logout")
                .header("token", "eyJhbGciOiJIUzI1NiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWyiwuVrJSKitNTcrJT1fSUUosTQHykzNKk7OBvNSKAiUrQzMjQ2MzQzNDUx2lzMQSkIChuTlUoCg_JxWowdHF19NPqRYAYK-x-1AAAAA.0HGOj3MnUsEkghrwPZOnFbM_gtZnxE_NTd9j0Wiojk8"));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        log.info("testLogout response:" + response);
        assert (!StringUtils.isEmpty(response));
        if (!Utils.isEmpty(response)) {
            CommonResult commonResult = objectMapper.readValue(response, CommonResult.class);
            assert (commonResult != null && commonResult.isSuccess());
        }
    }

    @Test
    public void testRegister() throws Exception {
        UserDO userDO = new UserDO();
        userDO.setUsername("chuck");
        userDO.setCreateTime(new Date());
        userDO.setNickname("chuck");
        userDO.setEnable(true);
        userDO.setPassword("chuckchan");
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDO)));
        // 校验结果
        String response = resultActions.andReturn().getResponse().getContentAsString();
        log.info("testRegister response:" + response);
        if (!Utils.isEmpty(response)) {
            CommonResult<UserDO> commonResult = objectMapper.readValue(response, CommonResult.class);
            assert (commonResult != null && commonResult.isSuccess() && commonResult.getData() != null);
            UserDO loginResult = objectMapper.convertValue(commonResult.getData(), UserDO.class);
            assert (loginResult != null && !StringUtils.isEmpty(loginResult.getToken()));
        }
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
        articleDO.setTitle("flutter tutorial");
        articleDO.setType("blog");
        Random random = new Random();
        int randomNum = random.nextInt((ArticleDO.STATUS_ALL - ArticleDO.STATUS_DELETED) + 1) + ArticleDO.STATUS_DELETED;
        log.info("random status " + randomNum);
        articleDO.setStatus(randomNum);
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

    @Test
    public void testGetByStatus() throws Exception {
        Random random = new Random();
        int randomNum = random.nextInt((ArticleDO.STATUS_ALL - ArticleDO.STATUS_DELETED) + 1) + ArticleDO.STATUS_DELETED;
        log.info("random get status " + randomNum);
        ResultActions resultActions = mvc.perform(MockMvcRequestBuilders
                .get("/article/status")
                .param("status", "" + randomNum)
                .param("pageIndex", "1")
                .param("pageSize", "3"));
        String response = resultActions.andReturn().getResponse().getContentAsString();
        log.info("testGetByStatus response:" + response);
        if (!Utils.isEmpty(response)) {
            CommonResult<ArticleDataObject> commonResult = (CommonResult<ArticleDataObject>) objectMapper.readValue(response, CommonResult.class);
            assert (commonResult != null && commonResult.isSuccess() && commonResult.getData() != null);
            ArticleDataObject articleDataObject = objectMapper.convertValue(commonResult.getData(), ArticleDataObject.class);
            assert (articleDataObject != null);
            List<ArticleDO> articleResult = articleDataObject.getArticleDOList();
            log.info("testGetByStatus result size:" + articleResult.size() + ",pageIndex:" + articleDataObject.getPageIndex() + ",pageSize:" + articleDataObject.getPageSize());
            assert (articleResult != null && articleResult.size() > 0);
        }
    }
}