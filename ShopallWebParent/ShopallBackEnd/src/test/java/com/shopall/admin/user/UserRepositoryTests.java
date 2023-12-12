package com.shopall.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopall.common.entity.Role;
import com.shopall.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user = new User("Kingdaav@gmail.com", "abcd123", "King", "Daav");
		user.addRole(roleAdmin);
		
		User savedUser = repo.save(user);
		System.out.println(user);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		User dabby = new User("dabby@gmail.com", "abc123", "Dabby", "Tam");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		dabby.addRole(roleEditor);
		dabby.addRole(roleAssistant);
		
		User savedUser = repo.save(dabby);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));	
		
	}
	
	@Test
	public void testGetUserById() {
		User user = repo.findById(2).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = repo.findById(1).get();
		user.setEnabled(true);
		user.setEmail("davnking@gmail.com");
		
		repo.save(user);
		System.out.println(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userDabby = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalesperson = new Role(2);
		
		
		userDabby.getRoles().remove(roleEditor);
		userDabby.addRole(roleSalesperson);
		
		repo.save(userDabby);
		
		
	}
	
	@Test
	public void testDeleteUserById() {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "davnking@gmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}

}
