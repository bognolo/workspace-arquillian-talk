package arquillian.talk.ejb;

import java.io.File;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PersonBeanArquillian {

	
//	@EJB(lookup = PersonRemote.EJB_JNDI_NAME)
	private PersonRemote personRemote;
	
	@Before
	public void init() throws NamingException {
		Properties jndiProps = new Properties();
		jndiProps.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
		Context context = new InitialContext(jndiProps);
		personRemote = (PersonRemote) context.lookup(PersonRemote.EJB_JNDI_NAME);
	}

	@Deployment
	public static EnterpriseArchive deploy() {
		JavaArchive ejbJar = ShrinkWrap.create(JavaArchive.class,"persistence-ejb.jar").addClasses(
				PersonBean.class);
		JavaArchive persistenceJar = ShrinkWrap.create(ZipImporter.class)
				.importFrom(new File("../persistence/dist/persistence.jar"))
				.as(JavaArchive.class).addClass(PersonBeanArquillian.class);
		EnterpriseArchive ear = ShrinkWrap
				.create(EnterpriseArchive.class, "persistence-ear.ear")
				.addAsModule(ejbJar).addAsLibraries(persistenceJar);
		System.out.println(ear.toString(true));
		return ear;
	}

	@Test
	public void test() {
		System.out.println(personRemote.getPerson(11L));
	}

}
