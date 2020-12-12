package com.garbagecollectors.app.repository;

import com.garbagecollectors.app.model.Picture;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface PictureRepository extends JpaRepository<Picture, Integer> {
}
