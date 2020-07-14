package org.vaadin.erik.data.service;

import org.vaadin.erik.data.entity.Person;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

}