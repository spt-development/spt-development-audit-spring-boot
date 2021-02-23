package com.spt.development.audit.spring.boot.autoconfigure;

import com.spt.development.audit.spring.AuditEventWriter;
import com.spt.development.audit.spring.JmsAuditEventWriter;
import com.spt.development.audit.spring.Slf4jAuditEventWriter;
import com.spt.development.audit.spring.aop.Auditor;
import com.spt.development.audit.spring.security.AuthenticationAdapterFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class AuditSpringAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void init() {
        this.context = new AnnotationConfigApplicationContext();
    }

    @AfterEach
    void closeContext() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    void register_happyPath_shouldRegisterDefaultAuditorBeans() {
        context.register(AuditSpringAutoConfiguration.class, BuildProperties.class);
        context.refresh();

        assertThat(context.getBean(Auditor.class), is(notNullValue()));
        assertThat(context.getBean(AuditEventWriter.class), is(notNullValue()));
        assertThat(context.getBean(AuditEventWriter.class), instanceOf(Slf4jAuditEventWriter.class));
        assertThat(context.getBean(AuthenticationAdapterFactory.class), is(notNullValue()));
    }

    @Test
    void register_jmsConfigured_shouldRegisterJmsAuditEventWriter() {
        final String jmsDestination = "test-destination";

        TestPropertyValues.of(
                "spt.audit.jms.destination:" + jmsDestination
        ).applyTo(context);

        context.registerBean("jmsTemplate", JmsTemplate.class, () -> new JmsTemplate() {

            @Override
            public void afterPropertiesSet() {
                // NOOP
            }
        });
        context.register(AuditSpringAutoConfiguration.class, BuildProperties.class);
        context.refresh();

        assertThat(context.getBean(Auditor.class), is(notNullValue()));
        assertThat(context.getBean(AuditEventWriter.class), is(notNullValue()));
        assertThat(context.getBean(AuditEventWriter.class), instanceOf(JmsAuditEventWriter.class));
        assertThat(context.getBean(AuthenticationAdapterFactory.class), is(notNullValue()));

        final JmsAuditEventWriter result = context.getBean(JmsAuditEventWriter.class);

        assertThat(result, is(notNullValue()));
        assertThat(ReflectionTestUtils.getField(result, "destinationName"), is(jmsDestination));
    }
}