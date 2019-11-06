package focus.editor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import focus.editor.entity.MdEditor;
import org.apache.ibatis.annotations.Select;

/**
 * @author sunc
 * @date 2019/11/6 10:30
 * @description MdEditorMapper
 */

public interface MdEditorMapper extends BaseMapper<MdEditor> {

    /**
     * select md by ip
     *
     * @param ip ip
     * @return editor
     */
    @Select({"select * from md_editor where ip = #{ip}"})
    MdEditor selectByIp(String ip);

}
