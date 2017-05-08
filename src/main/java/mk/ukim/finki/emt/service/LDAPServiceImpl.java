package mk.ukim.finki.emt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.directory.Attributes;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Created by Bojan on 5/8/2017.
 */
@Service
public class LDAPServiceImpl {

    private final Logger log = LoggerFactory.getLogger(LDAPServiceImpl.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    /*public LDAPServiceImpl(LdapTemplate ldapTemplate){
        this.ldapTemplate=ldapTemplate;
    }*/

    public void getUserDetails(String userName) {
        if (null != ldapTemplate) {
            List<String> vals = ldapTemplate.search(query().where("objectclass").is("person"),
                    new AttributesMapper<String>() {
                        @Override
                        public String mapFromAttributes(Attributes attributes) throws NamingException, javax.naming.NamingException {

                                return attributes.get("sAMAccountName").get().toString();

                        }
                    });
            for (String s : vals) {
                log.info("attr : " + s);
            }
        } else {
            log.info("Templates is null");
        }
    }
}