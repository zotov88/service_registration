package serviceregistration.MVC.controller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix="page.config.size")
public class PageConfig {

    private int pageDefault;

    private int pageSize;
}
