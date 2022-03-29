package org.example.databench.web.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

/**
 * Created by shuangbofu on 2019-06-05 15:18
 *
 * @author shuangbofu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Controller
@ResultBody
@RequestMapping
public @interface ResultController {
    @AliasFor(annotation = RequestMapping.class, attribute = "path")
    String[] value() default {};
}
