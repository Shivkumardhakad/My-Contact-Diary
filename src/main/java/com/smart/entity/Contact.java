package com.smart.entity;

import javax.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cId;

    private String name;
    private String nickName;
    private String work;
    private String email;
    private String phone;

    @Column(unique = false)
    private String image;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")   // IMPORTANT: Foreign Key column create karega
    private User user;

    public Contact() { }

    public Contact(int cId, String name, String nickName, String work, String email,
                   String phone, String image, String description) {
        this.cId = cId;
        this.name = name;
        this.nickName = nickName;
        this.work = work;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.description = description;
    }

    // getters & setters
    


    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Contact [cId=" + cId + ", name=" + name + ", nickName=" + nickName +
               ", work=" + work + ", email=" + email + ", phone=" + phone +
               ", image=" + image + ", description=" + description + "]";
    }
}
