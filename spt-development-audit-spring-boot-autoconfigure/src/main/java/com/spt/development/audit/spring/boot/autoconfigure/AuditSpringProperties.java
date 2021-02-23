package com.spt.development.audit.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Configuration properties for
 * <a href="https://github.com/spt-development/spt-development-audit-spring">spt-development/spt-development-audit-spring</a>.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "spt.audit")
public class AuditSpringProperties {
    private final Jms jms;

    /**
     * Creates an object to encapsulate the <code>spt.audit</code> properties.
     *
     * @param jms object encapsulating the <code>spt.audit</code> properties related to
     *            {@link com.spt.development.audit.spring.JmsAuditEventWriter}.
     */
    public AuditSpringProperties(final Jms jms) {
        this.jms = jms;
    }

    /**
     * An object encasulating the <code>spt.audit</code> properties related to
     * {@link com.spt.development.audit.spring.JmsAuditEventWriter}.
     *
     * @return the JMS audit event writer properties.
     */
    public Jms getJms() {
        return jms;
    }

    /**
     * Configuration properties for
     * <a href="https://github.com/spt-development/spt-development-audit-spring">spt-development/spt-development-audit-spring</a>.
     * relating to {@link com.spt.development.audit.spring.JmsAuditEventWriter}.
     */
    public static class Jms {
        private final String destination;

        /**
         * Creates an object to encapsulate the <code>spt.audit.jms</code> properties.
         *
         * @param destination the name of the JMS destination (queue/topic) to write audit event messages to.
         */
        public Jms(final String destination) {
            this.destination = destination;
        }

        /**
         * The name of the JMS destination (queue/topic) that audit event messages will be written to.
         *
         * @return the audit event JMS destination name.
         */
        public String getDestination() {
            return destination;
        }
    }
}
