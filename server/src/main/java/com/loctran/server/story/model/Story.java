package com.loctran.server.story.model;


import com.fasterxml.jackson.annotation.*;
import com.loctran.server.user.model.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "tbl_story"
)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Story {
  @Id
  @SequenceGenerator(
          name = "tbl_story_id_seq",
          sequenceName = "tbl_story_id_seq",
          allocationSize = 1
  )
  @GeneratedValue(
          strategy = GenerationType.SEQUENCE,
          generator = "tbl_story_id_seq"
  )
  private Integer id;
  
  @Column(nullable = false)
  private String thumbnail;
  
  @Column(nullable = false)
  private String title;
  
  @Column(nullable = false)
  private String slug;
  
  @Column(nullable = false)
  private String content;
  
  @Column(name="pre_content")
  private String preContent;
  
  @Column()
  private List<Long> upvote=   new ArrayList<>();
  
  @Column(columnDefinition = "jsonb")
  @Type(JsonBinaryType.class)
  private Toc[] toc;
  
  @Column(name="created_at")
  private Date createdAt;
  
  @Column(name="updated_at")
  private Date updatedAt;
  
  @ManyToOne()
  @JoinColumn(name = "user_id",nullable = false)
  public User author;

  @OneToMany(mappedBy = "story")
  @JsonIgnoreProperties("story")
  private List<Comment> comments;
  
  @PrePersist
  protected void onCreate() {
    createdAt = new Date();
    updatedAt = new Date();
  }
  
  @PreUpdate
  protected void onUpdate(){
    updatedAt = new Date();
  }
  
}
