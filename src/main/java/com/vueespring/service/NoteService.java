package com.vueespring.service;


import com.vueespring.entity.NoteEnity;
import com.vueespring.entity.WebEntity.NoteCardEnity;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cyk
 * @since 2022-10-27
 */
public interface NoteService{


    String saveImg(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    List<NoteEnity> getNotes(String username);

    NoteEnity getNoteById(String id);

    Boolean removeNoteById(String id);

    NoteCardEnity setCardByNoteDefault(NoteEnity entity);

    ArrayList<NoteCardEnity> getCardByUsername(String username);

    ArrayList<NoteCardEnity> getPublicCards();

    String getIntroByContent(String content);
}
