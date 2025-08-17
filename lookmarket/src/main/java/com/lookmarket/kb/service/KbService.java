package com.lookmarket.kb.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.lookmarket.Vector.EmbeddingService;
import com.lookmarket.kb.mapper.KbMapper;
import com.lookmarket.kb.vo.KbChunkVO;
import com.lookmarket.kb.vo.KbDocumentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KbService {
  private KbMapper kbMapper;
  private EmbeddingService embeddingService;

  @Transactional
  public long addDocumentWithChunks(String title, String url, String rawText, List<String> chunks){
    KbDocumentVO doc = new KbDocumentVO();
    doc.setTitle(title);
    doc.setSourceUrl(url);
    doc.setRawText(rawText);
    doc.setLang("ko");
    doc.setContentHash(org.apache.commons.codec.digest.DigestUtils.sha256Hex(rawText));
    kbMapper.insertDocument(doc);

    int order = 0;
    List<KbChunkVO> list = new java.util.ArrayList<>();
    for (String text : chunks) {
      KbChunkVO c = new KbChunkVO();
      c.setDocId(doc.getDocId());
      c.setTitle(title);
      c.setText(text);
      c.setEmbedding(embeddingService.embed(title + "\n" + text));
      c.setOrderInDoc(order++);
      list.add(c);
    }
    if (!list.isEmpty()) kbMapper.insertChunksBatch(list);
    return doc.getDocId();
  }
}