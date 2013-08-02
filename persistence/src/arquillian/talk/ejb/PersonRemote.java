package arquillian.talk.ejb;

import javax.ejb.Remote;

import arquillian.talk.jpa.Person;

@Remote
public interface PersonRemote {
	public static final String JNDI_NAME = "java:global/persistence-ear/persistence-ejb/PersonBean!arquillian.talk.ejb.PersonRemote";
	static final String EJB_JNDI_NAME = "ejb:persistence-ear/persistence-ejb/PersonBean!arquillian.talk.ejb.PersonRemote";

	Person getPerson(Long id);

	Person getPerson(String name);

	void deletePerson(Long id);

	void savePerson(Person person);
}
