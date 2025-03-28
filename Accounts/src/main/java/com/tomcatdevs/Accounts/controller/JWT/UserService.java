package com.tomcatdevs.Accounts.controller.JWT;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private List<User> list=new ArrayList<>();

    public UserService(){
        list.add(new User(UUID.randomUUID().toString(),"prateek arya","prateekraya100@gmail.com"));
        list.add(new User(UUID.randomUUID().toString(),"naveen singh","naveen@gmail.com"));
        list.add(new User(UUID.randomUUID().toString(),"karan jha","karan@gmail.com"));
        list.add(new User(UUID.randomUUID().toString(),"mayank singh","mayank@gmail.com"));
        list.add(new User(UUID.randomUUID().toString(),"kavita singh","kavita@gmail.com"));
    }

    public List<User> getUsers(){
        return this.list;
    }
}
