package focus.editor.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author sunc
 * @date 2019/10/29 9:56
 * @description Entity
 */

public interface Entity {

    /**
     * 通用返回 json 字符串, 格式化日期属性
     *
     * @return 返回 json 字符串
     */
    default String toJsonString() {
        return JSON.toJSONString(this, SerializerFeature.WriteDateUseDateFormat);
    }

}
