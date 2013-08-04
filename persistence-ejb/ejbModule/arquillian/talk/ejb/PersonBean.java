package arquillian.talk.ejb;

import java.util.List;

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
	// Note the access modifier is default to support mock assignment by JUnit
	// test
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Person getPerson(String name) {
		System.out.println("Getting person with name=" + name);
		Query query = entityManager
				.createQuery("FROM Person WHERE name = :name");
		query.setParameter("name", name);
		Person person;
		try {
			@SuppressWarnings("rawtypes")
			List resultList = query.getResultList();
			if (resultList != null) {
				person = (Person) resultList.get(0);
			} else {
				person = null;
			}
		} catch (NoResultException e) {
			person = null;
		}
		return person;
	}

	@Override
	public Person getPerson(Long id) {
		System.out.println("Getting person with id=" + id);
		return entityManager.find(Person.class, id);
	}

	@Override
	public void deletePerson(Long id) {
		System.out.println("Deleting person with id="+id);
		entityManager.remove(getPerson(id));
	}

	@Override
	public void savePerson(Person person) {
		entityManager.persist(person);
	}

}
