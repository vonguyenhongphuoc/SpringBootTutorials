package com.devhp.SpringRestDemoWithGradle.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.devhp.SpringRestDemoWithGradle.payload.album.PhotoDTO;
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

@RestController
@RequestMapping("/api/v1/album")
@Tag(name = "Album controller", description = "Controller for album and photo management")
@Slf4j
public class AlbumController {

    static final String PHOTOS_FOLDER_NAME = "photos";
    static final String THUMBNAIL_FOLDER_NAME = "thumbnails";
    static final int THUMBNAIL_WIDTH = 300;

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
            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), null);
            return ResponseEntity.ok(albumViewDTO);
        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
            return new ResponseEntity<AlbumViewDTO>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/albums")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public List<AlbumViewDTO> albums(Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        List<AlbumViewDTO> albums = new ArrayList<>();
        for (Album album : albumService.findByAccount_id(account.getId())) {

            List<PhotoDTO> photos = new ArrayList<>();
            for (Photo photo : photoService.findByAlbumId(album.getId())) {
                String link = "albums/" + album.getId() + "/photos/" + photo.getId() + "/downloadPhoto";
                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(),
                        link));
            }
            albums.add(new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos));

        }
        return albums;
    }

    @GetMapping(value = "/albums/{albumId}")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<AlbumViewDTO> albumById(@PathVariable(name = "albumId") long albumId,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);
        Account account = optionalAccount.get();
        Optional<Album> optionalAlbum = albumService.findById(albumId);
        Album album;
        if (optionalAlbum.isPresent()) {
            album = optionalAlbum.get();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        if (account.getId() != album.getAccount().getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<PhotoDTO> photos = new ArrayList<>();
        for (Photo photo : photoService.findByAlbumId(album.getId())) {
            String link = "albums/" + album.getId() + "/photos/" + photo.getId() + "/downloadPhoto";
            photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(), link));
        }

        AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(), photos);

        return ResponseEntity.ok(albumViewDTO);
    }

    @PutMapping(value = "/albums/{albumID}/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<AlbumViewDTO> updateAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            @PathVariable(name = "albumID") long albumID, Authentication authentication) {
        try {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            Account account = optionalAccount.get();

            Optional<Album> optionalAlbum = albumService.findById(albumID);
            Album album;
            if (optionalAlbum.isPresent()) {
                album = optionalAlbum.get();
                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            album = albumService.save(album);

            List<PhotoDTO> photos = new ArrayList<>();
            for (Photo photo : photoService.findByAlbumId(album.getId())) {
                String link = "albums/" + album.getId() + "/photos/" + photo.getId() + "/downloadPhoto";
                photos.add(new PhotoDTO(photo.getId(), photo.getName(), photo.getDescription(), photo.getFileName(),
                        link));
            }

            AlbumViewDTO albumViewDTO = new AlbumViewDTO(album.getId(), album.getName(), album.getDescription(),
                    photos);

            return ResponseEntity.ok(albumViewDTO);
        } catch (Exception e) {
           log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @PostMapping(value = "/{album_id}/photos", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Upload photo into album")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<List<HashMap<String, List<String>>>> photos(
            @RequestPart(value = "files", required = true) MultipartFile[] files,
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
                if (contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpg")
                        || contentType.equals("image/jpeg"))) {
                    fileNamesWithSuccess.add(file.getOriginalFilename());

                    int length = 10;
                    boolean userLetters = true;
                    boolean useNumbers = true;
                    try {
                        String fileName = file.getOriginalFilename();
                        String generatedString = RandomStringUtils.random(length, userLetters, useNumbers);
                        String final_photo_name = generatedString + fileName;
                        String absolute_fileLocation = AppUtil.getPhotoUploadPath(final_photo_name, PHOTOS_FOLDER_NAME,
                                albumId);
                        Path path = Paths.get(absolute_fileLocation);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        Photo photo = new Photo();
                        photo.setName(fileName);
                        photo.setOriginalFileName(final_photo_name);
                        photo.setAlbum(album);
                        photoService.save(photo);

                        BufferedImage thumbImg = AppUtil.getThumbnail(file, THUMBNAIL_WIDTH);
                        File thumbnailLocation = new File(
                                AppUtil.getPhotoUploadPath(final_photo_name, THUMBNAIL_FOLDER_NAME, albumId));

                        ImageIO.write(thumbImg, contentType.split("/")[1], thumbnailLocation);

                    } catch (Exception e) {
                        log.debug(AlbumError.PHOTO_UPLOAD_ERROR.toString() + ": " + e.getMessage());
                        fileNamesWithError.add(file.getOriginalFilename());
                    }
                } else {
                    fileNamesWithError.add(file.getOriginalFilename());
                }

            });
            HashMap<String, List<String>> result = new HashMap<>();
            result.put("SUCCESSS", fileNamesWithSuccess);
            result.put("ERROR", fileNamesWithError);

            List<HashMap<String, List<String>>> response = new ArrayList<>();
            response.add(result);

            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    public ResponseEntity<?> downloadFile(long albumId, long photoId, String folderName,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> optionalAccount = accountService.findByEmail(email);

        Optional<Album> optionalAlbum = albumService.findById(albumId);

        if (optionalAlbum.isPresent() && optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Album album = optionalAlbum.get();
            if (account.getId() != album.getAccount().getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Optional<Photo> optionalPhoto = photoService.findById(photoId);
        if (optionalPhoto.isPresent()) {
            Photo photo = optionalPhoto.get();
            Resource resource = null;
            try {
                resource = AppUtil.getFileAsResource(albumId, folderName, photo.getOriginalFileName());
            } catch (IOException e) {
                return ResponseEntity.internalServerError().build();
            }

            if (resource == null) {
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }
            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            String headerValue = "attachment; filename=\"" + photo.getOriginalFileName() + "\"";

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, headerValue).body(resource);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("albums/{albumId}/photos/{photoId}/downloadPhoto")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<?> downloadPhoto(@PathVariable(name = "albumId") long albumId,
            @PathVariable(name = "photoId") long photoId, Authentication authentication) {

        return downloadFile(albumId, photoId, PHOTOS_FOLDER_NAME, authentication);

    }

    @GetMapping("albums/{albumId}/photos/{photoId}/downloadThumbnail")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ResponseEntity<?> downloadThumbnail(@PathVariable(name = "albumId") long albumId,
            @PathVariable(name = "photoId") long photoId, Authentication authentication) {
        return downloadFile(albumId, photoId, THUMBNAIL_FOLDER_NAME, authentication);
    }

}
