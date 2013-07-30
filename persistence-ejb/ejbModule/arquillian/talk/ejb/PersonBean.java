package arquillian.talk.ejb;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import arquillian.talk.jpa.Person;

@Remote(PersonRemote.class)
public @Stateless class PersonBean implements PersonRemote {
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Person getPerson(String name) {
		Query query = entityManager.createQuery("FROM Person WHERE name = :name");
		query.setParameter("name", name);
		return (Person) query.getSingleResult();
	}

	@Override
	public Person getPerson(Long id) {
		return entityManager.find(Person.class, id);
	}

	@Override
	public void deletePerson(Person person) {
		entityManager.remove(person);
	}

	@Override
	public void savePerson(Person person) {
		entityManager.persist(person);
	}

}
