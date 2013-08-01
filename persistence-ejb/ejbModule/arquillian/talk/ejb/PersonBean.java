package arquillian.talk.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import arquillian.talk.jpa.Person;

@Remote(PersonRemote.class)
@Stateless 
public class PersonBean implements PersonRemote {
	// Note the access modifier is default to support mock assignment by JUnit test
	@PersistenceContext
	EntityManager entityManager;
	
	@Override
	public Person getPerson(String name) {
		Query query = entityManager.createQuery("FROM Person WHERE name = :name");
		query.setParameter("name", name);
		Person person;
		try {
			person = (Person) query.getSingleResult();
		} catch (NoResultException e) {
			person = null;
		}
		return person;
	}

	@Override
	public Person getPerson(Long id) {
		return entityManager.find(Person.class, id);
	}

	@Override
	public void deletePerson(Long id) {
		entityManager.remove(getPerson(id));
	}

	@Override
	public void savePerson(Person person) {
		entityManager.persist(person);
	}

}
