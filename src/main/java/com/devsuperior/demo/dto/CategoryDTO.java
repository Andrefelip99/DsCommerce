package com.devsuperior.demo.dto;

import com.devsuperior.demo.entities.Category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CategoryDTO {
   private Long id;
   private String name;

   public CategoryDTO(Category entity){
        id = entity.getId();
        name = entity.getName();
   }
}
