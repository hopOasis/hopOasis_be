package com.example.hop_oasis;

import static org.springframework.aot.hint.MemberCategory.DECLARED_FIELDS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_METHODS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS;
import static org.springframework.aot.hint.MemberCategory.PUBLIC_FIELDS;

import com.amazonaws.serverless.proxy.model.HttpApiV2AuthorizerMap;
import com.amazonaws.serverless.proxy.model.HttpApiV2AuthorizerMap.HttpApiV2AuthorizerDeserializer;
import com.amazonaws.serverless.proxy.model.HttpApiV2AuthorizerMap.HttpApiV2AuthorizerSerializer;
import com.amazonaws.serverless.proxy.model.HttpApiV2HttpContext;
import com.amazonaws.serverless.proxy.model.HttpApiV2JwtAuthorizer;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequestContext;
import org.hibernate.sql.results.graph.FetchParent;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

public class GraalVmRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(StreamLambdaHandler.class);
        registerClass(hints, HttpApiV2ProxyRequest.class);
        registerClass(hints, HttpApiV2ProxyRequestContext.class);
        registerClass(hints, HttpApiV2HttpContext.class);
        registerClass(hints, HttpApiV2AuthorizerMap.class);
        registerClass(hints, HttpApiV2JwtAuthorizer.class);
        registerClass(hints, HttpApiV2AuthorizerDeserializer.class);
        registerClass(hints, HttpApiV2AuthorizerSerializer.class);
        registerClass(hints, FetchParent.class);
    }

    private void registerClass(RuntimeHints hints, Class<?> clazz) {
        hints.reflection().registerType(clazz, hint ->
            hint.withMembers(
                INVOKE_DECLARED_CONSTRUCTORS,
                INVOKE_PUBLIC_CONSTRUCTORS,
                INVOKE_DECLARED_METHODS,
                INVOKE_PUBLIC_METHODS,
                DECLARED_FIELDS,
                PUBLIC_FIELDS
            )
        );
    }

}
