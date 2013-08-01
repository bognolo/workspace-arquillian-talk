package arquillian.talk.ejb;

import org.junit.Assert;
import org.junit.Before;

import org.junit.Test;

import arquillian.talk.ejb.mocks.EntityManagerMock;
import arquillian.talk.jpa.Person;
import javax.persistence.PersistenceException;

/**
 * @author luca
 * 
 */
public class PersonBeanJUnit {

	public static final Long INVALID_ID = -1L;
	public static final Long MY_ID = 1234L;
	public static final String MY_NAME = "myName";

	private PersonBean personBean;

	@Before
	public void setup() {
		personBean = new PersonBean();
		personBean.entityManager = new EntityManagerMock();
	}

	@Test
	public void testGetPersonByName() {
		Person person = personBean.getPerson(MY_NAME);
		Assert.assertEquals(MY_NAME, person.getName());
	}

	@Test
	public void testGetPersonById() {
		Person person = personBean.getPerson(MY_ID);
		Assert.assertEquals(MY_ID, person.getId());
	}

	@Test
	public void testSavePerson() {
		Person person = new Person();
		person.setId(MY_ID);
		personBean.savePerson(person);
	}

	@Test(expected = PersistenceException.class)
	public void testSavePersonError() {
		Person person = new Person();
		person.setId(INVALID_ID);
		personBean.savePerson(person);
	}

	@Test
	public void testDeletePerson() {
		personBean.deletePerson(MY_ID);
	}

	@Test(expected = PersistenceException.class)
	public void testDeletePersonError() {
		personBean.deletePerson(INVALID_ID);
	}
}
