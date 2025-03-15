package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.bean.restaurant;
import com.example.demo.dao.UserRepository;
import com.example.demo.dao.restaurantDaoImpl;
import com.example.demo.entity.m_restaurant;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    Logger logger 
    = LoggerFactory.getLogger(AuthenticationService.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	public SessionFactory getSessionfactory()
	{
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory=sessionFactory;
	}

    public AuthenticationService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public m_restaurant signup(m_restaurant input) {
        m_restaurant restaurantEntity=new m_restaurant();
        int id=getMaxIDFromTable("m_restaurant");
				
				restaurantEntity.setId(id);
				if(input.getUserName()!=null)
					restaurantEntity.setUserName(input.getUserName());
				if(input.getPassword()!=null)
					restaurantEntity.setPswd(passwordEncoder.encode(input.getPassword()));
				if(input.getCompanyId()!=null)
					restaurantEntity.setCompanyId(input.getCompanyId());
				if(input.getEmail()!=null)
					restaurantEntity.setEmail(input.getEmail());
				if(input.getTotalTables()!=null)
					restaurantEntity.setTotalTables(input.getTotalTables());
				if(input.getBankAccountNumber()!=null)
					restaurantEntity.setBankAccountNumber(input.getBankAccountNumber());
				if(input.getIfscCode()!=null)
					restaurantEntity.setIfscCode(input.getIfscCode());
				if(input.getGstNumber()!=null)
					restaurantEntity.setGstNumber(input.getGstNumber());
				if(input.getAddress()!=null)
					restaurantEntity.setAddress(input.getAddress());
				if(input.getPhoneNumber()!=null)
					restaurantEntity.setPhoneNumber(input.getPhoneNumber());
				
				restaurantEntity.setDelFlag("N");
				LocalDate currentDate = LocalDate.now(); 
				LocalTime currentTime = LocalTime.now(); 
				String dateAndTime= currentDate+" "+currentTime;
				restaurantEntity.setRecordInsertDate(dateAndTime);

        return userRepository.save(restaurantEntity);
    }

    public m_restaurant authenticate(restaurant input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPswd()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public int getMaxIDFromTable(String tableName)
	{
		logger.info("start of getMaxIDFromTable function");
		Session session=this.sessionFactory.openSession();
		List list=null;
		int maxNum=0;
		session.beginTransaction();
		StringBuilder queryStr=new StringBuilder(0);
		try
		{	
			queryStr.append("select max(id) from "+tableName);
			Query query=session.createNativeQuery(queryStr.toString());
			list =  query.getResultList();
			if(list!=null && list.size()>0)
			{
				maxNum=(Integer) list.get(0);
			}
			session.getTransaction().commit();
		    session.close();
		}
		catch(Exception e)
		{
			logger.info("Exception at getMaxIDFromTable function "+e);
			session.close();
		}
		logger.info("end of getMaxIDFromTable function");
		return maxNum+1;
	}
    
}
