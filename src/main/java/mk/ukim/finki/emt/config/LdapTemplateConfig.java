package mk.ukim.finki.emt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

/**
 * Created by Bojan on 5/8/2017.
 */
@Configuration
public class LdapTemplateConfig {

   /* @Bean
    public LdapTemplate ldapTemplate() {
        LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource());
        return ldapTemplate;
    }*/

    /*@Bean
    public LdapContextSource ldapContextSource() {

        String url = "ldap://kostancev.com/phpldapadmin";
        String base = "DC=demo1,DC=demo2,DC=demo3,DC=demo4";

        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(url);
        ldapContextSource.setUserDn(
                "CN=admin,OU=groups,OU=otherpeople,OU=people,OU=space cadets");
        ldapContextSource.setPassword("password");
        //  ldapContextSource.setReferral("follow");
        ldapContextSource.afterPropertiesSet();
        return ldapContextSource;
    }*/


    private final Logger log = LoggerFactory.getLogger(LdapTemplateConfig.class);

    @Bean(name = "ldapTemplate")
    // @Scope("singleton")
    public LdapTemplate ldapTemplate() {
        LdapTemplate ldapTemplate = new LdapTemplate(ldapContextSource());
        return ldapTemplate;
    }

    @Bean(name = "contextSource")
    // @Scope("singleton")
    public LdapContextSource ldapContextSource() {

        String url = "ldap://kostancev.com/phpldapadmin";
        String base = "DC=kostancev,DC=com";

        if (isConfigurationValid(url, base)) {
            LdapContextSource ldapContextSource = new LdapContextSource();
            ldapContextSource.setUrl(url);
            ldapContextSource.setBase(base);
            ldapContextSource.setUserDn("CN=admin,OU=groups,OU=otherpeople,OU=people,OU=space cadets");
            ldapContextSource.setPassword("password");
            ldapContextSource.setReferral("follow");
            // lcs.setPooled(false);
            // lcs.setDirObjectFactory(DefaultDirObjectFactory.class);
            ldapContextSource.afterPropertiesSet();
            return ldapContextSource;
        }
        return null;
    }

    public boolean isConfigurationValid(String url, String base) {
        if ((url == null) || url.isEmpty() || (base == null) || base.isEmpty()) {
            //log.fine("Warning! Your LDAP server is not configured.");
            log.error("Warning! Your LDAP server is not configured.");
            log.info("Did you configure your LDAP settings in your application.yml?");
            return false;
        } else {
            return true;
        }
    }

}