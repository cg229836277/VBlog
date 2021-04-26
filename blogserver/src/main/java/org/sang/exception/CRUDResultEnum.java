package org.sang.exception;

public enum CRUDResultEnum {
    //文章处理相关
    UPDATE_ARTICLE_FAIL(1001004001, "文章更新失败"),
    GET_ARTICLE_FAIL(1001004002, "文章获取失败"),
    DELETE_ARTICLE_FAIL(1001004003, "删除文章失败"),
    RESTORE_ARTICLE_FAIL(1001004004, "恢复文章失败"),


    //文章栏目处理相关
    ADD_CATEGORY_FAIL(1001005001, "栏目保存失败"),

    UPDATE_CATEGORY_FAIL(1001005002, "栏目更新失败"),

    DELETE_CATEGORY_FAIL(1001005003, "栏目删除失败"),

    GET_CATEGORY_FAIL(1001005004, "栏目获取失败"),

    //音乐列表相关
    GET_MUSIC_LIST_FAIL(1001006001, "音乐列表获取失败"),
    DELETE_MUSIC_LIST_FAIL(1001006002, "音乐删除失败"),
    ADD_MUSIC_LIST_FAIL(1001006003, "音乐添加失败"),
    ;

    /**
     * 错误码
     */
    private int code;
    /**
     * 错误提示
     */
    private String message;

    CRUDResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
