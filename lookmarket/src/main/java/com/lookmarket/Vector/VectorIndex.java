package com.lookmarket.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.lookmarket.kb.vo.KbChunkVO;

//VectorIndex.java — 메모리 인덱스
@Component
public class VectorIndex {
 private static class Entry {
     final long id; final String title; final String text; final float[] v; final float norm;
     Entry(long id, String title, String text, float[] v){ this.id=id; this.title=title; this.text=text; this.v=v; this.norm = l2(v); }
     private static float l2(float[] x){ double s=0; for(float f: x) s+=f*f; return (float)Math.sqrt(s); }
 }
 private final List<Entry> entries = new java.util.concurrent.CopyOnWriteArrayList<>();

 public void load(List<KbChunkVO> chunks) {
     entries.clear();
     for (KbChunkVO c : chunks) entries.add(new Entry(c.getId(), c.getTitle(), c.getText(), c.getEmbedding()));
 }
 public List<SearchHit> topK(float[] q, int k){
     float qn = Entry.l2(q);
     java.util.PriorityQueue<SearchHit> pq = new java.util.PriorityQueue<>(Comparator.comparingDouble(h -> h.score));
     for (Entry e : entries) {
         double sim = dot(q, e.v) / (qn * e.norm + 1e-9); // cosine
         if (pq.size() < k) pq.offer(new SearchHit(e.id, e.title, e.text, sim));
         else if (sim > pq.peek().score) { pq.poll(); pq.offer(new SearchHit(e.id, e.title, e.text, sim)); }
     }
     ArrayList<SearchHit> out = new ArrayList<>(pq);
     out.sort((a,b)->Double.compare(b.score, a.score));
     return out;
 }
 private static double dot(float[] a, float[] b){ double s=0; int n=Math.min(a.length,b.length); for(int i=0;i<n;i++) s+=a[i]*b[i]; return s; }

 public static record SearchHit(long id, String title, String text, double score) {}
}
