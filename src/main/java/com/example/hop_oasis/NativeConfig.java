package com.example.hop_oasis;

import com.amazonaws.serverless.proxy.model.HttpApiV2AuthorizerMap;
import com.amazonaws.serverless.proxy.model.HttpApiV2AuthorizerMap.HttpApiV2AuthorizerDeserializer;
import com.amazonaws.serverless.proxy.model.HttpApiV2AuthorizerMap.HttpApiV2AuthorizerSerializer;
import com.amazonaws.serverless.proxy.model.HttpApiV2HttpContext;
import com.amazonaws.serverless.proxy.model.HttpApiV2JwtAuthorizer;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequestContext;
import io.jsonwebtoken.impl.DefaultClaimsBuilder;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtHeaderBuilder;
import io.jsonwebtoken.impl.DefaultJwtParser;
import io.jsonwebtoken.impl.DefaultJwtParserBuilder;
import io.jsonwebtoken.impl.io.StandardCompressionAlgorithms;
import io.jsonwebtoken.impl.security.DefaultDynamicJwkBuilder;
import io.jsonwebtoken.impl.security.DefaultJwkParserBuilder;
import io.jsonwebtoken.impl.security.DefaultJwkSetBuilder;
import io.jsonwebtoken.impl.security.DefaultJwkSetParserBuilder;
import io.jsonwebtoken.impl.security.DefaultKeyOperationBuilder;
import io.jsonwebtoken.impl.security.DefaultKeyOperationPolicyBuilder;
import io.jsonwebtoken.impl.security.JwksBridge;
import io.jsonwebtoken.impl.security.KeysBridge;
import io.jsonwebtoken.impl.security.StandardEncryptionAlgorithms;
import io.jsonwebtoken.impl.security.StandardKeyAlgorithms;
import io.jsonwebtoken.impl.security.StandardKeyOperations;
import io.jsonwebtoken.impl.security.StandardSecureDigestAlgorithms;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.hibernate.sql.results.graph.FetchParent;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Configuration;

@RegisterReflectionForBinding({
    HttpApiV2ProxyRequest.class,
    HttpApiV2ProxyRequestContext.class,
    HttpApiV2HttpContext.class,
    HttpApiV2AuthorizerMap.class,
    HttpApiV2JwtAuthorizer.class,
    HttpApiV2AuthorizerDeserializer.class,
    HttpApiV2AuthorizerSerializer.class,
    FetchParent.class,
    DefaultJwtParser.class,
    DefaultClaimsBuilder.class,
    StandardSecureDigestAlgorithms.class,
    StandardKeyOperations.class,
    SignatureAlgorithm.class,
    StandardEncryptionAlgorithms.class,
    StandardKeyAlgorithms.class,
    StandardCompressionAlgorithms.class,
    Keys.class,
    KeysBridge.class,
    DefaultJwtHeaderBuilder.class,
    DefaultJwtBuilder.class,
    DefaultJwtParserBuilder.class,
    DefaultKeyOperationBuilder.class,
    DefaultKeyOperationPolicyBuilder.class,
    DefaultDynamicJwkBuilder.class,
    DefaultJwkParserBuilder.class,
    DefaultJwkSetBuilder.class,
    DefaultJwkSetParserBuilder.class,
    JwksBridge.class
})
@Configuration
public class NativeConfig {

}
