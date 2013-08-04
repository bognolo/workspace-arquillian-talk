package arquillian.talk.jpa;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the address database table.
 * 
 */
@Entity
@Table(name="address")
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long id;

	@Column(length=2147483647)
	private String city;

	private Integer postcode;

	@Column(length=2147483647)
	private String street;

	//bi-directional many-to-one association to Person
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="person_id")
	private Person person;

    public Address() {
    }

	@Override
	public boolean equals(Object obj) {
		boolean equals = obj instanceof Address;
		Address otherAddress  = (Address) obj;
		
		equals &= getCity() != null && getCity().equals(otherAddress.getCity());
		equals &= getPostcode() != null && getPostcode().equals(otherAddress.getPostcode());
		equals &= getStreet() != null && getStreet().equals(otherAddress.getStreet());
		
		return equals;
	}

	/**
	 * @param city
	 * @param postcode
	 * @param street
	 * @param person
	 */
	public Address(String city, Integer postcode, String street, Person person) {
		this.city = city;
		this.postcode = postcode;
		this.street = street;
		this.person = person;
	}

	/**
	 * @param id
	 * @param city
	 * @param postcode
	 * @param street
	 * @param person
	 */
	public Address(Long id, String city, Integer postcode, String street,
			Person person) {
		this.id = id;
		this.city = city;
		this.postcode = postcode;
		this.street = street;
		this.person = person;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPostcode() {
		return this.postcode;
	}

	public void setPostcode(Integer postcode) {
		this.postcode = postcode;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
	public String toString() {
		return "Address ID: " + id + "\nStreet: " + street + "\nCity: " + city + "\nPostcode: " + postcode + "\nPerson ID: " + person.getId();
	}
	
}