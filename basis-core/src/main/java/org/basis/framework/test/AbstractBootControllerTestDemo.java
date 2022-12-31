package org.basis.framework.test;

import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(classes = {
//        JPAConfig.class,
//        RedisConfig.class,
})
// 需要加载的Controller
@WebMvcTest({
//        CompanyOrgController.class
})
// 模拟一个bean
@MockBeans({
//        @MockBean(XxlJobConfig.class),
})
@ActiveProfiles("test")
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class))
@Transactional
@Commit
public class AbstractBootControllerTestDemo extends BaseTest {
}
