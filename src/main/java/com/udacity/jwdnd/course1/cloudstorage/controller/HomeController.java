package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileStorageService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/home")
public class HomeController {
    private Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private FileStorageService fileStorageService;


    @GetMapping()
    public String getHomePage(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credentialForm") CredentialForm credentialForm, @ModelAttribute("fileForm") FileForm fileForm, Model model) {
        String username = authentication.getName();
        User user = userService.getUser(username);
        Integer userId = user.getUserId();
        List<Note> notes = noteService.getNotesForUser(userId);
        List<Credential> credentials = credentialService.getCredentialsForUser(userId);
        List<FileDTO> files = fileStorageService.getFilesForUser(userId);
        model.addAttribute("notes", notes);
        model.addAttribute("credentials", credentials);
        model.addAttribute("files", files);
        return "home";
    }

    @PostMapping("/note")
    public String saveNote(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, Model model) {
        logger.info("request to save note for user {}", authentication.getName());
        logger.info("noteForm {}", noteForm.getNoteId());
        String creationError = null;
        String username = authentication.getName();
        User user = userService.getUser(username);
        noteForm.setUserId(user.getUserId());

        try {
            noteService.saveNote(noteForm);
        } catch (Exception ex) {
            creationError = ex.getLocalizedMessage();
        }

        if (creationError == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("failed", creationError);
        }
        return "result";
    }


    @PostMapping("/delete-note")
    public String deleteNote(Authentication authentication, @ModelAttribute("noteForm") NoteForm noteForm, Model model) {
        logger.info("request to delete note for user {}", authentication.getName());
        String creationError = null;
        int deletedRows = noteService.deleteNote(noteForm);
        if (deletedRows < 0) creationError = "There was an error deleting your Note. Please try again.";

        if (creationError == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("failed", creationError);
        }
        return "result";
    }

    @PostMapping("/credential")
    public String saveCredential(Authentication authentication, @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) {
        logger.info("request to save credential for user {}", authentication.getName());
        String creationError = null;
        String username = authentication.getName();
        User user = userService.getUser(username);
        credentialForm.setUserId(user.getUserId());
        try {
            credentialService.saveCredential(credentialForm);
        } catch (Exception ex) {
            creationError = ex.getLocalizedMessage();
        }
        if (creationError == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("failed", creationError);
        }

        return "result";
    }

    @PostMapping("/delete-credential")
    public String deleteCredential(Authentication authentication, @ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) {
        logger.info("request to delete credential for user {}", authentication.getName());
        String creationError = null;
        int deletedRows = credentialService.deleteCredential(credentialForm);
        if (deletedRows < 0) creationError = "There was an error deleting your Credential. Please try again.";

        if (creationError == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("failed", creationError);
        }
        return "result";
    }


    @RequestMapping(value = "/decrypt-credential", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, String> decryptCredential(@RequestBody EncryptionData data) {
        logger.info("request to decrypt credentialDTO for user ");
        return credentialService.decryptCredential(data);
    }

    @PostMapping("/file")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) {
        logger.info("request to save file for user {}", authentication.getName());
        String username = authentication.getName();
        User user = userService.getUser(username);

        // check if file is empty
        if (fileUpload.isEmpty()) {
            logger.info("File is empty");
            String message = "Please select a file to upload.";
            model.addAttribute("failed", message);
            return "result";
        }


        String message = fileStorageService.saveFile(user.getUserId(), fileUpload);
        if (message == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("failed", message);
        }
        return "result";
    }


    @GetMapping("/file")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam(value = "id") Integer id) {
        logger.info("request to download file for user {}", id);
        File file = fileStorageService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }


    @PostMapping("/delete-file")
    public String deleteFile(Authentication authentication, @ModelAttribute("fileForm") FileForm fileForm, Model model) {
        logger.info("request to delete file for user {}", authentication.getName());
        String creationError = null;
        int deletedRows = fileStorageService.deleteFile(fileForm);
        if (deletedRows < 0) creationError = "There was an error deleting the file. Please try again.";

        if (creationError == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("failed", creationError);
        }
        return "result";
    }
}
