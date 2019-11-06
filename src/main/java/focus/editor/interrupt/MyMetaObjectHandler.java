package focus.editor.interrupt;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author sunc
 * @date 2019/10/29 8:58
 * @description MyMetaObjectHandler
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String FOREIGN_KEY = "pk";

    /**
     * insert 操作时自动填充字段值
     *
     * @param metaObject meta
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        if (metaObject.hasSetter(CREATE_TIME)) {
            // 自动填充 create_time 为当前时间
            setInsertFieldValByName(CREATE_TIME, Calendar.getInstance().getTime(), metaObject);
        }

        if (metaObject.hasSetter(UPDATE_TIME)) {
            // 自动填充 update_time 为当前时间
            setInsertFieldValByName(UPDATE_TIME, Calendar.getInstance().getTime(), metaObject);
        }

        if (metaObject.hasSetter(FOREIGN_KEY)) {
            // 外键, 获取 bigint IdWorker.getId(), 自动填充
            setInsertFieldValByName(FOREIGN_KEY, IdWorker.getId(), metaObject);
        }

    }

    /**
     * update 操作时自动更新 update_time
     *
     * @param metaObject meta
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasSetter(UPDATE_TIME)) {
            setUpdateFieldValByName(UPDATE_TIME, Calendar.getInstance().getTime(), metaObject);
        }
    }

}