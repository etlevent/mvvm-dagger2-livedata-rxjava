package cn.homelabs.pdms.hospital.pojo;

import javax.inject.Inject;

public class User {
    private String name;

    @Inject
    public User(String name) {
    }

    public String getName() {
        return name;
    }
}
