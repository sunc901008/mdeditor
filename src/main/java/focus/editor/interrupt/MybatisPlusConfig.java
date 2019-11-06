package focus.editor.interrupt;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import focus.editor.context.TenantContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author miemie
 * @since 2018-08-10
 */
@Configuration
@MapperScan(basePackages = {"focus.editor.mapper"})
public class MybatisPlusConfig {
    private static final String SYSTEM_TENANT_ID = "tenant_id";
    private static final List<String> IGNORE_TENANT_TABLES = Arrays.asList("tenant_user", "md_editor");

    private final TenantContext tenantContext;

    @Autowired
    public MybatisPlusConfig(TenantContext tenantContext) {
        this.tenantContext = tenantContext;
    }

    /**
     * 多租户属于 SQL 解析部分，依赖 MP 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new MyTenantParser().setTenantHandler(new TenantHandler() {

            @Override
            public Expression getTenantId(boolean where) {
                int tenantId = tenantContext.getCurrentTenantId();
                return new LongValue(tenantId);
            }

            @Override
            public String getTenantIdColumn() {
                return SYSTEM_TENANT_ID;
            }

            @Override
            public boolean doTableFilter(String tableName) {
                // 这里可以判断是否过滤表
                return IGNORE_TENANT_TABLES.contains(tableName);
            }

        });

        sqlParserList.add(tenantSqlParser);
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }
}
