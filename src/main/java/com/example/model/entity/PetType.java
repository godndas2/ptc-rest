package com.example.model.entity;

import com.example.model.common.NamedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "types")
@Getter
@Setter
public class PetType extends NamedEntity {

}
