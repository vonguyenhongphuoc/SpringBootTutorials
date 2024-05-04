package com.devhp.SpringRestDemoWithGradle.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devhp.SpringRestDemoWithGradle.model.Account;
import com.devhp.SpringRestDemoWithGradle.model.Album;
import com.devhp.SpringRestDemoWithGradle.payload.album.AlbumPayloadDTO;
import com.devhp.SpringRestDemoWithGradle.payload.album.AlbumViewDTO;
import com.devhp.SpringRestDemoWithGradle.service.AccountService;
import com.devhp.SpringRestDemoWithGradle.service.AlbumService;
import com.devhp.SpringRestDemoWithGradle.util.constants.AlbumError;
import com.devhp.SpringRestDemoWithGradle.util.constants.Constants;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album controller", description = "Controller for album and photo management")
@Slf4j
public class AlbumController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;
    

    @PostMapping(value = "/albums/add",  consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,Authentication authentication) { 
        try {
            Album album = new Album();
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();
            album.setAccount(account);
a            albumService.save(album);
            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(),album.getName(), album.getDescription());
            return ResponseEntity.ok(albumViewDTO);
        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return new ResponseEntity<AlbumViewDTO>(HttpStatus.BAD_REQUEST);
        }
    }
}
