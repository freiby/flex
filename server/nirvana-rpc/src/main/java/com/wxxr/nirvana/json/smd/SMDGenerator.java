package com.wxxr.nirvana.json.smd;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.wxxr.nirvana.json.JSONUtil;
import com.wxxr.nirvana.json.annotations.SMD;
import com.wxxr.nirvana.json.annotations.SMDMethod;
import com.wxxr.nirvana.json.annotations.SMDMethodParameter;

import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

public class SMDGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(SMDGenerator.class);

    // rootObject is based on OGNL expression (action by default)
    private Object rootObject;
    private List<Pattern> excludeProperties;
    private boolean ignoreInterfaces;

    public SMDGenerator(Object root, List<Pattern> excludeProperties, boolean ignoreInterfaces) {
        this.rootObject = root;
        this.excludeProperties = excludeProperties;
        this.ignoreInterfaces = ignoreInterfaces;
    }

    public com.wxxr.nirvana.json.smd.SMD generate(ActionInvocation actionInvocation) {
        ActionContext actionContext = actionInvocation.getInvocationContext();
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);

        Class clazz = rootObject.getClass();
        com.wxxr.nirvana.json.smd.SMD smd = new com.wxxr.nirvana.json.smd.SMD();
        // URL
        smd.setServiceUrl(request.getRequestURI());

        // customize SMD
        com.wxxr.nirvana.json.annotations.SMD smdAnnotation = (SMD) clazz.getAnnotation(SMD.class);
        if (smdAnnotation != null) {
            smd.setObjectName(smdAnnotation.objectName());
            smd.setServiceType(smdAnnotation.serviceType());
            smd.setVersion(smdAnnotation.version());
        }

        // get public methods
        Method[] methods = JSONUtil.listSMDMethods(clazz, ignoreInterfaces);

        for (Method method : methods) {
            processAnnotatedMethod(smd, method);
        }
        return smd;

    }

    private void processAnnotatedMethod(com.wxxr.nirvana.json.smd.SMD smd, Method method) {
        SMDMethod smdMethodAnnotation = method.getAnnotation(SMDMethod.class);
        // SMDMethod annotation is required
        if (shouldProcessMethod(method, smdMethodAnnotation)) {
            String methodName = readMethodName(method, smdMethodAnnotation);
            com.wxxr.nirvana.json.smd.SMDMethod smdMethod = new com.wxxr.nirvana.json.smd.SMDMethod(methodName);
            smd.addSMDMethod(smdMethod);

            // find params for this method
            processMethodsParameters(method, smdMethod);

        } else if(LOG.isDebugEnabled()) {
            LOG.debug("Ignoring property " + method.getName());
        }
    }

    private void processMethodsParameters(Method method, com.wxxr.nirvana.json.smd.SMDMethod smdMethod) {
        int parametersCount = method.getParameterTypes().length;
        if (parametersCount > 0) {

            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for (int i = 0; i < parametersCount; i++) {
                processParameter(smdMethod, parameterAnnotations[i], i);
            }
        }
    }

    private void processParameter(com.wxxr.nirvana.json.smd.SMDMethod smdMethod, Annotation[] parameterAnnotation, int i) {
        // are you ever going to pick shorter names? nope
        SMDMethodParameter smdMethodParameterAnnotation = getSMDMethodParameterAnnotation(parameterAnnotation);
        String paramName = buildParamName(i, smdMethodParameterAnnotation);
        smdMethod.addSMDMethodParameter(new com.wxxr.nirvana.json.smd.SMDMethodParameter(paramName));
    }

    private String buildParamName(int i, SMDMethodParameter smdMethodParameterAnnotation) {
        return smdMethodParameterAnnotation != null ? smdMethodParameterAnnotation.name() : "p" + i;
    }

    private String readMethodName(Method method, SMDMethod smdMethodAnnotation) {
        return smdMethodAnnotation.name().length() == 0 ? method.getName() : smdMethodAnnotation.name();
    }

    private boolean shouldProcessMethod(Method method, SMDMethod smdMethodAnnotation) {
        return ((smdMethodAnnotation != null) && !this.shouldExcludeProperty(method.getName()));
    }

    private boolean shouldExcludeProperty(String expr) {
        if (this.excludeProperties != null) {
            for (Pattern pattern : this.excludeProperties) {
                if (pattern.matcher(expr).matches())
                    return true;
            }
        }
        return false;
    }

    /**
     * Find an SMDethodParameter annotation on this array
     */
    private com.wxxr.nirvana.json.annotations.SMDMethodParameter getSMDMethodParameterAnnotation(
            Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof com.wxxr.nirvana.json.annotations.SMDMethodParameter)
                return (com.wxxr.nirvana.json.annotations.SMDMethodParameter) annotation;
        }

        return null;
    }

}
