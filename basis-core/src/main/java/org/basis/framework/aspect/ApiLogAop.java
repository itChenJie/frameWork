package org.basis.framework.aspect;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author ChenWenJie
 * @Data 2021/12/3 3:20 下午
 **/
@Slf4j
@Aspect
@Component
@Order(1)
public class ApiLogAop implements Ordered {

    @Pointcut("@annotation(org.basis.framework.annotation.Log) ||@within(org.basis.framework.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        doBefore(joinPoint);
        Object result = joinPoint.proceed();
        // 打印出参
        log.info("Controller Response : {}", JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        log.info("request end:-------------------------------");
        return result;
    }

    private void doBefore(ProceedingJoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //获取对应方法
        Method m = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //判断代理对象本身是否是连接点所在的目标对象，不是的话就要通过反射重新获取真实方法
        if (joinPoint.getThis().getClass() != joinPoint.getTarget().getClass()) {
            m = ReflectUtil.getMethod(joinPoint.getTarget().getClass(), m.getName(), m.getParameterTypes());
        }
        //通过真实方法获取该方法的参数名称
        LocalVariableTableParameterNameDiscoverer paramNames = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = paramNames.getParameterNames(m);
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        if (parameterNames!=null){
            for (int i = 0; i < parameterNames.length; i++) {
                //如果使用requestParam接受参数，加了require=false，这里会存现不存在的现象 TODO
                if (ObjectUtils.isEmpty(args[i])
                        || (args[i] instanceof HttpServletResponse)
                        || (args[i] instanceof HttpServletRequest)){
                    continue;
                }
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.convertValue(args[i],args[i].getClass());
                params.put(parameterNames[i],JSON.toJSON(objectMapper.convertValue(args[i],args[i].getClass())));
            }
        }
        log.info("request start:-------------------------------");
        log.info("url : " + request.getRequestURL().toString());
        log.info("http_method : " + request.getMethod());
        log.info("ip : " + request.getRemoteAddr());
        log.info("class_method : " + joinPoint.getSignature().getDeclaringTypeName()+ "." + joinPoint.getSignature().getName());
        log.info("args-json : " + params);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
