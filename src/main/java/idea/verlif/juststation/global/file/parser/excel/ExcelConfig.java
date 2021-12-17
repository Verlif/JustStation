package idea.verlif.juststation.global.file.parser.excel;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Verlif
 * @version 1.0
 * @date 2021/12/17 17:04
 */
@Configuration
public class ExcelConfig {

    @Bean
    @ConditionalOnMissingBean(CellHandler.class)
    public CellHandler cellHandler() {
        return new CellHandlerAto();
    }
}
