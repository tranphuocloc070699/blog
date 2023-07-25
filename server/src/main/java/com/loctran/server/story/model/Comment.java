package com.loctran.server.story.model;

import com.fasterxml.jackson.annotation.*;
import com.loctran.server.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tbl_comment"
 
)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {
  @Id
  @SequenceGenerator(
          name = "tbl_comment_id_seq",
          sequenceName = "tbl_comment_id_seq",
          allocationSize = 1
  )
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "tbl_comment_id_seq"
  )
  private Integer id;
  
  @Column(nullable = false)
  private String name;
  
  @Column
  private String email;
  
  @Column(nullable = false)
  private String content;
  
  @Column(name="created_at",columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private Date createdAt;
  
  @ManyToOne()
  @JoinColumn(name = "user_id")
  public User user;
  
  @ManyToOne()

  @JoinColumn(name = "story_id",nullable = false)
  public Story story;

  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Comment> subComments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_comment_id")
  private Comment parentComment;
  
  @PrePersist
  protected void onCreate() {
    createdAt = new Date();
  }
  
  public Comment(Integer id, String name, String email, String content, Date createdAt, User user) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.content = content;
    this.createdAt = createdAt;
    this.user = user;
  }
  
}