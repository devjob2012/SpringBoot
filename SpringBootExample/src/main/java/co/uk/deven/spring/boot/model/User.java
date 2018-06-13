package co.uk.deven.spring.boot.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
	private String mail;
	private String password;
	private String lastName;
	private String name;
	private String address;
}
