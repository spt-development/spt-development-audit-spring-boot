package com.spt.development.audit.spring.boot.autoconfigure;

import com.spt.development.audit.spring.AuditEventWriter;
import com.spt.development.audit.spring.CorrelationIdProvider;
import com.spt.development.audit.spring.DefaultCorrelationIdProvider;
import com.spt.development.audit.spring.JmsAuditEventWriter;
import com.spt.development.audit.spring.Slf4jAuditEventWriter;
import com.spt.development.audit.spring.aop.Auditor;
import com.spt.development.audit.spring.security.AuthenticationAdapterFactory;
import com.spt.development.audit.spring.security.DefaultAuthenticationAdapterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;

import java.util.function.Function;

/**
 * {@link AutoConfiguration Auto-Configuration} for
 * <a href="https://github.com/spt-development/spt-development-audit-spring">spt-development/spt-development-audit-spring</a>.
 */
@AutoConfiguration
@EnableConfigurationProperties(AuditSpringProperties.class)
public class AuditSpringAutoConfiguration {
    private final String appName;
    private final String appVersion;
    private final boolean mdcDisabled;
    private final AuditSpringProperties auditSpringProperties;

    /**
     * Creates a new instance of the configuration bean.
     *
     * @param appName the name of the app.
     * @param mdcDisabled a flag to determine whether the correlation ID can be expected to be in the MDC or not. If
     *                    <code>false</code> then the correlation ID will be explicitly included in any log statements.
     * @param buildProperties {@link BuildProperties} encapsulating details about the build of the application, including
     *                        version number.
     * @param auditSpringProperties an object encapsulating the spt.audit properties.
     */
    public AuditSpringAutoConfiguration(
            @Value("${spring.application.name:Spring Boot}")
            final String appName,
            @Value("${spt.cid.mdc.disabled:false}")
            final boolean mdcDisabled,
            final BuildProperties buildProperties,
            final AuditSpringProperties auditSpringProperties) {

        this.appName = appName;
        this.appVersion = buildProperties.getVersion();
        this.mdcDisabled = mdcDisabled;
        this.auditSpringProperties = auditSpringProperties;
    }

    /**
     * Creates a {@link Auditor} (aspect) bean.
     *
     * @param auditEventWriter an {@link AuditEventWriter} bean for writing the
     *                         {@link com.spt.development.audit.spring.AuditEvent}s generated by the aspect.
     * @param authenticationAdapterFactory an {@link AuthenticationAdapterFactory} for generating adapters for retrieving
     *                                     details about the current user.
     * @param correlationIdProvider a correlation ID provider.
     *
     * @return a new {@link Auditor} bean.
     */
    @Bean
    @ConditionalOnMissingBean
    public Auditor auditor(AuditEventWriter auditEventWriter,
                           AuthenticationAdapterFactory authenticationAdapterFactory,
                           CorrelationIdProvider correlationIdProvider) {
        return new Auditor(appName, appVersion, auditEventWriter, mdcDisabled, correlationIdProvider, authenticationAdapterFactory);
    }

    /**
     * Creates a {@link JmsAuditEventWriter} bean. This bean is conditional on a {@link JmsTemplate} bean being created
     * and the <code>spt.audit.jms.destination</code> property being set.
     *
     * @param jmsTemplate a {@link JmsTemplate} used to add the {@link com.spt.development.audit.spring.AuditEvent}s to
     *                    the configured JMS destination.
     * @param correlationIdProvider a correlation ID provider.
     *
     * @return a new {@link JmsAuditEventWriter}.
     */
    @Bean
    @ConditionalOnBean({ JmsTemplate.class })
    @ConditionalOnProperty({ "spt.audit.jms.destination" })
    public AuditEventWriter jmsAuditEventWriter(JmsTemplate jmsTemplate, CorrelationIdProvider correlationIdProvider) {
        return new JmsAuditEventWriter(mdcDisabled, auditSpringProperties.getJms().getDestination(), jmsTemplate, correlationIdProvider);
    }

    /**
     * Creates a {@link Slf4jAuditEventWriter} which is the default implementation of {@link AuditEventWriter} and will
     * be created if no other {@link AuditEventWriter} beans exist. In general this implementation is not suitable for
     * production code.
     *
     * @param correlationIdProvider a correlation ID provider.
     *
     * @return a new {@link Slf4jAuditEventWriter}.
     */
    @Bean
    @ConditionalOnMissingBean({ AuditEventWriter.class })
    public AuditEventWriter slf4jAuditEventWriter(CorrelationIdProvider correlationIdProvider) {
        return new Slf4jAuditEventWriter(mdcDisabled, correlationIdProvider);
    }

    /**
     * Creates a {@link DefaultCorrelationIdProvider} which is the default implementation of {@link CorrelationIdProvider}.
     *
     * @return a new {@link DefaultCorrelationIdProvider}.
     */
    @Bean
    @ConditionalOnMissingBean({CorrelationIdProvider.class })
    public CorrelationIdProvider correlationIdProvider() {
        return new DefaultCorrelationIdProvider();
    }

    /**
     * Creates a {@link DefaultAuthenticationAdapterFactory} bean. In most cases, the default factory created by this
     * factory method will not be suitable and will need to be manually registered so that either
     * {@link DefaultAuthenticationAdapterFactory#withUsernamePasswordFactory(Function)} can be called to customise how
     * the current user's userId etc is obtained from the user principal or a custom implementation of
     * {@link AuthenticationAdapterFactory} used instead.
     *
     * @return a new {@link AuthenticationAdapterFactory}.
     */
    @Bean
    @ConditionalOnMissingBean
    public AuthenticationAdapterFactory authenticationAdapterFactory() {
        return new DefaultAuthenticationAdapterFactory();
    }
}
