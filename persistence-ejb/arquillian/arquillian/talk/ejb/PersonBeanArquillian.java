package arquillian.talk.ejb;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ejb.EJB;
import javax.naming.NamingException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.talk.jpa.Address;
import arquillian.talk.jpa.Person;

/**
 * Arquillian test for PersonBean
 */
@RunWith(Arquillian.class)
public class PersonBeanArquillian {

	@EJB(lookup = PersonRemote.JNDI_NAME)
	private PersonRemote personRemote;
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	private Person testPerson;

	@Before
	public void init() throws NamingException, ParseException {
		testPerson = new Person(DATE_FORMAT.parse("2003-02-01"), "Test Person");
		testPerson.getAddresses().add(
				new Address("Canberra", 2612, "London Circuit", testPerson));
		testPerson.getAddresses().add(
				new Address("Wollongong", 2034, "Cook Parade", testPerson));																					
	}

	@Deployment
	public static EnterpriseArchive deploy() {
		JavaArchive ejbJar = ShrinkWrap
				.create(JavaArchive.class, "persistence-ejb.jar")
				.addClasses(PersonBean.class)
				.addAsManifestResource("META-INF/beans.xml", "beans.xml");
		JavaArchive persistenceJar = ShrinkWrap
				.create(ZipImporter.class, "persistence.jar")
				.importFrom(new File("../persistence/dist/persistence.jar"))
				.as(JavaArchive.class);
		JavaArchive testJar = ShrinkWrap.create(JavaArchive.class,
				"testJar.jar").addClass(PersonBeanArquillian.class);
		EnterpriseArchive ear = ShrinkWrap
				.create(EnterpriseArchive.class, "persistence-ear.ear")
				.addAsModule(ejbJar).addAsLibraries(persistenceJar, testJar);

		System.out.println(ear.toString(true)); 

		return ear;
	}

	@Test
	public void test() {
		personRemote.savePerson(testPerson);

		Person personFoundByName = personRemote.getPerson(testPerson.getName());
		Long id = personFoundByName.getId();

		Assert.assertEquals(testPerson, personFoundByName);

		Person personFoundById = personRemote.getPerson(id);

		Assert.assertEquals(testPerson, personFoundById);

		personRemote.deletePerson(id);

		System.out.println(personRemote.getPerson(id));
	}

}
