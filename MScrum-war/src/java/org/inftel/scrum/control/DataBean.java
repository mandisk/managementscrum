/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.scrum.control;

import java.util.List;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import org.inftel.scrum.entity.User;



/**
 *
 * @author inftel
 */
@ManagedBean
@SessionScoped
public class DataBean {

 /*   private final static String[] colors = {"Black","White"};

	private final static String[] manufacturers = {"Mercedes","BMW"};
	
	private List<User> cars;
    public DataBean(){
    cars = new ArrayList<User>();
		
		populateRandomCars(cars, 50);
    }
    private void populateRandomCars(List<User> list, int size) {
		for(int i = 0 ; i < size ; i++)
			list.add(new User(getRandomModel(), getRandomYear(), getRandomManufacturer(), getRandomColor()));
	}
	
	public List<Car> getCars() {
		return cars;
	}

	private int getRandomYear() {
		return (int) (Math.random() * 50 + 1960);
	}
	
	private String getRandomColor() {
		return colors[(int) (Math.random() * 10)];
	}
	
	private String getRandomManufacturer() {
		return manufacturers[(int) (Math.random() * 10)];
	}
	
	private String getRandomModel() {
		return UUID.randomUUID().toString().substring(0, 8);
	}*/
}
