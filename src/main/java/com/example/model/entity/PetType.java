package com.example.model.entity;

import com.example.model.common.NamedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "types")
@Getter
@Setter
public class PetType extends NamedEntity {

//    @OneToMany(mappedBy = "type")
//    private List<Pet> pets = new ArrayList<>();
}
