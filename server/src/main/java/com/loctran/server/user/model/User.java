package com.loctran.server.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.loctran.server.story.model.Comment;
import com.loctran.server.story.model.Story;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Entity
@Table(
        name = "tbl_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "tbl_user_email_unique", columnNames = {"email"})
        }
)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements UserDetails {
  @Id
  @SequenceGenerator(
          name = "tbl_user_id_seq",
          sequenceName = "tbl_user_id_seq",
          allocationSize = 1
  )
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "tbl_user_id_seq"
  )
  private Integer id;
  
  @Column(nullable = false, unique = true)
  private String email;
  
  
  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "varchar(50) default USER")
  private Role role = Role.USER;
  
  @Column(nullable = false)
  private String name;
  
//  @JsonIgnore
  @Column(nullable = false)
  private String password;
  

  //mappedBy map to name of class property, ex:
  //Story.class
  //public User author;

//  @OneToMany(mappedBy = "author")
//  private List<Story> stories;
//
//  @OneToMany(mappedBy = "user")
//  private List<Comment> comments;
  
  
  @JsonIgnore
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (role == null) {
      return List.of(new SimpleGrantedAuthority(Role.USER.name()));
    }
    
    return role.getAuthorities();
  }
  
  @Override
  public String getPassword() {
    return password;
  }
  
  @JsonIgnore
  @Override
  public String getUsername() {
    return email;
  }
  
  @JsonIgnore
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }
  
  @JsonIgnore
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }
  
  @JsonIgnore
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
  
  @JsonIgnore
  @Override
  public boolean isEnabled() {
    return true;
  }
}
