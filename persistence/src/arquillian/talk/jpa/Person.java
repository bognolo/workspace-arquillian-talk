package arquillian.talk.jpa;

import java.io.Serializable;
import javax.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the person database table.
 * 
 */
@Entity
@Table(name="person")
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long id;

    @Temporal( TemporalType.DATE)
	private Date dob;

	@Column(length=2147483647)
	private String name;

	//bi-directional many-to-one association to Address
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<Address> addresses;

    public Person() {
    }

	/**
	 * @param dob
	 * @param name
	 * @param addresses
	 */
	public Person(Date dob, String name, Set<Address> addresses) {
		this.dob = dob;
		this.name = name;
		this.addresses = addresses;
	}

	/**
	 * @param id
	 * @param dob
	 * @param name
	 * @param addresses
	 */
	public Person(Long id, Date dob, String name, Set<Address> addresses) {
		this.id = id;
		this.dob = dob;
		this.name = name;
		this.addresses = addresses;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDob() {
		return this.dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		String s = "Person ID: " +id + "\nName: " + name + "\nDOB: " + SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(dob);
		if (addresses != null && addresses.size() > 0) {
			int c = 1;
			for (Address address : addresses) {
				s = s + "\n\nAddress#" + c++ + ": " + address.toString();
			}
		}
		return s;
	}
	
}