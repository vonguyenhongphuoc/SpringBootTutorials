package com.devhp.SpringRestDemoWithGradle.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devhp.SpringRestDemoWithGradle.model.Account;
import com.devhp.SpringRestDemoWithGradle.model.Album;
import com.devhp.SpringRestDemoWithGradle.model.Photo;
import com.devhp.SpringRestDemoWithGradle.payload.album.AlbumPayloadDTO;
import com.devhp.SpringRestDemoWithGradle.payload.album.AlbumViewDTO;
import com.devhp.SpringRestDemoWithGradle.service.AccountService;
import com.devhp.SpringRestDemoWithGradle.service.AlbumService;
import com.devhp.SpringRestDemoWithGradle.service.PhotoService;
import com.devhp.SpringRestDemoWithGradle.util.AppUtils.AppUtil;
import com.devhp.SpringRestDemoWithGradle.util.constants.AlbumError;
import com.devhp.SpringRestDemoWithGradle.util.constants.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album controller", description = "Controller for album and photo management")
@Slf4j
public class AlbumController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            Authentication authentication) {
        try {
            Album album = new Album();
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();
            album.setAccount(account);
            albumService.save(album);
            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription());
            return ResponseEntity.ok(albumViewDTO);
        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return new ResponseEntity<AlbumViewDTO>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public List<AlbumViewDTO> albums(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        List<AlbumViewDTO> albums = new ArrayList<>();
        for (Album album : albumService.findByAccount_id(account.getId())) {
            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription()));

        }
        return albums;
    }

    @PostMapping(value = "/{album_id}/photos", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Upload photo into album")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<List<String>> photos(@RequestPart(value = "files", required = true) MultipartFile[] files,
            @PathVariable(name = "album_id") long albumId, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Optional<Album> optionalAlbum = albumService.findById(albumId);

        if (optionalAccount.isPresent() && optionalAlbum.isPresent()) {
            Album album = optionalAlbum.get();
            Account account = optionalAccount.get();
            if (account.getId() != album.getAccount().getId()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            List<String> fileNamesWithSuccess = new ArrayList<>();
            List<String> fileNamesWithError = new ArrayList<>();

            Arrays.asList(files).stream().forEach(file -> {
                String contentType = file.getContentType();
                if (contentType.equals("image/png") || contentType.equals("image/jpg")
                        || contentType.equals("image/jpeg")) {
                    fileNamesWithSuccess.add(file.getOriginalFilename());

                    int length = 10;
                    boolean userLetters = true;
                    boolean useNumbers = true;
                    try {
                        String fileName = file.getOriginalFilename();
                        String generatedString = RandomStringUtils.random(length, userLetters, useNumbers);
                        String final_photo_name = generatedString + fileName;
                        String absolute_fileLocation = AppUtil.getPhotoUploadPath(final_photo_name, albumId);
                        Path path = Paths.get(absolute_fileLocation);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        Photo photo = new Photo();
                        photo.setName(fileName);
                        photo.setOriginalFileName(final_photo_name);
                        photo.setAlbum(album);
                        photoService.save(photo);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                } else {
                    fileNamesWithError.add(file.getOriginalFilename());
                }

            });
            return ResponseEntity.ok(fileNamesWithSuccess);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


    }

}
