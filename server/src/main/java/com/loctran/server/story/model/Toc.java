package com.loctran.server.story.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Toc {
  private String title;
  private String link;
  private String type;
}
