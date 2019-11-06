package focus.editor;

import org.apache.log4j.PropertyConfigurator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * @author sunc
 * @date 2019/10/9 10:46
 * @description Main
 */
@SpringBootApplication
@MapperScan(basePackages = {"focus.editor.mapper"})
public class Main {

    public static void main(String[] args) {
        PropertyConfigurator.configure(Main.class.getResourceAsStream("/conf/log4j.properties"));

        SpringApplication app = new SpringApplication(Main.class);

        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
//        FileSystemResource res = new FileSystemResource("G:\\png\\application.yml");
        Resource res = new ClassPathResource("application.yml");
        yaml.setResources(res);
        Properties defaultProperties = yaml.getObject();
        if (defaultProperties != null) {
            app.setDefaultProperties(defaultProperties);
        }

        app.run(args);

    }

}