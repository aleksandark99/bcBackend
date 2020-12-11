package com.garbagecollectors.app.service_impl;

import com.garbagecollectors.app.model.Picture;
import com.garbagecollectors.app.repository.PictureRepository;
import com.garbagecollectors.app.service.PictureService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository repository;

    @Inject
    public PictureServiceImpl(final PictureRepository repository){
        this.repository = repository;
    }


    @Override
    public Picture save(Picture picture) {

        return repository.save(picture);
    }
}
