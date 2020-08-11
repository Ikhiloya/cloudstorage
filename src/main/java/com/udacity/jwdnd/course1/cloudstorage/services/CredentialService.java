package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.EncryptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CredentialService {
    private Logger logger = LoggerFactory.getLogger(CredentialService.class);
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }


    public int saveCredential(CredentialForm credentialForm) {
        if (credentialForm.getCredentialId() == null) {
            logger.info("creating a new credential");
            //new note
            return addCredential(credentialForm);
        } else {
            logger.info("updating an existing credential");
            //existing note
            return updateCredential(credentialForm);
        }
    }

    public int addCredential(CredentialForm credentialForm) {
        String encodedKey = encryptionService.generateEncodedKey();
        return credentialMapper.insert(
                new Credential(
                        credentialForm.getUrl(),
                        credentialForm.getUsername(),
                        encodedKey,
                        encryptionService.encryptValue(credentialForm.getPassword(), encodedKey),
                        credentialForm.getUserId()
                )
        );
    }


    public int updateCredential(CredentialForm credentialForm) {
        String encodedKey = encryptionService.generateEncodedKey();
        Credential credential = credentialMapper.getCredential((credentialForm.getCredentialId()));
        return Optional.ofNullable(credential).map(currentCredential -> {
            currentCredential.setUrl(credentialForm.getUrl());
            currentCredential.setUsername(credentialForm.getUsername());
            currentCredential.setKey(encodedKey);
            currentCredential.setPassword(encryptionService.encryptValue(credentialForm.getPassword(), encodedKey));
            return credentialMapper.update(credential);

        }).orElse(-1);
    }


    public List<Credential> getCredentialsForUser(Integer userId) {
        return credentialMapper.getCredentialsForUser(userId);
    }

    public Map<String, String> decryptCredential(EncryptionData data) {
        Credential credential = credentialMapper.getCredential(data.getCredentialId());
        String decryptedValue = encryptionService.decryptValue(data.getCredential(), credential.getKey());
        return new HashMap<String, String>() {
            {
                put("data", decryptedValue);
            }
        };
    }

    public int deleteCredential(CredentialForm credentialForm) {
        return credentialMapper.delete(credentialForm.getCredentialId());
    }
}
