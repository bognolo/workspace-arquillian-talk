package arquillian.talk.ejb;

import javax.ejb.Remote;

import arquillian.talk.jpa.Person;

@Remote
public interface PersonRemote {
	Person getPerson(Long id);
	Person getPerson(String name);
	void deletePerson(Long id);
	void savePerson(Person person);
}
