package com.lazuardifachri.bps.lekdarjo.service;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PublicationServiceImplTest {

    @Test
    public void cleanDocumentUri() {

        String documentUri = "https://sidoarjokab.bps.go.id/publication/download.html?nrbvfeve=ZTNlMTBkODE5ZTBiYmZlZTUzNTNkYmJk&xzmn=aHR0cHM6Ly9zaWRvYXJqb2thYi5icHMuZ28uaWQvcHVibGljYXRpb24vMjAyMS8wMi8yNi9lM2UxMGQ4MTllMGJiZmVlNTM1M2RiYmQva2FidXBhdGVuLXNpZG9hcmpvLWRhbGFtLWFuZ2thLTIwMjEuaHRtbA%3D%3D&twoadfnoarfeauf=MjAyMS0wNC0xNiAyMTo1MTo1NA%3D%3D";

        String cover1 = "https://sidoarjokab.bps.go.id/publication/getImageCover.html?url=MjAyMS0wNC0yMCMjaHR0cHM6Ly9wb3J0YWxwdWJsaWthc2kuYnBzLmdvLmlkL2FwaS9nZXRLb3Zlci5waHA%2Fc2VsZWN0b3I9OTgxZTI5NjkzOTQ4MjkxMjhhYmRmNmRh";
        String cover2 = "https://sidoarjokab.bps.go.id/publication/getImageCover.html?url=MjAyMS0wNC0xOSMjaHR0cHM6Ly9wb3J0YWxwdWJsaWthc2kuYnBzLmdvLmlkL2FwaS9nZXRLb3Zlci5waHA%2Fc2VsZWN0b3I9OTgxZTI5NjkzOTQ4MjkxMjhhYmRmNmRh";

        String uri3 = "https://sidoarjokab.bps.go.id/publication/download.html?nrbvfeve=ZTNlMTBkODE5ZTBiYmZlZTUzNTNkYmJk&xzmn=aHR0cHM6Ly9zaWRvYXJqb2thYi5icHMuZ28uaWQvcHVibGljYXRpb24vMjAyMS8wMi8yNi9lM2UxMGQ4MTllMGJiZmVlNTM1M2RiYmQva2FidXBhdGVuLXNpZG9hcmpvLWRhbGFtLWFuZ2thLTIwMjEuaHRtbA%3D%3D&twoadfnoarfeauf=MjAyMS0wNC0xNiAyMTo1MTo1NA%3D%3D";
        String uri4 = "https://sidoarjokab.bps.go.id/publication/download.html?nrbvfeve=ZjY5ZGE4ZjIyN2MzYjI5OWJiNTEwYjk0&xzmn=aHR0cHM6Ly9zaWRvYXJqb2thYi5icHMuZ28uaWQvcHVibGljYXRpb24vMjAxOC8wOC8yMC9mNjlkYThmMjI3YzNiMjk5YmI1MTBiOTQva2FidXBhdGVuLXNpZG9hcmpvLWRhbGFtLWFuZ2thLTIwMTguaHRtbA%3D%3D&twoadfnoarfeauf=MjAyMS0wNC0xNiAyMjoxNjoyNA%3D%3D";

        String brs1 = "https://sidoarjokab.bps.go.id/pressrelease/download.html?nrbvfeve=MjA%3D&sdfs=ldjfdifsdjkfahi&twoadfnoarfeauf=MjAyMS0wNC0xOSAxMDoxOToyNQ%3D%3D";
        String brs2 = "https://sidoarjokab.bps.go.id/pressrelease/download.html?nrbvfeve=MjA%3D&sdfs=ldjfdifsdjkfahi&twoadfnoarfeauf=MjAyMS0wNC0yMCAwNToxMzowMA%3D%3D";

        String cleanUri;
        if (documentUri.contains("sidoarjokab.bps.go.id")) {
            String[] split = documentUri.split("=");
            String[] after = Arrays.copyOf(split, split.length -1);
            cleanUri = String.join("=",after).concat("=") ;
            System.out.println(cleanUri);
        }

//        assertEquals(cover1, cover2);

        assertEquals(brs1,brs2);
    }

}