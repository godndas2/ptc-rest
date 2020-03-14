package com.example.model.entity;

import com.example.model.common.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
 /**
 * @author halfdev
 * @since 2020-03-14
 * 전문 직종명
 */
@Entity
@Table(name = "specialties")
public class Specialty extends NamedEntity {
}
